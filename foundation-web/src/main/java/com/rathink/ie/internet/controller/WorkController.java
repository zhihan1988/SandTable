package com.rathink.ie.internet.controller;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.team.model.ECompanyStatus;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.*;
import com.rathink.ie.ibase.work.model.CampaignTermChoice;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.service.ChoiceManager;
import com.rathink.ie.internet.service.InstructionManager;
import com.rathink.ie.internet.service.InternetPropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.List;

/**
 * Created by Hean on 2015/8/24.
 */
@Controller
@RequestMapping("/work")
public class WorkController {
    private static Logger logger = LoggerFactory.getLogger(WorkController.class);
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private CompanyTermManager companyTermManager;
    @Autowired
    private ChoiceManager choiceManager;
    @Autowired
    private InstructionManager instructionManager;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private InternetPropertyManager internetPropertyManager;

    @RequestMapping("/main")
    public String main(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), company.getCampaign().getId());
        Integer currentCampaignDate = campaign.getCurrentCampaignDate();
        Integer preCampaignDate = campaign.getPreCampaignDate();
        CompanyTerm preCompanyTerm = companyTermManager.getCompanyTerm(company, preCampaignDate);
        List<CompanyTermProperty> preCompanyTermPropertyList = internetPropertyManager.listCompanyTermProperty(preCompanyTerm);
        Map<String, List<CompanyTermProperty>> deptPropertyMap = internetPropertyManager.partCompanyTermPropertyByDept(preCompanyTermPropertyList);
        List<CampaignTermChoice> officeChoiceList = choiceManager.listCompanyChoice(campaign.getId(), currentCampaignDate, EChoiceBaseType.OFFICE.name());
        List<CampaignTermChoice> humanList = choiceManager.listCompanyChoice(campaign.getId(), currentCampaignDate, EChoiceBaseType.HUMAN.name());
        List<CampaignTermChoice> marketActivityChoiceList = choiceManager.listCompanyChoice(campaign.getId(), currentCampaignDate, EChoiceBaseType.MARKET_ACTIVITY.name());
        List<CampaignTermChoice> productStudyList = choiceManager.listCompanyChoice(campaign.getId(), currentCampaignDate, EChoiceBaseType.PRODUCT_STUDY.name());
        List<CampaignTermChoice> productStudyFeeList = choiceManager.listCompanyChoice(campaign.getId(), currentCampaignDate, EChoiceBaseType.PRODUCT_STUDY_FEE.name());
        List<CampaignTermChoice> operationChoiceList = choiceManager.listCompanyChoice(campaign.getId(), currentCampaignDate, EChoiceBaseType.OPERATION.name());
        Integer companyCash = accountManager.getCompanyCash(company);
        Integer campaignDateInCash = accountManager.countAccountEntryFee(
                company, preCompanyTerm.getCampaignDate(), EAccountEntityType.COMPANY_CASH.name(), "1");
        Integer campaignDateOutCash = accountManager.countAccountEntryFee(
                company, preCompanyTerm.getCampaignDate(), EAccountEntityType.COMPANY_CASH.name(), "-1");
        List<CompanyTermInstruction> hrInstructionList = instructionManager.listCompanyInstruction(company, EChoiceBaseType.HUMAN.name());

        CompanyTermInstruction preProductStudyInstruction = preCompanyTerm == null ? null : instructionManager.getUniqueInstructionByBaseType(preCompanyTerm, EChoiceBaseType.PRODUCT_STUDY.name());
        CompanyTermInstruction preOfficeInstruction = preCompanyTerm == null ? null : instructionManager.getUniqueInstructionByBaseType(preCompanyTerm, EChoiceBaseType.OFFICE.name());
        model.addAttribute("company", company);
        model.addAttribute("campaign", campaign);
        model.addAttribute("deptPropertyMap", deptPropertyMap);
        model.addAttribute("officeChoiceList", officeChoiceList);
        model.addAttribute("humanList", humanList);
        model.addAttribute("marketActivityChoiceList", marketActivityChoiceList);
        model.addAttribute("productStudyList", productStudyList);
        model.addAttribute("productStudyFeeList", productStudyFeeList);
        model.addAttribute("operationChoiceList", operationChoiceList);
        model.addAttribute("companyCash", companyCash);
        model.addAttribute("campaignDateInCash", campaignDateInCash);
        model.addAttribute("campaignDateOutCash", campaignDateOutCash);
        model.addAttribute("hrInstructionList", hrInstructionList);
        model.addAttribute("preProductStudyInstruction", preProductStudyInstruction);
        model.addAttribute("preOfficeInstruction", preOfficeInstruction);
        model.addAttribute("preCampaignDate", preCampaignDate);
        model.addAttribute("companyNum", CampaignCenter.getCampaignHandler(campaign.getId()).getCompanyTermContextMap().size());

        //公司的竞争报告
        Map<String, Map<String, String>> competitionMap = new HashMap<>();
        List<Company> companyList = campaignManager.listCompany(campaign);
        for (Company c : companyList) {
            CompanyTerm pct = companyTermManager.getCompanyTerm(c, preCampaignDate);
            Map<String,String> map = internetPropertyManager.getCompanyTermReport(pct);
            competitionMap.put(c.getName(), map);
        }
        model.addAttribute("competitionMap", competitionMap);

        //调试报告
        Map<String, Map<String, Integer>> propertyReport = internetPropertyManager.getPropertyReport(company);
        Map<String, Map<String, String>> accountReport = accountManager.getAccountReport(company);
        model.addAttribute("propertyReport", propertyReport);
        model.addAttribute("accountReport", accountReport);

        if (!company.getStatus().equals(ECompanyStatus.NORMAL.name())) {
            model.addAttribute("result", ECompanyStatus.valueOf(company.getStatus()).getLabel());
            return "/internet/result";
        }
        return "/internet/main";
    }

    @RequestMapping("/makeInstruction")
    @ResponseBody
    public String makeInstruction(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        String choiceId = request.getParameter("choiceId");
        String value = request.getParameter("value");
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);
        instructionManager.saveOrUpdateInstructionByChoice(company, choiceId, value);
        return "success";
    }

    @RequestMapping("/makeUniqueInstruction")
    @ResponseBody
    public String makeUniqueInstruction(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        String choiceId = request.getParameter("choiceId");
        String value = request.getParameter("value");
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);
        CampaignTermChoice campaignTermChoice = (CampaignTermChoice) baseManager.getObject(CampaignTermChoice.class.getName(), choiceId);
        CompanyTerm companyTerm = companyTermManager.getCompanyTerm(company, company.getCampaign().getCurrentCampaignDate());
        CompanyTermInstruction companyTermInstruction = null;
        try {
            companyTermInstruction = instructionManager.getUniqueInstructionByBaseType(companyTerm, campaignTermChoice.getBaseType());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (companyTermInstruction == null) {
            instructionManager.saveOrUpdateInstructionByChoice(company, choiceId, value);
        } else {
            companyTermInstruction.setCampaignTermChoice(campaignTermChoice);
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

    @RequestMapping("/fire")
    @ResponseBody
    public String fire(HttpServletRequest request, Model model) throws Exception {
        String instructionId = request.getParameter("instructionId");
        instructionManager.fireHuman(instructionId);
        return "success";
    }
}
