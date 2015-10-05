package com.rathink.ie.ibase.service;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.util.RandomUtil;
import com.rathink.ie.ibase.work.model.CampaignTermChoice;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryChoice;

import java.util.*;

/**
 * Created by Hean on 2015/9/10.
 */
public class CampaignContext {
    private Campaign campaign;
    private Set<CompanyTermInstruction> currentCompanyTermInstructionSet = new HashSet<>();
    private Map<String, CompanyTermContext> companyTermContextMap;
    private Map<String, Integer> competitionMap = new HashMap<>();
    private Map<String, List<CampaignTermChoice>> typeCompanyChoiceMap = new HashMap<>();
    private Map<String, List<CompanyTermInstruction>> choiceInstructionMap = new HashMap<>();
    private List<CampaignTermChoice> currentCampaignTermChoiceList = new ArrayList<>();
    private Set<String> industryChoiceIdSet = new HashSet<>();

    public void next() {
        currentCompanyTermInstructionSet.clear();
        competitionMap.clear();
        typeCompanyChoiceMap.clear();
        choiceInstructionMap.clear();
        currentCampaignTermChoiceList.clear();
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

    public Map<String, Integer> getCompetitionMap() {
        return competitionMap;
    }

    public void setCompetitionMap(Map<String, Integer> competitionMap) {
        this.competitionMap = competitionMap;
    }

    /**
     * 当前比赛进度的全部可供选择的决策项
     * @param currentCampaignTermChoiceList
     */
    public void setCurrentCampaignTermChoiceList(List<CampaignTermChoice> currentCampaignTermChoiceList) {
        this.currentCampaignTermChoiceList = currentCampaignTermChoiceList;
        for (CampaignTermChoice campaignTermChoice : currentCampaignTermChoiceList) {
            String baseType = campaignTermChoice.getBaseType();
            if (typeCompanyChoiceMap.containsKey(baseType)) {
                typeCompanyChoiceMap.get(baseType).add(campaignTermChoice);
            } else {
                List<CampaignTermChoice> ccList = new ArrayList<>();
                ccList.add(campaignTermChoice);
                typeCompanyChoiceMap.put(baseType, ccList);
            }
        }
    }

    public List<CampaignTermChoice> getCurrentCampaignTermChoiceList() {
        return currentCampaignTermChoiceList;
    }

    public List<CampaignTermChoice> listCurrentCompanyChoiceByType(String baseType) {
        return typeCompanyChoiceMap.get(baseType);
    }

    /**
     * 当前比赛进度的全部决策
     * @param companyTermInstructionList
     */
    public void addAllCurrentInstruction(List<CompanyTermInstruction> companyTermInstructionList) {
        currentCompanyTermInstructionSet.addAll(companyTermInstructionList);

        for (CompanyTermInstruction companyTermInstruction : companyTermInstructionList) {
            String companyChoiceId = companyTermInstruction.getCampaignTermChoice().getId();
            if (choiceInstructionMap.containsKey(companyChoiceId)) {
                choiceInstructionMap.get(companyChoiceId).add(companyTermInstruction);
            } else {
                List<CompanyTermInstruction> ciList = new ArrayList<>();
                ciList.add(companyTermInstruction);
                choiceInstructionMap.put(companyChoiceId, ciList);
            }
        }
    }

    public List<CompanyTermInstruction> listCurrentCompanyInstructionByChoice(String choiceId) {
        return choiceInstructionMap.get(choiceId);
    }

    public Set<CompanyTermInstruction> getCurrentCompanyTermInstructionSet() {
        return currentCompanyTermInstructionSet;
    }

    public void addHumanResource(Set<String> humanSet) {
        industryChoiceIdSet.addAll(humanSet);
    }

    /**
     * 产生这一轮的随机数据
     */
    public List<String> randomHumans() {
        List<String> randomHumanIdList = new ArrayList<>();
        Integer needNum = getCompanyTermContextMap().size() * 3;
        for (int i = 0; i < needNum; i++) {
            Iterator<String> humanIterator = industryChoiceIdSet.iterator();
            Integer choiceSize = industryChoiceIdSet.size();
            if(choiceSize==0) break;
            int index = RandomUtil.random(0, choiceSize);
            for (int m = 0; humanIterator.hasNext(); m++) {
                String humanId = humanIterator.next();
                if (m == index) {
                    randomHumanIdList.add(humanId);
                    humanIterator.remove();
                    break;
                }
            }
        }
        return randomHumanIdList;
    }
}

