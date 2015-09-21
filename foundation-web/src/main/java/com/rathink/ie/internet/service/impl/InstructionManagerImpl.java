package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.AccountManager;
import com.rathink.ie.ibase.service.CompanyTermManager;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.service.ChoiceManager;
import com.rathink.ie.internet.service.InstructionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Hean on 2015/8/26.
 */
@Service
public class InstructionManagerImpl implements InstructionManager {

    @Autowired
    private BaseManager baseManager;
    @Autowired
    private CompanyTermManager companyTermManager;
    @Autowired
    private AccountManager accountManager;

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

    @Override
    public void deleteInstruction(Company company, String companyChoiceId) {
        String hql = "from CompanyInstruction where companyChoice.id = :companyChoiceId" +
                " and company.id = :companyId and campaignDate = :campaignDate";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyChoiceId", companyChoiceId);
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("campaignDate", company.getCurrentCampaignDate());
        CompanyInstruction companyInstruction = (CompanyInstruction) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        if (companyInstruction != null) {
            baseManager.delete(CompanyInstruction.class.getName(), companyInstruction.getId());
        }
    }

    @Override
    public List<CompanyInstruction> listCompanyInstructionByType(Company company, String baseType) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyInstruction where baseType = :baseType and  status=:status and company.id = :companyId");
        xQuery.put("baseType", baseType);
        xQuery.put("status", EInstructionStatus.YXZ.getValue());
        xQuery.put("companyId", company.getId());
        List<CompanyInstruction> companyInstructionList = baseManager.listObject(xQuery);
        return companyInstructionList;
    }

    @Override
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

    @Override
    public List<CompanyInstruction> listCompanyInstruction(CompanyChoice companyChoice) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyInstruction where companyChoice.id = :companyChoiceId");
        xQuery.put("companyChoiceId", companyChoice.getId());
        List<CompanyInstruction> companyInstructionList = baseManager.listObject(xQuery);
        return companyInstructionList;
    }

    public Integer countCompanyInstruction(CompanyChoice companyChoice) {
        String sql = "select count(id) from company_instruction where company_choice_id = :companyChoiceId";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyChoiceId", companyChoice.getId());
        List<BigInteger> list = (List) baseManager.executeSql("list", sql, queryParamMap);
        return list.get(0).intValue();
    }

    @Override
    public List<CompanyInstruction> listCampaignCompanyInstructionByDate(String campaignId, String campaignDate) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyInstruction where campaign.id = :campaignId and campaignDate = :campaignDate");
        xQuery.put("campaignId", campaignId);
        xQuery.put("campaignDate", campaignDate);
        List<CompanyInstruction> companyInstructionList = baseManager.listObject(xQuery);
        return companyInstructionList;
    }

    public CompanyInstruction getUniqueInstruction(CompanyTerm companyTerm, String baseType) {
        String hql = "from CompanyInstruction where baseType = :baseType and companyTerm.id = :companyTermId";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyTermId", companyTerm.getId());
        queryParamMap.put("baseType", baseType);
        CompanyInstruction companyInstruction = (CompanyInstruction) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        return companyInstruction;
    }

    public Integer sumFee(List<CompanyInstruction> companyInstructionList) {
        Integer fee = 0;
        if (companyInstructionList != null) {
            for (CompanyInstruction companyInstruction : companyInstructionList) {
                if (companyInstruction.getValue() != null) {
                    fee += Integer.parseInt(companyInstruction.getValue());
                }
            }
        }
        return fee;
    }

    public void fireHuman(String companyInstructionId) {
        CompanyInstruction companyInstruction = (CompanyInstruction) baseManager.getObject(CompanyInstruction.class.getName(), companyInstructionId);
        Integer fee = Integer.valueOf(companyInstruction.getValue()) * 3;
        Company company = companyInstruction.getCompany();
        CompanyTerm companyTerm = companyTermManager.getCompanyTerm(company, company.getCurrentCampaignDate());
        accountManager.saveAccount(String.valueOf(fee), EAccountEntityType.HR_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
        companyInstruction.setStatus(EInstructionStatus.YSC.getValue());
        baseManager.saveOrUpdate(CompanyInstruction.class.getName(), companyInstruction);
    }
}
