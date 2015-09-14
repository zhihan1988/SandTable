package com.rathink.ie.foundation.service;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.CampaignHandler;
import com.rathink.ie.ibase.service.CompanyTermHandler;

/**
 * Created by Hean on 2015/9/14.
 */
public interface CampaignCenterManager {
    void initCampaignCenter();

    void initCampaignHandler(Campaign campaign);

    CompanyTermHandler initCompanyTermHandler(CompanyTerm companyTerm, CampaignHandler campaignHandler);
}
