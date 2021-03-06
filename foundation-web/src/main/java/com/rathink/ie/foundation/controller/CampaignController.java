package com.rathink.ie.foundation.controller;


import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.util.DateUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.campaign.model.Industry;
import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.user.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    private CampaignManager campaignManager;

    @RequestMapping("/listCampaign")
    public String listCampaign(HttpServletRequest request, Model model) throws Exception {
        XQuery xQuery = new XQuery("plistCampaign_default", request);
        xQuery.addRequestParamToModel(model, request);
        List campaignList = baseManager.listPageInfo(xQuery).getList();
        model.addAttribute("campaignList", campaignList);
//        model.addAttribute("pageMsg", "3");
        return "/foundation/campaign/campaignList";
    }


    //制造业
//    @RequestMapping("/listCampaign")
//    public String listMnueCampaign(HttpServletRequest request, Model model) throws Exception {
//        XQuery xQuery = new XQuery("plistCampaign_default", request);
//        xQuery.addRequestParamToModel(model, request);
//        List campaignList = baseManager.listPageInfo(xQuery).getList();
//        model.addAttribute("campaignList", campaignList);
////        model.addAttribute("pageMsg", "3");
//        return "/foundation/campaign/campaignList";
//    }

    @RequestMapping("/{campaignId}")
    public String getCampaign(HttpServletRequest request, @PathVariable String campaignId, Model model) throws Exception {
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), campaignId);
        model.addAttribute("campaign", campaign);
        List companyList = campaignManager.listCompany(campaign);

        XQuery companyQuery = new XQuery("listCompany_default", request);
        companyQuery.put("director_id", AuthorizationUtil.getMyUser().getId());
        companyQuery.put("campaign_id", campaignId);
        List<Object> cList = baseManager.listObject(companyQuery);
        if (cList != null && cList.size() > 0) {
            model.addAttribute("companyStatus", false);
        } else {
            model.addAttribute("companyStatus", true);
        }
        model.addAttribute("myCompany", cList != null && cList.size() > 0 ? cList.get(0) : new Company());
        model.addAttribute("companyList", companyList);
        model.addAttribute("myUser", AuthorizationUtil.getMyUser());
        return "/foundation/campaign/campaignView";

    }


    @RequestMapping({"/saveOrUpdate"})
    public String saveOrUpdateCampaign(Campaign campaign, HttpServletRequest request) {
        campaign.setStatus("1");
        campaign.setCreateDatetime(new Date());
        campaign.setStartDatetime(DateUtil.parseAllDate(request.getParameter("startDateTime")));
        Industry industry = new Industry();
        industry.setId(request.getParameter("industryId"));
        campaign.setIndustry(industry);
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);
        return "redirect:/campaign/" + campaign.getId();
    }

    @RequestMapping({"/form"})
    public String formCampaign(HttpServletRequest request, Model model) throws Exception {
        XQuery industryQuery = new XQuery("listIndustry_default", request);
        List<Object> industryList = baseManager.listObject(industryQuery);
        model.addAttribute("industryList", industryList);
        return "/foundation/campaign/campaignForm";
    }

}
