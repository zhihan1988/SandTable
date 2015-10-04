package com.rathink.ie.foundation.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignCenterManager;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.service.*;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.internet.service.ChoiceManager;
import com.rathink.ie.internet.service.InstructionManager;
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
    private InternetPropertyManager internetPropertyManager;
    @Autowired
    private InstructionManager instructionManager;
    @Autowired
    private ChoiceManager choiceManager;

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
        CampaignContext campaignContext = new CampaignContext();
        campaignContext.setCampaign(campaign);

        Map<String, CompanyTermContext> companyTermHandlerMap = new HashMap<>();
        List<CompanyTerm> companyTermList = companyTermManager.listCompanyTerm(campaign.getId(), campaign.getCurrentCampaignDate());
        for (CompanyTerm companyTerm : companyTermList) {
            CompanyTermContext companyTermContext = initCompanyTermHandler(companyTerm, campaignContext);
            CompanyTerm preCompanyTerm = companyTermManager.getCompanyTerm(companyTerm.getCompany(), campaign.getPreCampaignDate());
            if (preCompanyTerm != null) {
                CompanyTermContext preCompanyTermContext = initCompanyTermHandler(preCompanyTerm, campaignContext);
                companyTermContext.setPreCompanyTermContext(preCompanyTermContext);
            }
            companyTermHandlerMap.put(companyTerm.getCompany().getId(), companyTermContext);
        }
        campaignContext.setCompanyTermContextMap(companyTermHandlerMap);
    /*    List<CompanyChoice> companyChoiceList = choiceManager.listCompanyChoice(campaign.getId(), campaign.getCurrentCampaignDate());
        campaignHandler.setCurrentCompanyChoiceList(companyChoiceList);*/
        CampaignCenter.putCampaignTermHandler(campaign.getId(), campaignContext);
    }

    @Override
    public CompanyTermContext initCompanyTermHandler(CompanyTerm companyTerm, CampaignContext campaignContext) {

        CompanyTermContext companyTermContext = new InternetCompanyTermContext();
        //1
        companyTermContext.setCampaignContext(campaignContext);
        //2
        companyTermContext.setCompanyTerm(companyTerm);
        //3
        List<CompanyTermInstruction> companyTermInstructionList = instructionManager.listCompanyInstruction(companyTerm);
        companyTermContext.putCompanyInstructionList(companyTermInstructionList);
        //4
        List<CompanyTermProperty> companyTermPropertyList = internetPropertyManager.listCompanyTermProperty(companyTerm);
        companyTermContext.setCompanyTermPropertyList(companyTermPropertyList);

        return companyTermContext;
    }
}
