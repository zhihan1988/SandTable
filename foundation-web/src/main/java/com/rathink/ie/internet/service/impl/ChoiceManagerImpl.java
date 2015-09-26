package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.service.CampaignCenter;
import com.rathink.ie.ibase.service.CampaignContext;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.Resource;
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

    public List<CompanyChoice> listCompanyChoice(String campaignId, String campaignDate) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyChoice where campaign.id = :campaignId and campaignDate = :campaignDate");
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap();
        queryParamMap.put("campaignId", campaignId);
        queryParamMap.put("campaignDate", campaignDate);
        xQuery.setQueryParamMap(queryParamMap);
        List companyChoiceList = baseManager.listObject(xQuery);
        return companyChoiceList;
    }

    @Override
    public List<CompanyChoice> listCompanyChoice(String campaignId, String campaignDate, String choiceType){
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyChoice where campaign.id = :campaignId and campaignDate = :campaignDate and baseType = :choiceType");
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap();
        queryParamMap.put("campaignId", campaignId);
        queryParamMap.put("campaignDate", campaignDate);
        queryParamMap.put("choiceType", choiceType);
        xQuery.setQueryParamMap(queryParamMap);
        List companyChoiceList = baseManager.listObject(xQuery);
        return companyChoiceList;
    }

    @Override
    public List<CompanyChoice> randomChoices(Campaign campaign) {
        List<CompanyChoice> companyChoiceList = new ArrayList<>();
        companyChoiceList.addAll(productOfficeChoice(campaign));
        companyChoiceList.addAll(produceHumanChoice(campaign));
        companyChoiceList.addAll(produceMarketActivityChoice(campaign));
        companyChoiceList.addAll(produceProductStudyChoice(campaign));
        companyChoiceList.addAll(produceProductStudyFeeChoice(campaign));
        companyChoiceList.addAll(produceOperationChoice(campaign));
        return companyChoiceList;
    }

    private List<CompanyChoice> produceHumanChoice(Campaign campaign) {
        CampaignContext campaignContext = CampaignCenter.getCampaignHandler(campaign.getId());
        List<Resource> humanResourceList = campaignContext.randomHumans();
        List<CompanyChoice> companyChoiceList = new ArrayList<>();
        for (Resource resource : humanResourceList) {
            CompanyChoice companyChoice = new CompanyChoice();
            companyChoice.setCampaignDate(campaign.getCurrentCampaignDate());
            companyChoice.setCampaign(campaign);
            companyChoice.setBaseType(resource.getBaseType());
            companyChoice.setDept(resource.getDept());
            companyChoice.setName(resource.getName());
            companyChoice.setValue(resource.getValue());
            companyChoice.setValue2(resource.getValue2());
            companyChoice.setType(resource.getType());
            companyChoice.setDescription(resource.getDescription());
            companyChoice.setFees(resource.getFees());
            companyChoice.setImg(resource.getImg());
            companyChoiceList.add(companyChoice);
        }
        return companyChoiceList;
    }

    private List<CompanyChoice> produceProductStudyChoice(Campaign campaign) {
        List<CompanyChoice> companyChoiceList = new ArrayList<>();
        CompanyChoice productStudy = new CompanyChoice();
        productStudy.setBaseType(EChoiceBaseType.PRODUCT_STUDY.name());
        productStudy.setCampaignDate(campaign.getCurrentCampaignDate());
        productStudy.setCampaign(campaign);
        productStudy.setDept(Edept.PRODUCT.name());
        productStudy.setName("高端");
        productStudy.setValue("1");
        companyChoiceList.add(productStudy);
        CompanyChoice productStudy2 = new CompanyChoice();
        productStudy2.setBaseType(EChoiceBaseType.PRODUCT_STUDY.name());
        productStudy2.setCampaignDate(campaign.getCurrentCampaignDate());
        productStudy2.setCampaign(campaign);
        productStudy2.setDept(Edept.PRODUCT.name());
        productStudy2.setName("中端");
        productStudy2.setValue("2");
        companyChoiceList.add(productStudy2);
        CompanyChoice productStudy3 = new CompanyChoice();
        productStudy3.setBaseType(EChoiceBaseType.PRODUCT_STUDY.name());
        productStudy3.setCampaignDate(campaign.getCurrentCampaignDate());
        productStudy3.setCampaign(campaign);
        productStudy3.setDept(Edept.PRODUCT.name());
        productStudy3.setName("低端");
        productStudy3.setValue("3");
        companyChoiceList.add(productStudy3);
        return companyChoiceList;
    }

    private List<CompanyChoice> produceProductStudyFeeChoice(Campaign campaign) {
        List<CompanyChoice> companyChoiceList = new ArrayList<>();
        CompanyChoice productStudyFee = new CompanyChoice();
        productStudyFee.setBaseType(EChoiceBaseType.PRODUCT_STUDY_FEE.name());
        productStudyFee.setCampaignDate(campaign.getCurrentCampaignDate());
        productStudyFee.setCampaign(campaign);
        productStudyFee.setDept(Edept.PRODUCT.name());
        productStudyFee.setName("产品研发投入");
        productStudyFee.setFees("20000,40000,80000,160000,320000");
        companyChoiceList.add(productStudyFee);
        return companyChoiceList;
    }

    private List<CompanyChoice> produceOperationChoice(Campaign campaign) {
        List<CompanyChoice> companyChoiceList = new ArrayList<>();
        CompanyChoice operationChoice = new CompanyChoice();
        operationChoice.setBaseType(EChoiceBaseType.OPERATION.name());
        operationChoice.setCampaignDate(campaign.getCurrentCampaignDate());
        operationChoice.setCampaign(campaign);
        operationChoice.setDept(Edept.OPERATION.name());
        operationChoice.setFees("40000,80000,160000,320000,640000");
        companyChoiceList.add(operationChoice);
        return companyChoiceList;
    }

    private List<CompanyChoice> produceMarketActivityChoice(Campaign campaign) {
        List<CompanyChoice> companyChoiceList = new ArrayList<>();
        CompanyChoice marketActivityChoice = new CompanyChoice();
        marketActivityChoice.setBaseType(EChoiceBaseType.MARKET_ACTIVITY.name());
        marketActivityChoice.setCampaignDate(campaign.getCurrentCampaignDate());
        marketActivityChoice.setCampaign(campaign);
        marketActivityChoice.setDept(Edept.MARKET.name());
        marketActivityChoice.setName("微博/微信");
        marketActivityChoice.setValue("20");
        marketActivityChoice.setFees("10000,20000,40000,80000,160000");
        companyChoiceList.add(marketActivityChoice);
        CompanyChoice marketActivityChoice2 = new CompanyChoice();
        marketActivityChoice2.setBaseType(EChoiceBaseType.MARKET_ACTIVITY.name());
        marketActivityChoice2.setCampaignDate(campaign.getCurrentCampaignDate());
        marketActivityChoice2.setCampaign(campaign);
        marketActivityChoice2.setDept(Edept.MARKET.name());
        marketActivityChoice2.setName("百度");
        marketActivityChoice2.setValue("30");
        marketActivityChoice2.setFees("10000,20000,40000,80000,160000");
        companyChoiceList.add(marketActivityChoice2);
        CompanyChoice marketActivityChoice3 = new CompanyChoice();
        marketActivityChoice3.setBaseType(EChoiceBaseType.MARKET_ACTIVITY.name());
        marketActivityChoice3.setCampaignDate(campaign.getCurrentCampaignDate());
        marketActivityChoice3.setCampaign(campaign);
        marketActivityChoice3.setDept(Edept.MARKET.name());
        marketActivityChoice3.setName("地推");
        marketActivityChoice3.setValue("50");
        marketActivityChoice3.setFees("10000,20000,40000,80000,160000");
        companyChoiceList.add(marketActivityChoice3);
        CompanyChoice marketActivityChoice4 = new CompanyChoice();
        marketActivityChoice4.setBaseType(EChoiceBaseType.MARKET_ACTIVITY.name());
        marketActivityChoice4.setCampaignDate(campaign.getCurrentCampaignDate());
        marketActivityChoice4.setCampaign(campaign);
        marketActivityChoice4.setDept(Edept.MARKET.name());
        marketActivityChoice4.setName("地面广告");
        marketActivityChoice4.setValue("40");
        marketActivityChoice4.setFees("10000,20000,40000,80000,160000");
        companyChoiceList.add(marketActivityChoice4);
        return companyChoiceList;
    }

    private List<CompanyChoice> productOfficeChoice(Campaign campaign) {
        List<CompanyChoice> companyChoiceList = new ArrayList<>();
        CompanyChoice officeChoice = new CompanyChoice();
        officeChoice.setBaseType(EChoiceBaseType.OFFICE.name());
        officeChoice.setCampaignDate(campaign.getCurrentCampaignDate());
        officeChoice.setCampaign(campaign);
        officeChoice.setName("科实大厦");
        officeChoice.setValue("20000");
        officeChoice.setFees("20000");
        officeChoice.setDescription("100P;15");
        officeChoice.setDept(Edept.AD.name());
        officeChoice.setImg("office-1.jpg");
        companyChoiceList.add(officeChoice);
        CompanyChoice officeChoice2 = new CompanyChoice();
        officeChoice2.setBaseType(EChoiceBaseType.OFFICE.name());
        officeChoice2.setCampaignDate(campaign.getCurrentCampaignDate());
        officeChoice2.setCampaign(campaign);
        officeChoice2.setName("建外SOHO");
        officeChoice2.setValue("30000");
        officeChoice2.setFees("30000");
        officeChoice2.setDescription("100P;15");
        officeChoice2.setDept(Edept.AD.name());
        officeChoice2.setImg("office-2.jpg");
        companyChoiceList.add(officeChoice2);
        CompanyChoice companyChoice3 = new CompanyChoice();
        companyChoice3.setBaseType(EChoiceBaseType.OFFICE.name());
        companyChoice3.setCampaignDate(campaign.getCurrentCampaignDate());
        companyChoice3.setCampaign(campaign);
        companyChoice3.setName("辉煌国际中心");
        companyChoice3.setValue("15000");
        companyChoice3.setFees("15000");
        companyChoice3.setDescription("60P;8");
        companyChoice3.setDept(Edept.AD.name());
        companyChoice3.setImg("office-3.jpg");
        companyChoiceList.add(companyChoice3);
        CompanyChoice companyChoice4 = new CompanyChoice();
        companyChoice4.setBaseType(EChoiceBaseType.OFFICE.name());
        companyChoice4.setCampaignDate(campaign.getCurrentCampaignDate());
        companyChoice4.setCampaign(campaign);
        companyChoice4.setName("中关村大厦");
        companyChoice4.setValue("40000");
        companyChoice4.setFees("40000");
        companyChoice4.setDescription("150P;20");
        companyChoice4.setDept(Edept.AD.name());
        companyChoice4.setImg("office-4.jpg");
        companyChoiceList.add(companyChoice4);
        CompanyChoice companyChoice5 = new CompanyChoice();
        companyChoice5.setBaseType(EChoiceBaseType.OFFICE.name());
        companyChoice5.setCampaignDate(campaign.getCurrentCampaignDate());
        companyChoice5.setCampaign(campaign);
        companyChoice5.setName("望京科技园");
        companyChoice5.setValue("30000");
        companyChoice5.setFees("30000");
        companyChoice5.setDescription("200P;25");
        companyChoice5.setDept(Edept.AD.name());
        companyChoice5.setImg("office-5.jpg");
        companyChoiceList.add(companyChoice5);
        return companyChoiceList;
    }

    @Deprecated
    public Set<Resource> loadAllHumans(Campaign campaign) {
        Set<Resource> humanSet = new HashSet<>();
        Resource human = new Resource();
        human.setBaseType(EChoiceBaseType.HUMAN.name());
        human.setDept(Edept.HR.name());
        human.setName("小张");
        human.setType(Edept.PRODUCT.name());
        human.setFees("10000,20000,30000,40000,50000");
        human.setValue("10");
        humanSet.add(human);
        Resource human3 = new Resource();
        human3.setBaseType(EChoiceBaseType.HUMAN.name());
        human3.setDept(Edept.HR.name());
        human3.setName("小王");
        human3.setType(Edept.MARKET.name());
        human3.setFees("10000,20000,30000,40000,50000");
        human3.setValue("8");
        humanSet.add(human3);
        Resource human4 = new Resource();
        human4.setBaseType(EChoiceBaseType.HUMAN.name());
        human4.setDept(Edept.HR.name());
        human4.setName("小明");
        human4.setType(Edept.OPERATION.name());
        human4.setFees("10000,20000,30000,40000,50000");
        human4.setValue("6");
        humanSet.add(human4);
        Resource human5 = new Resource();
        human5.setBaseType(EChoiceBaseType.HUMAN.name());
        human5.setDept(Edept.HR.name());
        human5.setName("小李");
        human5.setType(Edept.PRODUCT.name());
        human5.setFees("10000,20000,30000,40000,50000");
        human5.setValue("5");
        humanSet.add(human5);
        Resource human6 = new Resource();
        human6.setBaseType(EChoiceBaseType.HUMAN.name());
        human6.setDept(Edept.HR.name());
        human6.setName("小孙");
        human6.setType(Edept.MARKET.name());
        human6.setFees("10000,20000,30000,40000,50000");
        human6.setValue("7");
        humanSet.add(human6);
        Resource human7 = new Resource();
        human7.setBaseType(EChoiceBaseType.HUMAN.name());
        human7.setDept(Edept.HR.name());
        human7.setName("小黄");
        human7.setType(Edept.OPERATION.name());
        human7.setFees("10000,20000,30000,40000,50000");
        human7.setValue("9");
        humanSet.add(human7);


        Resource human8 = new Resource();
        human8.setBaseType(EChoiceBaseType.HUMAN.name());
        human8.setDept(Edept.HR.name());
        human8.setName("Tom");
        human8.setType(Edept.OPERATION.name());
        human8.setFees("10000,20000,30000,40000,50000");
        human8.setValue("4");
        humanSet.add(human8);

        Resource human9 = new Resource();
        human9.setBaseType(EChoiceBaseType.HUMAN.name());
        human9.setDept(Edept.HR.name());
        human9.setName("Nike");
        human9.setType(Edept.MARKET.name());
        human9.setFees("10000,20000,30000,40000,50000");
        human9.setValue("5");
        humanSet.add(human9);

        Resource human10 = new Resource();
        human10.setBaseType(EChoiceBaseType.HUMAN.name());
        human10.setDept(Edept.HR.name());
        human10.setName("Sony");
        human10.setType(Edept.PRODUCT.name());
        human10.setFees("10000,20000,30000,40000,50000");
        human10.setValue("7");
        humanSet.add(human10);

        return humanSet;
    }


}
