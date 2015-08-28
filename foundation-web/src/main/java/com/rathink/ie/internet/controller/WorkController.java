package com.rathink.ie.internet.controller;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.internet.service.ChoiceService;
import com.rathink.ie.ibase.service.CompanyStatusService;
import com.rathink.ie.internet.service.WorkService;
import com.rathink.ie.ibase.property.model.CompanyStatus;
import com.rathink.ie.ibase.property.model.CompanyStatusPropertyValue;
import com.rathink.ie.internet.choice.model.Human;
import com.rathink.ie.internet.instruction.model.HrInstruction;
import com.rathink.ie.foundation.team.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private WorkService workService;
    @Autowired
    private CompanyStatusService companyStatusService;
    @Autowired
    private ChoiceService choiceService;


    @RequestMapping("/main")
    public String main(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), company.getCampaign().getId());
        CompanyStatus companyStatus = companyStatusService.getCompanyStatus(company, campaign.getCurrentCampaignDate());
        Map<String, List<CompanyStatusPropertyValue>> deptPropertyMap = workService.partCompanyStatusPropertyByDept(companyStatus.getCompanyStatusPropertyValueList());
        List<Human> humanList = choiceService.listHuman(campaign);

        model.addAttribute("company", company);
        model.addAttribute("campaign", campaign);
        model.addAttribute("deptPropertyMap", deptPropertyMap);
        model.addAttribute("humanList", humanList);
        return "/internet/main";
    }

    @RequestMapping("/begin")
    @ResponseBody
    public String begin(HttpServletRequest request, Model model) throws Exception {
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), request.getParameter("campaignId"));
        workService.initCampaign(campaign);
        return "success";
    }

    @RequestMapping("/next")
    @ResponseBody
    public String next(HttpServletRequest request, Model model) throws Exception {
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), request.getParameter("campaignId"));
        workService.nextCampaign(campaign);
        return "success";
    }

    @RequestMapping("/pre")
    @ResponseBody
    public String pre(HttpServletRequest request, Model model) throws Exception {
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), request.getParameter("campaignId"));
        workService.preCampaign(campaign);
        return "success";
    }

    @RequestMapping("/hrChoices")
    public String hrChoices(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), company.getCampaign().getId());
        List<Human> humanList = choiceService.listHuman(campaign);
        model.addAttribute("humanList", humanList);
        model.addAttribute("company", company);
        return "/internet/hrPanel";
    }

    @RequestMapping("/makeInstruction")
    public String makeInstruction(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        String[] humanIdArray = request.getParameterValues("humanId");
        String[] humanSalaryArray = request.getParameterValues("humanSalary");
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), company.getCampaign().getId());
        if (humanSalaryArray != null) {
            for (int i = 0; i < humanSalaryArray.length; i++) {
                String humanSalary = humanSalaryArray[i];
                if (!"-1".equals(humanSalary)) {
                    Human human = (Human) baseManager.getObject(Human.class.getName(), humanIdArray[i]);
                    HrInstruction hrInstruction = new HrInstruction();
                    hrInstruction.setCampaignDate(campaign.getCurrentCampaignDate());
                    hrInstruction.setCampaign(campaign);
                    hrInstruction.setCompany(company);
                    hrInstruction.setDept(human.getDept());
                    hrInstruction.setHuman(human);
                    hrInstruction.setStatus(HrInstruction.Status.DQD.getValue());
                    hrInstruction.setFee(humanSalary);
                    baseManager.saveOrUpdate(HrInstruction.class.getName(), hrInstruction);
                }
            }
        }
        return "redirect:/campaign/main?companyId=" + companyId;
    }
}
