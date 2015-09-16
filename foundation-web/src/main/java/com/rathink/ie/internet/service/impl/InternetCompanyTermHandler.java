package com.rathink.ie.internet.service.impl;

import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.ibase.service.CampaignHandler;
import com.rathink.ie.ibase.service.CompanyTermHandler;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.Edept;
import com.rathink.ie.internet.service.InstructionManager;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/9/4.
 */
@Component
public class InternetCompanyTermHandler extends CompanyTermHandler {

    @Override
    public Double calculate(String key) {
        EPropertyName ePropertyName = EPropertyName.valueOf(key);
        Double value = 0d;
        switch (ePropertyName) {
            case OFFICE_RATIO:
                value = calculateOfficeRatio();
                break;
            case PRODUCT_ABILITY:
                value = calculateDeptAbility(Edept.PRODUCT.name());
                break;
            case PRODUCT_FEE_RATIO:
                value = calculateProductFeeRatio();
                break;
            case PRODUCT_RATIO:
                value = calculateProductRatio();
                break;
            case PER_ORDER_COST:
                value = calculatePerOrderCost();
                break;
            case PRODUCT_COMPETITION_RATIO:
                value = calculateProductCompetitionRatio();
                break;
            case MARKET_ABILITY:
                value = calculateDeptAbility(Edept.MARKET.name());
                break;
            case NEW_USER_AMOUNT:
                value = calculateNewUserAmount();
                break;
            case OPERATION_ABILITY:
                value = calculateDeptAbility(Edept.OPERATION.name());
                break;
            case SATISFACTION:
                value = calculateSatisfaction();
                break;
            case OLD_USER_AMOUNT:
                value = calculateOldUserAmount();
                break;
            case USER_AMOUNT:
                value = calculateUserAmount();
                break;
            case OPERATION_FEE_RATIO:
                value = calculateOperationFeeRatio();
                break;
            case CURRENT_PERIOD_INCOME:
                value = currentIncome();
                break;
            default:
                throw new NotImplementedException();
        }
        return value;
    }


    /**
     * 计算部门能力
     * @param type 部门类型
     * @return 部门能力
     */
    public Double calculateDeptAbility(String type) {
        InstructionManager instructionManager = (InstructionManager) ApplicationContextUtil.getApplicationContext().getBean("instructionManagerImpl");
        List<CompanyInstruction> companyInstructionList = instructionManager.listCompanyInstructionByType(getCompanyTerm().getCompany(), EChoiceBaseType.HUMAN.name());
        Double ability = 60d;
        if (companyInstructionList != null) {
            for (CompanyInstruction companyInstruction : companyInstructionList) {
                CompanyChoice human = companyInstruction.getCompanyChoice();
                if (human.getType().equals(type)) {
                    ability += Double.valueOf(human.getValue());
                }
            }
        }
        return ability;
    }

    /**
     *
     * @return 办公室系数
     */
    private Double calculateOfficeRatio() {
        List<CompanyInstruction> companyInstructionList = preCompanyTermHandler == null ? null
                : preCompanyTermHandler.listCompanyInstructionByType(EChoiceBaseType.OFFICE.name());
        Double officeFee = 0d;
        if (companyInstructionList != null) {
            for (CompanyInstruction companyInstruction : companyInstructionList) {
                officeFee += Double.valueOf(companyInstruction.getValue());
            }
        }
        Double officeRatio = Math.sqrt(Math.sqrt(officeFee)) * 2 + 60;
        return officeRatio;
    }

    //    private String calculateRecruitmentRatio() {
//        String officeRatio = get(EPropertyName.OFFICE_RATIO.name());
//    }

