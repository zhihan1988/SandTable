package com.rathink.ie.foundation.service;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ix.ibase.component.CampContext;
import com.rathink.ix.ibase.property.model.CompanyTerm;
import com.rathink.ix.ibase.service.CompanyTermContext;

/**
 * Created by Hean on 2015/9/14.
 */
public interface CampaignCenterManager {
    void initCampaignCenter();

    void initCampaignHandler(Campaign campaign);

    CompanyTermContext initCompanyTermHandler(CompanyTerm companyTerm, CampContext campaignContext);
}
