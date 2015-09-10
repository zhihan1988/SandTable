package com.rathink.ie.ibase.service;

import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyStatusProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.work.model.CompanyChoice;
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
    protected List<CompanyStatusProperty> companyStatusPropertyList = new ArrayList<>();
    protected Map<String,List<CompanyInstruction>> typeCompanyInstructionMap = new HashMap<>();
    protected List<Account> accountList = new ArrayList<>();
    protected Map<String, String> prePropertyValueMap = new HashMap<>();

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


    public List<CompanyInstruction> getCompanyInstructionListByType(String choiceType) {
        return typeCompanyInstructionMap.get(choiceType);
    }
  /*  public List<CompanyInstruction> listInstruction(String type) {
        List<CompanyInstruction> typeCompanyInstructionList = new ArrayList<>();
        if (companyInstructionList != null) {
            for (CompanyInstruction companyInstruction : companyInstructionList) {
                CompanyChoice companyChoice = companyInstruction.getCompanyChoice();
                if (type.equals(companyChoice.getType())) {
                    typeCompanyInstructionList.add(companyInstruction);
                }
            }
        }
        return typeCompanyInstructionList;
    }*/

   /* public CompanyTermHandler() {
        this.competitiveBidding();
        this.calculateAll();
    }

    public abstract void competitiveBidding();*/

/*
    public abstract void calculateAll();
*/

    public CampaignHandler getCampaignHandler() {
        return campaignHandler;
    }

    public void setCampaignHandler(CampaignHandler campaignHandler) {
        this.campaignHandler = campaignHandler;
    }

    public CompanyTerm getCompanyTerm() {
        return companyTerm;
    }

    public void setCompanyTerm(CompanyTerm companyTerm) {
        this.companyTerm = companyTerm;
    }

    public Map<String, String> getPrePropertyValueMap() {
        return prePropertyValueMap;
    }

    public void setPrePropertyValueMap(Map<String, String> prePropertyValueMap) {
        this.prePropertyValueMap = prePropertyValueMap;
    }

    public List<CompanyStatusProperty> getCompanyStatusPropertyList() {
        return companyStatusPropertyList;
    }

    public void setCompanyStatusPropertyList(List<CompanyStatusProperty> companyStatusPropertyList) {
        this.companyStatusPropertyList = companyStatusPropertyList;
    }

    public void setTypeCompanyInstructionMap(Map<String, List<CompanyInstruction>> typeCompanyInstructionMap) {
        this.typeCompanyInstructionMap = typeCompanyInstructionMap;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }
}
