package com.rathink.ie.foundation.controller;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.campaign.model.Campaign;
import com.rathink.ie.foundation.service.ChoiceService;
import com.rathink.ie.internet.choice.model.Human;
import com.rathink.ie.internet.instruction.model.HrInstruction;
import com.rathink.ie.team.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Hean on 2015/8/24.
 */
@Controller
@RequestMapping("/work")
public class CompanyController {
    @Autowired
    private BaseManager baseManager;

    @RequestMapping("/listCompany")
    public String listCompany(HttpServletRequest request, Model model) throws Exception {
        XQuery xQuery = new XQuery("plistCompany_default", request);
        xQuery.addRequestParamToModel(model, request);
        List companyList = baseManager.listPageInfo(xQuery).getList();
        model.addAttribute("companyList", companyList);
        return "/company/companyList";
    }

    @RequestMapping("/{companyId}")
    public String getCampaign(HttpServletRequest request, @PathVariable String companyId,Model model) throws Exception {
        Company company = (Company)baseManager.getObject(Campaign.class.getName(), companyId);
        model.addAttribute("company", company);
        return "/company/companyView";

    }
}
