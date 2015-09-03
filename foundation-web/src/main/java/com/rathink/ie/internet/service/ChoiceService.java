package com.rathink.ie.internet.service;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignService;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.internet.Edept;
import com.rathink.ie.internet.choice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Hean on 2015/8/25.
 */
@Service
public class ChoiceService {
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private CampaignService campaignService;

    public List<Human> listHuman(Campaign campaign){
        XQuery xQuery = new XQuery();
        xQuery.setHql("from Human where campaign.id = :campaignId and campaignDate = :campaignDate");
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<String, Object>();
        queryParamMap.put("campaignId", campaign.getId());
        queryParamMap.put("campaignDate", campaign.getCurrentCampaignDate());
        xQuery.setQueryParamMap(queryParamMap);
        List humanList = baseManager.listObject(xQuery);
        return humanList;
    }

    public List<MarketActivityChoice> listMarketActivityChoice(Campaign campaign){
        XQuery xQuery = new XQuery();
        xQuery.setHql("from MarketActivityChoice where campaign.id = :campaignId and campaignDate = :campaignDate");
        xQuery.put("campaignId", campaign.getId());
        xQuery.put("campaignDate", campaign.getCurrentCampaignDate());
        List marketActivityChoiceList = baseManager.listObject(xQuery);
        return marketActivityChoiceList;
    }

    public List<ProductStudy> listProductStudy(Campaign campaign){
        XQuery xQuery = new XQuery();
        xQuery.setHql("from ProductStudy where campaign.id = :campaignId and campaignDate = :campaignDate");
        xQuery.put("campaignId", campaign.getId());
        xQuery.put("campaignDate", campaign.getCurrentCampaignDate());
        List productStudyList = baseManager.listObject(xQuery);
        return productStudyList;
    }

    public List<OperationChoice> listOperationChoice(Campaign campaign) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from OperationChoice where campaign.id = :campaignId and campaignDate = :campaignDate");
        xQuery.put("campaignId", campaign.getId());
        xQuery.put("campaignDate", campaign.getCurrentCampaignDate());
        List operationChoiceList = baseManager.listObject(xQuery);
        return operationChoiceList;
    }

    public void produceChoice(Campaign campaign) {
        produceHumanChoice(campaign);
        produceMarketActivityChoice(campaign);
        produceProductStudyChoice(campaign);
        produceOperationChoice(campaign);
    }

    private void produceProductStudyChoice(Campaign campaign) {
        ProductStudy productStudy = new ProductStudy();
        productStudy.setCampaignDate(campaign.getCurrentCampaignDate());
        productStudy.setCampaign(campaign);
        productStudy.setDept(Edept.PRODUCT.name());
        productStudy.setGrade("1");
        productStudy.setFees("10000,20000,50000");
        baseManager.saveOrUpdate(ProductStudy.class.getName(), productStudy);
        ProductStudy productStudy2 = new ProductStudy();
        productStudy2.setCampaignDate(campaign.getCurrentCampaignDate());
        productStudy2.setCampaign(campaign);
        productStudy2.setDept(Edept.PRODUCT.name());
        productStudy2.setGrade("2");
        productStudy2.setFees("10000,20000,50000");
        baseManager.saveOrUpdate(ProductStudy.class.getName(), productStudy2);

    }

    private void produceOperationChoice(Campaign campaign) {
        OperationChoice operationChoice = new OperationChoice();
        operationChoice.setCampaignDate(campaign.getCurrentCampaignDate());
        operationChoice.setCampaign(campaign);
        operationChoice.setDept(Edept.OPERATION.name());
        operationChoice.setFees("10000,20000,50000");
        baseManager.saveOrUpdate(OperationChoice.class.getName(), operationChoice);
    }

    private void produceMarketActivityChoice(Campaign campaign) {
        MarketActivityChoice marketActivityChoice = new MarketActivityChoice();
        marketActivityChoice.setCampaignDate(campaign.getCurrentCampaignDate());
        marketActivityChoice.setCampaign(campaign);
        marketActivityChoice.setDept(Edept.MARKET.name());
        marketActivityChoice.setName("网络推广");
        marketActivityChoice.setCost("20");
        marketActivityChoice.setFees("10000,20000,50000");
        marketActivityChoice.setRandomHigh("100");
        marketActivityChoice.setRandomLow("20");
        baseManager.saveOrUpdate(MarketActivityChoice.class.getName(), marketActivityChoice);

        MarketActivityChoice marketActivityChoice2 = new MarketActivityChoice();
        marketActivityChoice2.setCampaignDate(campaign.getCurrentCampaignDate());
        marketActivityChoice2.setCampaign(campaign);
        marketActivityChoice2.setDept(Edept.MARKET.name());
        marketActivityChoice2.setName("地面推广");
        marketActivityChoice2.setCost("100");
        marketActivityChoice2.setFees("100000,200000,500000");
        marketActivityChoice2.setRandomHigh("60");
        marketActivityChoice2.setRandomLow("40");
        baseManager.saveOrUpdate(MarketActivityChoice.class.getName(), marketActivityChoice2);
    }
    private void produceHumanChoice(Campaign campaign) {
        Human human = new Human();
        human.setCampaignDate(campaign.getCurrentCampaignDate());
        human.setCampaign(campaign);
        human.setDept(Edept.HR.name());
        human.setName("小张");
        human.setType(Edept.PRODUCT.name());
        human.setFees("20000,25000,30000");
        human.setAbility("10");
        baseManager.saveOrUpdate(Human.class.getName(), human);
        Human human3 = new Human();
        human3.setCampaignDate(campaign.getCurrentCampaignDate());
        human3.setCampaign(campaign);
        human3.setDept(Edept.HR.name());
        human3.setName("小王");
        human3.setType(Edept.OPERATION.name());
        human3.setFees("15000,20000,25000");
        human3.setAbility("8");
        baseManager.saveOrUpdate(Human.class.getName(), human3);
        Human human4 = new Human();
        human4.setCampaignDate(campaign.getCurrentCampaignDate());
        human4.setCampaign(campaign);
        human4.setDept(Edept.HR.name());
        human4.setName("小明");
        human4.setType(Edept.MARKET.name());
        human4.setFees("10000,15000,20000");
        human4.setAbility("6");
        baseManager.saveOrUpdate(Human.class.getName(), human4);
    }


}
