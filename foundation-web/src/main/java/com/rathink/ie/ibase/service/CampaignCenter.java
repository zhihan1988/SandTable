package com.rathink.ie.ibase.service;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.internet.service.impl.InternetCompanyTermHandler;
import com.rathink.ie.user.util.AuthorizationUtil;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/9/4.
 */
public class CampaignCenter {
    private static Map<String, CampaignHandler> campaignHandlerMap = new HashMap<>();
    private CampaignCenter (){}
    public static CampaignHandler getCampaignHandler(String campaignId) {
        if (campaignHandlerMap.get(campaignId) == null) {
            init(campaignId);
        }
        return campaignHandlerMap.get(campaignId);
    }

    public static void putCampaignTermHandler(String campaignId, CampaignHandler campaignHandler) {
        campaignHandlerMap.put(campaignId, campaignHandler);
    }

    public static void init(String campaignId) {
        ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
        BaseManager baseManager = (BaseManager) applicationContext.getBean("baseManagerImpl");
        CompanyTermManager companyTermManager = (CompanyTermManager) applicationContext.getBean("companyTermManagerImpl");
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), campaignId);
        List<CompanyTerm> companyTermList = companyTermManager.listCompanyTerm(campaignId);

        CampaignHandler campaignHandler = new CampaignHandler();
        campaignHandler.setCampaign(campaign);

        Map<String, CompanyTermHandler> companyTermHandlerMap = new HashMap<>();
        for (CompanyTerm companyTerm : companyTermList) {
            CompanyTermHandler companyTermHandler = new InternetCompanyTermHandler();
            companyTermHandler.setCompanyTerm(companyTerm);
            companyTermHandler.setCampaignHandler(campaignHandler);
            companyTermHandlerMap.put(companyTerm.getCompany().getId(), companyTermHandler);
        }
        campaignHandler.setCompanyTermHandlerMap(companyTermHandlerMap);

        CampaignCenter.putCampaignTermHandler(campaign.getId(), campaignHandler);
    }
}
