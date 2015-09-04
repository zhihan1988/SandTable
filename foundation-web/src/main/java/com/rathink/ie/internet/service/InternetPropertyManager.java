package com.rathink.ie.internet.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.internet.instruction.model.HrInstruction;
import com.rathink.ie.internet.instruction.model.MarketInstruction;
import com.rathink.ie.internet.instruction.model.OperationInstruction;
import com.rathink.ie.internet.instruction.model.ProductStudyInstruction;

import java.util.List;

/**
 * Created by Hean on 2015/9/4.
 */
public interface InternetPropertyManager {
    Integer getAbilityValue(List<HrInstruction> hrInstructionList, String dept);

    Integer getNewUserAmount(List<MarketInstruction> marketInstructionList);

    Integer getSatisfaction(List<OperationInstruction> operationInstructionList, Integer operationAbility);

    Integer getOldUserAmount(Company company, Integer satisfaction);

    Integer getProductRadio(Company company, Integer productAbility, ProductStudyInstruction productStudyInstruction);

    Integer getPerOrderCost(Company company, ProductStudyInstruction productStudyInstruction);

    Integer getCurrentPeriodIncome(Integer userAmount, Integer perOrdreCost);
}
