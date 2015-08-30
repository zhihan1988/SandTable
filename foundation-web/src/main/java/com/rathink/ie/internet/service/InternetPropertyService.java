package com.rathink.ie.internet.service;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyStatus;
import com.rathink.ie.ibase.property.model.CompanyStatusPropertyValue;
import com.rathink.ie.ibase.service.CompanyStatusService;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.instruction.model.HrInstruction;
import com.rathink.ie.internet.instruction.model.MarketInstruction;
import com.rathink.ie.internet.instruction.model.OperationInstruction;
import com.rathink.ie.internet.instruction.model.ProductStudyInstruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hean on 2015/8/30.
 */
@Service
public class InternetPropertyService {
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private CompanyStatusService companyStatusService;

    /**
     * 计算部门能力
     * @param hrInstructionList hr部门的人才决策
     * @param dept 人才的部门类型
     * @return
     */
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

    public Integer getNewUserAmount(List<MarketInstruction> marketInstructionList) {
        Integer newUserAmount = 0;
        if(marketInstructionList==null) return 0;
        for (MarketInstruction marketInstruction : marketInstructionList) {
            Integer fee = Integer.valueOf(marketInstruction.getFee());
            Integer cost = Integer.valueOf(marketInstruction.getMarketActivityChoice().getCost());
            newUserAmount += fee / cost;
        }
        return newUserAmount;
    }

    public Integer getSatisfaction(List<OperationInstruction> operationInstructionList, Integer operationAbility){
        Integer satisfaction = 50;
        Integer operationFee = 0;
        if (operationInstructionList != null) {
            for (OperationInstruction operationInstruction : operationInstructionList) {
                operationFee += Integer.valueOf(operationInstruction.getFee());
            }
        }
        Integer operationFeeRatio = operationFee / 10000 + 50;
        satisfaction = operationAbility * operationFeeRatio / 100;
        return satisfaction;
    }

    public Integer getOldUserAmount(Company company, Integer satisfaction){
        Integer oldUserAmount = 0;
        CompanyStatus companyStatus = companyStatusService.getCompanyStatus(company, company.getCampaign().getCurrentCampaignDate());
        //上一期用户数
        CompanyStatusPropertyValue preUserAmount = companyStatusService
                .getCompanyStatusProperty(EPropertyName.USER_AMOUNT.name(), companyStatus);
        oldUserAmount = Integer.valueOf(preUserAmount.getValue()) * satisfaction / 100;
        return oldUserAmount;
    }


    public Integer getProductRadio(Company company, Integer productAbility, ProductStudyInstruction productStudyInstruction) {
        Integer productRadio = 10;
        String fee = productStudyInstruction == null ? "0" : productStudyInstruction.getFee();
        //资金投入系数
        Integer feeRatio = Integer.valueOf(fee) / 1000 + 100;
        //上一期的产品系数
        CompanyStatus companyStatus = companyStatusService.getCompanyStatus(company, company.getCampaign().getCurrentCampaignDate());
        CompanyStatusPropertyValue preProductRatio = companyStatusService
                .getCompanyStatusProperty(EPropertyName.PRODUCT_RATIO.name(), companyStatus);

        productRadio = (productAbility * feeRatio) / 100 + Integer.valueOf(preProductRatio.getValue());
        return productRadio;
    }

    public Integer getPerOrderCost(Company company, ProductStudyInstruction productStudyInstruction) {
        final String DEFAULT_GRADE = "3";
        Integer perOrderCost = 0;
        String grade = productStudyInstruction == null ? DEFAULT_GRADE : productStudyInstruction.getProductStudy().getGrade();
        perOrderCost = Integer.valueOf(grade) * 10 + 60;
        return perOrderCost;
    }

    public Integer getCurrentPeriodIncome(Integer userAmount, Integer perOrdreCost) {
        return userAmount * perOrdreCost;
    }
}
