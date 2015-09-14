package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.CompanyTermManager;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.internet.service.InstructionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Hean on 2015/8/26.
 */
@Service
public class InstructionManagerImpl implements InstructionManager {
    private static Logger logger = Logger.getLogger(InstructionManagerImpl.class.getName());

    @Autowired
    private BaseManager baseManager;
    @Autowired
    private CompanyTermManager companyTermManager;

    @Override
    public CompanyInstruction saveOrUpdateInstruction(Company company, String companyChoiceId, String value) {
        CompanyTerm companyTerm = companyTermManager.getCompanyTerm(company, company.getCurrentCampaignDate());
        CompanyChoice companyChoice = (CompanyChoice) baseManager.getObject(CompanyChoice.class.getName(), companyChoiceId);
        Campaign campaign = company.getCampaign();
        String hql = "from CompanyInstruction where companyChoice.id = :companyChoiceId" +
                " and company.id = :companyId and campaignDate = :campaignDate";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyChoiceId", companyChoiceId);
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("campaignDate", campaign.getCurrentCampaignDate());
        CompanyInstruction companyInstruction = (CompanyInstruction) baseManager.getUniqueObjectByConditions(hql, queryParamMap);

        if (companyInstruction == null) {
            companyInstruction = new CompanyInstruction();
            companyInstruction.setCampaignDate(campaign.getCurrentCampaignDate());
            companyInstruction.setCampaign(campaign);
            companyInstruction.setCompany(company);
        }
        companyInstruction.setCompanyTerm(companyTerm);
        companyInstruction.setCompanyChoice(companyChoice);
        companyInstruction.setBaseType(companyChoice.getBaseType());
        companyInstruction.setStatus(EInstructionStatus.DQD.getValue());
        companyInstruction.setValue(value);
        companyInstruction.setDept(companyChoice.getDept());
        baseManager.saveOrUpdate(CompanyInstruction.class.getName(), companyInstruction);

        return companyInstruction;
    }

    public void deleteInstruction(Company company, String companyChoiceId) {
        CompanyChoice companyChoice = (CompanyChoice) baseManager.getObject(CompanyChoice.class.getName(), companyChoiceId);
        Campaign campaign = company.getCampaign();
        String hql = "from CompanyInstruction where companyChoice.id = :companyChoiceId" +
                " and company.id = :companyId and campaignDate = :campaignDate";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyChoiceId", companyChoiceId);
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("campaignDate", campaign.getCurrentCampaignDate());
        CompanyInstruction companyInstruction = (CompanyInstruction) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        if (companyInstruction != null) {
            baseManager.delete(CompanyInstruction.class.getName(), companyInstruction.getId());
        }
    }

    public List<CompanyInstruction> listCompanyInstructionByType(Company company, String baseType) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyInstruction where baseType = :baseType and  status=:status and company.id = :companyId");
        xQuery.put("baseType", baseType);
        xQuery.put("status", EInstructionStatus.YXZ.getValue());
        xQuery.put("companyId", company.getId());
        List<CompanyInstruction> companyInstructionList = baseManager.listObject(xQuery);
        return companyInstructionList;
    }

    public List<CompanyInstruction> listCompanyInstructionByDept(Company company, String dept) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyInstruction where dept = :dept and  status=:status and company.id = :companyId");
        xQuery.put("dept", dept);
        xQuery.put("status", EInstructionStatus.YXZ.getValue());
        xQuery.put("companyId", company.getId());
        List<CompanyInstruction> companyInstructionList = baseManager.listObject(xQuery);
        return companyInstructionList;
    }

    public List<CompanyInstruction> listCompanyInstruction(CompanyTerm companyTerm){
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyInstruction where companyTerm.id = :companyTermId");
        xQuery.put("companyTermId", companyTerm.getId());
        List<CompanyInstruction> companyInstructionList = baseManager.listObject(xQuery);
        return companyInstructionList;
    }
}
