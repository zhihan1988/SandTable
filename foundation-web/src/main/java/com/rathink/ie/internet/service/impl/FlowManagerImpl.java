package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.campaign.model.Industry;
import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.team.model.ECompanyStatus;
import com.rathink.ie.foundation.util.RandomUtil;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.service.*;
import com.rathink.ie.ibase.work.model.*;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.service.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Hean on 2015/8/24.
 */
@Service
public class FlowManagerImpl implements FlowManager {
    private static Logger logger = LoggerFactory.getLogger(FlowManagerImpl.class);

    @Autowired
    private BaseManager baseManager;
    @Autowired
    private InstructionManager instructionManager;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private IndustryResourceManager industryResourceManager;
    @Autowired
    private IndustryResourceChoiceManager industryResourceChoiceManager;
    /**
     * 开始游戏 定时任务执行
     * @param campaignId
     */
    @Override
    public void begin(String campaignId) {
        CampaignContext campaignContext = CampaignCenter.getCampaignHandler(campaignId);

        initCampaignContext(campaignContext);

        newRound(campaignContext);

        campaignContext = CampaignCenter.getCampaignHandler(campaignId);

        saveCampaignContext(campaignContext);

        randomChoice(campaignContext);

    }

    /**
     * 开始新的回合
     * @param campaignId
     */
    public void next(String campaignId) {
        CampaignContext campaignContext = CampaignCenter.getCampaignHandler(campaignId);

        //回合结束前
        //收集决策信息
        collectCompanyInstruction(campaignContext);
        //竞标决策
        competitiveBidding(campaignContext);
        //非竞标决策
        competitiveUnBidding(campaignContext);
        //计算保存新回合的属性数据
        calculateProperty(campaignContext);
        //4计算保存新回合的财务数据
        calculateAccount(campaignContext);


        //回合结束进入新的回合
        newRound(campaignContext);


        //新回合开始后
        campaignContext = CampaignCenter.getCampaignHandler(campaignId);

        saveCampaignContext(campaignContext);

        randomChoice(campaignContext);

        dieOut(campaignContext);

        end(campaignContext);
    }

