package com.rathink.ie.foundation.controller;


import com.ming800.core.base.service.BaseManager;
import com.ming800.core.base.service.XdoManager;
import com.rathink.ie.campaign.model.Campaign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by Hean on 15/8/14.
 */
@Controller
@RequestMapping("/campaign")
public class CampaignController {
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private XdoManager xdoManager;

    @RequestMapping("/listCampaign.do")
    public ModelAndView listCampaign(HttpServletRequest request) {
        ModelMap modelMap = new ModelMap();
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), "1");
        modelMap.put("campaign", campaign);
        return new ModelAndView("/campaign/campaignList", modelMap);
    }
}
