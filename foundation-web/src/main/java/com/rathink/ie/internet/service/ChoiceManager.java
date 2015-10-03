package com.rathink.ie.internet.service;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.work.model.CampaignTermChoice;
import com.rathink.ie.ibase.work.model.IndustryChoice;

import java.util.List;
import java.util.Set;

/**
 * Created by Hean on 2015/9/4.
 */
public interface ChoiceManager {

    List<CampaignTermChoice> listCompanyChoice(String campaignId, Integer campaignDate);

    List<CampaignTermChoice> listCompanyChoice(String campaignId, Integer campaignDate, String choiceType);

    List<CampaignTermChoice> randomChoices(Campaign campaign);

}
