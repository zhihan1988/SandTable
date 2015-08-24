package com.rathink.ie.foundation.controller;


import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.campaign.model.Campaign;
import com.rathink.ie.team.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by Hean on 15/8/14.
 */
@Controller
@RequestMapping("/campaign")
public class CampaignController {
    @Autowired
    private BaseManager baseManager;

    @RequestMapping("/listCampaign.do")
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

        XQuery companyListQuery = new XQuery();
        companyListQuery.setHql("from Company where campaign.id = :campaignId");
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<String, Object>();
        queryParamMap.put("campaignId", campaignId);
        companyListQuery.setQueryParamMap(queryParamMap);
        List companyList = baseManager.listObject(companyListQuery);
        model.addAttribute("companyList", companyList);
//        modelMap.addAttribute("pageMsg","6");
        return "/campaign/campaignView";

    }

    @RequestMapping("/main")
    public String main(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), company.getCampaign().getId());
        model.addAttribute("company", company);
        model.addAttribute("campaign", campaign);
        return "/campaign/main";

    }
}
