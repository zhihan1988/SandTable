package com.rathink.ie.ibase.service;

import java.util.HashMap;
import java.util.Map;
import com.rathink.ie.ibase.component.CampContext;

/**
 * Created by Hean on 2015/9/4.
 */
public class CampaignCenter {
    private static Map<String, CampContext> campaignContextMap = new HashMap<>();
    private CampaignCenter (){}
    public static CampContext getCampaignHandler(String campaignId) {
        return campaignContextMap.get(campaignId);
    }

    public static void putCampaignTermHandler(String campaignId, CampContext campaignContext) {
        campaignContextMap.put(campaignId, campaignContext);
    }
}