    /**
     *
     * @return 产品资金投入系数
     */
    private Double calculateProductFeeRatio() {
        List<CompanyInstruction> typeCompanyInstructionList = preCompanyTermHandler == null ? null
                : preCompanyTermHandler.listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY_FEE.name());
        Double fee = new Double(0);
        if (typeCompanyInstructionList != null) {
            for (CompanyInstruction companyInstruction : typeCompanyInstructionList) {
                fee += Double.valueOf(companyInstruction.getValue());
            }
        }
        Double productFeeRatio = fee / 1000 + 50;
        return productFeeRatio;
    }

    /**
     *
     * @return 产品系数
     */
    private Double calculateProductRatio() {
        Double productAbility = Double.valueOf(get(EPropertyName.PRODUCT_ABILITY.name()));
        Double productFeeRatio = Double.valueOf(get(EPropertyName.PRODUCT_FEE_RATIO.name()));
        Double preProductRatio = preCompanyTermHandler == null ? 0 : Double.valueOf(preCompanyTermHandler.get(EPropertyName.PRODUCT_RATIO.name()));
        Double productRatio = Math.sqrt(Math.sqrt(productAbility * productFeeRatio) + preProductRatio) * 1.5 + 50;
        return productRatio;
    }

    /**
     *
     * @return  客单价
     */
    private Double calculatePerOrderCost() {
        List<CompanyInstruction> productStudyInstructionList = preCompanyTermHandler == null ? null
                : preCompanyTermHandler.listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY.name());
        String grade = "1";
        if (productStudyInstructionList != null && productStudyInstructionList.size() > 0) {
            grade = productStudyInstructionList.get(0).getValue();
        }
        Double perOrderCost = Double.valueOf(grade) * 10 + 30;
        return perOrderCost;
    }

    /**
     *
     * @return 产品竞争系数
     */
    private Double calculateProductCompetitionRatio() {
        //自己公司的定位
        List<CompanyInstruction> productStudyInstructionList = preCompanyTermHandler == null ? null
                : preCompanyTermHandler.listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY.name());
        String grade = "1";
        if (productStudyInstructionList != null && productStudyInstructionList.size() > 0) {
            grade = productStudyInstructionList.get(0).getValue();
        }
        //跟自己公司定位相同的公司数量
        int sameGradeCount = 1;
        CampaignHandler campaignHandler = getCampaignHandler();
        Map<String, CompanyTermHandler> companyTermHandlerMap = campaignHandler.getCompanyTermHandlerMap();
        for (String companyId : campaignHandler.getCompanyTermHandlerMap().keySet()) {
            if (!companyId.equals(getCompanyTerm().getCompany().getId())) {
                CompanyTermHandler companyTermHandler = companyTermHandlerMap.get(companyId);
                List<CompanyInstruction> companyInstructionList = companyTermHandler.listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY.name());
                String otherGrade = companyInstructionList == null ? "1" : companyInstructionList.get(0).getValue();
                if (grade.equals(otherGrade)) {
                    sameGradeCount++;
                }
            }

        }
        //产品竞争系数计算公式
        Double productCompetitionRatio = 30 + 20 * Math.sqrt(sameGradeCount);
        return productCompetitionRatio;
    }

    /**
     * $营销能力系数=50+∑(  营销人才系数)  ???不就是营销能力吗
     * @return 营销能力系数
     */
    private Double calculateOperationAbilityRatio() {
        Double operationHumanRatio = 50d;
        return operationHumanRatio;
    }

    /**
     *
     * @return 新用户数
     */
    private Double calculateNewUserAmount() {
        List<CompanyInstruction> marketInstructionList = preCompanyTermHandler == null ? null
                : preCompanyTermHandler.listCompanyInstructionByType(EChoiceBaseType.MARKET_ACTIVITY.name());

        Double newUserAmount = 0d;
        Double marketAbility = get(EPropertyName.MARKET_ABILITY.name());
        Double productCompetitionRatio = get(EPropertyName.PRODUCT_COMPETITION_RATIO.name());
        if (marketInstructionList != null) {
            for (CompanyInstruction companyInstruction : marketInstructionList) {
                CompanyChoice companyChoice = companyInstruction.getCompanyChoice();
                Double marketCost = Double.valueOf(companyChoice.getValue());
                Double marketFee = Double.valueOf(companyInstruction.getValue());
                newUserAmount += marketAbility * marketFee / marketCost / productCompetitionRatio;
            }
        }
        return newUserAmount;
    }

    /**
     *
     * @return 运营资金投入系数
     */
    private Double calculateOperationFeeRatio() {
        List<CompanyInstruction> typeCompanyInstructionList = preCompanyTermHandler == null ? null
                : preCompanyTermHandler.listCompanyInstructionByType(EChoiceBaseType.OPERATION.name());
        Double operationFee = 0d;
        if (typeCompanyInstructionList != null) {
            for (CompanyInstruction companyInstruction : typeCompanyInstructionList) {
                operationFee += Double.valueOf(companyInstruction.getValue());
            }
        }
        return operationFee / 10000 + 50;
    }

    /**
     * 满意度系数
     * @return
     */
    private Double calculateSatisfaction() {
        Double operationAbility = get(EPropertyName.OPERATION_ABILITY.name());
        Double operationFeeRatio = get(EPropertyName.OPERATION_FEE_RATIO.name());
        Double productRatio = get(EPropertyName.PRODUCT_RATIO.name());
        Double satisfaction = (operationAbility + operationFeeRatio + productRatio) / 3;
        return satisfaction;
    }

    /**
     *
     * @return 老用户数
     */
    private Double calculateOldUserAmount() {
        Double preUserAmount = preCompanyTermHandler == null ? 0 : preCompanyTermHandler.get(EPropertyName.USER_AMOUNT.name());
        Double satisfaction = get(EPropertyName.SATISFACTION.name());
        return preUserAmount * satisfaction / 100;
    }

    /**
     *
     * @return 总用户数
     */
    private Double calculateUserAmount() {
        Double oldUserAmount = get(EPropertyName.OLD_USER_AMOUNT.name());
        Double newUserAmount = get(EPropertyName.NEW_USER_AMOUNT.name());
        return oldUserAmount + newUserAmount;
    }

    /**
     * 本期收入
     * @return
     */
    private Double currentIncome() {
        Double userAmount = get(EPropertyName.USER_AMOUNT.name());
        Double perOrderCost = get(EPropertyName.PER_ORDER_COST.name());
        return userAmount * perOrderCost * 50 / 100;
    }

}
