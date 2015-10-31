package com.rathink.ie.base.component;

import com.rathink.ie.ibase.service.CampaignContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Hean on 2015/10/31.
 */
@Component
@Scope("prototype")
public class CyclePublisher implements ApplicationContextAware {
    private CampaignContext campaignContext;
    private Map<String, Boolean> companyFinishedMap = new ConcurrentHashMap<>();

    private ApplicationContext ctx;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    public void setCampaignContext(CampaignContext campaignContext) {
        this.campaignContext = campaignContext;
    }

    public void nextCycle() {
        CycleEvent cycleEvent = new CycleEvent(ctx, campaignContext);
        ctx.publishEvent(cycleEvent);
    }


    public Integer getUnFinishedNum() {
        int finishedCount = 0;
        Integer companyCount = campaignContext.getCompanyTermContextMap().size();
        for (Boolean isFinished : companyFinishedMap.values()) {
            if (isFinished) {
                finishedCount++;
            }
        }
        return companyCount - finishedCount;
    }


    public synchronized void finish(String companyId) {
        companyFinishedMap.put(companyId, true);
        if (getUnFinishedNum() == 0) {
            nextCycle();
            companyFinishedMap.clear();
        }
    }
}
