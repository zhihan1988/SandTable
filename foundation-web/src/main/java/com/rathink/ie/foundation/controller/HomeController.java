package com.rathink.ie.foundation.controller;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.work.model.IndustryAnalyzer;
import com.rathink.ie.ibase.work.model.IndustryExpression;
import com.rathink.ie.ibase.work.model.IndustryExpressionSimulation;
import com.rathink.ie.ibase.work.model.IndustryExpressionVariate;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by pgwt on 10/3/15.
 */
@Controller
public class HomeController {

    @Autowired
    private BaseManager baseManager;

    @RequestMapping({"/home.do"})
    public String home() {
        return "/home";
    }

    @RequestMapping({"/industry.do"})
    public String industry(Model model) {
        String hql = "select obj from "+ Campaign.class.getName() + " obj where obj.industry.id='1'";
        List<Object> objectList = baseManager.listObject(hql);
        model.addAttribute("campaignList",objectList);

        String hql2 = "select obj from "+ Campaign.class.getName() + " obj where obj.industry.id='2'";
        List<Object> objectList2 = baseManager.listObject(hql2);
        model.addAttribute("campaignList2",objectList2);
        return "/industry";
    }

    @RequestMapping({"/internet.do"})
    public String internet(Model model) {


        return "/internet";
    }
    @RequestMapping({"/manufacturing.do"})
    public String manufacturing(Model model) {


        return "/manufacturing";
    }






}
