package com.rathink.ie.ibase.service;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.util.CampaignUtil;

import java.util.Map;

/**
 * Created by Hean on 2015/9/10.
 */
public class CampaignHandler {
    private Campaign campaign;
    private Map<String, CompanyTermHandler> companyTermHandlerMap;

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Map<String, CompanyTermHandler> getCompanyTermHandlerMap() {
        return companyTermHandlerMap;
    }

    public void setCompanyTermHandlerMap(Map<String, CompanyTermHandler> companyTermHandlerMap) {
        this.companyTermHandlerMap = companyTermHandlerMap;
    }
}