    public void initCampaignContext(CampaignContext campaignContext){
        final Integer INIT_CAMPAIGN_DATE = 0;

        //1.比赛开始
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), campaignContext.getCampaign().getId());
        campaign.setCurrentCampaignDate(INIT_CAMPAIGN_DATE);
        campaign.setStatus(Campaign.Status.RUN.getValue());
        campaignContext.setCampaign(campaign);
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);
        //2.初始化各公司
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        List<Company> companyList = campaignManager.listCompany(campaign);
        for (Company company : companyList) {
            company.setStatus(ECompanyStatus.NORMAL.name());
            company.setCurrentCampaignDate(campaign.getCurrentCampaignDate());
            baseManager.saveOrUpdate(Company.class.getName(), company);
            //生成新回合的companyTerm
            CompanyTerm companyTerm = new CompanyTerm();
            companyTerm.setCampaign(company.getCampaign());
            companyTerm.setCompany(company);
            companyTerm.setCampaignDate(company.getCampaign().getCurrentCampaignDate());
            baseManager.saveOrUpdate(CompanyTerm.class.getName(), companyTerm);
            //生成新回合的companyTermHandler
            CompanyTermContext companyTermContext = new InternetCompanyTermContext();
            companyTermContext.setCampaignContext(campaignContext);
            companyTermContext.setCompanyTerm(companyTerm);
            companyTermHandlerMap.put(company.getId(), companyTermContext);
        }

        collectCompanyInstruction(campaignContext);

        for (CompanyTermContext companyTermContext : companyTermHandlerMap.values()) {
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


        for (CompanyTermContext companyTermContext : companyTermHandlerMap.values()) {
            List<CompanyTermProperty> companyTermPropertyList = companyTermContext.getCompanyTermPropertyList();
            companyTermPropertyList.forEach(companyTermProperty -> baseManager.saveOrUpdate(CompanyTermProperty.class.getName(), companyTermProperty));
            List<Account> accountList = companyTermContext.getAccountList();
            accountList.forEach(account -> baseManager.saveOrUpdate(Account.class.getName(), account));
        }

    }

    /**
     * 开始新的回合
     * @param campaignContext
     */
    private void newRound(CampaignContext campaignContext) {
        Campaign campaign = campaignContext.getCampaign();
        campaign.setCurrentCampaignDate(campaign.getNextCampaignDate());
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermContext preCompanyTermContext = companyTermHandlerMap.get(companyId);
            Company company = preCompanyTermContext.getCompanyTerm().getCompany();
            company.setCurrentCampaignDate(campaign.getCurrentCampaignDate());
            //生成新回合的companyTerm
            CompanyTerm companyTerm = new CompanyTerm();
            companyTerm.setCampaign(campaign);
            companyTerm.setCompany(company);
            companyTerm.setCampaignDate(campaign.getCurrentCampaignDate());
            //生成新回合的companyTermHandler
            CompanyTermContext companyTermContext = new InternetCompanyTermContext();
            companyTermContext.setCampaignContext(campaignContext);
            companyTermContext.setCompanyTerm(companyTerm);
            companyTermContext.setPreCompanyTermContext(preCompanyTermContext);
            //更新campaignCenter
            companyTermHandlerMap.put(companyId, companyTermContext);
        }
        campaignContext.next();
    }

    /**
     * 新回合开始后
     * @param campaignContext
     */
    private void randomChoice(CampaignContext campaignContext) {
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

    private void collectCompanyInstruction(CampaignContext campaignContext) {
        List<CompanyTermInstruction> allCompanyTermInstructionList = new ArrayList<>();
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermContext companyTermContext = companyTermHandlerMap.get(companyId);
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
            List<CompanyTermInstruction> companyTermInstructionList = instructionManager.listCompanyInstruction(companyTerm);
            companyTermContext.putCompanyInstructionList(companyTermInstructionList);
            allCompanyTermInstructionList.addAll(companyTermInstructionList);
        }
        campaignContext.addAllCurrentInstruction(allCompanyTermInstructionList);
    }

    private void competitiveBidding(CampaignContext campaignContext) {

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

    private void competitiveUnBidding(CampaignContext campaignContext) {
        Set<CompanyTermInstruction> companyTermInstructionSet = campaignContext.getCurrentCompanyTermInstructionSet();
        companyTermInstructionSet.stream().filter(companyInstruction -> EInstructionStatus.DQD.getValue().equals(companyInstruction.getStatus())).forEach(companyInstruction -> {
            companyInstruction.setStatus(EInstructionStatus.YXZ.getValue());
//            baseManager.saveOrUpdate(CompanyInstruction.class.getName(), companyInstruction);
        });
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

    private void calculateProperty(CampaignContext campaignContext) {
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

    private void calculateAccount(CampaignContext campaignContext) {
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

    private void saveCampaignContext (CampaignContext campaignContext) {
        Campaign campaign = campaignContext.getCampaign();
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermContext companyTermContext = companyTermHandlerMap.get(companyId);
            CompanyTermContext preCompanyTermContext = companyTermContext.getPreCompanyTermContext();
            List<CompanyTermInstruction> preCompanyTermInstructionList = preCompanyTermContext.getCompanyTermInstructionList();
            preCompanyTermInstructionList.forEach(companyInstruction -> baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyInstruction));
            List<CompanyTermProperty> preCompanyTermPropertyList = preCompanyTermContext.getCompanyTermPropertyList();
            preCompanyTermPropertyList.forEach(companyTermProperty -> baseManager.saveOrUpdate(CompanyTermProperty.class.getName(), companyTermProperty));
            List<Account> preAccountList = preCompanyTermContext.getAccountList();
            preAccountList.forEach(account -> baseManager.saveOrUpdate(Account.class.getName(), account));
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
            baseManager.saveOrUpdate(CompanyTerm.class.getName(), companyTerm);
            Company company = companyTerm.getCompany();
            baseManager.saveOrUpdate(Company.class.getName(),company);
        }
    }

    /**
     * 按条件淘汰部分公司
     */
    public void dieOut(CampaignContext campaignContext) {
        Iterator<CompanyTermContext> companyTermContextIterator = campaignContext.getCompanyTermContextMap().values().iterator();
        while (companyTermContextIterator.hasNext()) {
            CompanyTermContext companyTermContext = companyTermContextIterator.next();
            Company company = companyTermContext.getCompanyTerm().getCompany();
            Integer companyCash = accountManager.getCompanyCash(company);
            if (companyCash < 0) {
                company.setStatus(ECompanyStatus.END.name());
                companyTermContextIterator.remove();
            }
        }
    }

    public void end(CampaignContext campaignContext) {
        Integer endDate = campaignContext.getCampaign().getIndustry().getTotalTerm() + 1;
        if (campaignContext.getCampaign().getCurrentCampaignDate().equals(endDate)) {
            for (CompanyTermContext companyTermContext : campaignContext.getCompanyTermContextMap().values()) {
                Company company = companyTermContext.getCompanyTerm().getCompany();
                if (company.getCurrentCampaignDate().equals(endDate)) {
                    company.setStatus(ECompanyStatus.FINISH.name());
                    Integer campaignDateInCash = accountManager.countAccountEntryFee(
                            company, companyTermContext.getPreCompanyTermContext().getCompanyTerm().getCampaignDate(), EAccountEntityType.COMPANY_CASH.name(), "1");
                    Integer result = campaignDateInCash * 4 * 10;
                    company.setResult(result);
                }
            }
        }
    }

    @Override
    public void reset(String campaignId) {
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), campaignId);
        SessionFactory sessionFactory = (SessionFactory) ApplicationContextUtil.getApplicationContext().getBean("sessionFactory");
        Session session = sessionFactory.getCurrentSession();

        //删除所有决策信息
        XQuery instructionQuery = new XQuery();
        instructionQuery.setHql("from CompanyTermInstruction where campaign.id = :campaignId");
        instructionQuery.put("campaignId", campaign.getId());
        List<CompanyTermInstruction> companyTermInstructionList = baseManager.listObject(instructionQuery);
        if (companyTermInstructionList != null) {
            companyTermInstructionList.forEach(session::delete);
        }

        //删除所有属性信息 及财务信息
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTerm where campaign.id = :campaignId");
        xQuery.put("campaignId", campaignId);
        List<CompanyTerm> companyTermList = baseManager.listObject(xQuery);
        if (companyTermList != null) {
            for (CompanyTerm companyTerm : companyTermList) {
                XQuery propertyQuery = new XQuery();
                propertyQuery.setHql("from CompanyTermProperty where companyTerm.id = :companyTermId");
                propertyQuery.put("companyTermId", companyTerm.getId());
                List<CompanyTermProperty> companyTermPropertyList = baseManager.listObject(propertyQuery);
                if (companyTermPropertyList != null) {
                    companyTermPropertyList.forEach(session::delete);
                }
                XQuery accountQuery = new XQuery();
                accountQuery.setHql("from Account where companyTerm.id = :companyTermId");
                accountQuery.put("companyTermId", companyTerm.getId());
                List<Account> accountList = baseManager.listObject(accountQuery);
                if (accountList != null) {
                    accountList.forEach(session::delete);
                }
                session.delete(companyTerm);
            }
        }

        campaign.setCurrentCampaignDate(0);
        campaign.setStatus(Campaign.Status.PREPARE.getValue());
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);
        List<Company> companyList = campaignManager.listCompany(campaign);
        for (Company company : companyList) {
            company.setCurrentCampaignDate(campaign.getCurrentCampaignDate());
            company.setStatus(ECompanyStatus.PREPARE.name());
            baseManager.saveOrUpdate(Company.class.getName(), company);
        }
    }

}
