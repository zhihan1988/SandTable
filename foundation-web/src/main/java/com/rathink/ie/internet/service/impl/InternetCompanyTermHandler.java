package com.rathink.ie.internet.service.impl;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyStatusProperty;
import com.rathink.ie.ibase.service.CompanyTermHandler;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.Edept;
import com.rathink.ie.internet.choice.model.Human;
import com.rathink.ie.internet.choice.model.MarketActivityChoice;
import com.rathink.ie.internet.choice.model.ProductStudy;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Hean on 2015/9/4.
 */
@Component
public class InternetCompanyTermHandler extends CompanyTermHandler {

    @Override
    protected CompanyStatusProperty produceProperty(String key) {
        EPropertyName ePropertyName = EPropertyName.getEProperyName(key);
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
            default:
                throw new NotImplementedException();
        }
        return new CompanyStatusProperty(ePropertyName, value, companyTerm);
    }

    @Override
    public void competitiveBidding() {

    }

    @Override
    public void calculateAll() {
        for (EPropertyName ePropertyName : EPropertyName.values()) {

        }
    }

    /**
     * 计算部门能力
     * @param type 部门类型
     * @return 部门能力
     */
    public String calculateDeptAbility(String type) {
        List<CompanyInstruction> companyInstructionList = listInstruction(EChoiceBaseType.HUMAN.name());
        Integer ability = 60;
        for (CompanyInstruction companyInstruction : companyInstructionList) {
            Human human = ((Human)companyInstruction.getCompanyChoice());
            if (human.getType().equals(type)) {
                ability += Integer.valueOf(human.getAbility());
            }
        }
        return String.valueOf(ability);
    }

    private String calculateOfficeRatio(){
        String officeFee = get("officeFee");
        Double officeRatio = Math.sqrt(Integer.valueOf(officeFee))+60;
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
        Double fee = new Double(0);
        List<CompanyInstruction> typeCompanyInstructionList = listInstruction(EChoiceBaseType.PRODUCT_STUDY_FEE.name());
        for (CompanyInstruction companyInstruction : typeCompanyInstructionList) {
            fee += Double.valueOf(companyInstruction.getValue());
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
        Double productFeeRatio = Double.valueOf(get("productFeeRatio"));
        Double preProductRatio = Double.valueOf(getPrePropertyValue(EPropertyName.PRODUCT_RATIO.name()));
        Double productRatio = Math.sqrt(productAbility * productFeeRatio + 10) + preProductRatio;
        return String.valueOf(productRatio.intValue());
    }

    /**
     *
     * @return  客单价
     */
    private String calculatePerOrderCost() {
        String grade = null;
        for (CompanyInstruction companyInstruction : companyInstructionList) {
            CompanyChoice companyChoice = companyInstruction.getCompanyChoice();
            if (EChoiceBaseType.PRODUCT_STUDY.name().equals(companyChoice.getBaseType())) {
                grade = ((ProductStudy) companyChoice).getGrade();
                break;
            }
        }
        Integer perOrdreCost = Integer.valueOf(grade) * 10 + 60;
        return String.valueOf(perOrdreCost);
    }

    /**
     *
     * @return 产品竞争系数
     */
    private String calculateProductCompetitionRatio() {
        List<Company> companyList = campaignManager.listCompany(companyTerm.getCampaign());
        Integer companySize = companyList.size();
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
        Integer newUserAmount = 0;
        Integer marketAbility = Integer.valueOf(get(EPropertyName.MARKET_ABILITY.name()));
        Integer productCompetitionRatio = Integer.valueOf(get(EPropertyName.PRODUCT_COMPETITION_RATIO.name()));
        for (CompanyInstruction companyInstruction : companyInstructionList) {
            CompanyChoice companyChoice = companyInstruction.getCompanyChoice();
            if (EChoiceBaseType.MARKET_ACTIVITY.name().equals(companyChoice.getBaseType())) {
                String marketCost = ((MarketActivityChoice)companyChoice).getCost();
                String marketFee = companyInstruction.getValue();
                newUserAmount += marketAbility * Integer.valueOf(marketCost) / Integer.valueOf(marketFee) / productCompetitionRatio;
            }
        }
        return String.valueOf(newUserAmount);
    }

    /**
     *
     * @return 运营资金投入系数
     */
    private String calculateOperationFeeRatio() {
        Integer operationFee = 0;
        List<CompanyInstruction> typeCompanyInstructionList = listInstruction(EChoiceBaseType.OPERATION.name());
        for (CompanyInstruction companyInstruction : companyInstructionList) {
            operationFee += Integer.valueOf(companyInstruction.getValue());
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
        Integer preUserAmount = Integer.valueOf(getPrePropertyValue(EPropertyName.USER_AMOUNT.name()));
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
