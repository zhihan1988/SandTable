package com.rathink.ie.internet.controller;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.service.AccountService;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.choice.model.MarketActivityChoice;
import com.rathink.ie.internet.choice.model.OperationChoice;
import com.rathink.ie.internet.choice.model.ProductStudy;
import com.rathink.ie.internet.instruction.model.OperationInstruction;
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
import java.util.HashMap;
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
    @Autowired
    private AccountService accountService;

    @RequestMapping("/main")
    public String main(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), company.getCampaign().getId());
        CompanyStatus companyStatus = companyStatusService.getCompanyStatus(company, campaign.getCurrentCampaignDate());
        Map<String, List<CompanyStatusPropertyValue>> deptPropertyMap = workService.partCompanyStatusPropertyByDept(companyStatus.getCompanyStatusPropertyValueList());
        List<Human> humanList = choiceService.listHuman(campaign);
        List<MarketActivityChoice> marketActivityChoiceList = choiceService.listMarketActivityChoice(campaign);
        List<ProductStudy> productStudyList = choiceService.listProductStudy(campaign);
        List<OperationChoice> operationChoiceList = choiceService.listOperationChoice(campaign);
        Integer companyCash = accountService.getCompanyCash(company);
        Integer campaignDateInCash = accountService.countAccountEntryFee(
                company, campaign.getCurrentCampaignDate(), EAccountEntityType.COMPANY_CASH.name(), "1");
        Integer campaignDateOutCash = accountService.countAccountEntryFee(
                company, campaign.getCurrentCampaignDate(), EAccountEntityType.COMPANY_CASH.name(), "-1");
        List<HrInstruction> hrInstructionList = instructionService.listHrInstruction(company);
        model.addAttribute("company", company);
        model.addAttribute("campaign", campaign);
        model.addAttribute("deptPropertyMap", deptPropertyMap);
        model.addAttribute("humanList", humanList);
        model.addAttribute("marketActivityChoiceList", marketActivityChoiceList);
        model.addAttribute("productStudyList", productStudyList);
        model.addAttribute("operationChoiceList", operationChoiceList);
        model.addAttribute("companyCash", companyCash);
        model.addAttribute("campaignDateInCash", campaignDateInCash);
        model.addAttribute("campaignDateOutCash", campaignDateOutCash);
        model.addAttribute("hrInstructionList", hrInstructionList);
        return "/internet/main";
    }

    @RequestMapping("/makeInstruction")
    @ResponseBody
    public String makeInstruction(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        String choiceId = request.getParameter("choiceId");
        String entity = request.getParameter("entity");
        String fields = request.getParameter("fields");
        Map<String, String> fieldMap = new HashMap<>();
        for (String field : fields.split(";")) {
            fieldMap.put(field.split("=")[0], field.split("=")[1]);
        }
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);

        switch (entity) {
            case "hrInstruction":
                Human human = (Human) baseManager.getObject(Human.class.getName(), choiceId);
                instructionService.saveOrUpdateHrInstruction(company, human, fieldMap);
                break;
            case "marketInstruction":
                MarketActivityChoice marketActivityChoice = (MarketActivityChoice) baseManager.getObject(MarketActivityChoice.class.getName(), choiceId);
                instructionService.saveOrUpdateMarketInstruction(company, marketActivityChoice, fieldMap);
                break;
            case "productStudyInstruction":
                ProductStudy productStudy = (ProductStudy) baseManager.getObject(ProductStudy.class.getName(), choiceId);
                instructionService.saveOrUpdateProductStudyInstruction(company, productStudy, fieldMap);
                break;
            case "operationInstruction":
                OperationChoice operationChoice = (OperationChoice) baseManager.getObject(OperationChoice.class.getName(), choiceId);
                instructionService.saveOrUpdateOperationInstruction(company, operationChoice, fieldMap);
                break;
        }

        return "success";
    }
}
