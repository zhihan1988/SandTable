package com.rathink.iix.ibase.component;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.iix.manufacturing.message.IMessage;
import com.rathink.ix.ibase.work.model.CompanyPart;
import com.rathink.ix.ibase.work.model.IndustryResource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2016/2/2.
 */
public class MemoryCampaign {
    private Campaign campaign;
    private Map<String, MemoryCompany> memoryCompanyMap = new HashMap<>();
    private Map<String, CampaignParty> campaignPartyMap = new HashMap<>();
    private Map<String, IndustryResource> industryResourceMap = new HashMap<>();
    private List<Object> systemMessage = new ArrayList<>();

    public MemoryCampaign (Campaign campaign) {
        this.campaign = campaign;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public MemoryCompany getMemoryCompany(String companyId) {
        return memoryCompanyMap.get(companyId);
    }

    public void putMemoryCompany(String companyId, MemoryCompany memoryCompany) {
        memoryCompanyMap.put(companyId, memoryCompany);
    }

    public Map<String, MemoryCompany> getMemoryCompanyMap() {
        return memoryCompanyMap;
    }

    public void broadcast(IMessage message) {
        for (MemoryCompany memoryCompany : memoryCompanyMap.values()) {
            memoryCompany.offerMessage(message);
        }
    }

    public void listenMesage() {

    }

    public IndustryResource getIndustryResource(String type) {
        return industryResourceMap.get(type);
    }

    public void putIndustryResource(String type, IndustryResource industryResource) {
        industryResourceMap.put(type, industryResource);
    }

    public Map<String, CampaignParty> getCampaignPartyMap() {
        return campaignPartyMap;
    }

    public void nextTerm() {
        Integer currentCampaignDate = campaign.getCurrentCampaignDate();
        currentCampaignDate++;
        campaign.setCurrentCampaignDate(currentCampaignDate);

        for (MemoryCompany memoryCompany : getMemoryCompanyMap().values()) {
            Company company = memoryCompany.getCompany();
            company.setCurrentCampaignDate(currentCampaignDate);
        }

        campaignPartyMap.clear();
        industryResourceMap.clear();
    }
}
