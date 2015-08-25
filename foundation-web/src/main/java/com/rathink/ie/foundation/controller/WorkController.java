package com.rathink.ie.foundation.controller;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.campaign.model.Campaign;
import com.rathink.ie.internet.Edept;
import com.rathink.ie.internet.choice.model.Human;
import com.rathink.ie.internet.instruction.model.HrInstruction;
import com.rathink.ie.team.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Hean on 2015/8/24.
 */
@Controller
@RequestMapping("/work")
public class WorkController {
    @Autowired
    private BaseManager baseManager;

    @RequestMapping("/hrChoices")
    public String hrChoices(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), company.getCampaign().getId());
        String campaignDate = campaign.getCurrentCampaignDate();

        XQuery xQuery = new XQuery();
        xQuery.setHql("from Human where campaign.id = :campaignId and campaignDate = :campaignDate");
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<String, Object>();
        queryParamMap.put("campaignId", campaign.getId());
        queryParamMap.put("campaignDate", campaignDate);
        xQuery.setQueryParamMap(queryParamMap);
        List humanList = baseManager.listObject(xQuery);
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
                    hrInstruction.setDept(Edept.HR.name());
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
