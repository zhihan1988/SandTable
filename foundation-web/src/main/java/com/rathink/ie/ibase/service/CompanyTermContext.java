package com.rathink.ie.ibase.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.component.CampContext;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;

import java.util.*;

/**
 * Created by Hean on 2015/9/4.
 */
public abstract class CompanyTermContext<T extends CampContext,S extends CompanyTermContext> {
    protected T campaignContext;
    protected Company company;
    protected CompanyTerm companyTerm;
    protected Map<String, Integer> propertyValueMap = new LinkedHashMap<>();
    protected Map<String,List<CompanyTermInstruction>> typeCompanyInstructionMap = new LinkedHashMap<>();
    protected List<CompanyTermInstruction> companyTermInstructionList = new ArrayList<>();
    protected List<Account> accountList = new ArrayList<>();
    protected List<CompanyTermProperty> companyTermPropertyList = new ArrayList<>();
    protected S preCompanyTermContext;

    public Boolean contains(String key) {
        return propertyValueMap.containsKey(key);
    }




    public void put(String key, Integer value) {
        propertyValueMap.put(key, value);
    }

    public T getCampaignContext() {
        return campaignContext;
    }

    public void setCampaignContext(T campaignContext) {
        this.campaignContext = campaignContext;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public CompanyTerm getCompanyTerm() {
        return companyTerm;
    }

    public void setCompanyTerm(CompanyTerm companyTerm) {
        this.companyTerm = companyTerm;
    }

    public List<CompanyTermInstruction> listCompanyInstructionByType(String choiceType) {
        return typeCompanyInstructionMap.get(choiceType);
    }

    public void putCompanyInstructionList(List<CompanyTermInstruction> companyTermInstructionList) {
        typeCompanyInstructionMap = new HashMap<>();
        if (companyTermInstructionList != null) {
            for (CompanyTermInstruction companyTermInstruction : companyTermInstructionList) {
                String baseType = companyTermInstruction.getBaseType();
                if (typeCompanyInstructionMap.containsKey(baseType)) {
                    typeCompanyInstructionMap.get(baseType).add(companyTermInstruction);
                } else {
                    List typeCompanyInstructionList = new ArrayList<>();
                    typeCompanyInstructionList.add(companyTermInstruction);
                    typeCompanyInstructionMap.put(baseType, typeCompanyInstructionList);
                }
            }
        }
    }

    public List<CompanyTermInstruction> getCompanyTermInstructionList() {
        return companyTermInstructionList;
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

    public S getPreCompanyTermContext() {
        return preCompanyTermContext;
    }

    public void setPreCompanyTermContext(S preCompanyTermContext) {
        this.preCompanyTermContext = preCompanyTermContext;
    }
}
