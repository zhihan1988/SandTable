package com.rathink.ie.internet.service.impl;

import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.util.RandomUtil;
import com.rathink.ie.ibase.service.CompanyTermContext;
import com.rathink.ie.ibase.service.IndustryExpressionManager;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryAnalyzer;
import com.rathink.ie.ibase.work.model.IndustryResourceChoice;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.Edept;
import com.rathink.ie.ibase.service.InstructionManager;
import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Hean on 2015/9/4.
 */
@Component
public class InternetCompanyTermContext extends CompanyTermContext {
    private static Logger logger = LoggerFactory.getLogger(FlowManagerImpl.class);

    final Double PERCENT = 100d;
    @Override
    public Integer calculate(String key) {
        EPropertyName ePropertyName = EPropertyName.valueOf(key);
        Integer value = 0;
        switch (ePropertyName) {
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
        List<CompanyTermInstruction> companyTermInstructionList = instructionManager.listCompanyInstruction(getCompanyTerm().getCompany(), EChoiceBaseType.HUMAN.name());
        Double humanAbility = 0d;
        if (companyTermInstructionList != null) {
            for (CompanyTermInstruction companyTermInstruction : companyTermInstructionList) {
                IndustryResourceChoice human = companyTermInstruction.getIndustryResourceChoice();
//                if (!human.getCampaignDate().equals(getCompanyTerm().getCampaignDate())) {//不计算当期的人才能力（延迟一期生效）
                    if (human.getType().equals(type)) {
                        humanAbility += Math.pow(Double.valueOf(human.getValue()), 1.3);
                    }
//                }
            }
        }
        Double ability = humanAbility * 1.2 + 30;
        return ability.intValue();
    }

    /**
     *
     * @return 产品资金投入系数
     */
    private Integer calculateProductFeeRatio() {
        List<CompanyTermInstruction> typeCompanyTermInstructionList = listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY_FEE.name());
        Integer fee = 0;
        if (typeCompanyTermInstructionList != null) {
            for (CompanyTermInstruction companyTermInstruction : typeCompanyTermInstructionList) {
                fee += Integer.valueOf(companyTermInstruction.getValue());
            }
        }
        String expressionStr = campaignContext.getExpressionMap().get("PRODUCT_FEE_RATIO");
        IndustryAnalyzer industryAnalyzer = new IndustryAnalyzer(expressionStr);
        industryAnalyzer.add("FEE", fee);
        Integer productFeeRatio = industryAnalyzer.getResult();
        return productFeeRatio;
    }

    /**
     *
     * @return 产品系数
     */
    private Integer calculateProductRatio() {
        Integer productAbility = preCompanyTermContext.get(EPropertyName.PRODUCT_ABILITY.name());
        Integer productFeeRatio = get(EPropertyName.PRODUCT_FEE_RATIO.name());
        Integer preProductRatio = preCompanyTermContext.get(EPropertyName.PRODUCT_RATIO.name());

        String expressionStr = campaignContext.getExpressionMap().get("PRODUCT_RATIO");
        IndustryAnalyzer industryAnalyzer = new IndustryAnalyzer(expressionStr);
        industryAnalyzer.add("PRODUCT_ABILITY",productAbility);
        industryAnalyzer.add("PRODUCT_FEE_RATIO",productFeeRatio);
        industryAnalyzer.add("PRE_PRODUCT_RATIO",preProductRatio);
        return industryAnalyzer.getResult();
    }

