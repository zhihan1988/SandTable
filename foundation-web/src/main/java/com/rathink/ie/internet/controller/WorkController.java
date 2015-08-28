package com.rathink.ie.internet.controller;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.internet.choice.model.MarketActivityChoice;
import com.rathink.ie.internet.service.ChoiceService;
import com.rathink.ie.ibase.service.CompanyStatusService;
import com.rathink.ie.internet.service.InstructionService;
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
    @Autowired
    private InstructionService instructionService;

    @RequestMapping("/main")
    public String main(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), company.getCampaign().getId());
        CompanyStatus companyStatus = companyStatusService.getCompanyStatus(company, campaign.getCurrentCampaignDate());
        Map<String, List<CompanyStatusPropertyValue>> deptPropertyMap = workService.partCompanyStatusPropertyByDept(companyStatus.getCompanyStatusPropertyValueList());
        List<Human> humanList = choiceService.listHuman(campaign);
        List<MarketActivityChoice> marketActivityChoiceList = choiceService.listMarketActivityChoice(campaign);
        model.addAttribute("company", company);
        model.addAttribute("campaign", campaign);
        model.addAttribute("deptPropertyMap", deptPropertyMap);
        model.addAttribute("humanList", humanList);
        model.addAttribute("marketActivityChoiceList", marketActivityChoiceList);
        return "/internet/main";
    }

    @RequestMapping("/makeInstruction")
    @ResponseBody
    public String makeInstruction(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        String entity = request.getParameter("entity");
        String id = request.getParameter("id");
        String value = request.getParameter("value");
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);

        switch (entity) {
            case "HrInstruction":
                Human human = (Human) baseManager.getObject(HrInstruction.class.getName(), id);
                System.out.println("保存"+value);
//                instructionService.saveOrUpdateHrInstruction(company, human, value);
        }

        return "success";
    }
}