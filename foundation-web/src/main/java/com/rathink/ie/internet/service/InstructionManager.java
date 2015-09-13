package com.rathink.ie.internet.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.work.model.CompanyInstruction;

import java.util.List;

/**
 * Created by Hean on 2015/9/4.
 */
public interface InstructionManager {

    CompanyInstruction saveOrUpdateInstruction(Company company, String choiceId, String fee);

    void deleteInstruction(Company company, String companyChoiceId);

    List<CompanyInstruction> listCompanyInstructionByType(Company company, String baseType);

    List<CompanyInstruction> listCompanyInstructionByDept(Company company, String baseType);

    List<CompanyInstruction> listCompanyInstructionByCampaignDate(String companyId, String campaignDate);
}
