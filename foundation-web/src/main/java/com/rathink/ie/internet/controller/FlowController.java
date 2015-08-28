package com.rathink.ie.internet.controller;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.service.CompanyStatusService;
import com.rathink.ie.internet.service.ChoiceService;
import com.rathink.ie.internet.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Hean on 2015/8/28.
 */
@Controller
@RequestMapping("/work")
public class FlowController {

    @Autowired
    private BaseManager baseManager;
    @Autowired
    private WorkService workService;

    @RequestMapping("/begin")
    @ResponseBody
    public String begin(HttpServletRequest request, Model model) throws Exception {
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), request.getParameter("campaignId"));
        workService.initCampaign(campaign);
        return "success";
    }

    @RequestMapping("/next")
    @ResponseBody
    public String next(HttpServletRequest request, Model model) throws Exception {
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), request.getParameter("campaignId"));
        workService.nextCampaign(campaign);
        return "success";
    }

    @RequestMapping("/pre")
    @ResponseBody
    public String pre(HttpServletRequest request, Model model) throws Exception {
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), request.getParameter("campaignId"));
        workService.preCampaign(campaign);
        return "success";
    }
}
