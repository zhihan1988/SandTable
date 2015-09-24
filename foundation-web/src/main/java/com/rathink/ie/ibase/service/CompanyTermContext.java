package com.rathink.ie.ibase.service;

import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.work.model.CompanyInstruction;

import java.util.*;

/**
 * Created by Hean on 2015/9/4.
 */
public abstract class CompanyTermContext {
    protected CampaignHandler campaignHandler;
    protected CompanyTerm companyTerm;
    protected Map<String, Integer> propertyValueMap = new LinkedHashMap<>();
    protected Map<String,List<CompanyInstruction>> typeCompanyInstructionMap = new LinkedHashMap<>();
    protected List<CompanyInstruction> companyInstructionList = new ArrayList<>();
    protected List<Account> accountList = new ArrayList<>();
    protected List<CompanyTermProperty> companyTermPropertyList = new ArrayList<>();
    protected CompanyTermContext preCompanyTermContext;

    public Integer get(String key) {
        if (!propertyValueMap.containsKey(key)) {
            put(key, calculate(key));
        }
        return propertyValueMap.get(key);
    }

    abstract public Integer calculate(String key);

    public void put(String key, Integer value) {
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
                String choiceType = companyInstruction.getCompanyChoice().getBaseType();
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

    public List<CompanyInstruction> getCompanyInstructionList() {
        return companyInstructionList;
    }

    public List<CompanyTermProperty> getCompanyTermPropertyList() {
        return companyTermPropertyList;
    }

    public void setCompanyTermPropertyList(List<CompanyTermProperty> companyTermPropertyList) {
        this.companyTermPropertyList = companyTermPropertyList;
        if (companyTermPropertyList != null) {
            for (CompanyTermProperty preCompanyTermProperty : companyTermPropertyList) {
                propertyValueMap.put(preCompanyTermProperty.getName(), preCompanyTermProperty.getValue());
            }
        }
    }

    public CompanyTermContext getPreCompanyTermContext() {
        return preCompanyTermContext;
    }

    public void setPreCompanyTermContext(CompanyTermContext preCompanyTermContext) {
        this.preCompanyTermContext = preCompanyTermContext;
    }
}
