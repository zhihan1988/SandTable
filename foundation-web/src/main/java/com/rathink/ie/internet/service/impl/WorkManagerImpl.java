package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.util.CampaignUtil;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyStatusProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.AccountManager;
import com.rathink.ie.ibase.service.CompanyStatusManager;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.service.ChoiceManager;
import com.rathink.ie.internet.service.InstructionManager;
import com.rathink.ie.internet.service.InternetPropertyManager;
import com.rathink.ie.internet.service.WorkManager;
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
public class WorkManagerImpl implements WorkManager {

    @Autowired
    private BaseManager baseManager;
    @Autowired
    private ChoiceManager choiceManager;
    @Autowired
    private InstructionManager instructionManager;
    @Autowired
    private CompanyStatusManager companyStatusManager;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private InternetPropertyManager internetPropertyManager;
    /**
     * 开始游戏 定时任务执行
     * @param campaign
     */
    @Override
    public void initCampaign(Campaign campaign) {
        final String INIT_CAMPAIGNDATE = "010101";
        //1.比赛开始
        campaign.setCurrentCampaignDate(INIT_CAMPAIGNDATE);
        campaign.setStatus(Campaign.Status.RUN.getValue());
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);
        //2.初始化各公司基本属性
        XQuery xQuery = new XQuery();
        xQuery.setHql("from Company where campaign.id = :campaignId");
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<String, Object>();
        queryParamMap.put("campaignId", campaign.getId());
        xQuery.setQueryParamMap(queryParamMap);
        List<Company> companyList = baseManager.listObject(xQuery);
        for (Company company : companyList) {
            company.setCurrentCampaignDate(campaign.getCurrentCampaignDate());
            baseManager.saveOrUpdate(Company.class.getName(), company);
            initCompanyStatus(company);
            accountManager.initCompanyAccount(company);
        }
        //3.准备供用户决策用的随机数据
        choiceManager.produceChoice(campaign);

    }

    @Override
    public CompanyTerm initCompanyStatus(Company company) {
        CompanyTerm companyTerm = new CompanyTerm();
        companyTerm.setCampaign(company.getCampaign());
        companyTerm.setCompany(company);
        companyTerm.setCampaignDate(company.getCampaign().getCurrentCampaignDate());
        List<CompanyStatusProperty> companyStatusPropertyList = prepareCompanyStatusProperty(companyTerm);
        companyTerm.setCompanyStatusPropertyList(companyStatusPropertyList);
        baseManager.saveOrUpdate(CompanyTerm.class.getName(), companyTerm);
        return companyTerm;
    }

    @Override
    public List<CompanyStatusProperty> prepareCompanyStatusProperty(CompanyTerm companyTerm) {
        List<CompanyStatusProperty> companyStatusPropertyList = new ArrayList<CompanyStatusProperty>();
        companyStatusPropertyList.add(new CompanyStatusProperty(EPropertyName.OPERATION_ABILITY, "2000", companyTerm));
        companyStatusPropertyList.add(new CompanyStatusProperty(EPropertyName.SATISFACTION, "60", companyTerm));
        companyStatusPropertyList.add(new CompanyStatusProperty(EPropertyName.OLD_USER_AMOUNT, "2000", companyTerm));
        companyStatusPropertyList.add(new CompanyStatusProperty(EPropertyName.USER_AMOUNT, "2000", companyTerm));
//        companyStatusPropertyValueList.add(new CompanyStatusPropertyValue(EPropertyName.CURRENT_PERIOD_INCOME, "2000", companyTerm));
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

    /**
     * 进入下一回合  定时任务执行
     * @param campaign
     */
    @Override
    public void nextCampaign(Campaign campaign) {
      /*  //产生人才竞标结果
        instructionManager.produceHumanDiddingResult(campaign);


        String currentCampaignDate = campaign.getCurrentCampaignDate();
        String nextCampaignDate = CampaignUtil.getNextCampaignDate(currentCampaignDate);


        //计算下一回合各公司属性
        List<Company> companyList = campaignManager.listCompany(campaign);
        for (Company company : companyList) {

            CompanyTerm nextCompanyTerm = new CompanyTerm();
            nextCompanyTerm.setCampaign(company.getCampaign());
            nextCompanyTerm.setCompany(company);
            nextCompanyTerm.setCampaignDate(nextCampaignDate);
            baseManager.saveOrUpdate(CompanyTerm.class.getName(), nextCompanyTerm);

            Account nextAccount = new Account();
            nextAccount.setCampaign(campaign);
            nextAccount.setCampaignDate(nextCampaignDate);
            nextAccount.setCompany(company);
            baseManager.saveOrUpdate(Account.class.getName(), nextAccount);

            //1.获取公司各部门决策结果
            List<OfficeInstruction> officeInstructionList = instructionManager.listOfficeInstruction(company);
            List<HrInstruction> hrInstructionList = instructionManager.listHrInstruction(company);
            List<MarketInstruction> marketInstructionList = instructionManager.listMarketInstruction(company, currentCampaignDate);
            List<OperationInstruction> operationInstructionList = instructionManager.listOperationInstruction(company, currentCampaignDate);
            ProductStudyInstruction productStudyInstruction = instructionManager.getProductStudyInstruction(company, currentCampaignDate);


            //2.公司属性
            //部门能力
            List<CompanyStatusPropertyValue> cspList = new ArrayList<>();
            Integer marketAbility = internetPropertyManager.getAbilityValue(hrInstructionList, Edept.MARKET.name());
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.MARKET_ABILITY, String.valueOf(marketAbility), nextCompanyTerm));
            Integer operationAbility = internetPropertyManager.getAbilityValue(hrInstructionList, Edept.OPERATION.name());
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.OPERATION_ABILITY, String.valueOf(operationAbility), nextCompanyTerm));
            Integer productAbility = internetPropertyManager.getAbilityValue(hrInstructionList, Edept.PRODUCT.name());
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.PRODUCT_ABILITY, String.valueOf(productAbility), nextCompanyTerm));

            //新用户数
            Integer newUserAmount = internetPropertyManager.getNewUserAmount(marketInstructionList);
            //定位冲突导致的市场活动效果下降 降低市场营销系数
            Integer productGradeConflictRatio = instructionManager.productGradeConflict(productStudyInstruction);
            newUserAmount = newUserAmount * productGradeConflictRatio / 100;
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.NEW_USER_AMOUNT, String.valueOf(newUserAmount), nextCompanyTerm));

            //满意度
            Integer satisfaction = internetPropertyManager.getSatisfaction(operationInstructionList, operationAbility);
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.SATISFACTION, String.valueOf(satisfaction), nextCompanyTerm));

            //老用户数
            Integer oldUserAmount = internetPropertyManager.getOldUserAmount(company, satisfaction);
            //定位变动导致老用户流失
            Integer productGradeChangeRatio = instructionManager.getProductGradeChangeRatio(productStudyInstruction);
            oldUserAmount = oldUserAmount * productGradeChangeRatio / 100;
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.OLD_USER_AMOUNT, String.valueOf(oldUserAmount), nextCompanyTerm));

            //总用户数
            Integer userAmount = oldUserAmount + newUserAmount;
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.USER_AMOUNT, String.valueOf(userAmount), nextCompanyTerm));

            //产品系数
            Integer productRadio = internetPropertyManager.getProductRadio(company, productAbility, productStudyInstruction);
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.PRODUCT_RATIO, String.valueOf(productRadio), nextCompanyTerm));

            //客单价
            Integer perOrderCost = internetPropertyManager.getPerOrderCost(company, productStudyInstruction);
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.PER_ORDER_COST, String.valueOf(perOrderCost), nextCompanyTerm));

//            Integer currentPeriodIncome = internetPropertyService.getCurrentPeriodIncome(userAmount, perOrderCost);
//            cspList.add(new CompanyStatusPropertyValue(EPropertyName.CURRENT_PERIOD_INCOME, String.valueOf(currentPeriodIncome), nextCompanyStatus));

            nextCompanyTerm.setCompanyStatusPropertyValueList(cspList);
            baseManager.saveOrUpdate(CompanyTerm.class.getName(), nextCompanyTerm);

            //2.部门资金使用情况
            List<AccountEntry> accountEntryList = new ArrayList<>();
            List<AccountEntry> adAccountEntityList = accountManager
                    .prepareAccountEntity(officeInstructionList, EAccountEntityType.AD_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), nextAccount);
            accountEntryList.addAll(adAccountEntityList);
            List<AccountEntry> hrAccountEntityList = accountManager
                    .prepareAccountEntity(hrInstructionList, EAccountEntityType.HR_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), nextAccount);
            accountEntryList.addAll(hrAccountEntityList);

            List<AccountEntry> marketAccountEntityList = accountManager
                    .prepareAccountEntity(marketInstructionList, EAccountEntityType.MARKET_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), nextAccount);
            accountEntryList.addAll(marketAccountEntityList);

            List<AccountEntry> operationAccountEntityList = accountManager
                    .prepareAccountEntity(operationInstructionList, EAccountEntityType.OPERATION_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), nextAccount);
            accountEntryList.addAll(operationAccountEntityList);



            List<ProductStudyInstruction> productStudyInstructionList = new ArrayList<>();
            if (productStudyInstruction != null) {
                productStudyInstructionList.add(productStudyInstruction);
            }
            List<AccountEntry> productStudyEntityList = accountManager
                    .prepareAccountEntity(productStudyInstructionList, EAccountEntityType.PRODUCT_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), nextAccount);
            accountEntryList.addAll(productStudyEntityList);

            //本期收入（总用户数*客单价）
            Integer currentPeriodIncome = internetPropertyManager.getCurrentPeriodIncome(userAmount, perOrderCost);
            List<AccountEntry> currentPeriodIncomeEntityList = accountManager
                    .prepareAccountEntity(String.valueOf(currentPeriodIncome), EAccountEntityType.COMPANY_CASH.name(), EAccountEntityType.OTHER.name(), nextAccount);
            accountEntryList.addAll(currentPeriodIncomeEntityList);

            nextAccount.setAccountEntryList(accountEntryList);
            baseManager.saveOrUpdate(Account.class.getName(), nextAccount);

        }

        //开始下一回合
        campaign.setCurrentCampaignDate(nextCampaignDate);
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);

        //准备供用户决策用的随机数据
        choiceManager.produceChoice(campaign);*/
    }

    /**
     * 测试方法
     * 回到上一轮
     * @param campaign
     */
    @Override
    public void preCampaign(Campaign campaign) {
        SessionFactory sessionFactory = (SessionFactory) ApplicationContextUtil.getApplicationContext().getBean("sessionFactory");
        Session session = sessionFactory.getCurrentSession();

        String currentCampaignDate = campaign.getCurrentCampaignDate();
        String preCampaignDate = CampaignUtil.getPreCampaignDate(currentCampaignDate);

        //删除本轮属性信息
        List<Company> companyList = campaignManager.listCompany(campaign);
        for (Company company : companyList) {
            CompanyTerm companyTerm = companyStatusManager.getCompanyTerm(company, currentCampaignDate);
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
