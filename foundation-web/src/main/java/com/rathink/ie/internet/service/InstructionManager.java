package com.rathink.ie.internet.service;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.internet.choice.model.*;
import com.rathink.ie.internet.instruction.model.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/9/4.
 */
public interface InstructionManager {
    void produceHumanDiddingResult(Campaign campaign);

    List<OfficeInstruction> listOfficeInstruction(Company company);

    List<HrInstruction> listHrInstruction(Company company);

    List<MarketInstruction> listMarketInstruction(Company company, String campaignDate);

    List<OperationInstruction> listOperationInstruction(Company company, String campaignDate);

    ProductStudyInstruction getProductStudyInstruction(Company company, String campaignDate);

    void saveOrUpdateOfficeInstruction(Company company, OfficeChoice officeChoice, Map<String, String> map);

    void saveOrUpdateHrInstruction(Company company, Human human, Map<String, String> map);

    void saveOrUpdateMarketInstruction(Company company, MarketActivityChoice marketActivityChoice, Map<String, String> map);

    void saveOrUpdateProductStudyInstruction(Company company, ProductStudy productStudy, Map<String, String> map);

    void saveOrUpdateOperationInstruction(Company company, OperationChoice operationChoice, Map<String, String> map);

    Integer productGradeConflict(ProductStudyInstruction productStudyInstruction);

    Integer getProductGradeChangeRatio(ProductStudyInstruction productStudyInstruction);
}
