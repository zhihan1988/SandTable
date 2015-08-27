package com.rathink.ie.foundation.controller;


import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignService;
import com.rathink.ie.foundation.service.ChoiceService;
import com.rathink.ie.foundation.service.CompanyStatusService;
import com.rathink.ie.foundation.service.WorkService;
import com.rathink.ie.ibase.property.model.CompanyStatus;
import com.rathink.ie.ibase.property.model.CompanyStatusPropertyValue;
import com.rathink.ie.internet.choice.model.Human;
import com.rathink.ie.foundation.team.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * Created by Hean on 15/8/14.
 */
@Controller
@RequestMapping("/campaign")
public class CampaignController {
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private WorkService workService;
    @Autowired
    private CompanyStatusService companyStatusService;
    @Autowired
    private CampaignService campaignService;
    @Autowired
    private ChoiceService choiceService;

    @RequestMapping("/listCampaign")
    public String listCampaign(HttpServletRequest request, Model model) throws Exception {
        XQuery xQuery = new XQuery("plistCampaign_default", request);
        xQuery.addRequestParamToModel(model, request);
        List campaignList = baseManager.listPageInfo(xQuery).getList();
        model.addAttribute("campaignList", campaignList);
//        model.addAttribute("pageMsg", "3");
        return "/campaign/campaignList";
    }

    @RequestMapping("/{campaignId}")
    public String getCampaign(HttpServletRequest request, @PathVariable String campaignId,Model model) throws Exception {
        Campaign campaign = (Campaign)baseManager.getObject(Campaign.class.getName(), campaignId);
        model.addAttribute("campaign", campaign);
        List companyList = campaignService.listCompany(campaign);
        model.addAttribute("companyList", companyList);
        return "/campaign/campaignView";

    }

    @RequestMapping("/main")
    public String main(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), company.getCampaign().getId());
        CompanyStatus companyStatus = companyStatusService.getCompanyStatus(company, campaign.getCurrentCampaignDate());
        Map<String, List<CompanyStatusPropertyValue>> deptPropertyMap = workService.partCompanyStatusPropertyByDept(companyStatus.getCompanyStatusPropertyValueList());
        List<Human> humanList = choiceService.listHuman(campaign);

        model.addAttribute("company", company);
        model.addAttribute("campaign", campaign);
        model.addAttribute("deptPropertyMap", deptPropertyMap);
        model.addAttribute("humanList", humanList);
        return "/campaign/main";
    }

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
