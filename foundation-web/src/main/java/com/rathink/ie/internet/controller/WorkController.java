package com.rathink.ie.internet.controller;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyStatusProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.AccountManager;
import com.rathink.ie.ibase.service.CompanyTermManager;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.Edept;
import com.rathink.ie.internet.service.ChoiceManager;
import com.rathink.ie.internet.service.InstructionManager;
import com.rathink.ie.internet.service.WorkManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/8/24.
 */
@Controller
@RequestMapping("/work")
public class WorkController {
    private static Logger logger = Logger.getLogger(WorkController.class.getName());
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private WorkManager workManager;
    @Autowired
    private CompanyTermManager companyTermManager;
    @Autowired
    private ChoiceManager choiceManager;
    @Autowired
    private InstructionManager instructionManager;
    @Autowired
    private AccountManager accountManager;

    @RequestMapping("/main")
    public String main(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), company.getCampaign().getId());
        CompanyTerm companyTerm = companyTermManager.getCompanyTerm(company, campaign.getCurrentCampaignDate());
        Map<String, List<CompanyStatusProperty>> deptPropertyMap = workManager.partCompanyStatusPropertyByDept(companyTerm.getCompanyStatusPropertyList());
        List<CompanyChoice> officeChoiceList = choiceManager.listCompanyChoice(campaign, EChoiceBaseType.OFFICE.name());
        List<CompanyChoice> humanList = choiceManager.listCompanyChoice(campaign, EChoiceBaseType.HUMAN.name());
        List<CompanyChoice> marketActivityChoiceList = choiceManager.listCompanyChoice(campaign, EChoiceBaseType.MARKET_ACTIVITY.name());
        List<CompanyChoice> productStudyList = choiceManager.listCompanyChoice(campaign, EChoiceBaseType.PRODUCT_STUDY.name());
        List<CompanyChoice> operationChoiceList = choiceManager.listCompanyChoice(campaign, EChoiceBaseType.OPERATION.name());
        Integer companyCash = accountManager.getCompanyCash(company);
        Integer campaignDateInCash = accountManager.countAccountEntryFee(
                company, campaign.getCurrentCampaignDate(), EAccountEntityType.COMPANY_CASH.name(), "1");
        Integer campaignDateOutCash = accountManager.countAccountEntryFee(
                company, campaign.getCurrentCampaignDate(), EAccountEntityType.COMPANY_CASH.name(), "-1");
        List<CompanyInstruction> hrInstructionList = instructionManager .listCompanyInstructionByDept(company, Edept.HR.name());
        List<CompanyInstruction> officeInstructionList = instructionManager.listCompanyInstructionByDept(company, Edept.AD.name());
        model.addAttribute("company", company);
        model.addAttribute("campaign", campaign);
        model.addAttribute("deptPropertyMap", deptPropertyMap);
        model.addAttribute("officeChoiceList", officeChoiceList);
        model.addAttribute("humanList", humanList);
        model.addAttribute("marketActivityChoiceList", marketActivityChoiceList);
        model.addAttribute("productStudyList", productStudyList);
        model.addAttribute("operationChoiceList", operationChoiceList);
        model.addAttribute("companyCash", companyCash);
        model.addAttribute("campaignDateInCash", campaignDateInCash);
        model.addAttribute("campaignDateOutCash", campaignDateOutCash);
        model.addAttribute("officeInstructionList", officeInstructionList);
        model.addAttribute("hrInstructionList", hrInstructionList);
        return "/internet/main";
    }

    @RequestMapping("/makeInstruction")
    @ResponseBody
    public String makeInstruction(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        String choiceId = request.getParameter("choiceId");
        String fee = request.getParameter("fee");
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);
        instructionManager.saveOrUpdateInstruction(company, choiceId, fee);
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
