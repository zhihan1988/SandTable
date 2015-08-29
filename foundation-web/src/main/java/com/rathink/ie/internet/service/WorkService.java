package com.rathink.ie.internet.service;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;

import com.ming800.core.util.ApplicationContextUtil;
import com.ming800.core.util.DateUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignService;
import com.rathink.ie.foundation.util.CampaignUtil;
import com.rathink.ie.ibase.property.model.CompanyStatus;
import com.rathink.ie.ibase.property.model.CompanyStatusPropertyValue;
import com.rathink.ie.ibase.service.CompanyStatusService;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.Edept;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.internet.instruction.model.ProductStudyInstruction;
import com.rathink.ie.internet.service.ChoiceService;
import com.rathink.ie.internet.service.InstructionService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Created by Hean on 2015/8/24.
 */
@Service
public class WorkService {

    @Autowired
    private BaseManager baseManager;
    @Autowired
    private ChoiceService choiceService;
    @Autowired
    private InstructionService instructionService;
    @Autowired
    private CompanyStatusService companyStatusService;
    @Autowired
    private CampaignService campaignService;
    /**
     * 开始游戏 定时任务执行
     * @param campaign
     */
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
            initCompanyStatus(company);
        }
        //3.准备供用户决策用的随机数据
        choiceService.produceChoice(campaign);

    }

    public CompanyStatus initCompanyStatus(Company company) {
        CompanyStatus companyStatus = new CompanyStatus();
        companyStatus.setCampaign(company.getCampaign());
        companyStatus.setCompany(company);
        companyStatus.setCampaignDate(company.getCampaign().getCurrentCampaignDate());
        List<CompanyStatusPropertyValue> companyStatusPropertyValueList = prepareCompanyStatusProperty(companyStatus);
        companyStatus.setCompanyStatusPropertyValueList(companyStatusPropertyValueList);
        baseManager.saveOrUpdate(CompanyStatus.class.getName(), companyStatus);
        return companyStatus;
    }

    public List<CompanyStatusPropertyValue> prepareCompanyStatusProperty(CompanyStatus companyStatus) {
        List<CompanyStatusPropertyValue> companyStatusPropertyValueList = new ArrayList<CompanyStatusPropertyValue>();
        companyStatusPropertyValueList.add(new CompanyStatusPropertyValue(EPropertyName.OPERATION_ABILITY, "2000", companyStatus));
        companyStatusPropertyValueList.add(new CompanyStatusPropertyValue(EPropertyName.SATISFACTION, "60", companyStatus));
        companyStatusPropertyValueList.add(new CompanyStatusPropertyValue(EPropertyName.OLD_USER_AMOUNT, "2000", companyStatus));
        companyStatusPropertyValueList.add(new CompanyStatusPropertyValue(EPropertyName.USER_AMOUNT, "2000", companyStatus));
        companyStatusPropertyValueList.add(new CompanyStatusPropertyValue(EPropertyName.CURRENT_PERIOD_INCOME, "2000", companyStatus));
        companyStatusPropertyValueList.add(new CompanyStatusPropertyValue(EPropertyName.MARKET_ABILITY, "2000", companyStatus));
        companyStatusPropertyValueList.add(new CompanyStatusPropertyValue(EPropertyName.NEW_USER_AMOUNT, "0", companyStatus));
        companyStatusPropertyValueList.add(new CompanyStatusPropertyValue(EPropertyName.PRODUCT_ABILITY, "2000", companyStatus));
        companyStatusPropertyValueList.add(new CompanyStatusPropertyValue(EPropertyName.PRODUCT_RATIO, "60", companyStatus));
        companyStatusPropertyValueList.add(new CompanyStatusPropertyValue(EPropertyName.PER_ORDER_COST, "60", companyStatus));
        return companyStatusPropertyValueList;
    }

    /**
     * 按部门分离公司属性
     * @param companyStatusPropertyValueList
     * @return
     */
    public Map<String, List<CompanyStatusPropertyValue>> partCompanyStatusPropertyByDept(List<CompanyStatusPropertyValue> companyStatusPropertyValueList) {
        Map<String, List<CompanyStatusPropertyValue>> map = new LinkedHashMap<>();
        if (companyStatusPropertyValueList != null && !companyStatusPropertyValueList.isEmpty()) {
            for (CompanyStatusPropertyValue companyStatusPropertyValue : companyStatusPropertyValueList) {
                String dept = companyStatusPropertyValue.getDept();
                if (map.containsKey(dept)) {
                    map.get(dept).add(companyStatusPropertyValue);
                } else {
                    List<CompanyStatusPropertyValue> deptCompanyStatusPropertyValueList = new ArrayList<CompanyStatusPropertyValue>();
                    deptCompanyStatusPropertyValueList.add(companyStatusPropertyValue);
                    map.put(dept, deptCompanyStatusPropertyValueList);
                }
            }
        }
        return map;
    }

    /**
     * 进入下一回合  定时任务执行
     * @param campaign
     */
    public void nextCampaign(Campaign campaign) {
        //产生人才竞标结果
        instructionService.produceHumanDiddingResult(campaign);

        //回合结束 新回合开始
        String currentCampaignDate = campaign.getCurrentCampaignDate();
        String nextCampaignDate = CampaignUtil.getNextCampaignDate(currentCampaignDate);


        //计算下一回合各公司属性
        List<Company> companyList = campaignService.listCompany(campaign);
        for (Company company : companyList) {
            List<CompanyStatusPropertyValue> cspList = new ArrayList<>();
            CompanyStatus nextCompanyStatus = new CompanyStatus();
            nextCompanyStatus.setCampaign(company.getCampaign());
            nextCompanyStatus.setCompany(company);
            nextCompanyStatus.setCampaignDate(nextCampaignDate);
            baseManager.saveOrUpdate(CompanyStatus.class.getName(), nextCompanyStatus);

            //部门能力
            Integer marketAbility = instructionService.getDeptAbilityValue(company, Edept.MARKET.name());
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.MARKET_ABILITY, String.valueOf(marketAbility), nextCompanyStatus));
            Integer operationAbility = instructionService.getDeptAbilityValue(company, Edept.OPERATION.name());
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.OPERATION_ABILITY, String.valueOf(operationAbility), nextCompanyStatus));
            Integer productAbility = instructionService.getDeptAbilityValue(company, Edept.PRODUCT.name());
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.PRODUCT_ABILITY, String.valueOf(productAbility), nextCompanyStatus));

            //新用户数
            Integer newUserAmount = instructionService.getNewUserAmount(company);
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.NEW_USER_AMOUNT, String.valueOf(newUserAmount), nextCompanyStatus));

            //满意度
            Integer satisfaction = instructionService.getSatisfaction(company, operationAbility);
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.SATISFACTION, String.valueOf(satisfaction), nextCompanyStatus));

            //老用户数
            Integer oldUserAmount = instructionService.getOldUserAmount(company, satisfaction);
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.OLD_USER_AMOUNT, String.valueOf(oldUserAmount), nextCompanyStatus));

            //总用户数
            Integer userAmount = oldUserAmount + newUserAmount;
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.USER_AMOUNT, String.valueOf(userAmount), nextCompanyStatus));

            //产品系数
            Integer productRadio = instructionService.getProductRadio(company, productAbility);
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.PRODUCT_RATIO, String.valueOf(productRadio), nextCompanyStatus));

            //客单价
            Integer perOrderCost = instructionService.getPerOrderCost(company);
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.PER_ORDER_COST, String.valueOf(perOrderCost), nextCompanyStatus));

            //本期收入（总用户数*客单价）
            Integer currentPeriodIncome = instructionService.getCurrentPeriodIncome(userAmount, perOrderCost);
            cspList.add(new CompanyStatusPropertyValue(EPropertyName.CURRENT_PERIOD_INCOME, String.valueOf(currentPeriodIncome), nextCompanyStatus));

            nextCompanyStatus.setCompanyStatusPropertyValueList(cspList);
            baseManager.saveOrUpdate(CompanyStatus.class.getName(), nextCompanyStatus);

        }

        //开始下一回合
        campaign.setCurrentCampaignDate(nextCampaignDate);
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);

        //准备供用户决策用的随机数据
        choiceService.produceChoice(campaign);
    }

    /**
     * 测试方法
     * 回到上一轮
     * @param campaign
     */
    public void preCampaign(Campaign campaign) {
        SessionFactory sessionFactory = (SessionFactory) ApplicationContextUtil.getApplicationContext().getBean("sessionFactory");
        Session session = sessionFactory.getCurrentSession();

        String currentCampaignDate = campaign.getCurrentCampaignDate();
        String preCampaignDate = CampaignUtil.getPreCampaignDate(currentCampaignDate);

        //删除本轮属性信息
        List<Company> companyList = campaignService.listCompany(campaign);
        for (Company company : companyList) {
            CompanyStatus companyStatus = companyStatusService.getCompanyStatus(company, currentCampaignDate);
            session.delete(companyStatus);
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
        //比赛时间回到上一轮
        campaign.setCurrentCampaignDate(preCampaignDate);
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);
    }

}
