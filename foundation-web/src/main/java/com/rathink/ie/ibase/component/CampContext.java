package com.rathink.ie.ibase.component;

import com.rathink.ie.base.component.CyclePublisher;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.service.CompanyTermContext;

import java.util.Map;

/**
 * Created by Hean on 2016/1/30.
 */
public interface CampContext<T extends CompanyTermContext> {

    public String getCampaignId();

    public Campaign getCampaign();

    public Map<String, T> getCompanyTermContextMap();

    public CyclePublisher getCyclePublisher();
}
