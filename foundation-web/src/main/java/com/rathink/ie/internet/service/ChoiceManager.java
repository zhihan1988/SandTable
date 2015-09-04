package com.rathink.ie.internet.service;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.internet.choice.model.*;

import java.util.List;

/**
 * Created by Hean on 2015/9/4.
 */
public interface ChoiceManager {
    List<OfficeChoice> listOfficeChoice(Campaign campaign);

    List<Human> listHuman(Campaign campaign);

    List<MarketActivityChoice> listMarketActivityChoice(Campaign campaign);

    List<ProductStudy> listProductStudy(Campaign campaign);

    List<OperationChoice> listOperationChoice(Campaign campaign);

    void produceChoice(Campaign campaign);
}
