package com.rathink.ie.ibase.service;

import com.rathink.ie.ibase.account.model.Account;
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
    protected CompanyTerm preCompanyTerm;
    protected Map<String,List<CompanyInstruction>> preTypeCompanyInstructionMap = new HashMap<>();
    protected Map<String, String> prePropertyValueMap = new HashMap<>();
    protected CompanyTerm companyTerm;
    protected Map<String, String> propertyValueMap = new HashMap<>();
    protected List<Account> accountList = new ArrayList<>();

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

    public String getPrePropertyValue(String key) {
        return prePropertyValueMap.get(key);
    }


    public List<CompanyInstruction> listPreCompanyInstructionByType(String choiceType) {
        return preTypeCompanyInstructionMap.get(choiceType);
    }

    public CampaignHandler getCampaignHandler() {
        return campaignHandler;
    }

    public void setCampaignHandler(CampaignHandler campaignHandler) {
        this.campaignHandler = campaignHandler;
    }

    public CompanyTerm getPreCompanyTerm() {
        return preCompanyTerm;
    }

    public void setPreCompanyTerm(CompanyTerm preCompanyTerm) {
        this.preCompanyTerm = preCompanyTerm;
    }

    public Map<String, String> getPrePropertyValueMap() {
        return prePropertyValueMap;
    }

    public void setPrePropertyValueMap(Map<String, String> prePropertyValueMap) {
        this.prePropertyValueMap = prePropertyValueMap;
    }

    public void setPreTypeCompanyInstructionMap(Map<String, List<CompanyInstruction>> preTypeCompanyInstructionMap) {
        this.preTypeCompanyInstructionMap = preTypeCompanyInstructionMap;
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

}
