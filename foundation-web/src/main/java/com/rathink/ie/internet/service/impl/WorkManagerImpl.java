package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignCenterManager;
import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.util.CampaignUtil;
import com.rathink.ie.foundation.util.RandomUtil;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyStatusProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.*;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.service.ChoiceManager;
import com.rathink.ie.internet.service.InstructionManager;
import com.rathink.ie.internet.service.InternetPropertyManager;
import com.rathink.ie.internet.service.WorkManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Hean on 2015/8/24.
 */
@Service
public class WorkManagerImpl implements WorkManager {

    @Autowired
    private BaseManager baseManager;
    @Autowired
    private ChoiceManager choiceManager;
    @Autowired
    private CompanyTermManager companyTermManager;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private CampaignCenterManager campaignCenterManager;

    /**
     * 开始游戏 定时任务执行
     * @param campaign
     */
    @Override
    public void begin(Campaign campaign) {
        final String INIT_CAMPAIGN_DATE = "010101";
        //1.比赛开始
        campaign.setCurrentCampaignDate(INIT_CAMPAIGN_DATE);
        campaign.setStatus(Campaign.Status.RUN.getValue());
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);

        //2.初始化各公司
        List<Company> companyList = campaignManager.listCompany(campaign);
        for (Company company : companyList) {
            company.setCurrentCampaignDate(campaign.getCurrentCampaignDate());
            baseManager.saveOrUpdate(Company.class.getName(), company);
            CompanyTerm companyTerm = new CompanyTerm();
            companyTerm.setCampaign(company.getCampaign());
            companyTerm.setCompany(company);
            companyTerm.setCampaignDate(company.getCampaign().getCurrentCampaignDate());
            List<CompanyStatusProperty> companyStatusPropertyList = prepareCompanyStatusProperty(companyTerm);
            companyTerm.setCompanyStatusPropertyList(companyStatusPropertyList);
            baseManager.saveOrUpdate(CompanyTerm.class.getName(), companyTerm);
        }

