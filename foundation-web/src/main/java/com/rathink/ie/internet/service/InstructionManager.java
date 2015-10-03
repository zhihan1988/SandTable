package com.rathink.ie.internet.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.work.model.CampaignTermChoice;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;

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
    CompanyTermInstruction saveOrUpdateInstructionByChoice(Company company, String choiceId, String fee);

    void deleteInstruction(Company company, String companyChoiceId);

    List<CompanyTermInstruction> listCompanyInstructionByType(Company company, String baseType);

    List<CompanyTermInstruction> listCompanyInstructionByType(CompanyTerm companyTerm, String baseType);

//    List<CompanyInstruction> listCompanyInstructionByDept(Company company, String dept);

    List<CompanyTermInstruction> listCompanyInstruction(CompanyTerm companyTerm);

    List<CompanyTermInstruction> listCompanyInstruction(CampaignTermChoice campaignTermChoice);

    Integer countCompanyInstruction(CampaignTermChoice campaignTermChoice);

//    List<CompanyInstruction> listCampaignCompanyInstructionByDate(String campaignId, Integer campaignDate);

    CompanyTermInstruction getUniqueInstructionByBaseType(CompanyTerm companyTerm, String baseType);

    Integer sumFee(List<CompanyTermInstruction> companyTermInstructionList);

    void fireHuman(String companyInstructionId);
}
