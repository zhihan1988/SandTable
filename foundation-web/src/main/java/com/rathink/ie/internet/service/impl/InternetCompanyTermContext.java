package com.rathink.ie.internet.service.impl;

import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.util.RandomUtil;
import com.rathink.ie.ibase.service.CampaignContext;
import com.rathink.ie.ibase.service.CompanyTermContext;
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
public class InternetCompanyTermContext extends CompanyTermContext {

    @Override
    public Integer calculate(String key) {
        EPropertyName ePropertyName = EPropertyName.valueOf(key);
        Integer value = 0;
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
    public Integer calculateDeptAbility(String type) {
        InstructionManager instructionManager = (InstructionManager) ApplicationContextUtil.getApplicationContext().getBean("instructionManagerImpl");
        List<CompanyInstruction> companyInstructionList = instructionManager.listCompanyInstructionByType(getCompanyTerm().getCompany(), EChoiceBaseType.HUMAN.name());
        Integer ability = 30;
        if (companyInstructionList != null) {
            for (CompanyInstruction companyInstruction : companyInstructionList) {
                CompanyChoice human = companyInstruction.getCompanyChoice();
                if (human.getType().equals(type)) {
                    ability += Integer.valueOf(human.getValue());
                }
            }
        }
        return ability;
    }

    /**
     *
     * @return 办公室系数
     */
    private Integer calculateOfficeRatio() {
        List<CompanyInstruction> companyInstructionList = listCompanyInstructionByType(EChoiceBaseType.OFFICE.name());
        Integer officeFee = 0;
        if (companyInstructionList != null) {
            for (CompanyInstruction companyInstruction : companyInstructionList) {
                officeFee += Integer.valueOf(companyInstruction.getValue());
            }
        }
        Double officeRatio = Math.sqrt(Math.sqrt(officeFee)) * 2 + 60;
        return officeRatio.intValue();
    }

    //    private String calculateRecruitmentRatio() {
//        String officeRatio = get(EPropertyName.OFFICE_RATIO.name());
//    }

    /**
     *
     * @return 产品资金投入系数
     */
    private Integer calculateProductFeeRatio() {
        List<CompanyInstruction> typeCompanyInstructionList = listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY_FEE.name());
        Integer fee = 0;
        if (typeCompanyInstructionList != null) {
            for (CompanyInstruction companyInstruction : typeCompanyInstructionList) {
                fee += Integer.valueOf(companyInstruction.getValue());
            }
        }
        Integer productFeeRatio = fee / 1000 + 50;
        return productFeeRatio;
    }

    /**
     *
     * @return 产品系数
     */
    private Integer calculateProductRatio() {
        Integer productAbility = get(EPropertyName.PRODUCT_ABILITY.name());
        Integer productFeeRatio = get(EPropertyName.PRODUCT_FEE_RATIO.name());
        Integer preProductRatio = preCompanyTermContext == null ? 0 : preCompanyTermContext.get(EPropertyName.PRODUCT_RATIO.name());
        Double productRatio = Math.sqrt(productAbility * 3 + productFeeRatio * 3) + preProductRatio * 0.8 + 10 + RandomUtil.random(0, 10);
        return productRatio.intValue();
    }

