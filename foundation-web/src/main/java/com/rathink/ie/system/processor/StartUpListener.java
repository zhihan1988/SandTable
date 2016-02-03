package com.rathink.ie.system.processor;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.iix.ibase.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Hean on 2015/9/14.
 */
@Component
public class StartUpListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private BaseManager baseManager;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {//root application context 没有parent，他就是老大.
            //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
         /*   XQuery xQuery = new XQuery();
            xQuery.setHql("from Campaign");
            List<Campaign> campaignList = baseManager.listObject(xQuery);
            if (campaignList != null) {
                for (Campaign campaign : campaignList) {
                    if ("manufacturing".equals(campaign.getIndustry().getType())) {
                        IBaseService iBaseService = (IBaseService) ApplicationContextUtil.getBean(campaign.getIndustry().getType() + "ServiceImpl");
                        iBaseService.reset(campaign.getId());
                    }
                }
            }*/
        }
    }
}