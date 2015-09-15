package com.rathink.ie.internet.service.impl;

import com.rathink.ie.ibase.service.CampaignHandler;
import com.rathink.ie.ibase.service.CompanyTermHandler;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.Edept;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Hean on 2015/9/4.
 */
@Component
public class InternetCompanyTermHandler extends CompanyTermHandler {

    @Override
    public String calculate(String key) {
        EPropertyName ePropertyName = EPropertyName.valueOf(key);
        String value = null;
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
    public String calculateDeptAbility(String type) {
        List<CompanyInstruction> companyInstructionList = preCompanyTermHandler == null ? null
                : preCompanyTermHandler.listCompanyInstructionByType(EChoiceBaseType.HUMAN.name());
        Integer ability = 60;
        if (companyInstructionList != null) {
            for (CompanyInstruction companyInstruction : companyInstructionList) {
                CompanyChoice human = companyInstruction.getCompanyChoice();
                if (human.getType().equals(type)) {
                    ability += Integer.valueOf(human.getValue());
                }
            }
        }
        return String.valueOf(ability);
    }

    private String calculateOfficeRatio() {
        List<CompanyInstruction> companyInstructionList = preCompanyTermHandler == null ? null
                : preCompanyTermHandler.listCompanyInstructionByType(EChoiceBaseType.OFFICE.name());
        Integer officeFee = 0;
        if (companyInstructionList != null) {
            for (CompanyInstruction companyInstruction : companyInstructionList) {
                officeFee += Integer.valueOf(companyInstruction.getValue());
            }
        }
        Double officeRatio = Math.sqrt(Integer.valueOf(officeFee)) + 60;
        return String.valueOf(officeRatio.intValue());
    }

    //    private String calculateRecruitmentRatio() {
//        String officeRatio = get(EPropertyName.OFFICE_RATIO.name());
//    }

    /**
     *
     * @return 产品资金投入系数
     */
    private String calculateProductFeeRatio() {
        List<CompanyInstruction> typeCompanyInstructionList = preCompanyTermHandler == null ? null
                : preCompanyTermHandler.listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY_FEE.name());
        Double fee = new Double(0);
        if (typeCompanyInstructionList != null) {
            for (CompanyInstruction companyInstruction : typeCompanyInstructionList) {
                fee += Double.valueOf(companyInstruction.getValue());
            }
        }
        Double productFeeRatio = fee / 1000 + 100;
        return String.valueOf(productFeeRatio.intValue());
    }

    /**
     *
     * @return 产品系数
     */
    private String calculateProductRatio() {
        Double productAbility = Double.valueOf(get(EPropertyName.PRODUCT_ABILITY.name()));
        Double productFeeRatio = Double.valueOf(get(EPropertyName.PRODUCT_FEE_RATIO.name()));
        Double preProductRatio = preCompanyTermHandler == null ? 0 : Double.valueOf(preCompanyTermHandler.get(EPropertyName.PRODUCT_RATIO.name()));
        Double productRatio = Math.sqrt(productAbility * productFeeRatio + 10) + preProductRatio;
        return String.valueOf(productRatio.intValue());
    }

    /**
     *
     * @return  客单价
     */
    private String calculatePerOrderCost() {
        List<CompanyInstruction> productStudyInstructionList = preCompanyTermHandler == null ? null
                : preCompanyTermHandler.listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY.name());

