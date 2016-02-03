package com.rathink.iix.ibase.controller;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.ix.ibase.service.*;
import com.rathink.ix.internet.service.InternetWorkManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Hean on 2016/2/2.
 */
@Controller
public class IBaseController {

    @Autowired
    protected BaseManager baseManager;
    @Autowired
    protected CampaignManager campaignManager;
    @Autowired
    protected CompanyTermManager companyTermManager;
    @Autowired
    protected InstructionManager instructionManager;
    @Autowired
    protected AccountManager accountManager;
    @Autowired
    protected PropertyManager propertyManager;
    @Autowired
    protected InternetWorkManager internetWorkManager;
    @Autowired
    protected CompanyPartManager companyPartManager;
}
