package com.rathink.ie.internet.service.impl;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.util.RandomUtil;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.service.AbstractFlowManager;
import com.rathink.ie.ibase.service.CompanyTermContext;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryResource;
import com.rathink.ie.ibase.work.model.IndustryResourceChoice;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.internet.EPropertyName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Hean on 2015/8/24.
 */
@Service
public class InternetFlowManagerImpl extends AbstractFlowManager {
    private static Logger logger = LoggerFactory.getLogger(InternetFlowManagerImpl.class);


    @Override
    protected void initPropertyList() {
        for (CompanyTermContext companyTermContext : campaignContext.getCompanyTermContextMap().values()) {
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
            List<CompanyTermProperty> companyTermPropertyList = new ArrayList<>();
            for (EPropertyName ePropertyName : EPropertyName.values()) {
                if (!ePropertyName.equals(EPropertyName.CURRENT_PERIOD_INCOME)) {
                    companyTermContext.put(ePropertyName.name(), 0);
                    companyTermPropertyList.add(new CompanyTermProperty(ePropertyName, 0, companyTerm));
                } else {
                    companyTermContext.put(EPropertyName.CURRENT_PERIOD_INCOME.name(), 2500000);
                    companyTermPropertyList.add(new CompanyTermProperty(EPropertyName.CURRENT_PERIOD_INCOME, 2500000, companyTerm));
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
        Integer COMPANY_HUMAN_NUM_RATIO = 3;//参赛公司与人才的数量比率

        Map<String, IndustryResource> currentTypeIndustryResourceMap = campaignContext.getCurrentTypeIndustryResourceMap();
        String industryId = campaignContext.getCampaign().getIndustry().getId();
        //人员
        Set<IndustryResourceChoice> industryResourceChoiceSet = new HashSet<>();
        Integer needNum = campaignContext.getCompanyTermContextMap().size() * COMPANY_HUMAN_NUM_RATIO;
        IndustryResource humanResource = industryResourceManager.getUniqueIndustryResource(industryId, EChoiceBaseType.HUMAN.name());
        List<CompanyTermInstruction> humanInstructionList = instructionManager.listCompanyInstruction(campaignContext.getCampaign(), EChoiceBaseType.HUMAN.name());
        Set<String> usedResourceChoiceIdSet = humanInstructionList.stream().map(CompanyTermInstruction::getId).collect(Collectors.toSet());
        List<IndustryResourceChoice> industryResourceChoiceList;
        if(usedResourceChoiceIdSet.isEmpty()) {
            industryResourceChoiceList = industryResourceChoiceManager.listIndustryResourceChoice(humanResource.getId());
        } else {
            industryResourceChoiceList = industryResourceChoiceManager.listIndustryResourceChoice(humanResource.getId(), usedResourceChoiceIdSet);
        }

        for (int i = 0; i < needNum; i++) {
            Iterator<IndustryResourceChoice> industryResourceChoiceIterator = industryResourceChoiceList.iterator();
            Integer choiceSize = industryResourceChoiceList.size();
            int index = RandomUtil.random(0, choiceSize);
            for (int m = 0; industryResourceChoiceIterator.hasNext(); m++) {
                IndustryResourceChoice irc = industryResourceChoiceIterator.next();
                if (m == index) {
                    industryResourceChoiceSet.add(irc);
                    industryResourceChoiceIterator.remove();
                    break;
                }
            }
        }
        humanResource.setCurrentIndustryResourceChoiceSet(industryResourceChoiceSet);

        currentTypeIndustryResourceMap.put(EChoiceBaseType.HUMAN.name(), humanResource);
        //产品定位
        IndustryResource productStudyResource = industryResourceManager.getUniqueIndustryResource(industryId, EChoiceBaseType.PRODUCT_STUDY.name());
        productStudyResource.setCurrentIndustryResourceChoiceSet(new HashSet<>(industryResourceChoiceManager.listIndustryResourceChoice(productStudyResource.getId())));
        currentTypeIndustryResourceMap.put(EChoiceBaseType.PRODUCT_STUDY.name(), productStudyResource);
        //产品投入
        IndustryResource productStudyFeeResource = industryResourceManager.getUniqueIndustryResource(industryId, EChoiceBaseType.PRODUCT_STUDY_FEE.name());
        productStudyFeeResource.setCurrentIndustryResourceChoiceSet(new HashSet<>(industryResourceChoiceManager.listIndustryResourceChoice(productStudyFeeResource.getId())));
        currentTypeIndustryResourceMap.put(EChoiceBaseType.PRODUCT_STUDY_FEE.name(), productStudyFeeResource);
        //市场活动
        IndustryResource marketActivityResource = industryResourceManager.getUniqueIndustryResource(industryId, EChoiceBaseType.MARKET_ACTIVITY.name());
        marketActivityResource.setCurrentIndustryResourceChoiceSet(new HashSet<>(industryResourceChoiceManager.listIndustryResourceChoice(marketActivityResource.getId())));
        currentTypeIndustryResourceMap.put(EChoiceBaseType.MARKET_ACTIVITY.name(), marketActivityResource);
        //运营投入
        IndustryResource operationResource = industryResourceManager.getUniqueIndustryResource(industryId, EChoiceBaseType.OPERATION.name());
        operationResource.setCurrentIndustryResourceChoiceSet(new HashSet<>(industryResourceChoiceManager.listIndustryResourceChoice(operationResource.getId())));
        currentTypeIndustryResourceMap.put(EChoiceBaseType.OPERATION.name(), operationResource);

        campaignContext.setCurrentTypeIndustryResourceMap(currentTypeIndustryResourceMap);
    }

    protected void competitiveBidding() {

        Set<IndustryResourceChoice> humanSet = campaignContext.getCurrentTypeIndustryResourceMap().get(EChoiceBaseType.HUMAN.name()).getCurrentIndustryResourceChoiceSet();
        if (humanSet == null || humanSet.size() == 0) return;
        //竞标
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (IndustryResourceChoice human : humanSet) {
            List<CompanyTermInstruction> companyTermInstructionList = campaignContext.getCurrentChoiceInstructionMap().get(human.getId());
            if (companyTermInstructionList != null && companyTermInstructionList.size() > 0) {
                Double maxRecruitmentRatio = 0d;
                CompanyTermInstruction successCompanyTermInstruction = null;
                for (CompanyTermInstruction companyTermInstruction : companyTermInstructionList) {
                    CompanyTermContext companyTermContext = companyTermHandlerMap.get(companyTermInstruction.getCompany().getId());
                    Double fee = Double.valueOf(companyTermInstruction.getValue());
                    Double feeRatio = Math.pow(fee, 0.51) * 0.35;//薪酬系数
                    Integer randomRatio = RandomUtil.random(0, 70);
                    Double recruitmentRatio = feeRatio * 30 / 100 + randomRatio;//招聘能力系数
                    logger.info("公司：{}，员工：{}，工资：{}，薪酬系数：{}，随机值：{}，招聘能力系数：{}",
                            companyTermContext.getCompanyTerm().getCompany().getName(), human.getName(),
                            fee, feeRatio, randomRatio, recruitmentRatio);
                    if (recruitmentRatio > maxRecruitmentRatio) {
                        maxRecruitmentRatio = recruitmentRatio;
                        successCompanyTermInstruction = companyTermInstruction;
                    }
                }
                successCompanyTermInstruction.setStatus(EInstructionStatus.YXZ.getValue());
                //保存选中的
//                baseManager.saveOrUpdate(CompanyInstruction.class.getName(), successCompanyInstruction);
                //保存未选中的
                companyTermInstructionList.stream().filter(companyInstruction -> EInstructionStatus.DQD.getValue().equals(companyInstruction.getStatus())).forEach(companyInstruction -> {
                    companyInstruction.setStatus(EInstructionStatus.WXZ.getValue());
//                    baseManager.saveOrUpdate(CompanyInstruction.class.getName(), companyInstruction);
                });
            }
        }

    }

    /*private void calculateCompetitionMap(CampaignContext campaignContext) {
        Map<String, Integer> competitionMap = new HashMap<>();
        Set<IndustryResourceChoice> currentIndustryResourceChoiceList = new HashSet<>();
        Map<String, IndustryResource> currentTypeIndustryResourceMap = campaignContext.getCurrentTypeIndustryResourceMap();
        //产品定位
        currentIndustryResourceChoiceList.addAll(currentTypeIndustryResourceMap.get(EChoiceBaseType.PRODUCT_STUDY.name()).getCurrentIndustryResourceChoiceSet());
        //市场活动
        currentIndustryResourceChoiceList.addAll(currentTypeIndustryResourceMap.get(EChoiceBaseType.MARKET_ACTIVITY.name()).getCurrentIndustryResourceChoiceSet());
        for (IndustryResourceChoice industryResourceChoice : currentIndustryResourceChoiceList) {
            List<CompanyTermInstruction> companyTermInstructionList = campaignContext.listCurrentCompanyInstructionByChoice(industryResourceChoice.getId());
            if (companyTermInstructionList != null && companyTermInstructionList.size() > 0) {
                competitionMap.put(industryResourceChoice.getId(), companyTermInstructionList.size());
            }
        }
        campaignContext.setCompetitionMap(competitionMap);

    }*/

    protected void calculateProperty() {
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermContext companyTermContext = companyTermHandlerMap.get(companyId);
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
            List<CompanyTermProperty> companyTermPropertyList = new ArrayList<>();
            for (EPropertyName ePropertyName : EPropertyName.values()) {
                String key = ePropertyName.name();
                Integer value = companyTermContext.get(key);
                companyTermPropertyList.add(new CompanyTermProperty(ePropertyName, value, companyTerm));
            }
            companyTermContext.setCompanyTermPropertyList(companyTermPropertyList);
        }
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

            List<CompanyTermInstruction> humanInstructionList = instructionManager.listCompanyInstruction(companyTerm.getCompany(), EChoiceBaseType.HUMAN.name());
            Iterator<CompanyTermInstruction> companyInstructionIterator = humanInstructionList.iterator();
            while (companyInstructionIterator.hasNext()) {
                CompanyTermInstruction companyTermInstruction = companyInstructionIterator.next();
                if (companyTermInstruction.getCampaignDate().equals(currentCampaignDate)) {
                    companyInstructionIterator.remove();
                }
            }
            Integer humanFee = instructionManager.sumFee(humanInstructionList) * TIME_UNIT;
            Account humanAccount = accountManager.packageAccount(String.valueOf(humanFee), EAccountEntityType.HR_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(humanAccount);

            Integer adFee = humanFee * 20 / 100 + humanInstructionList.size() * 2000 + 20000;
            Account adAccount = accountManager.packageAccount(String.valueOf(adFee), EAccountEntityType.AD_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(adAccount);

            List<CompanyTermInstruction> productFeeInstructionList = companyTermContext.listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY_FEE.name());
            Integer productFee = instructionManager.sumFee(productFeeInstructionList);
            Account productFeeAccount = accountManager.packageAccount(String.valueOf(productFee), EAccountEntityType.PRODUCT_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(productFeeAccount);

            List<CompanyTermInstruction> marketFeeInstructionList = companyTermContext.listCompanyInstructionByType(EChoiceBaseType.MARKET_ACTIVITY.name());
            Integer marketFee = instructionManager.sumFee(marketFeeInstructionList);
            Account marketFeeAccount = accountManager.packageAccount(String.valueOf(marketFee), EAccountEntityType.MARKET_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(marketFeeAccount);

            List<CompanyTermInstruction> operationFeeInstructionList = companyTermContext.listCompanyInstructionByType(EChoiceBaseType.OPERATION.name());
            Integer operationFee = instructionManager.sumFee(operationFeeInstructionList);
            Account operationFeeAccount = accountManager.packageAccount(String.valueOf(operationFee), EAccountEntityType.OPERATION_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(operationFeeAccount);

            Integer currentPeriodIncome = companyTermContext.get(EPropertyName.CURRENT_PERIOD_INCOME.name());
            Account incomeAccount = accountManager.packageAccount(String.valueOf(currentPeriodIncome), EAccountEntityType.COMPANY_CASH.name(), EAccountEntityType.OTHER.name(), companyTerm);
            accountList.add(incomeAccount);

            companyTermContext.setAccountList(accountList);
        }
    }

}
