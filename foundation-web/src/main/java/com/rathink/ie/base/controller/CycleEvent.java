package com.rathink.ie.base.controller;

import com.rathink.ie.ibase.service.CampaignContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

/**
 * Created by Hean on 2015/10/31.
 */
public class CycleEvent extends ApplicationContextEvent {
    private CampaignContext campaignContext;
    public CycleEvent(ApplicationContext source, CampaignContext campaignContext) {
        super(source);
        this.campaignContext = campaignContext;
    }

    public CampaignContext getCampaignContext() {
        return campaignContext;
    }
}
