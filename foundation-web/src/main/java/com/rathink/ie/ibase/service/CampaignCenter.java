package com.rathink.ie.ibase.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hean on 2015/9/4.
 */
public class CampaignCenter {
    private static Map<String, CampaignHandler> campaignHandlerMap = new HashMap<>();
    private CampaignCenter (){}
    public static CampaignHandler getCampaignHandler(String campaignId) {
        return campaignHandlerMap.get(campaignId);
    }

    public static void putCompanyTermHandler(String campaignId, CampaignHandler campaignHandler) {
        campaignHandlerMap.put(campaignId, campaignHandler);
    }
}
