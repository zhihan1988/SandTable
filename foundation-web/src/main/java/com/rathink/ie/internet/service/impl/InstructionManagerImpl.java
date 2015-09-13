package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.util.RandomUtil;
import com.rathink.ie.ibase.service.CampaignCenter;
import com.rathink.ie.ibase.service.CampaignHandler;
import com.rathink.ie.ibase.service.CompanyTermHandler;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.service.InstructionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        CampaignHandler campaignHandler = CampaignCenter.getCampaignHandler(company.getCampaign().getId());
        CompanyTermHandler companyTermHandler = campaignHandler.getCompanyTermHandlerMap().get(company.getId());
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
        companyInstruction.setCompanyTerm(companyTermHandler.getCompanyTerm());
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

    public List<CompanyInstruction> listCompanyInstructionByCampaignDate(String companyId, String campaignDate){
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyInstruction where company.id = :companyId and campaignDate = :campaignDate");
        xQuery.put("companyId", companyId);
        xQuery.put("campaignDate", campaignDate);
        List<CompanyInstruction> companyInstructionList = baseManager.listObject(xQuery);
        return companyInstructionList;
    }
}
