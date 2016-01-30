package com.rathink.ie.ibase.service;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.base.component.CyclePublisher;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.team.model.ECompanyStatus;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.component.BaseCampaignContext;
import com.rathink.ie.ibase.component.CampContext;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.work.model.CompanyPart;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryExpression;
import com.rathink.ie.internet.EAccountEntityType;
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
public abstract class AbstractFlowManager<T extends BaseCampaignContext> implements FlowManager<CampContext> {
    private static Logger logger = LoggerFactory.getLogger(AbstractFlowManager.class);
    protected T campaignContext;

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
    @Autowired
    protected CompanyTermManager companyTermManager;
    @Autowired
    protected CompanyPartManager companyPartManager;


    //上下文变量
    protected String campaignId;
    protected Campaign campaign;



    protected void init(String campaignId){

        this.campaignId = campaignId;
        campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), campaignId);
        campaignContext = (T) CampaignCenter.getCampaignHandler(campaignId);
    }

    protected T getContext() {
        return (T) campaignContext;
    }

    @Override
    public void begin(String campaignId) {
        init(campaignId);

        initCampaignContext();

        newRound();

        saveCampaignContext();

        randomChoice();

        initCyclePublisher();
    }

    public void next(String campaignId) {
        init(campaignId);

        //子类实现
        process();

        //回合结束进入新的回合
        newRound();


        //新回合开始后

        saveCampaignContext();

        randomChoice();

        dieOut();

//        end();
    }


    private void initCampaignContext(){
        final Integer INIT_CAMPAIGN_DATE = 0;

        //1.比赛开始
        campaign.setCurrentCampaignDate(INIT_CAMPAIGN_DATE);
        campaign.setStatus(Campaign.Status.RUN.getValue());
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);
        //2.初始化各公司
        Map<String, CompanyTermContext> companyTermContextMap = campaignContext.getCompanyTermContextMap();
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
            companyTermContext.setCompany(company);
            companyTermContext.setCompanyTerm(companyTerm);
            companyTermContextMap.put(company.getId(), companyTermContext);
        }

        initPartList();
        initPropertyList();
        initAccountList();

        for (CompanyTermContext companyTermContext : companyTermContextMap.values()) {
            List<CompanyTermProperty> companyTermPropertyList = companyTermContext.getCompanyTermPropertyList();
            companyTermPropertyList.forEach(companyTermProperty -> baseManager.saveOrUpdate(CompanyTermProperty.class.getName(), companyTermProperty));
            List<Account> accountList = companyTermContext.getAccountList();
            accountList.forEach(account -> baseManager.saveOrUpdate(Account.class.getName(), account));
        }

        this.initSelfCampaignContext();
    }

    protected abstract void initSelfCampaignContext();

    protected abstract void initPartList();
    /**
     * 在比赛开始时 初始化起始属性数据
     */
    protected abstract void initPropertyList();
    /**
     * 在比赛开始时 初始化起始财务数据
     */
    protected abstract void initAccountList();



    protected abstract void process();

    /**
     * 开始新的回合
     */
    private void newRound() {
        campaign.setCurrentCampaignDate(campaign.getNextCampaignDate());
        Map<String, CompanyTermContext> companyTermContextMap = campaignContext.getCompanyTermContextMap();
        for (String companyId : companyTermContextMap.keySet()) {
            CompanyTermContext preCompanyTermContext = companyTermContextMap.get(companyId);
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
            companyTermContext.setCompany(company);
            companyTermContext.setCompanyTerm(companyTerm);
            companyTermContext.setPreCompanyTermContext(preCompanyTermContext);
            //更新campaignCenter
            companyTermContextMap.put(companyId, companyTermContext);
        }
        campaignContext.setCampaign(campaign);
//        campaignContext.next();
    }

    private void saveCampaignContext () {
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
          /*  List<CompanyPart> companyPartList = campaignContext.getCompanyPartMap().get(companyId);
            companyPartList.forEach(companyPart -> baseManager.saveOrUpdate(CompanyPart.class.getName(), companyPart));*/
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
        Integer endDate = campaign.getIndustry().getTotalTerm() + 1;
        if (campaign.getCurrentCampaignDate().equals(endDate)) {
            Map<String, CompanyTermContext> companyTermContextMap = campaignContext.getCompanyTermContextMap();
            for (CompanyTermContext companyTermContext : companyTermContextMap.values()) {
                Company company = companyTermContext.getCompanyTerm().getCompany();
                if (company.getCurrentCampaignDate().equals(endDate)) {
                    company.setStatus(ECompanyStatus.FINISH.name());
                    Integer campaignDateInCash = accountManager.countAccountEntryFee(
                            company, companyTermContext.getPreCompanyTermContext().getCompanyTerm().getCampaignDate(), EAccountEntityType.COMPANY_CASH.name(), "1");
                    Integer result = campaignDateInCash * 4 * 10;
                    company.setResult(result);
                }
            }

            //清空campaignContext数据
        }
    }

    public void initCyclePublisher() {
        CyclePublisher cyclePublisher = (CyclePublisher) ApplicationContextUtil.getBean("cyclePublisher");
        cyclePublisher.startTime();//开始计时
        cyclePublisher.setCampaignContext(campaignContext);
        campaignContext.setCyclePublisher(cyclePublisher);
    }

    @Override
    public void reset(String campaignId) {
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), campaignId);
        SessionFactory sessionFactory = (SessionFactory) ApplicationContextUtil.getApplicationContext().getBean("sessionFactory");
        Session session = sessionFactory.getCurrentSession();

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

                //删除所有决策信息
                XQuery instructionQuery = new XQuery();
                instructionQuery.setHql("from CompanyTermInstruction where companyTerm.id = :companyTermId");
                instructionQuery.put("companyTermId", companyTerm.getId());
                List<CompanyTermInstruction> companyTermInstructionList = baseManager.listObject(instructionQuery);
                if (companyTermInstructionList != null) {
                    companyTermInstructionList.forEach(session::delete);
                }

                session.delete(companyTerm);
            }
        }

        //删除所有part
        XQuery partQuery = new XQuery();
        partQuery.setHql("from CompanyPart where campaign.id = :campaignId");
        partQuery.put("campaignId", campaign.getId());
        List<CompanyPart> companyPartList = baseManager.listObject(partQuery);
        if (companyPartList != null) {
            companyPartList.forEach(session::delete);
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
