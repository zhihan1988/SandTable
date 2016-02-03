package com.rathink.iix.manufacturing.component;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.iix.ibase.component.MemoryCampaign;
import com.rathink.ix.manufacturing.model.ProduceLine;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Hean on 2016/2/2.
 */
public class ManufacturingMemoryCampaign extends MemoryCampaign {

    public ManufacturingMemoryCampaign(Campaign campaign) {
        super(campaign);
    }
}
