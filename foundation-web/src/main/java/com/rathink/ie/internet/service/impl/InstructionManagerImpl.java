package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
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

    @Override
    public CompanyInstruction saveOrUpdateInstruction(Company company, String companyChoiceId, String value) {
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
        companyInstruction.setCompanyChoice(companyChoice);
        companyInstruction.setValue(value);
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

    public List<CompanyInstruction> listCompanyInstruction(Company company, String type) {
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        String hql = "from CompanyInstruction where type = :type and  status=:status and company.id = :companyId";
        queryParamMap.put("type", type);
        queryParamMap.put("status", EInstructionStatus.YXZ.getValue());
        queryParamMap.put("companyId", company.getId());
        XQuery xQuery = new XQuery();
        xQuery.setHql(hql);
        xQuery.setQueryParamMap(queryParamMap);
        List<CompanyInstruction> companyInstructionList = baseManager.listObject(xQuery);
        return companyInstructionList;
    }
}
