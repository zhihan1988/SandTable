package com.rathink.ie.manufacturing.service.impl;

import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.RoundEndObserable;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.service.AbstractFlowManager;
import com.rathink.ie.ibase.service.CompanyTermContext;
import com.rathink.ie.ibase.work.model.CompanyPart;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryResource;
import com.rathink.ie.ibase.work.model.IndustryResourceChoice;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.manufacturing.*;
import com.rathink.ie.manufacturing.model.Market;
import com.rathink.ie.manufacturing.model.Material;
import com.rathink.ie.manufacturing.model.ProduceLine;
import com.rathink.ie.manufacturing.model.Product;
import com.rathink.ie.manufacturing.service.ProductManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Hean on 2015/8/24.
 */
@Service(value = "manufacturingFlowManagerImpl")
public class ManufacturingFlowManagerImpl extends AbstractFlowManager {
    private static Logger logger = LoggerFactory.getLogger(ManufacturingFlowManagerImpl.class);
    @Autowired
    private ProductManager productManager;

    @Override
    protected void initPartList() {

        String industryId = campaignContext.getCampaign().getIndustry().getId();
        //产品线
        IndustryResource produceLineResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.PRODUCE_LINE.name());
        List<IndustryResourceChoice> produceLineList = industryResourceChoiceManager.listIndustryResourceChoice(produceLineResource.getId());
        //原料
        IndustryResource materialResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.MATERIAL.name());
        List<IndustryResourceChoice> materialList = industryResourceChoiceManager.listIndustryResourceChoice(materialResource.getId());
        //产品
        IndustryResource productResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.PRODUCT.name());
        List<IndustryResourceChoice> productList = industryResourceChoiceManager.listIndustryResourceChoice(productResource.getId());
        //市场
        IndustryResource marketResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.MARKET.name());
        List<IndustryResourceChoice> marketList = industryResourceChoiceManager.listIndustryResourceChoice(marketResource.getId());

        Map<String, List<CompanyPart>> companyPartMap = campaignContext.getCompanyPartMap();
        for (CompanyTermContext companyTermContext : campaignContext.getCompanyTermContextMap().values()) {
            Company company = companyTermContext.getCompanyTerm().getCompany();

            List<CompanyPart> companyPartList = new ArrayList<>();
            for (IndustryResourceChoice produceLineChoice : produceLineList) {
                ProduceLine produceLine = new ProduceLine();
//                produceLine.setBaseType(EPartBaseType.PRODUCE_LINE.name());
                produceLine.setDept(produceLineResource.getDept());
                produceLine.setStatus(ProduceLine.Status.UN_BUILD.name());
                produceLine.setCampaign(campaignContext.getCampaign());
                produceLine.setCompany(company);
                produceLine.setName(produceLineChoice.getName());
                produceLine.setProduceType("1");
                produceLine.setProduceLineType("A");
                baseManager.saveOrUpdate(ProduceLine.class.getName(), produceLine);
//                companyPartList.add(produceLine);
            }
            for (IndustryResourceChoice materialChoice : materialList) {
                Material material = new Material();
                material.setName(materialChoice.getName());
                material.setType(materialChoice.getType());
                material.setAmount("0");
                material.setCampaign(campaignContext.getCampaign());
                material.setCompany(company);
                material.setStatus(Material.Status.NORMAL.name());
                baseManager.saveOrUpdate(Material.class.getName(), material);
            }
            for (IndustryResourceChoice productChoice : productList) {
                Product product = new Product();
                product.setAmount(0);
                product.setName(productChoice.getName());
                product.setType(productChoice.getType());
                product.setAmount(0);
                Integer developNeedCycle = Product.Type.valueOf(productChoice.getType()).getDevelopNeedCycle();
                product.setDevelopNeedCycle(developNeedCycle);
                product.setCampaign(campaignContext.getCampaign());
                product.setCompany(company);
                product.setStatus(Product.Status.NORMAL.name());
                baseManager.saveOrUpdate(Product.class.getName(), product);
            }
            for (IndustryResourceChoice marketChoice : marketList) {
                Market market = new Market();
                market.setName(marketChoice.getName());
                market.setType(marketChoice.getType());
                Integer devotionNeedCycle = Market.Type.valueOf(marketChoice.getType()).getDevotionNeedCycle();
                market.setDevotionNeedCycle(devotionNeedCycle);
                market.setCompany(company);
                market.setCampaign(campaignContext.getCampaign());
                market.setStatus(Market.Status.NORMAL.name());
                baseManager.saveOrUpdate(Market.class.getName(), market);
            }

            companyPartMap.put(company.getId(), companyPartList);
        }

    }

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

