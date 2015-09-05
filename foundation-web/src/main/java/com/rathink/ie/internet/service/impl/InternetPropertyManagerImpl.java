package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.ibase.service.CompanyStatusManager;
import com.rathink.ie.internet.service.InternetPropertyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Hean on 2015/8/30.
 */
@Service
public class InternetPropertyManagerImpl implements InternetPropertyManager {
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private CompanyStatusManager companyStatusManager;

 /*   public Integer getOfficeRatio(List<OfficeInstruction> officeInstructionList) {

        Integer officeFee = 10000;
        if (officeInstructionList != null) {
            officeFee = 0;
            for (OfficeInstruction officeInstruction : officeInstructionList) {
                officeFee += Integer.valueOf(officeInstruction.getFee());
            }
        }

        DecimalFormat df=new DecimalFormat(".##");
        Double officeRatio = Double.valueOf(df.format(Math.sqrt(officeFee))) / 10 + 60;
        return officeRatio.intValue();
    }



    *//**
     * 计算部门能力
     * @param hrInstructionList hr部门的人才决策
     * @param dept 人才的部门类型
     * @return
     *//*
    @Override
    public Integer getAbilityValue(List<HrInstruction> hrInstructionList, String dept) {
        Integer deptAbility = 60;
        if (hrInstructionList != null) {
            for (HrInstruction hrInstruction : hrInstructionList) {
                if (dept.equals(hrInstruction.getHuman().getType())) {
                    String level = hrInstruction.getHuman().getAbility();
                    deptAbility += Integer.valueOf(level);
                }
            }
        }
        return deptAbility;
    }

    *//**
     * 新用户数
     * @param marketAbility
     * @param marketFee
     * @param marketCost
     * @param productCompetitionRatio
     * @return
     *//*
    @Override
    public Integer getNewUserAmount(List<MarketInstruction> marketInstructionList, Integer marketAbility, Integer productCompetitionRatio) {
        Integer newUserAmount = 0;
        if(marketInstructionList == null) return 0;
        for (MarketInstruction marketInstruction : marketInstructionList) {
            Integer marketFee = Integer.valueOf(marketInstruction.getFee());
            Integer marketCost = Integer.valueOf(marketInstruction.getMarketActivityChoice().getCost());
            newUserAmount += marketAbility * marketFee / marketCost / productCompetitionRatio;
        }
        return newUserAmount;
    }

    public Integer getMarketRatio(Integer humanGrade) {

    }

    *//**
     * 满意度
     * @param operationInstructionList
     * @param operationAbility
     * @return
     *//*
    @Override
    public Integer getSatisfaction(List<OperationInstruction> operationInstructionList, Integer operationAbility, Integer){
        Integer satisfaction = 50;
        Integer operationFee = 0;
        if (operationInstructionList != null) {
            for (OperationInstruction operationInstruction : operationInstructionList) {
                operationFee += Integer.valueOf(operationInstruction.getFee());
            }
        }
        Integer operationFeeRatio = operationFee / 10000 + 80;
        satisfaction = operationAbility * operationFeeRatio / 100 + 30;
        System.out.println("运营系数" + operationFeeRatio + ";满意度：" + satisfaction);
        return satisfaction;
    }

    *//**
     * 老用户数
     * @param company
     * @param satisfaction
     * @return
     *//*
    @Override
    public Integer getOldUserAmount(Company company, Integer satisfaction){
        Integer oldUserAmount = 0;
        CompanyTerm companyTerm = companyStatusManager.getCompanyTerm(company, company.getCampaign().getCurrentCampaignDate());
        //上一期用户数
        CompanyStatusPropertyValue preUserAmount = companyStatusManager
                .getCompanyStatusProperty(EPropertyName.USER_AMOUNT.name(), companyTerm);
        oldUserAmount = Integer.valueOf(preUserAmount.getValue()) * satisfaction / 100;
        System.out.println("上一期老用户数：" + preUserAmount.getValue() + ";满意度：" + satisfaction + ";本轮老用户数：" + oldUserAmount);
        return oldUserAmount;
    }

    *//**
     * 产品资金投入系数
     * @param productFee
     * @return
     *//*
    public Integer getProductFeeRatio(Integer productFee) {
        Integer productFeeRatio = productFee / 1000 + 100;
        return productFeeRatio;
    }

    *//**
     * 产品系数
     * @param productAbility
     * @param productFeeRatio
     * @param preProductFeeRatio
     * @return
     *//*
    @Override
    public Integer getProductRatio(Integer productAbility, Integer productFeeRatio, Integer preProductFeeRatio) {
        Double productRatio  = Math.sqrt(productAbility*productFeeRatio+10)+preProductFeeRatio;
        return productRatio.intValue();
    }

    *//**
     * 客单价
     * @param productGrade
     * @return
     *//*
    @Override
    public Integer getPerOrderCost(Integer productGrade) {
        Integer perOrderCost = 60 + productGrade * 10;
        return perOrderCost;
    }

    *//**
     * 产品竞争系数
     * @return
     *//*
    public Integer getProductCompetitionRatio(Integer companyNum) {
        Double productCompetitionRatio = 30 + 20 * Math.sqrt(companyNum);
        return productCompetitionRatio.intValue();
    }

    *//**
     * 本轮收入
     * @param userAmount
     * @param perOrdreCost
     * @return
     *//*
    @Override
    public Integer getCurrentPeriodIncome(Integer userAmount, Integer perOrdreCost) {
        return userAmount * perOrdreCost / 10;
    }*/
}
