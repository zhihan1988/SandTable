package com.rathink.ie.base.component;

import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.service.CampaignContext;
import com.rathink.ie.ibase.service.FlowManager;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by Hean on 2015/10/31.
 */
@Component
public class CycleListener implements ApplicationListener<CycleEvent> {

    @Override
    public void onApplicationEvent(CycleEvent cycleEvent) {
        CampaignContext campaignContext = cycleEvent.getCampaignContext();
        Campaign campaign = campaignContext.getCampaign();
        FlowManager flowManager = (FlowManager) ApplicationContextUtil.getBean(campaign.getIndustry().getType() + "FlowManagerImpl");
        flowManager.next(campaign.getId());
    }
}
