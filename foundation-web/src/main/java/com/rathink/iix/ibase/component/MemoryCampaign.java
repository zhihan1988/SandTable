package com.rathink.iix.ibase.component;

import com.rathink.ie.foundation.campaign.model.Campaign;
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
    private List<CampaignParty> campaignPartyList = new ArrayList<>();
    private List<Object> systemMessage = new ArrayList<>();
    private Map<String, MemoryCompany> memoryCompanyMap = new HashMap<>();
    private Map<String, IndustryResource> industryResourceMap = new HashMap<>();

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

    public void broadcast() {

    }

    public void listenMesage() {

    }

    public IndustryResource getIndustryResource(String type) {
        return industryResourceMap.get(type);
    }

    public void putIndustryResource(String type, IndustryResource industryResource) {
        industryResourceMap.put(type, industryResource);
    }
}
