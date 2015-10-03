package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.AccountManager;
import com.rathink.ie.ibase.service.CompanyTermManager;
import com.rathink.ie.ibase.work.model.CampaignTermChoice;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.internet.service.InstructionManager;
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
    public CompanyTermInstruction saveOrUpdateInstructionByChoice(Company company, String companyChoiceId, String value) {
        CompanyTerm companyTerm = companyTermManager.getCompanyTerm(company, company.getCurrentCampaignDate());
        CampaignTermChoice campaignTermChoice = (CampaignTermChoice) baseManager.getObject(CampaignTermChoice.class.getName(), companyChoiceId);
        Campaign campaign = company.getCampaign();
        String hql = "from CompanyTermInstruction where companyChoice.id = :companyChoiceId" +
                " and company.id = :companyId and campaignDate = :campaignDate";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyChoiceId", companyChoiceId);
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("campaignDate", campaign.getCurrentCampaignDate());
        CompanyTermInstruction companyTermInstruction = (CompanyTermInstruction) baseManager.getUniqueObjectByConditions(hql, queryParamMap);

        if (companyTermInstruction == null) {
            companyTermInstruction = new CompanyTermInstruction();
            companyTermInstruction.setCampaignDate(campaign.getCurrentCampaignDate());
            companyTermInstruction.setCampaign(campaign);
            companyTermInstruction.setCompany(company);
        }
        companyTermInstruction.setCompanyTerm(companyTerm);
        companyTermInstruction.setCampaignTermChoice(campaignTermChoice);
        companyTermInstruction.setBaseType(campaignTermChoice.getBaseType());
        companyTermInstruction.setStatus(EInstructionStatus.DQD.getValue());
        companyTermInstruction.setValue(value);
        companyTermInstruction.setDept(campaignTermChoice.getDept());
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);

        return companyTermInstruction;
    }

    @Override
    public void deleteInstruction(Company company, String companyChoiceId) {
        String hql = "from CompanyTermInstruction where companyChoice.id = :companyChoiceId" +
                " and company.id = :companyId and campaignDate = :campaignDate";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyChoiceId", companyChoiceId);
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("campaignDate", company.getCurrentCampaignDate());
        CompanyTermInstruction companyTermInstruction = (CompanyTermInstruction) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        if (companyTermInstruction != null) {
            baseManager.delete(CompanyTermInstruction.class.getName(), companyTermInstruction.getId());
        }
    }

    @Override
    public List<CompanyTermInstruction> listCompanyInstructionByType(Company company, String baseType) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTermInstruction where baseType = :baseType and  status=:status and company.id = :companyId");
        xQuery.put("baseType", baseType);
        xQuery.put("status", EInstructionStatus.YXZ.getValue());
        xQuery.put("companyId", company.getId());
        List<CompanyTermInstruction> companyTermInstructionList = baseManager.listObject(xQuery);
        return companyTermInstructionList;
    }

    @Override
    public List<CompanyTermInstruction> listCompanyInstructionByType(CompanyTerm companyTerm, String baseType) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTermInstruction where baseType = :baseType and  status=:status and companyTerm.id = :companyTermId");
        xQuery.put("baseType", baseType);
        xQuery.put("status", EInstructionStatus.YXZ.getValue());
        xQuery.put("companyTermId", companyTerm.getId());
        List<CompanyTermInstruction> companyTermInstructionList = baseManager.listObject(xQuery);
        return companyTermInstructionList;
    }

   /* @Override
    public List<CompanyInstruction> listCompanyInstructionByDept(Company company, String dept) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTermInstruction where dept = :dept and  status=:status and company.id = :companyId");
        xQuery.put("dept", dept);
        xQuery.put("status", EInstructionStatus.YXZ.getValue());
        xQuery.put("companyId", company.getId());
        List<CompanyInstruction> companyInstructionList = baseManager.listObject(xQuery);
        return companyInstructionList;
    }*/

    public List<CompanyTermInstruction> listCompanyInstruction(CompanyTerm companyTerm){
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTermInstruction where companyTerm.id = :companyTermId");
        xQuery.put("companyTermId", companyTerm.getId());
        List<CompanyTermInstruction> companyTermInstructionList = baseManager.listObject(xQuery);
        return companyTermInstructionList;
    }

    @Override
    public List<CompanyTermInstruction> listCompanyInstruction(CampaignTermChoice campaignTermChoice) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTermInstruction where companyChoice.id = :companyChoiceId");
        xQuery.put("companyChoiceId", campaignTermChoice.getId());
        List<CompanyTermInstruction> companyTermInstructionList = baseManager.listObject(xQuery);
        return companyTermInstructionList;
    }

    public Integer countCompanyInstruction(CampaignTermChoice campaignTermChoice) {
        String sql = "select count(id) from company_instruction where company_choice_id = :companyChoiceId";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyChoiceId", campaignTermChoice.getId());
        List<BigInteger> list = (List) baseManager.executeSql("list", sql, queryParamMap);
        return list.get(0).intValue();
    }

   /* @Override
    public List<CompanyInstruction> listCampaignCompanyInstructionByDate(String campaignId, String campaignDate) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTermInstruction where campaign.id = :campaignId and campaignDate = :campaignDate");
        xQuery.put("campaignId", campaignId);
        xQuery.put("campaignDate", campaignDate);
        List<CompanyInstruction> companyInstructionList = baseManager.listObject(xQuery);
        return companyInstructionList;
    }*/

    public CompanyTermInstruction getUniqueInstructionByBaseType(CompanyTerm companyTerm, String baseType) {
        String hql = "from CompanyTermInstruction where baseType = :baseType and companyTerm.id = :companyTermId";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyTermId", companyTerm.getId());
        queryParamMap.put("baseType", baseType);
        CompanyTermInstruction companyTermInstruction = (CompanyTermInstruction) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        return companyTermInstruction;
    }

    public Integer sumFee(List<CompanyTermInstruction> companyTermInstructionList) {
        Integer fee = 0;
        if (companyTermInstructionList != null) {
            for (CompanyTermInstruction companyTermInstruction : companyTermInstructionList) {
                if (companyTermInstruction.getValue() != null) {
                    fee += Integer.parseInt(companyTermInstruction.getValue());
                }
            }
        }
        return fee;
    }

    public void fireHuman(String companyInstructionId) {
        CompanyTermInstruction companyTermInstruction = (CompanyTermInstruction) baseManager.getObject(CompanyTermInstruction.class.getName(), companyInstructionId);
        Integer fee = Integer.valueOf(companyTermInstruction.getValue()) * 2;
        Company company = companyTermInstruction.getCompany();
        CompanyTerm companyTerm = companyTermManager.getCompanyTerm(company, company.getCurrentCampaignDate());
        Account account = accountManager.packageAccount(String.valueOf(fee), EAccountEntityType.HR_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
        baseManager.saveOrUpdate(Account.class.getName(), account);
        companyTermInstruction.setStatus(EInstructionStatus.YSC.getValue());
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);
    }
}
