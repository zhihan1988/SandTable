package com.rathink.ix.ibase.component;

import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ix.ibase.service.FlowManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Hean on 2015/10/31.
 */
@Component
@Scope("prototype")
public class CyclePublisher {
    private CampContext campaignContext;
    private Map<String, Boolean> companyFinishedMap = new ConcurrentHashMap<>();
    private final Float CYCLE_TIME = 120000F;//每一回合的操作时长1min
    private Float leftTime = CYCLE_TIME;//剩余时长
    private Timer timer;

    public void setCampaignContext(CampContext campaignContext) {
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
     /*   timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                leftTime = leftTime - 1000;
                if (leftTime <= 0) {
//                    publish();
                }
            }
        }, 2000, 1000);*/
    }

    public synchronized void finish(String companyId) {
        companyFinishedMap.put(companyId, true);
        if (getUnFinishedNum() == 0) {
            publish();
        }
    }

    private void publish(){
        Campaign campaign = campaignContext.getCampaign();
        FlowManager flowManager = (FlowManager) ApplicationContextUtil.getBean(campaign.getIndustry().getType() + "FlowManagerImpl");
        flowManager.next(campaign.getId());

        reset();
    }

    public void reset() {
        companyFinishedMap.clear();

       /* timer.cancel();
        leftTime = CYCLE_TIME;
        startTime();*/
    }
}
