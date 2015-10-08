package com.rathink.ie.manufacturing.service.impl;

import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.RoundEndObserable;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.service.AbstractFlowManager;
import com.rathink.ie.ibase.service.CompanyTermContext;
import com.rathink.ie.ibase.service.InstructionManager;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryResource;
import com.rathink.ie.ibase.work.model.IndustryResourceChoice;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.manufacturing.EManufacturingAccountEntityType;
import com.rathink.ie.manufacturing.EManufacturingChoiceBaseType;
import com.rathink.ie.manufacturing.EManufacturingPropertyName;
import com.rathink.ie.manufacturing.EProduceLineType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Hean on 2015/8/24.
 */
@Service(value = "manufacturingFlowManagerImpl")
public class ManufacturingFlowManagerImpl extends AbstractFlowManager {
    private static Logger logger = LoggerFactory.getLogger(ManufacturingFlowManagerImpl.class);

    @Override
    protected void initPropertyList() {
        for (CompanyTermContext companyTermContext : campaignContext.getCompanyTermContextMap().values()) {
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
            List<CompanyTermProperty> companyTermPropertyList = new ArrayList<>();
            for (EPropertyName ePropertyName : EPropertyName.values()) {
                if (!ePropertyName.equals(EPropertyName.CURRENT_PERIOD_INCOME)) {
                    companyTermContext.put(ePropertyName.name(), 0);
                    companyTermPropertyList.add(new CompanyTermProperty(ePropertyName.name(), ePropertyName.getDept(), 0, companyTerm));
                } else {
                    companyTermContext.put(EPropertyName.CURRENT_PERIOD_INCOME.name(), 2500000);
                    companyTermPropertyList.add(new CompanyTermProperty(EPropertyName.CURRENT_PERIOD_INCOME.name(), EPropertyName.CURRENT_PERIOD_INCOME.getDept(), 2500000, companyTerm));
                }
            }
            companyTermContext.setCompanyTermPropertyList(companyTermPropertyList);
        }

    }

