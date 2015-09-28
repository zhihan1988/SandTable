package com.rathink.ie.internet.controller;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.team.model.ECompanyStatus;
import com.rathink.ie.foundation.util.CampaignUtil;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
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
        String currentCampaignDate = campaign.getCurrentCampaignDate();
        String preCampaignDate = CampaignUtil.getPreCampaignDate(currentCampaignDate);
        CompanyTerm preCompanyTerm = companyTermManager.getCompanyTerm(company, preCampaignDate);
        List<CompanyTermProperty> preCompanyTermPropertyList = internetPropertyManager.listCompanyTermProperty(preCompanyTerm);
        Map<String, List<CompanyTermProperty>> deptPropertyMap = internetPropertyManager.partCompanyTermPropertyByDept(preCompanyTermPropertyList);
        List<CompanyChoice> officeChoiceList = choiceManager.listCompanyChoice(campaign.getId(), currentCampaignDate, EChoiceBaseType.OFFICE.name());
        List<CompanyChoice> humanList = choiceManager.listCompanyChoice(campaign.getId(), currentCampaignDate, EChoiceBaseType.HUMAN.name());
        List<CompanyChoice> marketActivityChoiceList = choiceManager.listCompanyChoice(campaign.getId(), currentCampaignDate, EChoiceBaseType.MARKET_ACTIVITY.name());
        List<CompanyChoice> productStudyList = choiceManager.listCompanyChoice(campaign.getId(), currentCampaignDate, EChoiceBaseType.PRODUCT_STUDY.name());
        List<CompanyChoice> productStudyFeeList = choiceManager.listCompanyChoice(campaign.getId(), currentCampaignDate, EChoiceBaseType.PRODUCT_STUDY_FEE.name());
        List<CompanyChoice> operationChoiceList = choiceManager.listCompanyChoice(campaign.getId(), currentCampaignDate, EChoiceBaseType.OPERATION.name());
        Integer companyCash = accountManager.getCompanyCash(company);
        Integer campaignDateInCash = accountManager.countAccountEntryFee(
                company, preCompanyTerm.getCampaignDate(), EAccountEntityType.COMPANY_CASH.name(), "1");
        Integer campaignDateOutCash = accountManager.countAccountEntryFee(
                company, preCompanyTerm.getCampaignDate(), EAccountEntityType.COMPANY_CASH.name(), "-1");
        List<CompanyInstruction> hrInstructionList = instructionManager.listCompanyInstructionByDept(company, Edept.HR.name());

        CompanyInstruction preProductStudyInstruction = preCompanyTerm == null ? null : instructionManager.getUniqueInstructionByBaseType(preCompanyTerm, EChoiceBaseType.PRODUCT_STUDY.name());
        CompanyInstruction preOfficeInstruction = preCompanyTerm == null ? null : instructionManager.getUniqueInstructionByBaseType(preCompanyTerm, EChoiceBaseType.OFFICE.name());
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

        //产品定位冲突报告
        Map<String, Integer> productStudyCompetitionReport = new LinkedHashMap();
        List<CompanyChoice> preProductStudyList = choiceManager.listCompanyChoice(campaign.getId(), preCampaignDate, EChoiceBaseType.PRODUCT_STUDY.name());
        if (preProductStudyList != null) {
            for (CompanyChoice productStudy : preProductStudyList) {
                Integer count = instructionManager.countCompanyInstruction(productStudy);
                productStudyCompetitionReport.put(productStudy.getName(), count);
            }
        }
        model.addAttribute("productStudyCompetitionReport", productStudyCompetitionReport);
        //市场活动冲突报告
        Map<String, Integer> marketCompetitionReport = new LinkedHashMap();
        List<CompanyChoice> preMarketActivityList = choiceManager.listCompanyChoice(campaign.getId(), preCampaignDate, EChoiceBaseType.MARKET_ACTIVITY.name());
        if (preMarketActivityList != null) {
            for (CompanyChoice marketChoice : preMarketActivityList) {
                Integer count = instructionManager.countCompanyInstruction(marketChoice);
                marketCompetitionReport.put(marketChoice.getName(), count);
            }
        }
        model.addAttribute("marketCompetitionReport", marketCompetitionReport);

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
        CompanyChoice companyChoice = (CompanyChoice) baseManager.getObject(CompanyChoice.class.getName(), choiceId);
        CompanyTerm companyTerm = companyTermManager.getCompanyTerm(company, company.getCampaign().getCurrentCampaignDate());
        CompanyInstruction companyInstruction = instructionManager.getUniqueInstructionByBaseType(companyTerm, companyChoice.getBaseType());
        if (companyInstruction == null) {
            instructionManager.saveOrUpdateInstructionByChoice(company, choiceId, value);
        } else {
            companyInstruction.setCompanyChoice(companyChoice);
            companyInstruction.setValue(value);
            baseManager.saveOrUpdate(CompanyInstruction.class.getName(), companyInstruction);
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
