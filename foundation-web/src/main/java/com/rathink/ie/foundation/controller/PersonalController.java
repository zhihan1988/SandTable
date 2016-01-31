package com.rathink.ie.foundation.controller;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.user.util.AuthorizationUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.PreUpdate;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by pgwt on 11/29/15.
 */
@Controller
@RequestMapping({"/personal"})
public class PersonalController {

    @Autowired
    private BaseManager baseManager;


    @RequestMapping({"/company/list"})
    public String listMyCompany(HttpServletRequest request, Model model) throws Exception {
        XQuery companyQuery = new XQuery("listCompany_myCompany", request);
        companyQuery.put("director_id", AuthorizationUtil.getMyUser().getId());
        List<Object> companyList = baseManager.listObject(companyQuery);
        model.addAttribute("companyList", companyList);
        return "/personal/companyList";
    }


    @RequestMapping({"/myCode"})
    public String getMyCode(HttpServletRequest request, Model model) {
        model.addAttribute("myCode", AuthorizationUtil.getMyUser().getMyCode());
        return "/personal/myCode";
    }

    @RequestMapping({"/company/{companyId}"})
    public String viewCompany(HttpServletRequest request, @PathVariable String companyId,Model model) {
        Company company = (Company)baseManager.getObject(Company.class.getName(),companyId);
        model.addAttribute("company",company);
        return "/personal/companyView";
    }



}
