package com.rathink.ie.ibase.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hean on 2015/9/4.
 */
public class CampaignCenter {
    private static Map<String, CompanyTermHandler> companyTermHandlerMap = new HashMap<>();

    public static CompanyTermHandler getMyCompanyTermHandler(String companyId) {
        return companyTermHandlerMap.get(companyId);
    }

    public static void putCompanyTermHandler(String companyId, CompanyTermHandler companyTermHandler) {
        companyTermHandlerMap.put(companyId, companyTermHandler);
    }
}
