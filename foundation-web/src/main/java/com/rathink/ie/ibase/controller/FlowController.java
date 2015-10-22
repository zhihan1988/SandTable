package com.rathink.ie.ibase.controller;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignCenterManager;
import com.rathink.ie.foundation.service.RoundEndObserable;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.service.CampaignCenter;
import com.rathink.ie.ibase.service.CampaignContext;
import com.rathink.ie.ibase.service.CompanyTermContext;
import com.rathink.ie.ibase.service.FlowManager;
import com.rathink.ie.internet.service.RobotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Hean on 2015/8/28.
 */
@Controller
@RequestMapping("/flow")
public class FlowController {
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private CampaignCenterManager campaignCenterManager;
    @Autowired
    private RobotManager robotManager;

    @RequestMapping("/begin")
    @ResponseBody
    public String begin(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), campaignId);
        FlowManager flowManager = (FlowManager) ApplicationContextUtil.getBean(campaign.getIndustry().getType() + "FlowManagerImpl");
        flowManager.begin(campaignId);
        return "success";
    }

    @RequestMapping("/next")
    @ResponseBody
    public String next(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), campaignId);
        FlowManager flowManager = (FlowManager) ApplicationContextUtil.getBean(campaign.getIndustry().getType() + "FlowManagerImpl");
        flowManager.next(campaignId);
        return "success";
    }

    @RequestMapping("/reset")
    @ResponseBody
    public String reset(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), campaignId);
        FlowManager flowManager = (FlowManager) ApplicationContextUtil.getBean(campaign.getIndustry().getType() + "FlowManagerImpl");
        flowManager.reset(campaignId);
        campaignCenterManager.initCampaignHandler(campaign);
        flowManager.begin(campaignId);
        return "success";
    }

    @RequestMapping("/companyNext")
    @ResponseBody
    public void companyNext(HttpServletRequest request, Model model) throws Exception {
        CampaignContext campaignContext = CampaignCenter.getCampaignHandler(request.getParameter("campaignId"));
        String campaignDate = request.getParameter("campaignDate");
        String key = campaignDate + ":" + request.getParameter("roundType");
        RoundEndObserable roundEndObserable = (RoundEndObserable) campaignContext.getObservableMap().get(key);
        roundEndObserable.finish(request.getParameter("companyId"));
        return;
    }

    @RequestMapping("/isCampaignNext")
    @ResponseBody
    public Integer isNext(HttpServletRequest request, Model model) throws Exception {
        CampaignContext campaignContext = CampaignCenter.getCampaignHandler(request.getParameter("campaignId"));
        String campaignDate = request.getParameter("campaignDate");
        String key = campaignDate + ":" + request.getParameter("roundType");
        RoundEndObserable roundEndObserable = (RoundEndObserable) campaignContext.getObservableMap().get(key);
        return roundEndObserable.getUnFinishedNum();
    }

    @RequestMapping("/random")
    @ResponseBody
    public String random(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        CampaignContext campaignContext = CampaignCenter.getCampaignHandler(campaignId);
        Integer currentCampaignDate = campaignContext.getCampaign().getCurrentCampaignDate();
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (CompanyTermContext companyTermContext : companyTermHandlerMap.values()) {
            Company company = companyTermContext.getCompanyTerm().getCompany();
            if (company.getCurrentCampaignDate().equals(currentCampaignDate)) {
                robotManager.randomInstruction(companyTermContext);
            }
        }
        return "success";
    }
}
