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
        industryChoiceList.addAll(produceMarketActivityChoice(campaign));
        industryChoiceList.addAll(produceProductStudyChoice(campaign));
        industryChoiceList.addAll(produceProductStudyFeeChoice(campaign));
        industryChoiceList.addAll(produceOperationChoice(campaign));

        List<CampaignTermChoice> campaignTermChoiceList = new ArrayList<>();
        for (IndustryChoice industryChoice : industryChoiceList) {
            CampaignTermChoice campaignTermChoice = new CampaignTermChoice();
            campaignTermChoice.setIndustryChoice(industryChoice);
            campaignTermChoice.setCampaign(campaign);
            campaignTermChoice.setCampaignDate(campaign.getCurrentCampaignDate());
            campaignTermChoiceList.add(campaignTermChoice);
        }

        return campaignTermChoiceList;
    }

    private List<IndustryChoice> produceHumanChoice(Campaign campaign) {
        CampaignContext campaignContext = CampaignCenter.getCampaignHandler(campaign.getId());
        List<IndustryChoice> humanIndustryChoiceList = campaignContext.randomHumans();
        return humanIndustryChoiceList;
    }

    private List<IndustryChoice> produceProductStudyChoice(Campaign campaign) {
        List<IndustryChoice> industryChoiceList = new ArrayList<>();
        IndustryChoice productStudy = new IndustryChoice();
        productStudy.setBaseType(EChoiceBaseType.PRODUCT_STUDY.name());
        productStudy.setDept(Edept.PRODUCT.name());
        productStudy.setName("高端");
        productStudy.setValue("3");
        industryChoiceList.add(productStudy);
        IndustryChoice productStudy2 = new IndustryChoice();
        productStudy2.setBaseType(EChoiceBaseType.PRODUCT_STUDY.name());
        productStudy2.setDept(Edept.PRODUCT.name());
        productStudy2.setName("中端");
        productStudy2.setValue("2");
        industryChoiceList.add(productStudy2);
        IndustryChoice productStudy3 = new IndustryChoice();
        productStudy3.setBaseType(EChoiceBaseType.PRODUCT_STUDY.name());
        productStudy3.setDept(Edept.PRODUCT.name());
        productStudy3.setName("低端");
        productStudy3.setValue("1");
        industryChoiceList.add(productStudy3);
        return industryChoiceList;
    }

    private List<IndustryChoice> produceProductStudyFeeChoice(Campaign campaign) {
        List<IndustryChoice> industryChoiceList = new ArrayList<>();
        IndustryChoice productStudyFee = new IndustryChoice();
        productStudyFee.setBaseType(EChoiceBaseType.PRODUCT_STUDY_FEE.name());
        productStudyFee.setDept(Edept.PRODUCT.name());
        productStudyFee.setName("产品研发投入");
        productStudyFee.setFees("20000,40000,80000,160000,320000");
        industryChoiceList.add(productStudyFee);
        return industryChoiceList;
    }

    private List<IndustryChoice> produceOperationChoice(Campaign campaign) {
        List<IndustryChoice> industryChoiceList = new ArrayList<>();
        IndustryChoice operationChoice = new IndustryChoice();
        operationChoice.setBaseType(EChoiceBaseType.OPERATION.name());
        operationChoice.setDept(Edept.OPERATION.name());
        operationChoice.setFees("40000,80000,160000,320000,640000");
        industryChoiceList.add(operationChoice);
        return industryChoiceList;
    }

    private List<IndustryChoice> produceMarketActivityChoice(Campaign campaign) {
        List<IndustryChoice> industryChoiceList = new ArrayList<>();
        IndustryChoice marketActivityChoice = new IndustryChoice();
        marketActivityChoice.setBaseType(EChoiceBaseType.MARKET_ACTIVITY.name());
        marketActivityChoice.setDept(Edept.MARKET.name());
        marketActivityChoice.setName("社交网络");
        marketActivityChoice.setValue("30");
        marketActivityChoice.setFees("10000,20000,40000,80000,160000");
        industryChoiceList.add(marketActivityChoice);
        IndustryChoice marketActivityChoice2 = new IndustryChoice();
        marketActivityChoice2.setBaseType(EChoiceBaseType.MARKET_ACTIVITY.name());
        marketActivityChoice2.setDept(Edept.MARKET.name());
        marketActivityChoice2.setName("搜索引擎");
        marketActivityChoice2.setValue("35");
        marketActivityChoice2.setFees("10000,20000,40000,80000,160000");
        industryChoiceList.add(marketActivityChoice2);
        IndustryChoice marketActivityChoice3 = new IndustryChoice();
        marketActivityChoice3.setBaseType(EChoiceBaseType.MARKET_ACTIVITY.name());
        marketActivityChoice3.setDept(Edept.MARKET.name());
        marketActivityChoice3.setName("地面推广");
        marketActivityChoice3.setValue("40");
        marketActivityChoice3.setFees("10000,20000,40000,80000,160000");
        industryChoiceList.add(marketActivityChoice3);
        IndustryChoice marketActivityChoice4 = new IndustryChoice();
        marketActivityChoice4.setBaseType(EChoiceBaseType.MARKET_ACTIVITY.name());
        marketActivityChoice4.setDept(Edept.MARKET.name());
        marketActivityChoice4.setName("户外广告");
        marketActivityChoice4.setValue("50");
        marketActivityChoice4.setFees("10000,20000,40000,80000,160000");
        industryChoiceList.add(marketActivityChoice4);
        return industryChoiceList;
    }

}
