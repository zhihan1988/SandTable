package com.rathink.ie.internet.service;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/9/4.
 */
public interface WorkManager {
    void begin(Campaign campaign);

    Map<String, List<CompanyTermProperty>> partCompanyStatusPropertyByDept(List<CompanyTermProperty> companyTermPropertyList);

    void next(String campaignId);

    void pre(String campaignId);
}
