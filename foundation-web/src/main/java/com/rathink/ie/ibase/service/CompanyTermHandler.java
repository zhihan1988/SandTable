package com.rathink.ie.ibase.service;

import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.ie.ibase.property.model.CompanyStatusProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import org.aspectj.apache.bcel.generic.Instruction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/9/4.
 */
public abstract class CompanyTermHandler {
    protected CompanyTerm companyTerm;
    protected Map<String, String> propertyValueMap = new HashMap<>();
    protected Map<String, String> prePropertyValueMap = new HashMap<>();
    protected List<CompanyStatusProperty> companyStatusPropertyList = new ArrayList<>();
    protected List<CompanyInstruction> companyInstructionList = new ArrayList<>();

    @Autowired
    protected CampaignManager campaignManager;

    public String get(String key) {
        String value = null;
        if (propertyValueMap.containsKey(key)) {
            return propertyValueMap.get(key);
        } else {
            CompanyStatusProperty companyStatusProperty = produceProperty(key);
            put(key, companyStatusProperty.getValue());
            companyStatusPropertyList.add(companyStatusProperty);
            return value;
        }
    }

    abstract protected CompanyStatusProperty produceProperty(String key);

    public void put(String key, String value) {
        propertyValueMap.put(key, value);
    }

    public String getPrePropertyValue(String key) {
        return prePropertyValueMap.get(key);
    }

    public void putInstruction(CompanyInstruction companyInstruction) {
        companyInstructionList.add(companyInstruction);
    }

    public List<CompanyInstruction> listInstruction(String type) {
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
    }

    public CompanyTermHandler() {
        this.competitiveBidding();
        this.calculateAll();
    }

    public abstract void competitiveBidding();

    public abstract void calculateAll();

}