        //3.准备供用户决策用的随机数据
        choiceManager.produceChoice(campaign);

    }

    private List<CompanyStatusProperty> prepareCompanyStatusProperty(CompanyTerm companyTerm) {
        List<CompanyStatusProperty> companyStatusPropertyList = new ArrayList<CompanyStatusProperty>();
        companyStatusPropertyList.add(new CompanyStatusProperty(EPropertyName.OPERATION_ABILITY, "2000", companyTerm));
        companyStatusPropertyList.add(new CompanyStatusProperty(EPropertyName.SATISFACTION, "60", companyTerm));
        companyStatusPropertyList.add(new CompanyStatusProperty(EPropertyName.OLD_USER_AMOUNT, "2000", companyTerm));
        companyStatusPropertyList.add(new CompanyStatusProperty(EPropertyName.USER_AMOUNT, "2000", companyTerm));
//        companyStatusPropertyValueList.add(new CompanyStatusPropertyValue(EPropertyName.CURRENT_PERIOD_INCOME, "2000", preCompanyTerm));
        companyStatusPropertyList.add(new CompanyStatusProperty(EPropertyName.MARKET_ABILITY, "2000", companyTerm));
        companyStatusPropertyList.add(new CompanyStatusProperty(EPropertyName.NEW_USER_AMOUNT, "0", companyTerm));
        companyStatusPropertyList.add(new CompanyStatusProperty(EPropertyName.PRODUCT_ABILITY, "2000", companyTerm));
        companyStatusPropertyList.add(new CompanyStatusProperty(EPropertyName.PRODUCT_RATIO, "60", companyTerm));
        companyStatusPropertyList.add(new CompanyStatusProperty(EPropertyName.PER_ORDER_COST, "60", companyTerm));
        return companyStatusPropertyList;
    }

    /**
     * 按部门分离公司属性
     * @param companyStatusPropertyList
     * @return
     */
    @Override
    public Map<String, List<CompanyStatusProperty>> partCompanyStatusPropertyByDept(List<CompanyStatusProperty> companyStatusPropertyList) {
        Map<String, List<CompanyStatusProperty>> map = new LinkedHashMap<>();
        if (companyStatusPropertyList != null && !companyStatusPropertyList.isEmpty()) {
            for (CompanyStatusProperty companyStatusProperty : companyStatusPropertyList) {
                String dept = companyStatusProperty.getDept();
                if (map.containsKey(dept)) {
                    map.get(dept).add(companyStatusProperty);
                } else {
                    List<CompanyStatusProperty> deptCompanyStatusPropertyList = new ArrayList<CompanyStatusProperty>();
                    deptCompanyStatusPropertyList.add(companyStatusProperty);
                    map.put(dept, deptCompanyStatusPropertyList);
                }
            }
        }
        return map;
    }

    public void next(String campaignId) {
        processNext(campaignId);
        CampaignHandler campaignHandler = CampaignCenter.getCampaignHandler(campaignId);

        Map<String, CompanyTermHandler> companyTermHandlerMap = campaignHandler.getCompanyTermHandlerMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermHandler companyTermHandler = companyTermHandlerMap.get(companyId);
         /*   //0.收集上一轮的属性数据
            collectPreProperty(companyTermHandler);
            //1.收集上一轮的决策数据
            collectInstruction(companyTermHandler);*/
            //2.人才招聘等竞标结果
            competitiveBidding(companyTermHandler);
            //3.计算保存本轮属性数据
            calculateProperty(companyTermHandler);
            //4.计算本轮财务数据
            calculateAccount(companyTermHandler);
        }

        //准备供用户决策用的随机数据
        choiceManager.produceChoice(campaignHandler.getCampaign());
    }

    /**
     * 开始新的回合
     * @param campaignId
     */
    private void processNext(String campaignId) {
        CampaignHandler campaignHandler = CampaignCenter.getCampaignHandler(campaignId);
        Map<String, CompanyTermHandler> companyTermHandlerMap = campaignHandler.getCompanyTermHandlerMap();
        Campaign campaign = campaignHandler.getCampaign();
        campaign.setCurrentCampaignDate(CampaignUtil.getNextCampaignDate(campaign.getCurrentCampaignDate()));
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);
        List<Company> companyList = campaignManager.listCompany(campaign);
        for (Company company : companyList) {
            company.setCurrentCampaignDate(campaign.getCurrentCampaignDate());
            baseManager.saveOrUpdate(Company.class.getName(), company);

            CompanyTerm companyTerm = new CompanyTerm();
            companyTerm.setCampaign(campaign);
            companyTerm.setCompany(company);
            companyTerm.setCampaignDate(campaign.getCurrentCampaignDate());
            baseManager.saveOrUpdate(CompanyTerm.class.getName(), companyTerm);

            CompanyTermHandler companyTermHandler = campaignCenterManager.initCompanyTermHandler(companyTerm, campaignHandler);
            CompanyTermHandler cth = campaignHandler.getCompanyTermHandlerMap().get(company.getId());
            companyTermHandler.setPreCompanyTermHandler(cth);

            companyTermHandlerMap.put(company.getId(), companyTermHandler);
        }
    }

