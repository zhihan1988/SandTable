package com.rathink.iix.ibase.service;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.p.service.AutoSerialManager;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.team.model.ECompanyStatus;
import com.rathink.iix.ibase.component.CampaignServer;
import com.rathink.iix.ibase.component.MemoryCampaign;
import com.rathink.iix.ibase.component.MemoryCompany;
import com.rathink.ix.ibase.account.model.Account;
import com.rathink.ix.ibase.property.model.CompanyTerm;
import com.rathink.ix.ibase.service.*;
import com.rathink.ix.ibase.work.model.CompanyPart;
import com.rathink.ix.ibase.work.model.CompanyTermInstruction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hean on 2016/2/2.
 */
@Service
public abstract class IBaseService<T extends MemoryCampaign> implements FlowManager{

    @Autowired
    protected BaseManager baseManager;
    @Autowired
    protected AutoSerialManager autoSerialManager;
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

    protected Campaign campaign;
    protected List<Company> companyList;
    protected T memoryCampaign;

    public void begin(String campaignId) {
        init(campaignId);
        initContext(campaignId);
        newRound();
        randomChoice();
    }

    public void next(String campaignId) {
        initContext(campaignId);
        beforeProcess();
        newRound();
        afterProcess();
        randomChoice();
        end();
    }

    public void reset(String campaignId){
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), campaignId);
        SessionFactory sessionFactory = (SessionFactory) ApplicationContextUtil.getApplicationContext().getBean("sessionFactory");
        Session session = sessionFactory.getCurrentSession();

        XQuery accountQuery = new XQuery();
        accountQuery.setHql("from Account where campaign.id = :campaignId");
        accountQuery.put("campaignId", campaign.getId());
        List<Account> accountList = baseManager.listObject(accountQuery);
        if (accountList != null) {
            accountList.forEach(session::delete);
        }


        XQuery instructionQuery = new XQuery();
        instructionQuery.setHql("from CompanyTermInstruction where campaign.id = :campaignId");
        instructionQuery.put("campaignId", campaign.getId());
        List<CompanyTermInstruction> companyTermInstructionList = baseManager.listObject(instructionQuery);
        if (companyTermInstructionList != null) {
            companyTermInstructionList.forEach(session::delete);
        }

        //删除所有part
        XQuery partQuery = new XQuery();
        partQuery.setHql("from CompanyPart where campaign.id = :campaignId");
        partQuery.put("campaignId", campaign.getId());
        List<CompanyPart> companyPartList = baseManager.listObject(partQuery);
        if (companyPartList != null) {
            companyPartList.forEach(session::delete);
        }

        //删除所有属性信息 及财务信息
       /* XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTerm where campaign.id = :campaignId");
        xQuery.put("campaignId", campaignId);
        List<CompanyTerm> companyTermList = baseManager.listObject(xQuery);
        if (companyTermList != null) {
            companyTermList.forEach(session::delete);
        }*/

        campaign.setCurrentCampaignDate(0);
        campaign.setStatus(Campaign.Status.PREPARE.getValue());
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);
        List<Company> companyList = campaignManager.listCompany(campaign);
        for (Company company : companyList) {
            company.setCurrentCampaignDate(campaign.getCurrentCampaignDate());
            company.setStatus(ECompanyStatus.PREPARE.name());
            baseManager.saveOrUpdate(Company.class.getName(), company);
        }

        CampaignServer.remove(campaignId);
    }

    /**
     * 初始化一场新的比赛并构建内存对象
     * @param campaignId
     */
    protected abstract void init(String campaignId);

    /**
     * 初始化上下文
     * @param campaignId
     */
    protected void initContext(String campaignId) {
        this.memoryCampaign = (T)CampaignServer.getMemoryCampaign(campaignId);
        this.campaign = memoryCampaign.getCampaign();
        this.companyList = new ArrayList<>();
        for (MemoryCompany memoryCompany : memoryCampaign.getMemoryCompanyMap().values()) {
            this.companyList.add(memoryCompany.getCompany());
        }
    }

    /**
     * 开始新的回合
     */
    protected void newRound() {
        /*campaign.setCurrentCampaignDate(campaign.getNextCampaignDate());
        for (Company company : companyList) {
            company.setCurrentCampaignDate(campaign.getCurrentCampaignDate());
        }*/
        memoryCampaign.nextTerm();
    }

    protected abstract void beforeProcess();
    protected abstract void afterProcess();

    protected abstract void randomChoice();

    protected abstract void end();

}