/*
        if (campaign.getCurrentCampaignDate() % 4 == 1) {
            RoundEndObserable marketPayRoundObserable = new RoundEndObserable(campaign.getId(), companyIdSet);
            marketPayRoundObserable.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), arg.toString());
                    List<CompanyTerm> companyTermList = companyTermManager.listCompanyTerm(campaign.getId(), campaign.getCurrentCampaignDate());
                    for (CompanyTerm companyTerm : companyTermList) {
                        CompanyTermInstruction companyTermInstruction = instructionManager.getUniqueInstructionByBaseType(companyTerm, EManufacturingChoiceBaseType.MARKET_FEE.name());
                        companyTermInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
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
                        companyTermInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);
                    }
                }
            });
            observableMap.put(campaignDate + ":" + "ORDER_ROUND", orderRoundObserable);
        }
*/
    }

    protected void processInstruction() {
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermContext companyTermContext = companyTermHandlerMap.get(companyId);
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();

            //生产线建造
            List<CompanyTermInstruction> produceLineInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingChoiceBaseType.PRODUCE_LINE.name());
            if (produceLineInstructionList != null) {
                for (CompanyTermInstruction produceLineInstruction : produceLineInstructionList) {
                    ProduceLine produceLine = (ProduceLine) baseManager.getObject(ProduceLine.class.getName(), produceLineInstruction.getCompanyPart().getId());
//                    Integer instructionDate = produceLineInstruction.getCompanyTerm().getCampaignDate();
//                    ProduceLine.Type eProduceLineType = ProduceLine.Type.valueOf(produceLineInstruction.getValue());
                    Integer lineBuildNeedCycle = Integer.valueOf(produceLine.getLineBuildNeedCycle());
                    if (lineBuildNeedCycle > 0) {
                        lineBuildNeedCycle--;
                    }
                    if (lineBuildNeedCycle == 0) {//建造完成
                        produceLine.setStatus(ProduceLine.Status.FREE.name());
                        baseManager.saveOrUpdate(ProduceLine.class.getName(), produceLine);
                    }

                    produceLineInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                    baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), produceLineInstruction);
                }
            }

            //原材料采购
            List<CompanyTermInstruction> materialInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingChoiceBaseType.MATERIAL.name());
            if (materialInstructionList != null) {
                for (CompanyTermInstruction materialInstruction : materialInstructionList) {
                    Material material = (Material) baseManager.getObject(Material.class.getName(), materialInstruction.getCompanyPart().getId());
                    Integer amount = Integer.valueOf(materialInstruction.getValue());
                    material.setAmount(material.getAmount() + amount);
                    baseManager.saveOrUpdate(Material.class.getName(), material);

                    materialInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                    baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), materialInstruction);
                }
            }

            //产品研发
            List<CompanyTermInstruction> productInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingChoiceBaseType.PRODUCT.name());
            if (productInstructionList != null) {
                for (CompanyTermInstruction productInstruction : productInstructionList) {
                    Product product = (Product) baseManager.getObject(Product.class.getName(), productInstruction.getCompanyPart().getId());
                    Integer developNeedCycle = product.getDevelopNeedCycle();
                    product.setDevelopNeedCycle(--developNeedCycle);
                    baseManager.saveOrUpdate(Product.class.getName(), product);

                    productInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                    baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), productInstruction);
                }
            }
        }
    }

    protected void processPart() {
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermContext companyTermContext = companyTermHandlerMap.get(companyId);
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
            Company company = companyTerm.getCompany();

            XQuery produceLineQuery = new XQuery();
            produceLineQuery.setHql("from ProduceLine where company.id = :companyId and status = :status");
            produceLineQuery.put("companyId", company.getId());
            produceLineQuery.put("status",ProduceLine.Status.PRODUCING.name());
            List<ProduceLine> produceLineList = baseManager.listObject(produceLineQuery);
            for (ProduceLine produceLine : produceLineList) {
                Integer produceNeedCycle = produceLine.getProduceNeedCycle();
                produceNeedCycle = produceNeedCycle - 1;
                produceLine.setProduceNeedCycle(produceNeedCycle);

                if (produceNeedCycle == 0) {//生产完成
                    Product product = productManager.getProduct(company, produceLine.getProduceType());
                    Integer productAmount = product.getAmount();
                    product.setAmount(++productAmount);
                    baseManager.saveOrUpdate(Product.class.getName(), product);

                    produceLine.setStatus(ProduceLine.Status.FREE.name());
                }

                baseManager.saveOrUpdate(ProduceLine.class.getName(), produceLine);
            }
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

            Account productFeeAccount = accountManager.packageAccount(String.valueOf(produceLineTotalCost)
                    , EManufacturingAccountEntityType.PRODUCE_LINE_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(productFeeAccount);

            companyTermContext.setAccountList(accountList);
        }
    }



}
