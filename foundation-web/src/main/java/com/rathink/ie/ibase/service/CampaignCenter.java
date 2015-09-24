package com.rathink.ie.ibase.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hean on 2015/9/4.
 */
public class CampaignCenter {
    private static Map<String, CampaignContext> campaignContextMap = new HashMap<>();
    private CampaignCenter (){}
    public static CampaignContext getCampaignHandler(String campaignId) {
        return campaignContextMap.get(campaignId);
    }

    public static void putCampaignTermHandler(String campaignId, CampaignContext campaignContext) {
        campaignContextMap.put(campaignId, campaignContext);
    }
}
