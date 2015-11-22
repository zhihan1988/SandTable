package com.rathink.ie.base.component;

import com.rathink.ie.ibase.service.CampaignContext;
import org.jgroups.protocols.pbcast.STABLE;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Hean on 2015/10/31.
 */
@Component
@Scope("prototype")
public class CyclePublisher implements ApplicationContextAware {
    private CampaignContext campaignContext;
    private Map<String, Boolean> companyFinishedMap = new ConcurrentHashMap<>();
    private final Float CYCLE_TIME = 300000F;//每一回合的操作时长1min
    private Float leftTime = CYCLE_TIME;//剩余时长
    private Timer timer;

    private ApplicationContext ctx;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    public void setCampaignContext(CampaignContext campaignContext) {
        this.campaignContext = campaignContext;
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

    public Float getSchedule() {
        return (CYCLE_TIME - leftTime) / CYCLE_TIME;
    }

    public void startTime() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                leftTime = leftTime - 1000;
                if (leftTime <= 0) {
                    publish();
                }
            }
        }, 2000, 1000);
    }

    public synchronized void finish(String companyId) {
        companyFinishedMap.put(companyId, true);
        if (getUnFinishedNum() == 0) {
            publish();
        }
    }

    private void publish(){
        CycleEvent cycleEvent = new CycleEvent(ctx, campaignContext);
        ctx.publishEvent(cycleEvent);

        reset();
    }

    public void reset() {
        companyFinishedMap.clear();

        timer.cancel();
        leftTime = CYCLE_TIME;
        startTime();
    }
}
