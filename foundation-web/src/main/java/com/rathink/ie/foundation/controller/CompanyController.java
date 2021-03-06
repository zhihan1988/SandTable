package com.rathink.ie.foundation.controller;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.util.DateUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.campaign.model.Industry;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.user.model.User;
import com.rathink.ie.user.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Hean on 2015/8/24.
 */
@Controller
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private BaseManager baseManager;

    @RequestMapping("/listCompany")
    public String listCompany(HttpServletRequest request, Model model) throws Exception {
        User user = AuthorizationUtil.getMyUser();
        XQuery xQuery = new XQuery("plistCompany_default", request);
        xQuery.setHql("from Company where director.id = :userId");
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("userId", user.getId());
        xQuery.setQueryParamMap(queryParamMap);
        xQuery.addRequestParamToModel(model, request);
        List companyList = baseManager.listPageInfo(xQuery).getList();
        model.addAttribute("companyList", companyList);
        return "/foundation/company/companyList";
    }

    @RequestMapping("/{companyId}")
    public String getCampaign(HttpServletRequest request, @PathVariable String companyId,Model model) throws Exception {
        Company company = (Company)baseManager.getObject(Company.class.getName(), companyId);
        model.addAttribute("company", company);
        return "/foundation/company/companyView";

    }

    @RequestMapping({"/saveOrUpdate"})
    public String saveOrUpdateCompany(Company company,HttpServletRequest request){
        company.setStatus("1");
        company.setCreateDatetime(new Date());
        Campaign campaign = (Campaign)baseManager.getObject(Campaign.class.getName(),request.getParameter("campaignId"));
        company.setCampaign(campaign);
        company.setDirector(AuthorizationUtil.getMyUser());
        baseManager.saveOrUpdate(Company.class.getName(), company);
        return "redirect:/campaign/"+request.getParameter("campaignId");
    }

    @RequestMapping({"/form"})
    public String formCompany(HttpServletRequest request , Model model) throws Exception{
        return "/foundation/company/companyForm";
    }
}
