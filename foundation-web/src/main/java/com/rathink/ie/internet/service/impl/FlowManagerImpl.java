package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignCenterManager;
import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.ie.foundation.service.impl.CampaignCenterManagerImpl;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.util.CampaignUtil;
import com.rathink.ie.foundation.util.RandomUtil;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.service.*;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.service.ChoiceManager;
import com.rathink.ie.internet.service.FlowManager;
import com.rathink.ie.internet.service.InstructionManager;
import com.rathink.ie.internet.service.InternetPropertyManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/8/24.
 */
@Service
public class FlowManagerImpl implements FlowManager {

    @Autowired
    private BaseManager baseManager;
    @Autowired
    private ChoiceManager choiceManager;
    @Autowired
    private InstructionManager instructionManager;
    @Autowired
    private CompanyTermManager companyTermManager;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private InternetPropertyManager internetPropertyManager;
    @Autowired
    private CampaignCenterManager campaignCenterManager;

    /**
     * 开始游戏 定时任务执行
     * @param campaignId
     */
    @Override
    public void begin(String campaignId) {
        final String INIT_CAMPAIGN_DATE = "010101";
        CampaignHandler campaignHandler = CampaignCenter.getCampaignHandler(campaignId);
        //1.比赛开始
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), campaignId);
        campaign.setCurrentCampaignDate(INIT_CAMPAIGN_DATE);
        campaign.setStatus(Campaign.Status.RUN.getValue());
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);
        campaignHandler.setCampaign(campaign);

        //2.初始化各公司
        Map<String, CompanyTermHandler> companyTermHandlerMap = campaignHandler.getCompanyTermHandlerMap();
        List<Company> companyList = campaignManager.listCompany(campaign);
        for (Company company : companyList) {
            company.setCurrentCampaignDate(campaign.getCurrentCampaignDate());
            baseManager.saveOrUpdate(Company.class.getName(), company);
            //生成新回合的companyTerm
            CompanyTerm companyTerm = new CompanyTerm();
            companyTerm.setCampaign(company.getCampaign());
            companyTerm.setCompany(company);
            companyTerm.setCampaignDate(company.getCampaign().getCurrentCampaignDate());
            baseManager.saveOrUpdate(CompanyTerm.class.getName(), companyTerm);
            //生成新回合的companyTermHandler
            CompanyTermHandler companyTermHandler = new InternetCompanyTermHandler();
            companyTermHandler.setCampaignHandler(campaignHandler);
            companyTermHandler.setCompanyTerm(companyTerm);
            //计算保存新回合的属性数据
            calculateProperty(companyTermHandler);
            //初始化财务数据
            accountManager.initCompanyAccount(company);
            //更新campaignCenter
            companyTermHandlerMap.put(company.getId(), companyTermHandler);
        }
        //准备供用户决策用的随机数据
        choiceManager.produceChoice(campaignHandler.getCampaign());
    }

    /**
     * 开始新的回合
     * @param campaignId
     */
    public void next(String campaignId) {
        CampaignHandler campaignHandler = CampaignCenter.getCampaignHandler(campaignId);
        //回合结束前
        before(campaignHandler);
        //回合结束
        newRound(campaignHandler);
        //新回合开始
        after(campaignHandler);
    }


    public void before(CampaignHandler campaignHandler) {
        //获取property Instruction 等
        Map<String, CompanyTermHandler> companyTermHandlerMap = campaignHandler.getCompanyTermHandlerMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermHandler companyTermHandler = companyTermHandlerMap.get(companyId);
            CompanyTerm companyTerm = companyTermHandler.getCompanyTerm();
            List<CompanyInstruction> companyInstructionList = instructionManager.listCompanyInstruction(companyTerm);
            companyTermHandler.putCompanyInstructionList(companyInstructionList);
            List<CompanyTermProperty> companyTermPropertyList = internetPropertyManager.listCompanyTermProperty(companyTerm);
            companyTermHandler.putPropertyList(companyTermPropertyList);
        }

        //竞标决策
        competitiveBidding(campaignHandler);
        //非竞标决策
        competitiveUnBidding(campaignHandler);
    }

    public void newRound(CampaignHandler campaignHandler) {
        Campaign campaign = campaignHandler.getCampaign();
        campaign.setCurrentCampaignDate(CampaignUtil.getNextCampaignDate(campaign.getCurrentCampaignDate()));
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);
        Map<String, CompanyTermHandler> companyTermHandlerMap = campaignHandler.getCompanyTermHandlerMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermHandler preCompanyTermHandler = companyTermHandlerMap.get(companyId);
            Company company = preCompanyTermHandler.getCompanyTerm().getCompany();
            company.setCurrentCampaignDate(campaign.getCurrentCampaignDate());
            baseManager.saveOrUpdate(Company.class.getName(), company);
            //生成新回合的companyTerm
            CompanyTerm companyTerm = new CompanyTerm();
            companyTerm.setCampaign(campaign);
            companyTerm.setCompany(company);
            companyTerm.setCampaignDate(campaign.getCurrentCampaignDate());
            baseManager.saveOrUpdate(CompanyTerm.class.getName(), companyTerm);
            //生成新回合的companyTermHandler
            CompanyTermHandler companyTermHandler = new InternetCompanyTermHandler();
            companyTermHandler.setCampaignHandler(campaignHandler);
            companyTermHandler.setCompanyTerm(companyTerm);
            companyTermHandler.setPreCompanyTermHandler(preCompanyTermHandler);
            //更新campaignCenter
            companyTermHandlerMap.put(companyId, companyTermHandler);
        }
    }

    public void after(CampaignHandler campaignHandler) {
        Map<String, CompanyTermHandler> companyTermHandlerMap = campaignHandler.getCompanyTermHandlerMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermHandler companyTermHandler = companyTermHandlerMap.get(companyId);
            //计算保存新回合的属性数据
            calculateProperty(companyTermHandler);
            //4计算保存新回合的财务数据
            calculateAccount(companyTermHandler);
        }

        //准备供用户决策用的随机数据
        choiceManager.produceChoice(campaignHandler.getCampaign());
    }

    public void competitiveBidding(CampaignHandler campaignHandler) {

        Campaign campaign = campaignHandler.getCampaign();
        List<CompanyChoice> companyChoiceList = choiceManager.listCompanyChoice(campaign.getId(), campaign.getCurrentCampaignDate(), EChoiceBaseType.HUMAN.name());

        //竞标
        Map<String, CompanyTermHandler> companyTermHandlerMap = campaignHandler.getCompanyTermHandlerMap();
        for (CompanyChoice companyChoice : companyChoiceList) {
            List<CompanyInstruction> companyInstructionList = instructionManager.listCompanyInstruction(companyChoice);
            if (companyInstructionList != null && companyInstructionList.size() > 0) {
                int maxRecruitmentRatio = 0;
                CompanyInstruction successCompanyInstruction = null;
                for (CompanyInstruction companyInstruction : companyInstructionList) {
                    CompanyTermHandler preCompanyTermHandler = companyTermHandlerMap.get(companyInstruction.getCompany().getId()).getPreCompanyTermHandler();
                    Integer officeRatio = preCompanyTermHandler == null ? 50 : Integer.valueOf(preCompanyTermHandler.get(EPropertyName.OFFICE_RATIO.name()));
                    Integer fee = Integer.valueOf(companyInstruction.getValue());
                    Integer feeRatio = fee / 200;
                    Integer randomRatio = RandomUtil.random(0, 20);
                    Integer recruitmentRatio = officeRatio * 10 / 100 + feeRatio * 60 / 100 + randomRatio;
                    if (recruitmentRatio > maxRecruitmentRatio) {
                        maxRecruitmentRatio = recruitmentRatio;
                        successCompanyInstruction = companyInstruction;
                    }
                }
                successCompanyInstruction.setStatus(EInstructionStatus.YXZ.getValue());
                //保存选中的
                baseManager.saveOrUpdate(CompanyInstruction.class.getName(), successCompanyInstruction);
                //保存未选中的
                companyInstructionList.stream().filter(companyInstruction -> EInstructionStatus.DQD.getValue().equals(companyInstruction.getStatus())).forEach(companyInstruction -> {
                    companyInstruction.setStatus(EInstructionStatus.WXZ.getValue());
                    baseManager.saveOrUpdate(CompanyInstruction.class.getName(), companyInstruction);
                });
            }
        }

    }

    public void competitiveUnBidding(CampaignHandler campaignHandler) {
        Campaign campaign = campaignHandler.getCampaign();
        List<CompanyInstruction> companyInstructionList = instructionManager.listCampaignCompanyInstructionByDate(campaign.getId(), campaign.getCurrentCampaignDate());
        companyInstructionList.stream().filter(companyInstruction -> EInstructionStatus.DQD.getValue().equals(companyInstruction.getStatus())).forEach(companyInstruction -> {
            companyInstruction.setStatus(EInstructionStatus.YXZ.getValue());
            baseManager.saveOrUpdate(CompanyInstruction.class.getName(), companyInstruction);
        });
    }

    public void calculateProperty(CompanyTermHandler companyTermHandler) {
        CompanyTerm companyTerm = companyTermHandler.getCompanyTerm();
        List<CompanyTermProperty> companyTermPropertyList = new ArrayList<>();
        for (EPropertyName ePropertyName : EPropertyName.values()) {
            String value = companyTermHandler.calculate(ePropertyName.name());
            companyTermPropertyList.add(new CompanyTermProperty(ePropertyName, value, companyTerm));
        }
        companyTerm.setCompanyTermPropertyList(companyTermPropertyList);
        baseManager.saveOrUpdate(CompanyTerm.class.getName(), companyTerm);
    }

    private void calculateAccount(CompanyTermHandler companyTermHandler) {
        CompanyTerm companyTerm = companyTermHandler.getCompanyTerm();
        List<Account> accountList = new ArrayList<>();
        List<CompanyInstruction> officeInstructionList = companyTermHandler.listCompanyInstructionByType(EChoiceBaseType.OFFICE.name());
        Account adAccount = accountManager
                .saveAccount(officeInstructionList, EAccountEntityType.AD_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
        accountList.add(adAccount);
        List<CompanyInstruction> humanInstructionList = companyTermHandler.listCompanyInstructionByType(EChoiceBaseType.HUMAN.name());
        Account humanAccount = accountManager
                .saveAccount(humanInstructionList, EAccountEntityType.HR_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
        accountList.add(humanAccount);
        List<CompanyInstruction> productFeeInstructionList = companyTermHandler.listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY_FEE.name());
        Account productFeeAccount = accountManager
                .saveAccount(productFeeInstructionList, EAccountEntityType.PRODUCT_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
        accountList.add(productFeeAccount);
        List<CompanyInstruction> marketFeeInstructionList = companyTermHandler.listCompanyInstructionByType(EChoiceBaseType.MARKET_ACTIVITY.name());
        Account marketFeeAccount = accountManager
                .saveAccount(marketFeeInstructionList, EAccountEntityType.MARKET_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
        accountList.add(marketFeeAccount);
        List<CompanyInstruction> operationFeeInstructionList = companyTermHandler.listCompanyInstructionByType(EChoiceBaseType.OPERATION.name());
        Account operationFeeAccount = accountManager
                .saveAccount(operationFeeInstructionList, EAccountEntityType.OPERATION_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
        accountList.add(operationFeeAccount);
        String currentPeriodIncome = companyTermHandler.get(EPropertyName.CURRENT_PERIOD_INCOME.name());
        Account incomeAccount = accountManager
                .saveAccount(currentPeriodIncome, EAccountEntityType.OPERATION_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
        accountList.add(incomeAccount);
        companyTermHandler.setAccountList(accountList);
    }


    /**
     * 测试方法
     * 回到上一轮
     * @param campaignId
     */
    @Override
    public void pre(String campaignId) {
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), campaignId);
        SessionFactory sessionFactory = (SessionFactory) ApplicationContextUtil.getApplicationContext().getBean("sessionFactory");
        Session session = sessionFactory.getCurrentSession();

        String currentCampaignDate = campaign.getCurrentCampaignDate();
        String preCampaignDate = CampaignUtil.getPreCampaignDate(currentCampaignDate);

        //删除本轮以及上一轮的决策信息
        XQuery instructionQuery = new XQuery();
        instructionQuery.setHql("from CompanyInstruction where campaign.id = :campaignId and campaignDate in (:campaignDates)");
        instructionQuery.put("campaignId", campaign.getId());
        instructionQuery.put("campaignDates", new String[]{currentCampaignDate, preCampaignDate});
        List<CompanyInstruction> companyInstructionList = baseManager.listObject(instructionQuery);
        if (companyInstructionList != null) {
            companyInstructionList.forEach(session::delete);
        }

        //删除本轮属性信息 及财务信息
        List<Company> companyList = campaignManager.listCompany(campaign);
        for (Company company : companyList) {
            CompanyTerm companyTerm = companyTermManager.getCompanyTerm(company, currentCampaignDate);
            session.delete(companyTerm);
        }

        //删除本轮的随机选项
        XQuery choiceQuery = new XQuery();
        choiceQuery.setHql("from CompanyChoice where campaign.id = :campaignId and campaignDate = :campaignDate");
        choiceQuery.put("campaignId", campaign.getId());
        choiceQuery.put("campaignDate", currentCampaignDate);
        List<CompanyChoice> companyChoiceList = baseManager.listObject(choiceQuery);
        if (companyChoiceList != null) {
            companyChoiceList.forEach(session::delete);
        }

        //比赛时间回到上一轮
        campaign.setCurrentCampaignDate(preCampaignDate);
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);
        for (Company company : companyList) {
            company.setCurrentCampaignDate(campaign.getCurrentCampaignDate());
            baseManager.saveOrUpdate(Company.class.getName(), company);

        }

        //初始化handler
        campaignCenterManager.initCampaignHandler(campaign);
    }

}