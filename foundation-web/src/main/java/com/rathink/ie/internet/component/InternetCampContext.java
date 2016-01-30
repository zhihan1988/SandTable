package com.rathink.ie.internet.component;

import com.rathink.ie.base.component.CyclePublisher;
import com.rathink.ie.base.component.DevoteCycle;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.component.BaseCampaignContext;
import com.rathink.ie.ibase.service.CompanyTermContext;
import com.rathink.ie.ibase.work.model.CompanyPart;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryResource;
import com.rathink.ie.internet.service.impl.InternetCompanyTermContext;

import java.util.*;

/**
 * Created by Hean on 2015/9/10.
 */
public class InternetCampContext extends BaseCampaignContext<InternetCompanyTermContext> {
    private Campaign campaign;
    private Set<CompanyTermInstruction> currentCompanyTermInstructionSet = new HashSet<>();

    private Map<String, List<CompanyTermInstruction>> currentChoiceInstructionMap = new HashMap<>();
    //    private Map<String, List<CompanyTermInstruction>> currentResourceInstructionMap = new HashMap<>();
    private Map<String, String> expressionMap = new HashMap<>();
    private Map<String, List<CompanyPart>> companyPartMap = new HashMap<>();
    private CyclePublisher cyclePublisher;
    private DevoteCycle devoteCycle;

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

    public DevoteCycle getDevoteCycle() {
        return devoteCycle;
    }

    public void setDevoteCycle(DevoteCycle devoteCycle) {
        this.devoteCycle = devoteCycle;
    }


}

