package com.rathink.ie.ibase.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.AccountManager;
import com.rathink.ie.ibase.service.CompanyTermManager;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryResource;
import com.rathink.ie.ibase.work.model.IndustryResourceChoice;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.ibase.service.InstructionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public CompanyTermInstruction saveOrUpdateInstructionByChoice(CompanyTerm companyTerm, String choiceId, String value) {
        IndustryResourceChoice industryResourceChoice = (IndustryResourceChoice) baseManager.getObject(IndustryResourceChoice.class.getName(), choiceId);
        IndustryResource industryResource = industryResourceChoice.getIndustryResource();

        String hql = "from CompanyTermInstruction where industryResourceChoice.id = :industryResourceChoiceId and companyTerm.id = :companyTermId";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("industryResourceChoiceId", industryResourceChoice.getId());
        queryParamMap.put("companyTermId", companyTerm.getId());
        CompanyTermInstruction companyTermInstruction = (CompanyTermInstruction) baseManager.getUniqueObjectByConditions(hql, queryParamMap);

        if (companyTermInstruction == null) {
            companyTermInstruction = new CompanyTermInstruction();
            companyTermInstruction.setCampaignDate(companyTerm.getCampaignDate());
            companyTermInstruction.setCampaign(companyTerm.getCampaign());
            companyTermInstruction.setCompany(companyTerm.getCompany());
            companyTermInstruction.setCompanyTerm(companyTerm);
            companyTermInstruction.setIndustryResourceChoice(industryResourceChoice);
            companyTermInstruction.setIndustryResource(industryResourceChoice.getIndustryResource());
            companyTermInstruction.setBaseType(industryResource.getName());
            companyTermInstruction.setStatus(EInstructionStatus.DQD.getValue());
            companyTermInstruction.setDept(industryResource.getDept());
        }
        companyTermInstruction.setValue(value);
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);

        return companyTermInstruction;
    }

    @Override
    public CompanyTermInstruction saveOrUpdateInstructionByResource(CompanyTerm companyTerm, String resourceId, String value) {
        IndustryResource industryResource = (IndustryResource) baseManager.getObject(IndustryResource.class.getName(), resourceId);

        String hql = "from CompanyTermInstruction where industryResource.id = :industryResourceId and companyTerm.id = :companyTermId";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("industryResourceId", industryResource.getId());
        queryParamMap.put("companyTermId", companyTerm.getId());
        CompanyTermInstruction companyTermInstruction = (CompanyTermInstruction) baseManager.getUniqueObjectByConditions(hql, queryParamMap);

        if (companyTermInstruction == null) {
            companyTermInstruction = new CompanyTermInstruction();
            companyTermInstruction.setCampaignDate(companyTerm.getCampaignDate());
            companyTermInstruction.setCampaign(companyTerm.getCampaign());
            companyTermInstruction.setCompany(companyTerm.getCompany());
            companyTermInstruction.setCompanyTerm(companyTerm);
            companyTermInstruction.setIndustryResource(industryResource);
            companyTermInstruction.setBaseType(industryResource.getName());
            companyTermInstruction.setStatus(EInstructionStatus.DQD.getValue());
            companyTermInstruction.setDept(industryResource.getDept());
        }
        companyTermInstruction.setValue(value);
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);

        return companyTermInstruction;
    }

    @Override
    public void deleteInstruction(Company company, String campaignTermChoiceId) {
        String hql = "from CompanyTermInstruction where campaignTermChoice.id = :campaignTermChoiceId" +
                " and company.id = :companyId and campaignDate = :campaignDate";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("campaignTermChoiceId", campaignTermChoiceId);
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("campaignDate", company.getCurrentCampaignDate());
        CompanyTermInstruction companyTermInstruction = (CompanyTermInstruction) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        if (companyTermInstruction != null) {
            baseManager.delete(CompanyTermInstruction.class.getName(), companyTermInstruction.getId());
        }
    }

    @Override
    public List<CompanyTermInstruction> listCompanyInstruction(Campaign campaign, String baseType){
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTermInstruction where campaign.id = :campaignId and baseType = :baseType and  status=:status");
        xQuery.put("campaignId", campaign.getId());
        xQuery.put("baseType", baseType);
        xQuery.put("status", EInstructionStatus.YXZ.getValue());
        List<CompanyTermInstruction> companyTermInstructionList = baseManager.listObject(xQuery);
        return companyTermInstructionList;
    }

    @Override
    public List<CompanyTermInstruction> listCompanyInstruction(Campaign campaign, Integer campaignDate, String baseType){
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTermInstruction where campaign.id = :campaignId and campaignDate = :campaignDate" +
                " and baseType = :baseType and  status=:status");
        xQuery.put("campaignId", campaign.getId());
        xQuery.put("campaignDate", campaignDate);
        xQuery.put("baseType", baseType);
        xQuery.put("status", EInstructionStatus.YXZ.getValue());
        List<CompanyTermInstruction> companyTermInstructionList = baseManager.listObject(xQuery);
        return companyTermInstructionList;
    }


    @Override
    public List<CompanyTermInstruction> listCompanyInstruction(Company company, String baseType) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTermInstruction where baseType = :baseType and  status=:status and company.id = :companyId");
        xQuery.put("baseType", baseType);
        xQuery.put("status", EInstructionStatus.YXZ.getValue());
        xQuery.put("companyId", company.getId());
        List<CompanyTermInstruction> companyTermInstructionList = baseManager.listObject(xQuery);
        return companyTermInstructionList;
    }

    @Override
    public List<CompanyTermInstruction> listCompanyInstruction(Company company, String baseType, String status) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTermInstruction where baseType = :baseType and  status=:status and company.id = :companyId");
        xQuery.put("baseType", baseType);
        xQuery.put("status", status);
        xQuery.put("companyId", company.getId());
        List<CompanyTermInstruction> companyTermInstructionList = baseManager.listObject(xQuery);
        return companyTermInstructionList;
    }

    @Override
    public List<CompanyTermInstruction> listCompanyInstruction(CompanyTerm companyTerm, String baseType) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTermInstruction where baseType = :baseType and  status=:status and companyTerm.id = :companyTermId");
        xQuery.put("baseType", baseType);
        xQuery.put("status", EInstructionStatus.YXZ.getValue());
        xQuery.put("companyTermId", companyTerm.getId());
        List<CompanyTermInstruction> companyTermInstructionList = baseManager.listObject(xQuery);
        return companyTermInstructionList;
    }

    public List<CompanyTermInstruction> listCompanyInstruction(CompanyTerm companyTerm){
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTermInstruction where companyTerm.id = :companyTermId");
        xQuery.put("companyTermId", companyTerm.getId());
        List<CompanyTermInstruction> companyTermInstructionList = baseManager.listObject(xQuery);
        return companyTermInstructionList;
    }

/*    public Integer countCompanyInstruction(CampaignTermChoice campaignTermChoice) {
        String sql = "select count(id) from company_instruction where campaign_term_choice_id = :campaignTermChoiceId";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("campaignTermChoiceId", campaignTermChoice.getId());
        List<BigInteger> list = (List) baseManager.executeSql("list", sql, queryParamMap);
        return list.get(0).intValue();
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

}
