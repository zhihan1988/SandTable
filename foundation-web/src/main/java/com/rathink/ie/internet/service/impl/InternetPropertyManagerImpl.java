package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.util.RandomUtil;
import com.rathink.ie.ibase.property.model.CompanyStatus;
import com.rathink.ie.ibase.property.model.CompanyStatusPropertyValue;
import com.rathink.ie.ibase.service.CompanyStatusManager;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.choice.model.MarketActivityChoice;
import com.rathink.ie.internet.instruction.model.HrInstruction;
import com.rathink.ie.internet.instruction.model.MarketInstruction;
import com.rathink.ie.internet.instruction.model.OperationInstruction;
import com.rathink.ie.internet.instruction.model.ProductStudyInstruction;
import com.rathink.ie.internet.service.InternetPropertyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hean on 2015/8/30.
 */
@Service
public class InternetPropertyManagerImpl implements InternetPropertyManager {
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private CompanyStatusManager companyStatusManager;

    /**
     * 计算部门能力
     * @param hrInstructionList hr部门的人才决策
     * @param dept 人才的部门类型
     * @return
     */
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

    /**
     * 新用户数
     * @param marketInstructionList
     * @return
     */
    @Override
    public Integer getNewUserAmount(List<MarketInstruction> marketInstructionList) {
        Integer newUserAmount = 0;
        if(marketInstructionList == null) return 0;
        for (MarketInstruction marketInstruction : marketInstructionList) {
            MarketActivityChoice marketActivityChoice = marketInstruction.getMarketActivityChoice();
            Integer fee = Integer.valueOf(marketInstruction.getFee());
            Integer cost = Integer.valueOf(marketInstruction.getMarketActivityChoice().getCost());
            int randomRatio = RandomUtil.random(Integer.valueOf(marketActivityChoice.getRandomLow()), Integer.valueOf(marketActivityChoice.getRandomHigh()));
            newUserAmount += fee * randomRatio / cost / 100;
            System.out.println(marketInstruction.getCompany().getName() + ";市场投入:" + fee + ";成本:" + cost + ";市场随机系数：" + randomRatio + "新用户数");
        }
        return newUserAmount;
    }

    /**
     * 满意度
     * @param operationInstructionList
     * @param operationAbility
     * @return
     */
    @Override
    public Integer getSatisfaction(List<OperationInstruction> operationInstructionList, Integer operationAbility){
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

    /**
     * 老用户数
     * @param company
     * @param satisfaction
     * @return
     */
    @Override
    public Integer getOldUserAmount(Company company, Integer satisfaction){
        Integer oldUserAmount = 0;
        CompanyStatus companyStatus = companyStatusManager.getCompanyStatus(company, company.getCampaign().getCurrentCampaignDate());
        //上一期用户数
        CompanyStatusPropertyValue preUserAmount = companyStatusManager
                .getCompanyStatusProperty(EPropertyName.USER_AMOUNT.name(), companyStatus);
        oldUserAmount = Integer.valueOf(preUserAmount.getValue()) * satisfaction / 100;
        System.out.println("上一期老用户数：" + preUserAmount.getValue() + ";满意度：" + satisfaction + ";本轮老用户数：" + oldUserAmount);
        return oldUserAmount;
    }

    /**
     * 产品系数
     * @param company
     * @param productAbility
     * @param productStudyInstruction
     * @return
     */
    @Override
    public Integer getProductRadio(Company company, Integer productAbility, ProductStudyInstruction productStudyInstruction) {
        Integer productRadio = 10;
        String fee = productStudyInstruction == null ? "0" : productStudyInstruction.getFee();
        //资金投入系数
        Integer feeRatio = Integer.valueOf(fee) / 1000 + 50;
        //上一期的产品系数
        CompanyStatus companyStatus = companyStatusManager.getCompanyStatus(company, company.getCampaign().getCurrentCampaignDate());
        CompanyStatusPropertyValue preProductRatio = companyStatusManager
                .getCompanyStatusProperty(EPropertyName.PRODUCT_RATIO.name(), companyStatus);

        productRadio = (productAbility * feeRatio) / 500 + Integer.valueOf(preProductRatio.getValue());
        System.out.println("上一期产品系数：" + preProductRatio + ";产品能力：" + productAbility + ";资金投入系数：" + feeRatio+"本轮产品系数："+productRadio);
        return productRadio;
    }

    /**
     * 客单价
     * @param company
     * @param productStudyInstruction
     * @return
     */
    @Override
    public Integer getPerOrderCost(Company company, ProductStudyInstruction productStudyInstruction) {
        final String DEFAULT_GRADE = "3";
        Integer perOrderCost = 0;
        String grade = productStudyInstruction == null ? DEFAULT_GRADE : productStudyInstruction.getProductStudy().getGrade();
        perOrderCost = Integer.valueOf(grade) * 10 + 150;
        return perOrderCost;
    }

    /**
     * 本轮收入
     * @param userAmount
     * @param perOrdreCost
     * @return
     */
    @Override
    public Integer getCurrentPeriodIncome(Integer userAmount, Integer perOrdreCost) {
        return userAmount * perOrdreCost / 10;
    }
}
