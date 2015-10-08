package com.rathink.ie.manufacturing.controller;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignCenterManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.team.model.ECompanyStatus;
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
@RequestMapping("/manufacturing/flow")
public class ManufacturingFlowController {
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private FlowManager manufacturingFlowManagerImpl;
    @Autowired
    private CampaignCenterManager campaignCenterManager;
    @Autowired
    private RobotManager robotManager;



}
