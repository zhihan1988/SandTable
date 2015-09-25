package com.rathink.ie.internet.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.CompanyInstruction;

import java.util.List;

/**
 * Created by Hean on 2015/9/4.
 */
public interface InstructionManager {

    /**
     * 针对选项choice保存或修改instruction
     * @param company
     * @param choiceId
     * @param fee
     * @return
     */
    CompanyInstruction saveOrUpdateInstructionByChoice(Company company, String choiceId, String fee);

    void deleteInstruction(Company company, String companyChoiceId);

    List<CompanyInstruction> listCompanyInstructionByType(Company company, String baseType);

    List<CompanyInstruction> listCompanyInstructionByDept(Company company, String dept);

    List<CompanyInstruction> listCompanyInstruction(CompanyTerm companyTerm);

    List<CompanyInstruction> listCompanyInstruction(CompanyChoice companyChoice);

    Integer countCompanyInstruction(CompanyChoice companyChoice);

    List<CompanyInstruction> listCampaignCompanyInstructionByDate(String campaignId, String campaignDate);

    CompanyInstruction getUniqueInstructionByBaseType(CompanyTerm companyTerm, String baseType);

    Integer sumFee(List<CompanyInstruction> companyInstructionList);

    void fireHuman(String companyInstructionId);
}
