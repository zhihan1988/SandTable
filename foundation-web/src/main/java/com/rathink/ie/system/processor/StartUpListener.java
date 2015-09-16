package com.rathink.ie.system.processor;

import com.rathink.ie.foundation.service.CampaignCenterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by Hean on 2015/9/14.
 */
@Component
public class StartUpListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private CampaignCenterManager campaignCenterManager;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null){//root application context 没有parent，他就是老大.
            //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
            campaignCenterManager.initCampaignCenter();
        }
    }
}