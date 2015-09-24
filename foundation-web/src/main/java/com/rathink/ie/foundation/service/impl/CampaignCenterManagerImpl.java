package com.rathink.ie.foundation.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignCenterManager;
import com.rathink.ie.foundation.util.CampaignUtil;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.CampaignCenter;
import com.rathink.ie.ibase.service.CampaignHandler;
import com.rathink.ie.ibase.service.CompanyTermContext;
import com.rathink.ie.ibase.service.CompanyTermManager;
import com.rathink.ie.internet.service.InternetPropertyManager;
import com.rathink.ie.internet.service.impl.InternetCompanyTermContext;
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
    InternetPropertyManager internetPropertyManager;


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
        CampaignHandler campaignHandler = new CampaignHandler();
        campaignHandler.setCampaign(campaign);

        Map<String, CompanyTermContext> companyTermHandlerMap = new HashMap<>();
        List<CompanyTerm> companyTermList = companyTermManager.listCompanyTerm(campaign.getId(), campaign.getCurrentCampaignDate());
        for (CompanyTerm companyTerm : companyTermList) {
            CompanyTermContext companyTermContext = initCompanyTermHandler(companyTerm, campaignHandler);
            CompanyTerm preCompanyTerm = companyTermManager.getCompanyTerm(companyTerm.getCompany(), CampaignUtil.getPreCampaignDate(campaign.getCurrentCampaignDate()));
            if (preCompanyTerm != null) {
                CompanyTermContext preCompanyTermContext = initCompanyTermHandler(preCompanyTerm, campaignHandler);
                companyTermContext.setPreCompanyTermContext(preCompanyTermContext);
            }
            companyTermHandlerMap.put(companyTerm.getCompany().getId(), companyTermContext);
        }
        campaignHandler.setCompanyTermHandlerMap(companyTermHandlerMap);

        CampaignCenter.putCampaignTermHandler(campaign.getId(), campaignHandler);
    }

    @Override
    public CompanyTermContext initCompanyTermHandler(CompanyTerm companyTerm, CampaignHandler campaignHandler) {

        CompanyTermContext companyTermContext = new InternetCompanyTermContext();
        //1
        companyTermContext.setCampaignHandler(campaignHandler);
        //2
        companyTermContext.setCompanyTerm(companyTerm);
        //3
     /*   List<CompanyInstruction> companyInstructionList = instructionManager.listCompanyInstruction(companyTerm);
        companyTermHandler.putCompanyInstructionList(companyInstructionList);*/
        //4
     /*   List<CompanyTermProperty> companyTermPropertyList = internetPropertyManager.listCompanyTermProperty(companyTerm);
        companyTermHandler.putPropertyList(companyTermPropertyList);*/

        return companyTermContext;
    }
}
