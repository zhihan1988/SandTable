package com.rathink.ie.foundation.service;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.campaign.model.Campaign;
import com.rathink.ie.internet.Edept;
import com.rathink.ie.internet.choice.model.Human;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Hean on 2015/8/25.
 */
@Service
public class ChoiceService {
    @Autowired
    private BaseManager baseManager;

    public void produceChoice(Campaign campaign) {
        Human human = new Human();
        human.setCampaignDate(campaign.getCurrentCampaignDate());
        human.setCampaign(campaign);
        human.setDept(Edept.HR.name());
        human.setName("产品");
        human.setSalary("15000");
        human.setIntelligent("B");
        human.setHard("B");
        baseManager.saveOrUpdate(Human.class.getName(), human);
        Human human2 = new Human();
        human2.setCampaignDate(campaign.getCurrentCampaignDate());
        human2.setCampaign(campaign);
        human2.setDept(Edept.HR.name());
        human2.setName("技术");
        human2.setSalary("15000");
        human2.setIntelligent("B");
        human2.setHard("B");
        baseManager.saveOrUpdate(Human.class.getName(), human2);
        Human human3 = new Human();
        human3.setCampaignDate(campaign.getCurrentCampaignDate());
        human3.setCampaign(campaign);
        human3.setDept(Edept.HR.name());
        human3.setName("运营");
        human3.setSalary("20000");
        human3.setIntelligent("A");
        human3.setHard("A");
        baseManager.saveOrUpdate(Human.class.getName(), human3);
    }
}
