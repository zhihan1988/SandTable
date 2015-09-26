package com.rathink.ie.internet.service;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.Resource;

import java.util.List;
import java.util.Set;

/**
 * Created by Hean on 2015/9/4.
 */
public interface ChoiceManager {

    List<CompanyChoice> listCompanyChoice(String campaignId, String campaignDate);

    List<CompanyChoice> listCompanyChoice(String campaignId, String campaignDate, String choiceType);

    List<CompanyChoice> randomChoices(Campaign campaign);

    Set<Resource> loadAllHumans(Campaign campaign);
}
