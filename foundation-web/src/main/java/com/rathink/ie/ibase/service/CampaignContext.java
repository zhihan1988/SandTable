package com.rathink.ie.ibase.service;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.util.RandomUtil;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.ibase.work.model.Resource;

import java.util.*;

/**
 * Created by Hean on 2015/9/10.
 */
public class CampaignContext {
    private Campaign campaign;
    private Set<CompanyInstruction> currentCompanyInstructionSet = new HashSet<>();
    private Map<String, CompanyTermContext> companyTermContextMap;
    private Map<String, Integer> competitionMap;
    private Map<String, List<CompanyChoice>> typeCompanyChoiceMap = new HashMap<>();
    private Map<String, List<CompanyInstruction>> choiceInstructionMap = new HashMap<>();
    private List<CompanyChoice> currentCompanyChoiceList = new ArrayList<>();
    private Set<Resource> humanRepository = new HashSet<>();

    public void next() {
        currentCompanyInstructionSet.clear();
        competitionMap.clear();
        typeCompanyChoiceMap.clear();
        choiceInstructionMap.clear();
        currentCompanyChoiceList.clear();
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
     * @param currentCompanyChoiceList
     */
    public void setCurrentCompanyChoiceList(List<CompanyChoice> currentCompanyChoiceList) {
        this.currentCompanyChoiceList = currentCompanyChoiceList;
        for (CompanyChoice companyChoice : currentCompanyChoiceList) {
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

    public List<CompanyChoice> getCurrentCompanyChoiceList() {
        return currentCompanyChoiceList;
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

    public void addHumanResource(Set<Resource> humanSet) {
        humanRepository.addAll(humanSet);
    }

    /**
     * 产生这一轮的随机数据
     */
    public List<Resource> randomHumans() {
        List<Resource> randomHumanList = new ArrayList<>();
        Integer needNum = getCompanyTermContextMap().size() * 3;
        for (int i = 0; i < needNum; i++) {
             Iterator<Resource> humanIterator = humanRepository.iterator();
            Integer choiceSize = humanRepository.size();
            if(choiceSize==0) break;
            int index = RandomUtil.random(0, choiceSize);
            for (int m = 0; humanIterator.hasNext(); m++) {
                Resource human = humanIterator.next();
                if (m == index) {
                    randomHumanList.add(human);
                    humanIterator.remove();
                    break;
                }
            }
        }
        return randomHumanList;
    }
}