    /**
     *
     * @return  客单价
     */
    private Integer calculatePerOrderCost() {
        List<CompanyTermInstruction> productStudyInstructionList = listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY.name());
        String grade = "3";//默认高端
        if (productStudyInstructionList != null && productStudyInstructionList.size() > 0) {
            grade = productStudyInstructionList.get(0).getValue();
        }
        Integer perOrderCost = Integer.valueOf(grade) * 5 + 50;
        return perOrderCost;
    }

    /**
     *
     * @return 产品竞争系数
     */
    private Integer calculateProductCompetitionRatio() {
        //自己公司的定位
        List<CompanyTermInstruction> productStudyInstructionList = listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY.name());
        Integer sameGradeCount = 1;
        if (productStudyInstructionList != null && productStudyInstructionList.size() > 0) {
            CompanyTermInstruction productStudy = productStudyInstructionList.get(0);
            String choiceId = productStudy.getIndustryResourceChoice().getId();
            List<CompanyTermInstruction> allCompanyTermInstructionList = campaignContext.getCurrentChoiceInstructionMap().get(choiceId);
            sameGradeCount = allCompanyTermInstructionList == null ? 0 : allCompanyTermInstructionList.size();
        }
        //产品竞争系数计算公式
        Double productCompetitionRatio = 0d;
        try {
            productCompetitionRatio = 30 + 20 * Math.pow(sameGradeCount, 0.5);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

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

        List<CompanyTermInstruction> marketInstructionList = listCompanyInstructionByType(EChoiceBaseType.MARKET_ACTIVITY.name());

        Integer marketAbility = preCompanyTermContext.get(EPropertyName.MARKET_ABILITY.name());
        Integer productRatio = preCompanyTermContext.get(EPropertyName.PRODUCT_RATIO.name());
        Integer productCompetitionRatio = get(EPropertyName.PRODUCT_COMPETITION_RATIO.name());
        Double marketX = 0d;
        if (marketInstructionList != null) {
            for (CompanyTermInstruction companyTermInstruction : marketInstructionList) {
                String choiceId = companyTermInstruction.getIndustryResourceChoice().getId();
                List<CompanyTermInstruction> allCompanyTermInstructionList = campaignContext.getCurrentChoiceInstructionMap().get(choiceId);
                //市场活动竞争人数
                Integer count = allCompanyTermInstructionList == null ? 0 : allCompanyTermInstructionList.size();
                //市场活动竞争系数
                Double marketCompetitionRatio = 0d;
                try {
                    marketCompetitionRatio = Math.pow(count - 1, 0.7) * 20;
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                Double marketCost = Double.valueOf(companyTermInstruction.getIndustryResourceChoice().getValue());
                Integer marketFee = Integer.valueOf(companyTermInstruction.getValue());

                marketX += marketFee * (marketCompetitionRatio + 100) / 100 / marketCost / productCompetitionRatio * 100;
            }
        }
        Double newUserAmount = marketX * marketAbility/PERCENT * productRatio/PERCENT * 1.6 * RandomUtil.random(80, 120)/PERCENT;
        return newUserAmount.intValue();
    }

    /**
     *
     * @return 运营资金投入系数
     */
    private Integer calculateOperationFeeRatio() {
        List<CompanyTermInstruction> typeCompanyTermInstructionList = listCompanyInstructionByType(EChoiceBaseType.OPERATION.name());
        Integer operationFee = 0;
        if (typeCompanyTermInstructionList != null) {
            for (CompanyTermInstruction companyTermInstruction : typeCompanyTermInstructionList) {
                operationFee += Integer.valueOf(companyTermInstruction.getValue());
            }
        }
        Double operationFeeRatio = Math.pow(operationFee, 0.5) * 0.2;
        return operationFeeRatio.intValue();
    }

    /**
     * 满意度系数
     * @return
     */
    private Integer calculateSatisfaction() {
        Integer operationAbility = preCompanyTermContext.get(EPropertyName.OPERATION_ABILITY.name());
        Integer operationFeeRatio = get(EPropertyName.OPERATION_FEE_RATIO.name());
        Integer preProductRatio = preCompanyTermContext.get(EPropertyName.PRODUCT_RATIO.name());
        Double satisfaction = (1 + operationAbility / PERCENT) * (1 + operationFeeRatio / PERCENT) * 14 + preProductRatio * 0.4 + 10 + RandomUtil.random(0, 20);
        return satisfaction.intValue();
    }

    /**
     *
     * @return 老用户数
     */
    private Integer calculateOldUserAmount() {
        List<CompanyTermInstruction> companyTermInstructionList = listCompanyInstructionByType(EChoiceBaseType.PRODUCT_STUDY.name());
        CompanyTermInstruction productStudy = companyTermInstructionList == null ? null : companyTermInstructionList.get(0);
        Integer preUserAmount = 0;
        if (preCompanyTermContext != null) {
            InstructionManager instructionManager = (InstructionManager) ApplicationContextUtil.getApplicationContext().getBean("instructionManagerImpl");
            CompanyTermInstruction preProductStudy = instructionManager.getUniqueInstructionByBaseType(preCompanyTermContext.getCompanyTerm(), EChoiceBaseType.PRODUCT_STUDY.name());
            preUserAmount = preCompanyTermContext.get(EPropertyName.USER_AMOUNT.name());

            if (productStudy != null && preProductStudy != null && !productStudy.getValue().equals(preProductStudy.getValue())) {//定位变动用户损失
                preUserAmount = preUserAmount * 80 / 100;
            }
        }
        Integer satisfaction = preCompanyTermContext.get(EPropertyName.SATISFACTION.name());
        Integer oldUserAmount = preUserAmount * satisfaction / 100 * RandomUtil.random(80, 120) / 100;
        logger.info("计算老用户数-----------上一期总用户数：{}，满意度：{},本期老用户数：{}",preUserAmount,satisfaction,oldUserAmount);
        return oldUserAmount;
    }

    /**
     *
     * @return 总用户数
     */
    private Integer calculateUserAmount() {
        Integer oldUserAmount = get(EPropertyName.OLD_USER_AMOUNT.name());
        Integer newUserAmount = get(EPropertyName.NEW_USER_AMOUNT.name());
        logger.info("计算总用户数-----------老用户数量:{},新用户数量：{},总用户数量：{}",oldUserAmount,newUserAmount,oldUserAmount+newUserAmount);
        return oldUserAmount + newUserAmount;
    }

    /**
     * 本期收入
     * @return
     */
    private Integer currentIncome() {
        Integer userAmount = preCompanyTermContext.get(EPropertyName.USER_AMOUNT.name());
        Integer perOrderCost = preCompanyTermContext.get(EPropertyName.PER_ORDER_COST.name());
        return userAmount * perOrderCost;
    }

}
