package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.team.model.ECompanyStatus;
import com.rathink.ie.foundation.util.CampaignUtil;
import com.rathink.ie.foundation.util.RandomUtil;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.service.AccountManager;
import com.rathink.ie.ibase.service.CampaignCenter;
import com.rathink.ie.ibase.service.CampaignContext;
import com.rathink.ie.ibase.service.CompanyTermContext;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.ibase.work.model.Resource;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.service.ChoiceManager;
import com.rathink.ie.internet.service.FlowManager;
import com.rathink.ie.internet.service.InstructionManager;
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
    private ChoiceManager choiceManager;
    @Autowired
    private InstructionManager instructionManager;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private AccountManager accountManager;
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

        randomChoice(campaignContext);

        saveCampaignContext(campaignContext);
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
        //释放未被选择的人才
        releaseHuman(campaignContext);
        //竞标决策
        competitiveBidding(campaignContext);
        //非竞标决策
        competitiveUnBidding(campaignContext);

        calculateCompetitionMap(campaignContext);
        //计算保存新回合的属性数据
        calculateProperty(campaignContext);
        //4计算保存新回合的财务数据
        calculateAccount(campaignContext);


        //回合结束进入新的回合
        newRound(campaignContext);


        //新回合开始后
        campaignContext = CampaignCenter.getCampaignHandler(campaignId);

        randomChoice(campaignContext);

        saveCampaignContext(campaignContext);

        dieOut(campaignContext);

        end(campaignContext);
    }

    public void initCampaignContext(CampaignContext campaignContext){
        final String INIT_CAMPAIGN_DATE = "010101";

        //1.比赛开始
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), campaignContext.getCampaign().getId());
        campaign.setCurrentCampaignDate(CampaignUtil.getPreCampaignDate(INIT_CAMPAIGN_DATE));
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
        }

        calculateAccount(campaignContext);

        for (CompanyTermContext companyTermContext : companyTermHandlerMap.values()) {
            List<CompanyTermProperty> companyTermPropertyList = companyTermContext.getCompanyTermPropertyList();
            companyTermPropertyList.forEach(companyTermProperty -> baseManager.saveOrUpdate(CompanyTermProperty.class.getName(), companyTermProperty));
            List<Account> accountList = companyTermContext.getAccountList();
            accountList.forEach(account -> baseManager.saveOrUpdate(Account.class.getName(), account));
        }

        XQuery xQuery = new XQuery();
        xQuery.setHql("from Resource where baseType = 'HUMAN'");
        List<Resource> allHumanList = baseManager.listObject(xQuery);
        campaignContext.addHumanResource(new HashSet<>(allHumanList));
    }

    private void releaseHuman(CampaignContext campaignContext) {
        Set<Resource> humanResourceSet = new HashSet<>();
        Set<CompanyInstruction> companyInstructionSet = campaignContext.getCurrentCompanyInstructionSet();
        Set<String> instructionChoiceIdSet = companyInstructionSet.stream().filter(companyInstruction -> companyInstruction.getBaseType().equals(EChoiceBaseType.HUMAN.name()))
                .map(companyInstruction -> companyInstruction.getCompanyChoice().getId()).collect(Collectors.toSet());
        List<CompanyChoice> companyChoiceList = campaignContext.getCurrentCompanyChoiceList();
        companyChoiceList.stream().filter(companyChoice ->
                companyChoice.getBaseType().equals(EChoiceBaseType.HUMAN.name()) && !instructionChoiceIdSet.contains(companyChoice.getId()))
                .forEach(companyChoice -> {
                    Resource human = new Resource();
                    human.setBaseType(companyChoice.getBaseType());
                    human.setDept(companyChoice.getDept());
                    human.setName(companyChoice.getName());
                    human.setType(companyChoice.getType());
                    human.setFees(companyChoice.getFees());
                    human.setValue(companyChoice.getValue());
                    human.setImg(companyChoice.getImg());
                    humanResourceSet.add(human);
                });
        campaignContext.addHumanResource(humanResourceSet);
    }

    /**
     * 开始新的回合
     * @param campaignContext
     */
    private void newRound(CampaignContext campaignContext) {
        Campaign campaign = campaignContext.getCampaign();
        campaign.setCurrentCampaignDate(CampaignUtil.getNextCampaignDate(campaign.getCurrentCampaignDate()));
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
        //准备供用户决策用的随机数据
        List<CompanyChoice> companyChoiceList = choiceManager.randomChoices(campaignContext.getCampaign());
        campaignContext.setCurrentCompanyChoiceList(companyChoiceList);
    }

    private void collectCompanyInstruction(CampaignContext campaignContext) {
        List<CompanyInstruction> allCompanyInstructionList = new ArrayList<>();
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermContext companyTermContext = companyTermHandlerMap.get(companyId);
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
            List<CompanyInstruction> companyInstructionList = instructionManager.listCompanyInstruction(companyTerm);
            companyTermContext.putCompanyInstructionList(companyInstructionList);
            allCompanyInstructionList.addAll(companyInstructionList);
        }
        campaignContext.addAllCurrentInstruction(allCompanyInstructionList);
    }

    private void competitiveBidding(CampaignContext campaignContext) {

        List<CompanyChoice> companyChoiceList = campaignContext.listCurrentCompanyChoiceByType(EChoiceBaseType.HUMAN.name());
        if (companyChoiceList == null || companyChoiceList.size() == 0) return;
        //竞标
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (CompanyChoice companyChoice : companyChoiceList) {
            List<CompanyInstruction> companyInstructionList = campaignContext.listCurrentCompanyInstructionByChoice(companyChoice.getId());
            if (companyInstructionList != null && companyInstructionList.size() > 0) {
                Double maxRecruitmentRatio = 0d;
                CompanyInstruction successCompanyInstruction = null;
                for (CompanyInstruction companyInstruction : companyInstructionList) {
                    CompanyTermContext companyTermContext = companyTermHandlerMap.get(companyInstruction.getCompany().getId());
                    Double fee = Double.valueOf(companyInstruction.getValue());
                    Double feeRatio = Math.pow(fee, 0.51) * 0.35;//薪酬系数
                    Integer randomRatio = RandomUtil.random(0, 70);
                    Double recruitmentRatio = feeRatio * 30 / 100 + randomRatio;//招聘能力系数
                    logger.info("公司：{}，员工：{}，工资：{}，薪酬系数：{}，随机值：{}，招聘能力系数：{}",
                            companyTermContext.getCompanyTerm().getCompany().getName(), companyChoice.getName(),
                            fee, feeRatio, randomRatio, recruitmentRatio);
                    if (recruitmentRatio > maxRecruitmentRatio) {
                        maxRecruitmentRatio = recruitmentRatio;
                        successCompanyInstruction = companyInstruction;
                    }
                }
                successCompanyInstruction.setStatus(EInstructionStatus.YXZ.getValue());
                //保存选中的
//                baseManager.saveOrUpdate(CompanyInstruction.class.getName(), successCompanyInstruction);
                //保存未选中的
                companyInstructionList.stream().filter(companyInstruction -> EInstructionStatus.DQD.getValue().equals(companyInstruction.getStatus())).forEach(companyInstruction -> {
                    companyInstruction.setStatus(EInstructionStatus.WXZ.getValue());
//                    baseManager.saveOrUpdate(CompanyInstruction.class.getName(), companyInstruction);
                });
            }
        }

    }

    private void competitiveUnBidding(CampaignContext campaignContext) {
        Set<CompanyInstruction> companyInstructionSet = campaignContext.getCurrentCompanyInstructionSet();
        companyInstructionSet.stream().filter(companyInstruction -> EInstructionStatus.DQD.getValue().equals(companyInstruction.getStatus())).forEach(companyInstruction -> {
            companyInstruction.setStatus(EInstructionStatus.YXZ.getValue());
//            baseManager.saveOrUpdate(CompanyInstruction.class.getName(), companyInstruction);
        });
    }


    private void calculateCompetitionMap(CampaignContext campaignContext) {
        Map<String, Integer> competitionMap = new HashMap<>();
        List<CompanyChoice> companyChoiceList = new ArrayList<>();
        //产品定位
        List<CompanyChoice> productStudyList = campaignContext.listCurrentCompanyChoiceByType(EChoiceBaseType.PRODUCT_STUDY.name());
        if(productStudyList!=null) companyChoiceList.addAll(productStudyList);
        //市场活动
        List<CompanyChoice> marketActivityList = campaignContext.listCurrentCompanyChoiceByType(EChoiceBaseType.MARKET_ACTIVITY.name());
        if(marketActivityList!=null) companyChoiceList.addAll(marketActivityList);
        for (CompanyChoice companyChoice : companyChoiceList) {
            List<CompanyInstruction> companyInstructionList = campaignContext.listCurrentCompanyInstructionByChoice(companyChoice.getId());
            if (companyInstructionList != null && companyInstructionList.size() > 0) {
                competitionMap.put(companyChoice.getId(), companyInstructionList.size());
            }
        }
        campaignContext.setCompetitionMap(competitionMap);

    }

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
        String currentCampaignDate = campaignContext.getCampaign().getCurrentCampaignDate();
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermContext companyTermContext = companyTermHandlerMap.get(companyId);
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
            List<Account> accountList = new ArrayList<>();

            List<CompanyInstruction> humanInstructionList = instructionManager.listCompanyInstructionByType(companyTerm.getCompany(), EChoiceBaseType.HUMAN.name());
            Iterator<CompanyInstruction> companyInstructionIterator = humanInstructionList.iterator();
            while (companyInstructionIterator.hasNext()) {
                CompanyInstruction companyInstruction = companyInstructionIterator.next();
                if (companyInstruction.getCampaignDate().equals(currentCampaignDate)) {
                    companyInstructionIterator.remove();
                }
            }
            Integer humanFee = instructionManager.sumFee(humanInstructionList) * CampaignUtil.TIME_UNIT;
            Account humanAccount = accountManager.packageAccount(String.valueOf(humanFee), EAccountEntityType.HR_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(humanAccount);

            Integer adFee = humanFee * 20 / 100 + humanInstructionList.size() * 2000 + 20000;
            Account adAccount = accountManager.packageAccount(String.valueOf(adFee), EAccountEntityType.AD_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(adAccount);

            List<CompanyInstruction> productFeeInstructionList = companyTermContext.listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY_FEE.name());
            Integer productFee = instructionManager.sumFee(productFeeInstructionList);
            Account productFeeAccount = accountManager.packageAccount(String.valueOf(productFee), EAccountEntityType.PRODUCT_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(productFeeAccount);

            List<CompanyInstruction> marketFeeInstructionList = companyTermContext.listCompanyInstructionByType(EChoiceBaseType.MARKET_ACTIVITY.name());
            Integer marketFee = instructionManager.sumFee(marketFeeInstructionList);
            Account marketFeeAccount = accountManager.packageAccount(String.valueOf(marketFee), EAccountEntityType.MARKET_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(marketFeeAccount);

            List<CompanyInstruction> operationFeeInstructionList = companyTermContext.listCompanyInstructionByType(EChoiceBaseType.OPERATION.name());
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
            List<CompanyInstruction> preCompanyInstructionList = preCompanyTermContext.getCompanyInstructionList();
            preCompanyInstructionList.forEach(companyInstruction -> baseManager.saveOrUpdate(CompanyInstruction.class.getName(), companyInstruction));
            List<CompanyTermProperty> preCompanyTermPropertyList = preCompanyTermContext.getCompanyTermPropertyList();
            preCompanyTermPropertyList.forEach(companyTermProperty -> baseManager.saveOrUpdate(CompanyTermProperty.class.getName(), companyTermProperty));
            List<Account> preAccountList = preCompanyTermContext.getAccountList();
            preAccountList.forEach(account -> baseManager.saveOrUpdate(Account.class.getName(), account));
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
            baseManager.saveOrUpdate(CompanyTerm.class.getName(), companyTerm);
            Company company = companyTerm.getCompany();
            baseManager.saveOrUpdate(Company.class.getName(),company);
        }
        List<CompanyChoice> companyChoiceList = campaignContext.getCurrentCompanyChoiceList();
        companyChoiceList.forEach(companyChoice -> baseManager.saveOrUpdate(CompanyChoice.class.getName(), companyChoice));
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
        final String END_DATE = "030101";
        if (campaignContext.getCampaign().getCurrentCampaignDate().equals(END_DATE)) {
            for (CompanyTermContext companyTermContext : campaignContext.getCompanyTermContextMap().values()) {
                Company company = companyTermContext.getCompanyTerm().getCompany();
                if (company.getCurrentCampaignDate().equals(END_DATE)) {
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
        instructionQuery.setHql("from CompanyInstruction where campaign.id = :campaignId");
        instructionQuery.put("campaignId", campaign.getId());
        List<CompanyInstruction> companyInstructionList = baseManager.listObject(instructionQuery);
        if (companyInstructionList != null) {
            companyInstructionList.forEach(session::delete);
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


        //删除所有随机选项
        XQuery choiceQuery = new XQuery();
        choiceQuery.setHql("from CompanyChoice where campaign.id = :campaignId");
        choiceQuery.put("campaignId", campaign.getId());
        List<CompanyChoice> companyChoiceList = baseManager.listObject(choiceQuery);
        if (companyChoiceList != null) {
            companyChoiceList.forEach(session::delete);
        }
        campaign.setCurrentCampaignDate(CampaignUtil.getPreCampaignDate("010101"));
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
