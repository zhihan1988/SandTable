package com.rathink.ie.ibase.service;

import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyStatusProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.work.model.CompanyInstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/9/4.
 */
public abstract class CompanyTermHandler {
    protected CampaignHandler campaignHandler;
    protected CompanyTerm companyTerm;
    protected Map<String, String> propertyValueMap = new HashMap<>();
    protected Map<String,List<CompanyInstruction>> typeCompanyInstructionMap = new HashMap<>();
    protected List<Account> accountList = new ArrayList<>();
    protected CompanyTermHandler preCompanyTermHandler;

    public String get(String key) {
        if (!propertyValueMap.containsKey(key)) {
            put(key, calculate(key));
        }
        return propertyValueMap.get(key);
    }

    abstract public String calculate(String key);

    public void put(String key, String value) {
        propertyValueMap.put(key, value);
    }

    public CampaignHandler getCampaignHandler() {
        return campaignHandler;
    }

    public void setCampaignHandler(CampaignHandler campaignHandler) {
        this.campaignHandler = campaignHandler;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public CompanyTerm getCompanyTerm() {
        return companyTerm;
    }

    public void setCompanyTerm(CompanyTerm companyTerm) {
        this.companyTerm = companyTerm;
    }

    public List<CompanyInstruction> listCompanyInstructionByType(String choiceType) {
        return typeCompanyInstructionMap.get(choiceType);
    }

    public void putCompanyInstructionList(List<CompanyInstruction> companyInstructionList) {
        typeCompanyInstructionMap = new HashMap<>();
        if (companyInstructionList != null) {
            for (CompanyInstruction companyInstruction : companyInstructionList) {
                String choiceType = companyInstruction.getCompanyChoice().getType();
                if (typeCompanyInstructionMap.containsKey(choiceType)) {
                    typeCompanyInstructionMap.get(choiceType).add(companyInstruction);
                } else {
                    List typeCompanyInstructionList = new ArrayList<>();
                    typeCompanyInstructionList.add(companyInstruction);
                    typeCompanyInstructionMap.put(choiceType, typeCompanyInstructionList);
                }
            }
        }
    }

    public void putPropertyList(List<CompanyStatusProperty> companyStatusPropertyList) {
        if (companyStatusPropertyList != null) {
            for (CompanyStatusProperty preCompanyStatusProperty : companyStatusPropertyList) {
                propertyValueMap.put(preCompanyStatusProperty.getName(), preCompanyStatusProperty.getValue());
            }
        }

    }

    public CompanyTermHandler getPreCompanyTermHandler() {
        return preCompanyTermHandler;
    }

    public void setPreCompanyTermHandler(CompanyTermHandler preCompanyTermHandler) {
        this.preCompanyTermHandler = preCompanyTermHandler;
    }
}