    /**
     *
     * @return  客单价
     */
    private Integer calculatePerOrderCost() {
        List<CompanyInstruction> productStudyInstructionList = listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY.name());
        String grade = "1";
        if (productStudyInstructionList != null && productStudyInstructionList.size() > 0) {
            grade = productStudyInstructionList.get(0).getValue();
        }
        Integer perOrderCost = Integer.valueOf(grade) * 10 + 40;
        return perOrderCost;
    }

    /**
     *
     * @return 产品竞争系数
     */
    private Integer calculateProductCompetitionRatio() {
        //自己公司的定位
        List<CompanyInstruction> productStudyInstructionList = listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY.name());
        String grade = "1";
        if (productStudyInstructionList != null && productStudyInstructionList.size() > 0) {
            grade = productStudyInstructionList.get(0).getValue();
        }
        //跟自己公司定位相同的公司数量
        int sameGradeCount = 1;
        CampaignContext campaignContext = getCampaignContext();
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (String companyId : campaignContext.getCompanyTermContextMap().keySet()) {
            if (!companyId.equals(getCompanyTerm().getCompany().getId())) {
                CompanyTermContext companyTermContext = companyTermHandlerMap.get(companyId);
                List<CompanyInstruction> companyInstructionList = companyTermContext.listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY.name());
                String otherGrade = companyInstructionList == null ? "1" : companyInstructionList.get(0).getValue();
                if (grade.equals(otherGrade)) {
                    sameGradeCount++;
                }
            }

        }
        //产品竞争系数计算公式
        Double productCompetitionRatio = 30 + 20 * Math.sqrt(sameGradeCount);
        return productCompetitionRatio.intValue();
    }

    /**
     * $营销能力系数=50+∑(  营销人才系数)  ???不就是营销能力吗
     * @return 营销能力系数
     */
    private Integer calculateOperationAbilityRatio() {
        Integer operationHumanRatio = 50;
        return operationHumanRatio;
    }

    /**
     *
     * @return 新用户数
     */
    private Integer calculateNewUserAmount() {

        Map<String, Integer> competitionMap = getCampaignContext().getCompetitionMap();
        List<CompanyInstruction> marketInstructionList = listCompanyInstructionByType(EChoiceBaseType.MARKET_ACTIVITY.name());

        Integer newUserAmount = 0;
        Integer marketAbility = get(EPropertyName.MARKET_ABILITY.name());
        Integer productCompetitionRatio = get(EPropertyName.PRODUCT_COMPETITION_RATIO.name());
        if (marketInstructionList != null) {
            for (CompanyInstruction companyInstruction : marketInstructionList) {
                CompanyChoice companyChoice = companyInstruction.getCompanyChoice();
                Integer count = competitionMap.get(companyInstruction.getCompanyChoice().getId());
                Double marketCost = Double.valueOf(companyChoice.getValue());
                Integer marketFee = Integer.valueOf(companyInstruction.getValue());
                newUserAmount += marketAbility * marketFee / marketCost.intValue() / productCompetitionRatio * 2 * RandomUtil.random(80, 120) / 100;
                Double marketCompetitiveRatio = 100 / Math.sqrt(Math.sqrt(count));
                newUserAmount = newUserAmount * marketCompetitiveRatio.intValue() / 100;
            }
        }
        return newUserAmount;
    }

    /**
     *
     * @return 运营资金投入系数
     */
    private Integer calculateOperationFeeRatio() {
        List<CompanyInstruction> typeCompanyInstructionList = listCompanyInstructionByType(EChoiceBaseType.OPERATION.name());
        Integer operationFee = 0;
        if (typeCompanyInstructionList != null) {
            for (CompanyInstruction companyInstruction : typeCompanyInstructionList) {
                operationFee += Integer.valueOf(companyInstruction.getValue());
            }
        }
        return operationFee / 8000 + 50;
    }

    /**
     * 满意度系数
     * @return
     */
    private Integer calculateSatisfaction() {
        Integer operationAbility = get(EPropertyName.OPERATION_ABILITY.name());
        Integer operationFeeRatio = get(EPropertyName.OPERATION_FEE_RATIO.name());
        Integer productRatio = get(EPropertyName.PRODUCT_RATIO.name());
        Integer satisfaction = (operationAbility + operationFeeRatio + productRatio) / 3 + RandomUtil.random(0, 10);
        return satisfaction;
    }

    /**
     *
     * @return 老用户数
     */
    private Integer calculateOldUserAmount() {
        List<CompanyInstruction> companyInstructionList = listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY.name());
        CompanyInstruction productStudy = companyInstructionList == null ? null : companyInstructionList.get(0);
        Integer preUserAmount = 0;
        if (preCompanyTermContext != null) {
            InstructionManager instructionManager = (InstructionManager) ApplicationContextUtil.getApplicationContext().getBean("instructionManagerImpl");
            CompanyInstruction preProductStudy = instructionManager.getUniqueInstructionByBaseType(preCompanyTermContext.getCompanyTerm(), EChoiceBaseType.PRODUCT_STUDY.name());
            preUserAmount = preCompanyTermContext.get(EPropertyName.USER_AMOUNT.name());

            if (productStudy != null && preProductStudy != null && !productStudy.getValue().equals(preProductStudy.getValue())) {//定位变动用户损失
                preUserAmount = preUserAmount * 80 / 100;
            }
        }
        Integer satisfaction = get(EPropertyName.SATISFACTION.name());
        return preUserAmount * satisfaction / 100 * RandomUtil.random(80, 120) / 100;
    }

    /**
     *
     * @return 总用户数
     */
    private Integer calculateUserAmount() {
        Integer oldUserAmount = get(EPropertyName.OLD_USER_AMOUNT.name());
        Integer newUserAmount = get(EPropertyName.NEW_USER_AMOUNT.name());
        return oldUserAmount + newUserAmount;
    }

    /**
     * 本期收入
     * @return
     */
    private Integer currentIncome() {
        Integer userAmount = get(EPropertyName.USER_AMOUNT.name());
        Integer perOrderCost = get(EPropertyName.PER_ORDER_COST.name());
        return userAmount * perOrderCost * 50 / 100;
    }

}
