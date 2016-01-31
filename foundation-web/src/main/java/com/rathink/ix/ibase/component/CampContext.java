package com.rathink.ix.ibase.component;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ix.ibase.service.CompanyTermContext;

import java.util.Map;

/**
 * Created by Hean on 2016/1/30.
 */
public interface CampContext<T extends CompanyTermContext> {

    public Campaign getCampaign();

    public Map<String, T> getCompanyTermContextMap();

    public CyclePublisher getCyclePublisher();
}
