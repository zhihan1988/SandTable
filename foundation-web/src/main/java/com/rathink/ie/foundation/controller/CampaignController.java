package com.rathink.ie.foundation.controller;


import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Created by Hean on 15/8/14.
 */
@Controller
@RequestMapping("/campaign")
public class CampaignController {
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private CampaignService campaignService;

    @RequestMapping("/listCampaign")
    public String listCampaign(HttpServletRequest request, Model model) throws Exception {
        XQuery xQuery = new XQuery("plistCampaign_default", request);
        xQuery.addRequestParamToModel(model, request);
        List campaignList = baseManager.listPageInfo(xQuery).getList();
        model.addAttribute("campaignList", campaignList);
//        model.addAttribute("pageMsg", "3");
        return "/foundation/campaign/campaignList";
    }

    @RequestMapping("/{campaignId}")
    public String getCampaign(HttpServletRequest request, @PathVariable String campaignId,Model model) throws Exception {
        Campaign campaign = (Campaign)baseManager.getObject(Campaign.class.getName(), campaignId);
        model.addAttribute("campaign", campaign);
        List companyList = campaignService.listCompany(campaign);
        model.addAttribute("companyList", companyList);
        return "/foundation/campaign/campaignView";

    }
}
