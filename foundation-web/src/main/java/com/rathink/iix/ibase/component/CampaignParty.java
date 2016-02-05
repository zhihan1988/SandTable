package com.rathink.iix.ibase.component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hean on 2016/2/2.
 */
public class CampaignParty<T> {
    protected MemoryCampaign memoryCampaign;
    protected Set<T> joinedSet = new HashSet<>();
    private String type;
    private String status;

    public CampaignParty(MemoryCampaign memoryCampaign, String type) {
        this.memoryCampaign = memoryCampaign;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void join(T o) {
        joinedSet.add(o);
        if (isAll()) {
            iNotify();
        }
    }

    public boolean isAll() {
        return memoryCampaign.getMemoryCompanyMap().size() == joinedSet.size();
    }

    public void iNotify() {

    }
}
