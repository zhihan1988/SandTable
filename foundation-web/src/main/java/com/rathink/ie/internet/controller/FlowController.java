package com.rathink.ie.internet.controller;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignCenterManager;
import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.util.CampaignUtil;
import com.rathink.ie.internet.service.FlowManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    private CampaignManager campaignManager;

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
    public String companyNext(HttpServletRequest request, Model model) throws Exception {
        Company company = (Company) baseManager.getObject(Company.class.getName(), request.getParameter("companyId"));
        Campaign campaign = company.getCampaign();
        String currentCampaignDate = campaign.getCurrentCampaignDate();
        String nextCampaignDate = CampaignUtil.getNextCampaignDate(currentCampaignDate);
        company.setCurrentCampaignDate(nextCampaignDate);
        baseManager.saveOrUpdate(Company.class.getName(), company);

        boolean isAllNext = true;
        List<Company> companyList = campaignManager.listCompany(campaign);
        if (companyList != null) {
            for (Company c : companyList) {
                if (!nextCampaignDate.equals(c.getCurrentCampaignDate())) {
                    isAllNext = false;
                }
            }
        }
        if (isAllNext) {
            flowManager.next(campaign.getId());
        }
        return "success";
    }
}
