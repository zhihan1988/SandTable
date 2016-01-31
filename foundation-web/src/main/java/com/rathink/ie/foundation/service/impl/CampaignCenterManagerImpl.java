package com.rathink.ie.foundation.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignCenterManager;
import com.rathink.ie.ibase.component.BaseCampaignContext;
import com.rathink.ie.ibase.component.CampContext;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.service.*;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.service.InstructionManager;
import com.rathink.ie.ibase.service.PropertyManager;
import com.rathink.ie.internet.component.InternetCampContext;
import com.rathink.ie.internet.service.impl.InternetCompanyTermContext;
import com.rathink.ie.manufacturing.component.ManufacturingCampContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/9/14.
 */
@Service
public class CampaignCenterManagerImpl implements CampaignCenterManager {
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private CompanyTermManager companyTermManager;
    @Autowired
    private PropertyManager propertyManager;
    @Autowired
    private InstructionManager instructionManager;

    @Override
    public void initCampaignCenter() {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from Campaign where status = :status");
        xQuery.put("status", Campaign.Status.RUN.getValue());
        List<Campaign> campaignList = baseManager.listObject(xQuery);
        if (campaignList != null) {
            campaignList.forEach(campaign -> initCampaignHandler(campaign));
        }
    }

    @Override
    public void initCampaignHandler(Campaign campaign) {
        FlowManager flowManager = (FlowManager) ApplicationContextUtil.getBean(campaign.getIndustry().getType() + "FlowManagerImpl");
        flowManager.reset(campaign.getId());

        BaseCampaignContext campaignContext = null;
        String industryType = campaign.getIndustry().getType();
        if ("internet".equals(industryType)) {
            campaignContext = new InternetCampContext();
        } else if ("manufacturing".equals(industryType)) {
            campaignContext = new ManufacturingCampContext();
        } else {
            throw new RuntimeException("初始化失败，行业类型异常：" + campaign.getId());
        }
        campaignContext.setCampaign(campaign);
        CampaignCenter.putCampaignTermHandler(campaign.getId(), campaignContext);

        flowManager.begin(campaign.getId());


       /* CampaignContext campaignContext = new CampaignContext();
        campaignContext.setCampaign(campaign);

        Map<String, CompanyTermContext> companyTermHandlerMap = new HashMap<>();
        List<CompanyTerm> companyTermList = companyTermManager.listCompanyTerm(campaign.getId(), campaign.getCurrentCampaignDate());
        for (CompanyTerm companyTerm : companyTermList) {
            CompanyTermContext companyTermContext = initCompanyTermHandler(companyTerm, campaignContext);
            CompanyTerm preCompanyTerm = companyTermManager.getCompanyTerm(companyTerm.getCompany().getId(), campaign.getPreCampaignDate());
            if (preCompanyTerm != null) {
                CompanyTermContext preCompanyTermContext = initCompanyTermHandler(preCompanyTerm, campaignContext);
                companyTermContext.setPreCompanyTermContext(preCompanyTermContext);
            }
            companyTermHandlerMap.put(companyTerm.getCompany().getId(), companyTermContext);
        }
        campaignContext.setCompanyTermContextMap(companyTermHandlerMap);
    /*    List<CompanyChoice> companyChoiceList = choiceManager.listCompanyChoice(campaign.getId(), campaign.getCurrentCampaignDate());
        campaignHandler.setCurrentCompanyChoiceList(companyChoiceList);*/
//        CampaignCenter.putCampaignTermHandler(campaign.getId(), campaignContext);
    }

    @Override
    public CompanyTermContext initCompanyTermHandler(CompanyTerm companyTerm, CampContext campaignContext) {

      /*  CompanyTermContext companyTermContext = new InternetCompanyTermContext();
        //1
        companyTermContext.setCampaignContext(campaignContext);
        //2
        companyTermContext.setCompanyTerm(companyTerm);
        //3
        List<CompanyTermInstruction> companyTermInstructionList = instructionManager.listCompanyInstruction(companyTerm);
        companyTermContext.putCompanyInstructionList(companyTermInstructionList);
        //4
        List<CompanyTermProperty> companyTermPropertyList = propertyManager.listCompanyTermProperty(companyTerm);
        companyTermContext.setCompanyTermPropertyList(companyTermPropertyList);

        return companyTermContext;*/
        return null;
    }
}