/*    private void collectPreProperty(CompanyTermHandler companyTermHandler) {
        //上一轮的属性数据
        CompanyTerm preCompanyTerm = companyTermHandler.getPreCompanyTerm();
        List<CompanyStatusProperty> preCompanyStatusPropertyList = internetPropertyManager.listCompanyStatusProperty(preCompanyTerm);
        Map<String, String> prePropertyValueMap = new HashMap<>();
        if (preCompanyStatusPropertyList != null) {
            for (CompanyStatusProperty preCompanyStatusProperty : preCompanyStatusPropertyList) {
                prePropertyValueMap.put(preCompanyStatusProperty.getName(), preCompanyStatusProperty.getValue());
            }
        }
        companyTermHandler.setPrePropertyValueMap(prePropertyValueMap);
    }

    public void collectInstruction(CompanyTermHandler companyTermHandler) {
        //本轮决策
        CompanyTerm companyTerm = companyTermHandler.getPreCompanyTerm();
        List<CompanyInstruction> preCompanyInstructionList = instructionManager.listCompanyInstruction(companyTerm);
        companyTermHandler.putCompanyInstructionList(preCompanyInstructionList);
    }*/

    public void competitiveBidding(CompanyTermHandler companyTermHandler) {
        //将决策按人才分类
        Map<String, List<CompanyInstruction>> humanInstructionMap = new HashMap<>();
        List<CompanyInstruction> companyInstructionList = companyTermHandler.listCompanyInstructionByType(EChoiceBaseType.HUMAN.name());
        if (companyInstructionList != null) {
            for (CompanyInstruction companyInstruction : companyInstructionList) {
                String humanId = companyInstruction.getCompanyChoice().getId();
                if (humanInstructionMap.containsKey(humanId)) {
                    humanInstructionMap.get(humanId).add(companyInstruction);
                } else {
                    List<CompanyInstruction> cil = new ArrayList<>();
                    cil.add(companyInstruction);
                    humanInstructionMap.put(humanId, cil);
                }
            }
        }
        //竞标
        Map<String, CompanyTermHandler> companyTermHandlerMap = companyTermHandler.getCampaignHandler().getCompanyTermHandlerMap();
        for (String choiceId : humanInstructionMap.keySet()) {
            List<CompanyInstruction> cil = humanInstructionMap.get(choiceId);
            if (cil != null && cil.size() > 0) {
                int maxRecruitmentRatio = 0;
                CompanyInstruction successCompanyInstruction = null;
                for (CompanyInstruction companyInstruction : cil) {
                    CompanyTermHandler ctHandler = companyTermHandlerMap.get(companyInstruction.getCompany().getId());
                    Integer officeRatio = Integer.valueOf(ctHandler.get(EPropertyName.OFFICE_RATIO.name()));
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
            }
        }
        //统一更新未选中的
        if (companyInstructionList != null) {
            companyInstructionList.stream().filter(companyInstruction -> EInstructionStatus.DQD.name().equals(companyInstruction.getStatus())).forEach(companyInstruction -> {
                companyInstruction.setStatus(EInstructionStatus.WXZ.name());
                baseManager.saveOrUpdate(CompanyInstruction.class.getName(), companyInstruction);
            });
        }

    }


    public void calculateProperty(CompanyTermHandler companyTermHandler) {
        CompanyTerm companyTerm = companyTermHandler.getCompanyTerm();
        List<CompanyStatusProperty> companyStatusPropertyList = new ArrayList<>();
        for (EPropertyName ePropertyName : EPropertyName.values()) {
            String value = companyTermHandler.calculate(ePropertyName.name());
            companyStatusPropertyList.add(new CompanyStatusProperty(ePropertyName, value, companyTerm));
        }
        companyTerm.setCompanyStatusPropertyList(companyStatusPropertyList);
        baseManager.saveOrUpdate(CompanyTerm.class.getName(), companyTerm);
//        companyTermHandler.setCompanyStatusPropertyList(companyStatusPropertyList);
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
        CampaignHandler campaignHandler = CampaignCenter.getCampaignHandler(campaignId);
        Campaign campaign = campaignHandler.getCampaign();
        SessionFactory sessionFactory = (SessionFactory) ApplicationContextUtil.getApplicationContext().getBean("sessionFactory");
        Session session = sessionFactory.getCurrentSession();

        String currentCampaignDate = campaign.getCurrentCampaignDate();
        String preCampaignDate = CampaignUtil.getPreCampaignDate(currentCampaignDate);

        //删除本轮属性信息
        List<Company> companyList = campaignManager.listCompany(campaign);
        for (Company company : companyList) {
            CompanyTerm companyTerm = companyTermManager.getCompanyTerm(company, currentCampaignDate);
            session.delete(companyTerm);
        }

        //删除本轮以及上一轮的决策信息
        XQuery instructionQuery = new XQuery();
        instructionQuery.setHql("from CompanyInstruction where campaign.id = :campaignId and campaignDate in (:campaignDates)");
        instructionQuery.put("campaignId", campaign.getId());
        instructionQuery.put("campaignDates", new String[]{currentCampaignDate, preCampaignDate});
        List<CompanyInstruction> companyInstructionList = baseManager.listObject(instructionQuery);
        if (companyInstructionList != null) {
            companyInstructionList.forEach(session::delete);
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

        //删除本轮财务数据
        for (Company company : companyList) {
            String hql = "from Account where company.id = :companyId and campaignDate = :campaignDate";
            LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
            queryParamMap.put("companyId", company.getId());
            queryParamMap.put("campaignDate", currentCampaignDate);
            Account account = (Account) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
            session.delete(account);
        }


        //比赛时间回到上一轮
        campaign.setCurrentCampaignDate(preCampaignDate);
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);
    }

}
