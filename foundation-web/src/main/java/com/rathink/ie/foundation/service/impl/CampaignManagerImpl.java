package com.rathink.ie.foundation.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.ie.foundation.team.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Hean on 15/8/14.
 */
@Service
public class CampaignManagerImpl implements CampaignManager {
    @Autowired
    private BaseManager baseManager;

    @Override
    public List<Company> listCompany(Campaign campaign) {
        XQuery companyListQuery = new XQuery();
        companyListQuery.setHql("from Company where campaign.id = :campaignId");
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<String, Object>();
        queryParamMap.put("campaignId", campaign.getId());
        companyListQuery.setQueryParamMap(queryParamMap);
        List companyList = baseManager.listObject(companyListQuery);
        return companyList;
    }
}
