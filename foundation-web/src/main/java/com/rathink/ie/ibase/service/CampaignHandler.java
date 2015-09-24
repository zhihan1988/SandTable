package com.rathink.ie.ibase.service;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.CompanyInstruction;

import java.util.*;

/**
 * Created by Hean on 2015/9/10.
 */
public class CampaignHandler {
    private Campaign campaign;
    private Set<CompanyInstruction> currentCompanyInstructionSet = new HashSet<>();
    private Map<String, CompanyTermContext> companyTermHandlerMap;
    private Map<String, Integer> competitionMap;
    private Map<String, List<CompanyChoice>> typeCompanyChoiceMap = new HashMap<>();
    private Map<String, List<CompanyInstruction>> choiceInstructionMap = new HashMap<>();
    private List<CompanyChoice> companyChoiceList = new ArrayList<>();
    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Map<String, CompanyTermContext> getCompanyTermHandlerMap() {
        return companyTermHandlerMap;
    }

    public void setCompanyTermHandlerMap(Map<String, CompanyTermContext> companyTermHandlerMap) {
        this.companyTermHandlerMap = companyTermHandlerMap;
    }

    public Map<String, Integer> getCompetitionMap() {
        return competitionMap;
    }

    public void setCompetitionMap(Map<String, Integer> competitionMap) {
        this.competitionMap = competitionMap;
    }

    /**
     * 当前比赛进度的全部可供选择的决策项
     * @param companyChoiceList
     */
    public void setCompanyChoiceList(List<CompanyChoice> companyChoiceList) {
        this.companyChoiceList = companyChoiceList;
        for (CompanyChoice companyChoice : companyChoiceList) {
            String baseType = companyChoice.getBaseType();
            if (typeCompanyChoiceMap.containsKey(baseType)) {
                typeCompanyChoiceMap.get(baseType).add(companyChoice);
            } else {
                List<CompanyChoice> ccList = new ArrayList<>();
                ccList.add(companyChoice);
                typeCompanyChoiceMap.put(baseType, ccList);
            }
        }
    }

    public List<CompanyChoice> getCompanyChoiceList() {
        return companyChoiceList;
    }

    public List<CompanyChoice> listCurrentCompanyChoiceByType(String baseType) {
        return typeCompanyChoiceMap.get(baseType);
    }

    /**
     * 当前比赛进度的全部决策
     * @param companyInstructionList
     */
    public void addAllCurrentInstruction(List<CompanyInstruction> companyInstructionList) {
        currentCompanyInstructionSet.addAll(companyInstructionList);

        for (CompanyInstruction companyInstruction : companyInstructionList) {
            String companyChoiceId = companyInstruction.getCompanyChoice().getId();
            if (choiceInstructionMap.containsKey(companyChoiceId)) {
                choiceInstructionMap.get(companyChoiceId).add(companyInstruction);
            } else {
                List<CompanyInstruction> ciList = new ArrayList<>();
                ciList.add(companyInstruction);
                choiceInstructionMap.put(companyChoiceId, ciList);
            }
        }
    }

    public List<CompanyInstruction> listCurrentCompanyInstructionByChoice(String choiceId) {
        return choiceInstructionMap.get(choiceId);
    }

    public Set<CompanyInstruction> getCurrentCompanyInstructionSet() {
        return currentCompanyInstructionSet;
    }
}