    @Override
    protected void initAccountList() {
        for (CompanyTermContext companyTermContext : campaignContext.getCompanyTermContextMap().values()) {
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
            List<Account> accountList = new ArrayList<>();
            Account humanAccount = accountManager.packageAccount("0", EAccountEntityType.HR_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(humanAccount);
            Account adAccount = accountManager.packageAccount("0", EAccountEntityType.AD_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(adAccount);
            Account productFeeAccount = accountManager.packageAccount("0", EAccountEntityType.PRODUCT_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(productFeeAccount);
            Account marketFeeAccount = accountManager.packageAccount("0", EAccountEntityType.MARKET_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(marketFeeAccount);
            Account operationFeeAccount = accountManager.packageAccount("0", EAccountEntityType.OPERATION_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(operationFeeAccount);
            Integer currentPeriodIncome = companyTermContext.get(EPropertyName.CURRENT_PERIOD_INCOME.name());
            Account incomeAccount = accountManager.packageAccount(String.valueOf(currentPeriodIncome), EAccountEntityType.COMPANY_CASH.name(), EAccountEntityType.OTHER.name(), companyTerm);
            accountList.add(incomeAccount);
            companyTermContext.setAccountList(accountList);
        }

    }


    protected void randomChoice() {

        Map<String, IndustryResource> currentTypeIndustryResourceMap = campaignContext.getCurrentTypeIndustryResourceMap();
        String industryId = campaignContext.getCampaign().getIndustry().getId();

        Integer currentCampaignDate = campaignContext.getCampaign().getCurrentCampaignDate();
        if (currentCampaignDate == 1) {
            //生产线
            IndustryResource produceLineResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.PRODUCE_LINE.name());
            produceLineResource.setCurrentIndustryResourceChoiceSet(new LinkedHashSet<>(industryResourceChoiceManager.listIndustryResourceChoice(produceLineResource.getId())));
            currentTypeIndustryResourceMap.put(EManufacturingChoiceBaseType.PRODUCE_LINE.name(), produceLineResource);
        }
        if (currentCampaignDate % 4 == 1) {
            //市场投放
            IndustryResource marketFeeResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.MARKET_FEE.name());
            marketFeeResource.setCurrentIndustryResourceChoiceSet(new LinkedHashSet<>(industryResourceChoiceManager.listIndustryResourceChoice(marketFeeResource.getId())));
            currentTypeIndustryResourceMap.put(EManufacturingChoiceBaseType.MARKET_FEE.name(), marketFeeResource);

            //订单
            IndustryResource marketOrderResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.MARKET_ORDER.name());
            marketOrderResource.setCurrentIndustryResourceChoiceSet(new LinkedHashSet<>(industryResourceChoiceManager.listIndustryResourceChoice(marketOrderResource.getId())));
            currentTypeIndustryResourceMap.put(EManufacturingChoiceBaseType.MARKET_ORDER.name(), marketOrderResource);
        }
        campaignContext.setCurrentTypeIndustryResourceMap(currentTypeIndustryResourceMap);
    }

    @Override
    public void initNextObserable() {
        Campaign campaign = campaignContext.getCampaign();
        Integer campaignDate = campaign.getCurrentCampaignDate();
        Set<String> companyIdSet = campaignContext.getCompanyTermContextMap().keySet();
        Map<String, Observable> observableMap = campaignContext.getObservableMap();

        RoundEndObserable dateRoundObserable = new RoundEndObserable(campaign.getId(), companyIdSet);
        dateRoundObserable.addObserver((o, arg) -> next(arg.toString()));
        observableMap.put(campaignDate + ":" + "DATE_ROUND", dateRoundObserable);

        if (campaign.getCurrentCampaignDate() % 4 == 1) {
            RoundEndObserable marketPayRoundObserable = new RoundEndObserable(campaign.getId(), companyIdSet);
            marketPayRoundObserable.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), arg.toString());
                    List<CompanyTerm> companyTermList = companyTermManager.listCompanyTerm(campaign.getId(), campaign.getCurrentCampaignDate());
                    for (CompanyTerm companyTerm : companyTermList) {
                        CompanyTermInstruction companyTermInstruction = instructionManager.getUniqueInstructionByBaseType(companyTerm, EManufacturingChoiceBaseType.MARKET_FEE.name());
                        companyTermInstruction.setStatus(EInstructionStatus.YXZ.getValue());
                        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);
                    }
                }
            });
            observableMap.put(campaignDate + ":" + "MARKET_PAY_ROUND", marketPayRoundObserable);

            RoundEndObserable orderRoundObserable = new RoundEndObserable(campaign.getId(), companyIdSet);
            orderRoundObserable.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), arg.toString());
                    List<CompanyTerm> companyTermList = companyTermManager.listCompanyTerm(campaign.getId(), campaign.getCurrentCampaignDate());
                    for (CompanyTerm companyTerm : companyTermList) {
                        CompanyTermInstruction companyTermInstruction = instructionManager.getUniqueInstructionByBaseType(companyTerm, EManufacturingChoiceBaseType.MARKET_ORDER.name());
                        companyTermInstruction.setStatus(EInstructionStatus.YXZ.getValue());
                        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);
                    }
                }
            });
            observableMap.put(campaignDate + ":" + "ORDER_ROUND", orderRoundObserable);
        }
    }

    protected void processInstruction() {
        Integer currentCampaignDate = campaignContext.getCampaign().getCurrentCampaignDate();
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermContext companyTermContext = companyTermHandlerMap.get(companyId);
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
            List<CompanyTermInstruction> companyTermInstructionList = instructionManager.listCompanyInstruction(companyTerm.getCompany(), EManufacturingChoiceBaseType.PRODUCE_LINE.name());

            List<CompanyTermProperty> companyTermPropertyList = new ArrayList<>();
            for (CompanyTermInstruction companyTermInstruction : companyTermInstructionList) {
                Integer instructionDate = companyTermInstruction.getCampaignDate();
                String lineType = companyTermInstruction.getValue();
                EProduceLineType eProduceLineType = EProduceLineType.valueOf(lineType);
                if (currentCampaignDate - instructionDate >= eProduceLineType.getInstallCycle()) {
                    companyTermInstruction.setStatus(EInstructionStatus.YXZ.name());
                } else {

                }
            }
            companyTermContext.setCompanyTermPropertyList(companyTermPropertyList);
        }
    }


    protected void calculateProperty() {

        /*Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermContext companyTermContext = companyTermHandlerMap.get(companyId);
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
            List<CompanyTermProperty> companyTermPropertyList = new ArrayList<>();
            for (EManufacturingPropertyName ePropertyName : EManufacturingPropertyName.values()) {
                String key = ePropertyName.name();
                Integer value = companyTermContext.get(key);
                companyTermPropertyList.add(new CompanyTermProperty(ePropertyName.name(), ePropertyName.getDept(), value, companyTerm));
            }
            companyTermContext.setCompanyTermPropertyList(companyTermPropertyList);
        }*/
    }

    protected void calculateAccount() {
        Campaign campaign = campaignContext.getCampaign();
        Integer TIME_UNIT = campaign.getIndustry().getTerm();
        Integer currentCampaignDate = campaign.getCurrentCampaignDate();
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermContext companyTermContext = companyTermHandlerMap.get(companyId);
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
            List<Account> accountList = new ArrayList<>();

            Integer produceLineTotalCost = 0;
            List<CompanyTermInstruction> instructionList = instructionManager.listCompanyInstruction(companyTerm.getCompany(), EManufacturingChoiceBaseType.PRODUCE_LINE.name());
            for (CompanyTermInstruction instruction : instructionList) {
                EProduceLineType eProduceLineType = EProduceLineType.valueOf(instruction.getValue());
                Integer cost = eProduceLineType.getCost();
                produceLineTotalCost += cost;
            }
            Account productFeeAccount = accountManager.packageAccount(String.valueOf(produceLineTotalCost)
                    , EManufacturingAccountEntityType.PRODUCE_LINE_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(productFeeAccount);

            companyTermContext.setAccountList(accountList);
        }
    }



}
