package com.rathink.ie.internet.controller;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.util.CampaignUtil;
import com.rathink.ie.ibase.service.CampaignCenter;
import com.rathink.ie.ibase.service.CampaignHandler;
import com.rathink.ie.ibase.service.CompanyTermHandler;
import com.rathink.ie.internet.service.FlowManager;
import com.sun.org.apache.xpath.internal.operations.Bool;
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
    private FlowManager flowManager;

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

    @RequestMapping("/pre")
    @ResponseBody
    public String pre(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        flowManager.pre(campaignId);
        return "success";
    }

    @RequestMapping("/reset")
    @ResponseBody
    public String reset(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        flowManager.reset(campaignId);
        return "success";
    }

    @RequestMapping("/companyNext")
    @ResponseBody
    public synchronized Boolean companyNext(HttpServletRequest request, Model model) throws Exception {
        CampaignHandler campaignHandler = CampaignCenter.getCampaignHandler(request.getParameter("campaignId"));
        Campaign campaign = campaignHandler.getCampaign();
        Map<String, CompanyTermHandler> companyTermHandlerMap = campaignHandler.getCompanyTermHandlerMap();
        CompanyTermHandler companyTermHandler = companyTermHandlerMap.get(request.getParameter("companyId"));
        Company company = companyTermHandler.getCompanyTerm().getCompany();

        String currentCampaignDate = campaign.getCurrentCampaignDate();
        String nextCampaignDate = CampaignUtil.getNextCampaignDate(currentCampaignDate);
        company.setCurrentCampaignDate(nextCampaignDate);

        boolean isAllNext = true;
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermHandler ctHandler = companyTermHandlerMap.get(companyId);
            if (!nextCampaignDate.equals(ctHandler.getCompanyTerm().getCompany().getCurrentCampaignDate())) {
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
    public Boolean isNext(HttpServletRequest request, Model model) throws Exception {
        String campaignDate = request.getParameter("campaignDate");
        CampaignHandler campaignHandler = CampaignCenter.getCampaignHandler(request.getParameter("campaignId"));
        Campaign campaign = campaignHandler.getCampaign();
        String currentCampaignDate = campaign.getCurrentCampaignDate();
        if (!campaignDate.equals(currentCampaignDate)) {
            return true;
        } else {
            return false;
        }
    }
}
