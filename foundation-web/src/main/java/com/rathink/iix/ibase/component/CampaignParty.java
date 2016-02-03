package com.rathink.iix.ibase.component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hean on 2016/2/2.
 */
public class CampaignParty {
    private MemoryCampaign memoryCampaign;
    private String type;
    private String status;
    private List joinedCompanyList = new ArrayList<>();

    public CampaignParty(MemoryCampaign memoryCampaign) {
        this.memoryCampaign = memoryCampaign;
    }

    public void join(Object o) {
        joinedCompanyList.add(o);
    }

    public boolean isAll() {
        return memoryCampaign.getMemoryCompanyMap().size() == joinedCompanyList.size();
    }

    public void iNotify() {

    }
}
