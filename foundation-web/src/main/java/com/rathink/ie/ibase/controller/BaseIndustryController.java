package com.rathink.ie.ibase.controller;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.*;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryResourceChoice;
import com.rathink.ie.internet.service.InternetWorkManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Hean on 2015/10/9.
 */
@Controller
@RequestMapping("/work")
public class BaseIndustryController {

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

    @RequestMapping("/makeInstruction")
    @ResponseBody
    public String makeInstruction(HttpServletRequest request, Model model) throws Exception {
        String companyTermId = request.getParameter("companyTermId");
        String choiceId = request.getParameter("choiceId");
        String value = request.getParameter("value");

        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        if (StringUtils.isNotBlank(choiceId)) {
            instructionManager.saveOrUpdateInstructionByChoice(companyTerm, choiceId, value);
        }

        return "success";
    }

    @RequestMapping("/makeUniqueInstruction")
    @ResponseBody
    public String makeUniqueInstruction(HttpServletRequest request, Model model) throws Exception {
        String companyTermId = request.getParameter("companyTermId");
        String choiceId = request.getParameter("choiceId");
        String value = request.getParameter("value");
        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        IndustryResourceChoice industryResourceChoice = (IndustryResourceChoice) baseManager.getObject(IndustryResourceChoice.class.getName(), choiceId);
        CompanyTermInstruction companyTermInstruction = instructionManager.getUniqueInstructionByBaseType(companyTerm, industryResourceChoice.getIndustryResource().getName());

        if (companyTermInstruction == null) {
            instructionManager.saveOrUpdateInstructionByChoice(companyTerm, choiceId, value);
        } else {
            companyTermInstruction.setIndustryResourceChoice(industryResourceChoice);
            companyTermInstruction.setValue(value);
            baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);
        }
        return "success";
    }

    @RequestMapping("/cancelInstruction")
    @ResponseBody
    public String cancelInstruction(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        String choiceId = request.getParameter("choiceId");
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);
        instructionManager.deleteInstruction(company, choiceId);
        return "success";
    }

}
