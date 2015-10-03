package com.rathink.ie.internet.controller;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignCenterManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.team.model.ECompanyStatus;
import com.rathink.ie.foundation.campaign.model.CampaignUtil;
import com.rathink.ie.ibase.service.CampaignCenter;
import com.rathink.ie.ibase.service.CampaignContext;
import com.rathink.ie.ibase.service.CompanyTermContext;
import com.rathink.ie.internet.service.FlowManager;
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
    private FlowManager flowManager;
    @Autowired
    private CampaignCenterManager campaignCenterManager;
    @Autowired
    private RobotManager robotManager;

    @RequestMapping("/begin")
    @ResponseBody
    public String begin(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        flowManager.begin(campaignId);
        return "success";
    }

    @RequestMapping("/next")
    @ResponseBody
    public String next(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        flowManager.next(campaignId);
        return "success";
    }

    @RequestMapping("/reset")
    @ResponseBody
    public String reset(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        flowManager.reset(campaignId);
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), campaignId);
        campaignCenterManager.initCampaignHandler(campaign);
        flowManager.begin(campaignId);
        return "success";
    }

    @RequestMapping("/companyNext")
    @ResponseBody
    public synchronized Boolean companyNext(HttpServletRequest request, Model model) throws Exception {
        CampaignContext campaignContext = CampaignCenter.getCampaignHandler(request.getParameter("campaignId"));
        Campaign campaign = campaignContext.getCampaign();
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        CompanyTermContext companyTermContext = companyTermHandlerMap.get(request.getParameter("companyId"));
        Company company = companyTermContext.getCompanyTerm().getCompany();

        Integer currentCampaignDate = campaign.getCurrentCampaignDate();
        Integer nextCampaignDate = campaign.getNextCampaignDate();
        company.setCurrentCampaignDate(nextCampaignDate);

        boolean isAllNext = true;
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermContext ctContext = companyTermHandlerMap.get(companyId);
            Company c = ctContext.getCompanyTerm().getCompany();
            if (c.getStatus().equals(ECompanyStatus.NORMAL.name()) && !nextCampaignDate.equals(c.getCurrentCampaignDate())) {
                isAllNext = false;
            }
        }
        if (isAllNext) {
            flowManager.next(campaign.getId());
        }
        return isAllNext;
    }

    @RequestMapping("/isNext")
    @ResponseBody
    public Integer isNext(HttpServletRequest request, Model model) throws Exception {
        int unFinishNum = 0;
        Integer campaignDate = Integer.parseInt(request.getParameter("campaignDate"));
        CampaignContext campaignContext = CampaignCenter.getCampaignHandler(request.getParameter("campaignId"));
        for (CompanyTermContext companyTermContext : campaignContext.getCompanyTermContextMap().values()) {
            Company company = companyTermContext.getCompanyTerm().getCompany();
            if (company.getStatus().equals(ECompanyStatus.NORMAL.name()) && campaignDate.equals(company.getCurrentCampaignDate())) {
                unFinishNum++;
            }
        }
        return unFinishNum;
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
