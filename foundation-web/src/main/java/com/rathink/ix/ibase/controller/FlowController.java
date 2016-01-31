package com.rathink.ix.ibase.controller;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ix.ibase.component.CyclePublisher;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignCenterManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ix.ibase.component.CampContext;
import com.rathink.ix.ibase.service.CampaignCenter;
import com.rathink.ix.ibase.service.CompanyTermContext;
import com.rathink.ix.ibase.service.FlowManager;
import com.rathink.ix.internet.service.RobotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
//        flowManager.reset(campaignId);
        campaignCenterManager.initCampaignHandler(campaign);
//        flowManager.begin(campaignId);
        return "success";
    }

    @RequestMapping("/companyNext")
    @ResponseBody
    public void companyNext(HttpServletRequest request, Model model) throws Exception {
        CampContext campaignContext = CampaignCenter.getCampaignHandler(request.getParameter("campaignId"));
        campaignContext.getCyclePublisher().finish(request.getParameter("companyId"));
        return;
    }

    @RequestMapping("/isCampaignNext")
    @ResponseBody
    public Map isNext(HttpServletRequest request, Model model) throws Exception {
        Map result = new HashMap<>();

        CampContext campaignContext = CampaignCenter.getCampaignHandler(request.getParameter("campaignId"));
        String clientCampaignDate = request.getParameter("campaignDate");
        Integer serverCampaignDate = campaignContext.getCampaign().getCurrentCampaignDate();
        if (Integer.parseInt(clientCampaignDate) == serverCampaignDate) {
            result.put("isNext", false);
            CyclePublisher cyclePublisher = campaignContext.getCyclePublisher();
            result.put("unFinishedNum", cyclePublisher.getUnFinishedNum());
            result.put("schedule", cyclePublisher.getSchedule());
        } else {
            //刷新页面 进入下一回合
            result.put("isNext", true);
        }

        return result;
    }

    @RequestMapping("/random")
    @ResponseBody
    public String random(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        CampContext campaignContext = CampaignCenter.getCampaignHandler(campaignId);
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
