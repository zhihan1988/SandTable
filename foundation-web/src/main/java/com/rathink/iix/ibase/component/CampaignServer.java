package com.rathink.iix.ibase.component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hean on 2016/2/2.
 */
public class CampaignServer {

    private static Map<String ,MemoryCampaign> memoryCampaignMap = new HashMap<>();

    public static MemoryCampaign getMemoryCampaign(String campaignId) {
        return memoryCampaignMap.get(campaignId);
    }

    public static void putMemoryCampaign(String campaignId, MemoryCampaign memoryCampaign) {
        memoryCampaignMap.put(campaignId, memoryCampaign);
    }

    public static void remove(String campaignId){
        memoryCampaignMap.remove(campaignId);
    }
}
