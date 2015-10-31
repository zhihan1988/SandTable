package com.rathink.ie.ibase.service;

import com.rathink.ie.base.controller.CyclePublisher;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.work.model.CompanyPart;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryResource;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Hean on 2015/9/10.
 */
public class CampaignContext {
    private Campaign campaign;
    private Set<CompanyTermInstruction> currentCompanyTermInstructionSet = new HashSet<>();
    private Map<String, CompanyTermContext> companyTermContextMap;
    private Map<String, IndustryResource> currentTypeIndustryResourceMap = new HashMap<>();//key:资源类型  value:资源
    private Map<String, List<CompanyTermInstruction>> currentChoiceInstructionMap = new HashMap<>();
    //    private Map<String, List<CompanyTermInstruction>> currentResourceInstructionMap = new HashMap<>();
    private Map<String, String> expressionMap = new HashMap<>();
    private Map<String, List<CompanyPart>> companyPartMap = new HashMap<>();
    private CyclePublisher cyclePublisher;

    public void next() {
        currentCompanyTermInstructionSet.clear();
        currentChoiceInstructionMap.clear();
//        currentResourceInstructionMap.clear();
        currentTypeIndustryResourceMap.clear();
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Map<String, CompanyTermContext> getCompanyTermContextMap() {
        return companyTermContextMap;
    }

    public void setCompanyTermContextMap(Map<String, CompanyTermContext> companyTermContextMap) {
        this.companyTermContextMap = companyTermContextMap;
    }

    /**
     * 当前比赛进度的全部决策
     *
     * @param companyTermInstructionList
     */
    public void addAllCurrentInstruction(List<CompanyTermInstruction> companyTermInstructionList) {
        currentCompanyTermInstructionSet.addAll(companyTermInstructionList);

        for (CompanyTermInstruction companyTermInstruction : companyTermInstructionList) {
            if (companyTermInstruction.getIndustryResourceChoice() != null) {
                String choiceId = companyTermInstruction.getIndustryResourceChoice().getId();
                if (currentChoiceInstructionMap.containsKey(choiceId)) {
                    currentChoiceInstructionMap.get(choiceId).add(companyTermInstruction);
                } else {
                    List<CompanyTermInstruction> ciList = new ArrayList<>();
                    ciList.add(companyTermInstruction);
                    currentChoiceInstructionMap.put(choiceId, ciList);
                }
            }
        }
    }

    public Map<String, IndustryResource> getCurrentTypeIndustryResourceMap() {
        return currentTypeIndustryResourceMap;
    }

    public void setCurrentTypeIndustryResourceMap(Map<String, IndustryResource> currentTypeIndustryResourceMap) {
        this.currentTypeIndustryResourceMap = currentTypeIndustryResourceMap;
    }

    public Set<CompanyTermInstruction> getCurrentCompanyTermInstructionSet() {
        return currentCompanyTermInstructionSet;
    }

    public Map<String, List<CompanyTermInstruction>> getCurrentChoiceInstructionMap() {
        return currentChoiceInstructionMap;
    }

    public Map<String, String> getExpressionMap() {
        return expressionMap;
    }

    public void setExpressionMap(Map<String, String> expressionMap) {
        this.expressionMap = expressionMap;
    }

    public Map<String, List<CompanyPart>> getCompanyPartMap() {
        return companyPartMap;
    }

    public void setCompanyPartMap(Map<String, List<CompanyPart>> companyPartMap) {
        this.companyPartMap = companyPartMap;
    }

    public CyclePublisher getCyclePublisher() {
        return cyclePublisher;
    }

    public void setCyclePublisher(CyclePublisher cyclePublisher) {
        this.cyclePublisher = cyclePublisher;
    }
}

