package com.rathink.ie.ibase.service;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.team.model.ECompanyStatus;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryExpression;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.internet.service.FlowManager;
import com.rathink.ie.internet.service.impl.InternetCompanyTermContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Hean on 2015/10/6.
 */
@Service
public abstract class AbstractFlowManager implements FlowManager {
    private static Logger logger = LoggerFactory.getLogger(AbstractFlowManager.class);
    protected CampaignContext campaignContext;

    @Autowired
    protected BaseManager baseManager;
    @Autowired
    protected InstructionManager instructionManager;
    @Autowired
    protected CampaignManager campaignManager;
    @Autowired
    protected AccountManager accountManager;
    @Autowired
    protected IndustryResourceManager industryResourceManager;
    @Autowired
    protected IndustryResourceChoiceManager industryResourceChoiceManager;

    @Override
    public void begin(String campaignId) {
        campaignContext = CampaignCenter.getCampaignHandler(campaignId);

        initCampaignContext();

        newRound();

        saveCampaignContext();

        randomChoice();

    }

    public void next(String campaignId) {
        campaignContext = CampaignCenter.getCampaignHandler(campaignId);

        //回合结束前
        //收集决策信息
        collectCompanyInstruction(campaignContext);
        //竞标决策
        competitiveBidding();
        //非竞标决策
        competitiveUnBidding();
        //计算保存新回合的属性数据
        calculateProperty();
        //4计算保存新回合的财务数据
        calculateAccount();


        //回合结束进入新的回合
        newRound();


        //新回合开始后

        saveCampaignContext();

        randomChoice();

        dieOut();

        end();
    }


    public void initCampaignContext(){
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

        initPropertyList();
        initAccountList();

        for (CompanyTermContext companyTermContext : companyTermHandlerMap.values()) {
            List<CompanyTermProperty> companyTermPropertyList = companyTermContext.getCompanyTermPropertyList();
            companyTermPropertyList.forEach(companyTermProperty -> baseManager.saveOrUpdate(CompanyTermProperty.class.getName(), companyTermProperty));
            List<Account> accountList = companyTermContext.getAccountList();
            accountList.forEach(account -> baseManager.saveOrUpdate(Account.class.getName(), account));
        }

        XQuery xQuery = new XQuery();
        xQuery.setHql("from IndustryExpression");
        List<IndustryExpression> industryExpressionList = baseManager.listObject(xQuery);
        for (IndustryExpression industryExpression : industryExpressionList) {
            campaignContext.getExpressionMap().put(industryExpression.getName(),industryExpression.getExpression());
        }
    }

    /**
     * 在比赛开始时 初始化起始属性数据
     */
    protected abstract void initPropertyList();
    /**
     * 在比赛开始时 初始化起始财务数据
     */
    protected abstract void initAccountList();


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

    /**
     * 处理竞标结果
     */
    protected abstract void competitiveBidding();

    /**
     * 处理非竞标结果
     */
    protected void competitiveUnBidding() {
        Set<CompanyTermInstruction> companyTermInstructionSet = campaignContext.getCurrentCompanyTermInstructionSet();
        companyTermInstructionSet.stream().filter(companyInstruction -> EInstructionStatus.DQD.getValue().equals(companyInstruction.getStatus())).forEach(companyInstruction -> {
            companyInstruction.setStatus(EInstructionStatus.YXZ.getValue());
        });
    }

    /**
     * 计算属性数据
     */
    protected abstract void calculateProperty();

    /**
     * 计算财务数据
     */
    protected abstract void calculateAccount();

    /**
     * 开始新的回合
     */
    private void newRound() {
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

    private void saveCampaignContext () {
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
            baseManager.saveOrUpdate(Company.class.getName(), company);
        }
    }

    /**
     * 产生供决策用的选项数据
     */
    protected abstract void randomChoice();

    /**
     * 按条件淘汰部分公司
     */
    public void dieOut() {
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

    /**
     * 比赛进度到达最后一轮时 结束比赛
     */
    public void end() {
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
