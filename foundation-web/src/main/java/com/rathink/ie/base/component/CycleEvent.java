package com.rathink.ie.base.component;

import com.rathink.ie.ibase.component.CampContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

/**
 * Created by Hean on 2015/10/31.
 */
public class CycleEvent extends ApplicationContextEvent {
    private CampContext campaignContext;
    public CycleEvent(ApplicationContext source, CampContext campaignContext) {
        super(source);
        this.campaignContext = campaignContext;
    }

    public CampContext getCampaignContext() {
        return campaignContext;
    }
}
