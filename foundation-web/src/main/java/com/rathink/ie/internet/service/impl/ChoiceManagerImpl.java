package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.service.CampaignCenter;
import com.rathink.ie.ibase.service.CampaignContext;
import com.rathink.ie.ibase.work.model.CampaignTermChoice;
import com.rathink.ie.ibase.work.model.IndustryChoice;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.Edept;
import com.rathink.ie.internet.service.ChoiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Hean on 2015/8/25.
 */
@Service
public class ChoiceManagerImpl implements ChoiceManager {
    @Autowired
    private BaseManager baseManager;

    public List<CampaignTermChoice> listCompanyChoice(String campaignId, Integer campaignDate) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CampaignTermChoice where campaign.id = :campaignId and campaignDate = :campaignDate");
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap();
        queryParamMap.put("campaignId", campaignId);
        queryParamMap.put("campaignDate", campaignDate);
        xQuery.setQueryParamMap(queryParamMap);
        List companyChoiceList = baseManager.listObject(xQuery);
        return companyChoiceList;
    }

    @Override
    public List<CampaignTermChoice> listCompanyChoice(String campaignId, Integer campaignDate, String choiceType){
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CampaignTermChoice where campaign.id = :campaignId and campaignDate = :campaignDate and baseType = :choiceType");
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap();
        queryParamMap.put("campaignId", campaignId);
        queryParamMap.put("campaignDate", campaignDate);
        queryParamMap.put("choiceType", choiceType);
        xQuery.setQueryParamMap(queryParamMap);
        List companyChoiceList = baseManager.listObject(xQuery);
        return companyChoiceList;
    }

    @Override
    public List<CampaignTermChoice> randomChoices(Campaign campaign) {
        List<IndustryChoice> industryChoiceList = new ArrayList<>();
        industryChoiceList.addAll(produceHumanChoice(campaign));
        industryChoiceList.addAll(loadIndustryChoice(campaign.getIndustry().getId(), EChoiceBaseType.MARKET_ACTIVITY.name()));
        industryChoiceList.addAll(loadIndustryChoice(campaign.getIndustry().getId(), EChoiceBaseType.PRODUCT_STUDY.name()));
        industryChoiceList.addAll(loadIndustryChoice(campaign.getIndustry().getId(), EChoiceBaseType.PRODUCT_STUDY_FEE.name()));
        industryChoiceList.addAll(loadIndustryChoice(campaign.getIndustry().getId(), EChoiceBaseType.OPERATION.name()));

        List<CampaignTermChoice> campaignTermChoiceList = new ArrayList<>();
        for (IndustryChoice industryChoice : industryChoiceList) {
            CampaignTermChoice campaignTermChoice = new CampaignTermChoice();
            campaignTermChoice.setIndustryChoice(industryChoice);
            campaignTermChoice.setCampaign(campaign);
            campaignTermChoice.setCampaignDate(campaign.getCurrentCampaignDate());
            campaignTermChoice.setBaseType(industryChoice.getBaseType());
            campaignTermChoiceList.add(campaignTermChoice);
        }

        return campaignTermChoiceList;
    }

    private List<IndustryChoice> produceHumanChoice(Campaign campaign) {
        CampaignContext campaignContext = CampaignCenter.getCampaignHandler(campaign.getId());
        List<IndustryChoice> humanIndustryChoiceList = campaignContext.randomHumans();
        return humanIndustryChoiceList;
    }

    public List<IndustryChoice> loadIndustryChoice(String industryId, String baseType) {
        XQuery xQuery = new XQuery();
        String hql = "from IndustryChoice where industry.id=:industryId and baseType = :baseType";
        xQuery.setHql(hql);
        xQuery.put("industryId", industryId);
        xQuery.put("baseType", baseType);
        List<IndustryChoice> industryChoiceList = baseManager.listObject(xQuery);
        return industryChoiceList;
    }

}
