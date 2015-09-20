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
        productStudy.setName("高端");
        productStudy.setValue("1");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), productStudy);
        CompanyChoice productStudy2 = new CompanyChoice();
        productStudy2.setBaseType(EChoiceBaseType.PRODUCT_STUDY.name());
        productStudy2.setCampaignDate(campaign.getCurrentCampaignDate());
        productStudy2.setCampaign(campaign);
        productStudy2.setDept(Edept.PRODUCT.name());
        productStudy2.setName("中端");
        productStudy2.setValue("2");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), productStudy2);
        CompanyChoice productStudy3 = new CompanyChoice();
        productStudy3.setBaseType(EChoiceBaseType.PRODUCT_STUDY.name());
        productStudy3.setCampaignDate(campaign.getCurrentCampaignDate());
        productStudy3.setCampaign(campaign);
        productStudy3.setDept(Edept.PRODUCT.name());
        productStudy3.setName("低端");
        productStudy3.setValue("3");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), productStudy3);
    }

    private void produceProductStudyFeeChoice(Campaign campaign) {
        CompanyChoice productStudyFee = new CompanyChoice();
        productStudyFee.setBaseType(EChoiceBaseType.PRODUCT_STUDY_FEE.name());
        productStudyFee.setCampaignDate(campaign.getCurrentCampaignDate());
        productStudyFee.setCampaign(campaign);
        productStudyFee.setDept(Edept.PRODUCT.name());
        productStudyFee.setName("产品研发投入");
        productStudyFee.setFees("10000,20000,40000,80000,160000");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), productStudyFee);
    }

    private void produceOperationChoice(Campaign campaign) {
        CompanyChoice operationChoice = new CompanyChoice();
        operationChoice.setBaseType(EChoiceBaseType.OPERATION.name());
        operationChoice.setCampaignDate(campaign.getCurrentCampaignDate());
        operationChoice.setCampaign(campaign);
        operationChoice.setDept(Edept.OPERATION.name());
        operationChoice.setFees("10000,20000,40000,80000,160000");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), operationChoice);
    }

    private void produceMarketActivityChoice(Campaign campaign) {
        CompanyChoice marketActivityChoice = new CompanyChoice();
        marketActivityChoice.setBaseType(EChoiceBaseType.MARKET_ACTIVITY.name());
        marketActivityChoice.setCampaignDate(campaign.getCurrentCampaignDate());
        marketActivityChoice.setCampaign(campaign);
        marketActivityChoice.setDept(Edept.MARKET.name());
        marketActivityChoice.setName("微博/微信");
        marketActivityChoice.setValue("20");
        marketActivityChoice.setFees("10000,20000,40000,80000,160000");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), marketActivityChoice);
        CompanyChoice marketActivityChoice2 = new CompanyChoice();
        marketActivityChoice2.setBaseType(EChoiceBaseType.MARKET_ACTIVITY.name());
        marketActivityChoice2.setCampaignDate(campaign.getCurrentCampaignDate());
        marketActivityChoice2.setCampaign(campaign);
        marketActivityChoice2.setDept(Edept.MARKET.name());
        marketActivityChoice2.setName("百度");
        marketActivityChoice2.setValue("30");
        marketActivityChoice2.setFees("10000,20000,40000,80000,160000");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), marketActivityChoice2);
        CompanyChoice marketActivityChoice3 = new CompanyChoice();
        marketActivityChoice3.setBaseType(EChoiceBaseType.MARKET_ACTIVITY.name());
        marketActivityChoice3.setCampaignDate(campaign.getCurrentCampaignDate());
        marketActivityChoice3.setCampaign(campaign);
        marketActivityChoice3.setDept(Edept.MARKET.name());
        marketActivityChoice3.setName("地推");
        marketActivityChoice3.setValue("50");
        marketActivityChoice3.setFees("10000,20000,40000,80000,160000");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), marketActivityChoice3);
        CompanyChoice marketActivityChoice4 = new CompanyChoice();
        marketActivityChoice4.setBaseType(EChoiceBaseType.MARKET_ACTIVITY.name());
        marketActivityChoice4.setCampaignDate(campaign.getCurrentCampaignDate());
        marketActivityChoice4.setCampaign(campaign);
        marketActivityChoice4.setDept(Edept.MARKET.name());
        marketActivityChoice4.setName("地面广告");
        marketActivityChoice4.setValue("40");
        marketActivityChoice4.setFees("10000,20000,40000,80000,160000");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), marketActivityChoice4);

    }

    private void productOfficeChoice(Campaign campaign) {
        CompanyChoice officeChoice = new CompanyChoice();
        officeChoice.setBaseType(EChoiceBaseType.OFFICE.name());
        officeChoice.setCampaignDate(campaign.getCurrentCampaignDate());
        officeChoice.setCampaign(campaign);
        officeChoice.setName("科实大厦");
        officeChoice.setFees("20000");
        officeChoice.setDescription("100P;15");
        officeChoice.setDept(Edept.AD.name());
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), officeChoice);
        CompanyChoice officeChoice2 = new CompanyChoice();
        officeChoice2.setBaseType(EChoiceBaseType.OFFICE.name());
        officeChoice2.setCampaignDate(campaign.getCurrentCampaignDate());
        officeChoice2.setCampaign(campaign);
        officeChoice2.setName("建外SOHO");
        officeChoice2.setFees("30000");
        officeChoice2.setDescription("100P;15");
        officeChoice2.setDept(Edept.AD.name());
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), officeChoice2);
        CompanyChoice companyChoice3 = new CompanyChoice();
        companyChoice3.setBaseType(EChoiceBaseType.OFFICE.name());
        companyChoice3.setCampaignDate(campaign.getCurrentCampaignDate());
        companyChoice3.setCampaign(campaign);
        companyChoice3.setName("辉煌国际中心");
        companyChoice3.setFees("15000");
        companyChoice3.setDescription("60P;8");
        companyChoice3.setDept(Edept.AD.name());
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), companyChoice3);
        CompanyChoice companyChoice4 = new CompanyChoice();
        companyChoice4.setBaseType(EChoiceBaseType.OFFICE.name());
        companyChoice4.setCampaignDate(campaign.getCurrentCampaignDate());
        companyChoice4.setCampaign(campaign);
        companyChoice4.setName("中关村大厦");
        companyChoice4.setFees("40000");
        companyChoice4.setDescription("150P;20");
        companyChoice4.setDept(Edept.AD.name());
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), companyChoice4);
        CompanyChoice companyChoice5 = new CompanyChoice();
        companyChoice5.setBaseType(EChoiceBaseType.OFFICE.name());
        companyChoice5.setCampaignDate(campaign.getCurrentCampaignDate());
        companyChoice5.setCampaign(campaign);
        companyChoice5.setName("望京科技园");
        companyChoice5.setFees("30000");
        companyChoice5.setDescription("200P;25");
        companyChoice5.setDept(Edept.AD.name());
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), companyChoice5);
    }

    private void produceHumanChoice(Campaign campaign) {
        CompanyChoice human = new CompanyChoice();
        human.setBaseType(EChoiceBaseType.HUMAN.name());
        human.setCampaignDate(campaign.getCurrentCampaignDate());
        human.setCampaign(campaign);
        human.setDept(Edept.HR.name());
        human.setName("小张");
        human.setType(Edept.PRODUCT.name());
        human.setFees("10000,20000,30000,40000,50000");
        human.setValue("10");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), human);
        CompanyChoice human3 = new CompanyChoice();
        human3.setBaseType(EChoiceBaseType.HUMAN.name());
        human3.setCampaignDate(campaign.getCurrentCampaignDate());
        human3.setCampaign(campaign);
        human3.setDept(Edept.HR.name());
        human3.setName("小王");
        human3.setType(Edept.MARKET.name());
        human3.setFees("10000,20000,30000,40000,50000");
        human3.setValue("8");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), human3);
        CompanyChoice human4 = new CompanyChoice();
        human4.setBaseType(EChoiceBaseType.HUMAN.name());
        human4.setCampaignDate(campaign.getCurrentCampaignDate());
        human4.setCampaign(campaign);
        human4.setDept(Edept.HR.name());
        human4.setName("小明");
        human4.setType(Edept.OPERATION.name());
        human4.setFees("10000,20000,30000,40000,50000");
        human4.setValue("6");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), human4);
        CompanyChoice human5 = new CompanyChoice();
        human5.setBaseType(EChoiceBaseType.HUMAN.name());
        human5.setCampaignDate(campaign.getCurrentCampaignDate());
        human5.setCampaign(campaign);
        human5.setDept(Edept.HR.name());
        human5.setName("小李");
        human5.setType(Edept.PRODUCT.name());
        human5.setFees("10000,20000,30000,40000,50000");
        human5.setValue("5");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), human5);
        CompanyChoice human6 = new CompanyChoice();
        human6.setBaseType(EChoiceBaseType.HUMAN.name());
        human6.setCampaignDate(campaign.getCurrentCampaignDate());
        human6.setCampaign(campaign);
        human6.setDept(Edept.HR.name());
        human6.setName("小孙");
        human6.setType(Edept.MARKET.name());
        human6.setFees("10000,20000,30000,40000,50000");
        human6.setValue("7");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), human6);
        CompanyChoice human7 = new CompanyChoice();
        human7.setBaseType(EChoiceBaseType.HUMAN.name());
        human7.setCampaignDate(campaign.getCurrentCampaignDate());
        human7.setCampaign(campaign);
        human7.setDept(Edept.HR.name());
        human7.setName("小黄");
        human7.setType(Edept.OPERATION.name());
        human7.setFees("10000,20000,30000,40000,50000");
        human7.setValue("9");
        baseManager.saveOrUpdate(CompanyChoice.class.getName(), human7);
    }


}
