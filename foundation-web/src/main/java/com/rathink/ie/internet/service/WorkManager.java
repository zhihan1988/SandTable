package com.rathink.ie.internet.service;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyStatus;
import com.rathink.ie.ibase.property.model.CompanyStatusPropertyValue;

import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/9/4.
 */
public interface WorkManager {
    void initCampaign(Campaign campaign);

    CompanyStatus initCompanyStatus(Company company);

    List<CompanyStatusPropertyValue> prepareCompanyStatusProperty(CompanyStatus companyStatus);

    Map<String, List<CompanyStatusPropertyValue>> partCompanyStatusPropertyByDept(List<CompanyStatusPropertyValue> companyStatusPropertyValueList);

    void nextCampaign(Campaign campaign);

    void preCampaign(Campaign campaign);
}