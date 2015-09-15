package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.Edept;
import com.rathink.ie.internet.service.ChoiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Hean on 2015/8/25.
 */
@Service
public class ChoiceManagerImpl implements ChoiceManager {
    @Autowired
    private BaseManager baseManager;

    @Override
    public List<CompanyChoice> listCompanyChoice(String campaignId, String campaignDate, String choiceType){
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyChoice where campaign.id = :campaignId and campaignDate = :campaignDate and baseType = :choiceType");
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<String, Object>();
        queryParamMap.put("campaignId", campaignId);
        queryParamMap.put("campaignDate", campaignDate);
        queryParamMap.put("choiceType", choiceType);
        xQuery.setQueryParamMap(queryParamMap);
        List humanList = baseManager.listObject(xQuery);
        return humanList;
    }

    @Override
    public void produceChoice(Campaign campaign) {
        productOfficeChoice(campaign);
        produceHumanChoice(campaign);
        produceMarketActivityChoice(campaign);
        produceProductStudyChoice(campaign);
        produceProductStudyFeeChoice(campaign);
        produceOperationChoice(campaign);
    }

    private void produceProductStudyChoice(Campaign campaign) {
        CompanyChoice productStudy = new CompanyChoice();
        productStudy.setBaseType(EChoiceBaseType.PRODUCT_STUDY.name());
        productStudy.setCampaignDate(campaign.getCurrentCampaignDate());
        productStudy.setCampaign(campaign);
        productStudy.setDept(Edept.PRODUCT.name());
        productStudy.setName("低端定位");
        productStudy.setValue("1,2,3");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), productStudy);
     /*   CompanyChoice productStudy2 = new CompanyChoice();
        productStudy2.setBaseType(EChoiceBaseType.PRODUCT_STUDY.name());
        productStudy2.setCampaignDate(campaign.getCurrentCampaignDate());
        productStudy2.setCampaign(campaign);
        productStudy2.setDept(Edept.PRODUCT.name());
        productStudy2.setName("中端定位");
        productStudy2.setValue("2");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), productStudy2);*/
    }

    private void produceProductStudyFeeChoice(Campaign campaign) {
        CompanyChoice productStudyFee = new CompanyChoice();
        productStudyFee.setBaseType(EChoiceBaseType.PRODUCT_STUDY_FEE.name());
        productStudyFee.setCampaignDate(campaign.getCurrentCampaignDate());
        productStudyFee.setCampaign(campaign);
        productStudyFee.setDept(Edept.PRODUCT.name());
        productStudyFee.setName("产品研发投入");
        productStudyFee.setFees("10000,20000,30000");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), productStudyFee);
    }

    private void produceOperationChoice(Campaign campaign) {
        CompanyChoice operationChoice = new CompanyChoice();
        operationChoice.setBaseType(EChoiceBaseType.OPERATION.name());
        operationChoice.setCampaignDate(campaign.getCurrentCampaignDate());
        operationChoice.setCampaign(campaign);
        operationChoice.setDept(Edept.OPERATION.name());
        operationChoice.setFees("10000,20000,50000");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), operationChoice);
    }

    private void produceMarketActivityChoice(Campaign campaign) {
        CompanyChoice marketActivityChoice = new CompanyChoice();
        marketActivityChoice.setBaseType(EChoiceBaseType.MARKET_ACTIVITY.name());
        marketActivityChoice.setCampaignDate(campaign.getCurrentCampaignDate());
        marketActivityChoice.setCampaign(campaign);
        marketActivityChoice.setDept(Edept.MARKET.name());
        marketActivityChoice.setName("网络推广");
        marketActivityChoice.setValue("20");//成本
        marketActivityChoice.setFees("10000,20000,50000");
        marketActivityChoice.setRandomHigh("100");
        marketActivityChoice.setRandomLow("20");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), marketActivityChoice);

        CompanyChoice marketActivityChoice2 = new CompanyChoice();
        marketActivityChoice2.setBaseType(EChoiceBaseType.MARKET_ACTIVITY.name());
        marketActivityChoice2.setCampaignDate(campaign.getCurrentCampaignDate());
        marketActivityChoice2.setCampaign(campaign);
        marketActivityChoice2.setDept(Edept.MARKET.name());
        marketActivityChoice2.setName("地面推广");
        marketActivityChoice2.setValue("30");//成本
        marketActivityChoice2.setFees("20000,50000,100000");
        marketActivityChoice2.setRandomHigh("60");
        marketActivityChoice2.setRandomLow("40");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), marketActivityChoice2);
    }

    private void productOfficeChoice(Campaign campaign) {
        CompanyChoice officeChoice = new CompanyChoice();
        officeChoice.setBaseType(EChoiceBaseType.OFFICE.name());
        officeChoice.setCampaignDate(campaign.getCurrentCampaignDate());
        officeChoice.setCampaign(campaign);
        officeChoice.setFees("30000");
        officeChoice.setDescription("近地铁");
        officeChoice.setDept(Edept.AD.name());
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), officeChoice);
        CompanyChoice officeChoice2 = new CompanyChoice();
        officeChoice2.setBaseType(EChoiceBaseType.OFFICE.name());
        officeChoice2.setCampaignDate(campaign.getCurrentCampaignDate());
        officeChoice2.setCampaign(campaign);
        officeChoice2.setFees("20000");
        officeChoice2.setDescription("安静");
        officeChoice2.setDept(Edept.AD.name());
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), officeChoice2);
    }

    private void produceHumanChoice(Campaign campaign) {
        CompanyChoice human = new CompanyChoice();
        human.setBaseType(EChoiceBaseType.HUMAN.name());
        human.setCampaignDate(campaign.getCurrentCampaignDate());
        human.setCampaign(campaign);
        human.setDept(Edept.HR.name());
        human.setName("小张");
        human.setType(Edept.PRODUCT.name());
        human.setFees("20000,25000,30000");
        human.setValue("10");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), human);
        CompanyChoice human3 = new CompanyChoice();
        human3.setBaseType(EChoiceBaseType.HUMAN.name());
        human3.setCampaignDate(campaign.getCurrentCampaignDate());
        human3.setCampaign(campaign);
        human3.setDept(Edept.HR.name());
        human3.setName("小王");
        human3.setType(Edept.OPERATION.name());
        human3.setFees("15000,20000,25000");
        human3.setValue("8");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), human3);
        CompanyChoice human4 = new CompanyChoice();
        human4.setBaseType(EChoiceBaseType.HUMAN.name());
        human4.setCampaignDate(campaign.getCurrentCampaignDate());
        human4.setCampaign(campaign);
        human4.setDept(Edept.HR.name());
        human4.setName("小明");
        human4.setType(Edept.MARKET.name());
        human4.setFees("10000,15000,20000");
        human4.setValue("6");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), human4);
    }


}