        String grade = "1";
        if (productStudyInstructionList != null) {
            for (CompanyInstruction companyInstruction : productStudyInstructionList) {
                CompanyChoice companyChoice = companyInstruction.getCompanyChoice();
                grade = companyChoice.getValue();
                break;
            }
        }
        Integer perOrderCost = Integer.valueOf(grade) * 10 + 60;
        return String.valueOf(perOrderCost);
    }

    /**
     *
     * @return 产品竞争系数
     */
    private String calculateProductCompetitionRatio() {
        CampaignHandler campaignHandler = getCampaignHandler();
        Integer companySize = campaignHandler.getCompanyTermHandlerMap().size();
        Double productCompetitionRatio = 30 + 20 * Math.sqrt(companySize);
        return String.valueOf(productCompetitionRatio.intValue());
    }

    /**
     * $营销能力系数=50+∑(  营销人才系数)  ???不就是营销能力吗
     * @return 营销能力系数
     */
    private String calculateOperationAbilityRatio() {
        Integer operationHumanRatio = 50;
        return String.valueOf(operationHumanRatio);
    }

    /**
     *
     * @return 新用户数
     */
    private String calculateNewUserAmount() {
        List<CompanyInstruction> marketInstructionList = preCompanyTermHandler == null ? null
                : preCompanyTermHandler.listCompanyInstructionByType(EChoiceBaseType.MARKET_ACTIVITY.name());

        Integer newUserAmount = 0;
        Integer marketAbility = Integer.valueOf(get(EPropertyName.MARKET_ABILITY.name()));
        Integer productCompetitionRatio = Integer.valueOf(get(EPropertyName.PRODUCT_COMPETITION_RATIO.name()));
        if (marketInstructionList != null) {
            for (CompanyInstruction companyInstruction : marketInstructionList) {
                CompanyChoice companyChoice = companyInstruction.getCompanyChoice();
                String marketCost = companyChoice.getValue();
                String marketFee = companyInstruction.getValue();
                newUserAmount += marketAbility * Integer.valueOf(marketFee) / Integer.valueOf(marketCost) / productCompetitionRatio;
            }
        }
        return String.valueOf(newUserAmount);
    }

    /**
     *
     * @return 运营资金投入系数
     */
    private String calculateOperationFeeRatio() {
        List<CompanyInstruction> typeCompanyInstructionList = preCompanyTermHandler == null ? null
                : preCompanyTermHandler.listCompanyInstructionByType(EChoiceBaseType.OPERATION.name());
        Integer operationFee = 0;
        if (typeCompanyInstructionList != null) {
            for (CompanyInstruction companyInstruction : typeCompanyInstructionList) {
                operationFee += Integer.valueOf(companyInstruction.getValue());
            }
        }
        return String.valueOf(operationFee / 10000 + 50);
    }

    /**
     * 满意度系数
     * @return
     */
    private String calculateSatisfaction() {
        Integer operationAbility = Integer.valueOf(get(EPropertyName.OPERATION_ABILITY.name()));
        Integer operationFeeRatio = Integer.valueOf(get(EPropertyName.OPERATION_FEE_RATIO.name()));
        Integer productRatio = Integer.valueOf(get(EPropertyName.PRODUCT_RATIO.name()));
        return String.valueOf(operationAbility * operationFeeRatio * productRatio);
    }

    /**
     *
     * @return 老用户数
     */
    private String calculateOldUserAmount() {
        Integer preUserAmount = preCompanyTermHandler == null ? 0 : Integer.valueOf(preCompanyTermHandler.get(EPropertyName.USER_AMOUNT.name()));
        Integer satisfaction = Integer.valueOf(get(EPropertyName.SATISFACTION.name()));
        return String.valueOf(preUserAmount * satisfaction / 100);
    }

    /**
     *
     * @return 总用户数
     */
    private String calculateUserAmount() {
        Integer oldUserAmount = Integer.valueOf(get(EPropertyName.OLD_USER_AMOUNT.name()));
        Integer newUserAmount = Integer.valueOf(get(EPropertyName.NEW_USER_AMOUNT.name()));
        return String.valueOf(oldUserAmount + newUserAmount);
    }

    /**
     * 本期收入
     * @return
     */
    private String currentIncome() {
        Integer userAmount = Integer.valueOf(get(EPropertyName.USER_AMOUNT.name()));
        Integer perOrderCost = Integer.valueOf(get(EPropertyName.PER_ORDER_COST.name()));
        return String.valueOf(userAmount * perOrderCost);
    }

}
