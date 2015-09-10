package com.rathink.ie.internet.service;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyStatusProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/9/4.
 */
public interface WorkManager {
    void initCampaign(Campaign campaign);

    CompanyTerm initCompanyStatus(Company company);

    List<CompanyStatusProperty> prepareCompanyStatusProperty(CompanyTerm companyTerm);

    Map<String, List<CompanyStatusProperty>> partCompanyStatusPropertyByDept(List<CompanyStatusProperty> companyStatusPropertyList);

    void nextCampaign(Campaign campaign);

    void preCampaign(Campaign campaign);
}
