package com.rathink.ie.ibase.service;

import com.rathink.ie.foundation.campaign.model.Campaign;

import java.util.Map;

/**
 * Created by Hean on 2015/9/10.
 */
public class CampaignHandler {
    private Campaign campaign;
    private Map<String, CompanyTermContext> companyTermHandlerMap;
    private Map<String, Integer> competitionMap;

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
}

