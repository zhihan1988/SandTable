package com.rathink.ie.internet.controller;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.util.CampaignUtil;
import com.rathink.ie.ibase.service.CampaignCenter;
import com.rathink.ie.ibase.service.CampaignHandler;
import com.rathink.ie.internet.service.WorkManager;
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
    private WorkManager workManager;
    @Autowired
    private CampaignManager campaignManager;

    @RequestMapping("/begin")
    @ResponseBody
    public String begin(HttpServletRequest request, Model model) throws Exception {
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), request.getParameter("campaignId"));
        workManager.initCampaign(campaign);
        return "success";
    }

    @RequestMapping("/next")
    @ResponseBody
    public String next(HttpServletRequest request, Model model) throws Exception {
        CampaignHandler campaignHandler = CampaignCenter.getCampaignHandler(request.getParameter("campaignId"));
        workManager.next(campaignHandler);
        return "success";
    }

    @RequestMapping("/pre")
    @ResponseBody
    public String pre(HttpServletRequest request, Model model) throws Exception {
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), request.getParameter("campaignId"));
        workManager.pre(campaign);
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
            CampaignHandler campaignHandler = CampaignCenter.getCampaignHandler(campaign.getId());
            workManager.next(campaignHandler);
        }
        return "success";
    }
}
