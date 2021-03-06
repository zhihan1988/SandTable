package com.rathink.ix.ibase.service;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ix.ibase.property.model.CompanyTerm;
import com.rathink.ix.ibase.work.model.CompanyTermInstruction;

import java.util.List;

/**
 * Created by Hean on 2015/9/4.
 */
public interface InstructionManager {

    CompanyTermInstruction saveOrUpdateInstructionByChoice(CompanyTerm companyTerm, String choiceId, String value);

    CompanyTermInstruction saveOrUpdateInstructionByResource(CompanyTerm companyTerm, String resourceId, String value);

    void deleteInstruction(Company company, String companyChoiceId);

    List<CompanyTermInstruction> listCompanyInstruction(Campaign campaign, String baseType);

    List<CompanyTermInstruction> listCompanyInstruction(Campaign campaign, Integer campaignDate, String baseType);

    List<CompanyTermInstruction> listCompanyInstruction(Company company, String baseType);

    List<CompanyTermInstruction> listCompanyInstruction(Company company, String baseType, String status);

    List<CompanyTermInstruction> listCompanyInstruction(CompanyTerm companyTerm, String baseType);

    List<CompanyTermInstruction> listCompanyInstruction(CompanyTerm companyTerm);

    CompanyTermInstruction getUniqueInstructionByBaseType(CompanyTerm companyTerm, String baseType);

    Integer sumFee(List<CompanyTermInstruction> companyTermInstructionList);

}
