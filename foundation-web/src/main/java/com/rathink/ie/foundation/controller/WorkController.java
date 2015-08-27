package com.rathink.ie.foundation.controller;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.ChoiceService;
import com.rathink.ie.internet.choice.model.Human;
import com.rathink.ie.internet.instruction.model.HrInstruction;
import com.rathink.ie.foundation.team.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Hean on 2015/8/24.
 */
@Controller
@RequestMapping("/work")
public class WorkController {
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private ChoiceService choiceService;

    @RequestMapping("/hrChoices")
    public String hrChoices(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), company.getCampaign().getId());
        List<Human> humanList = choiceService.listHuman(campaign);
        model.addAttribute("humanList", humanList);
        model.addAttribute("company", company);
        return "/work/hrPanel";
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
                    hrInstruction.setSalary(humanSalary);
                    baseManager.saveOrUpdate(HrInstruction.class.getName(), hrInstruction);
                }
            }
        }
        return "redirect:/campaign/main?companyId=" + companyId;
    }
}
