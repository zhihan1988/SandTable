package com.rathink.ie.foundation.service;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;

import java.util.List;

/**
 * Created by Hean on 2015/9/4.
 */
public interface CampaignManager {
    List<Company> listCompany(Campaign campaign);
}
