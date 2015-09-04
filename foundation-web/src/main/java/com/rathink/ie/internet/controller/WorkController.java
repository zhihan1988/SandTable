package com.rathink.ie.internet.controller;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.service.AccountManager;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.choice.model.*;
import com.rathink.ie.internet.instruction.model.OfficeInstruction;
import com.rathink.ie.internet.service.ChoiceManager;
import com.rathink.ie.ibase.service.CompanyStatusManager;
import com.rathink.ie.internet.service.InstructionManager;
import com.rathink.ie.internet.service.WorkManager;
import com.rathink.ie.ibase.property.model.CompanyStatus;
import com.rathink.ie.ibase.property.model.CompanyStatusPropertyValue;
import com.rathink.ie.internet.instruction.model.HrInstruction;
import com.rathink.ie.foundation.team.model.Company;
import org.apache.log4j.Logger;
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
    private static Logger logger = Logger.getLogger(WorkController.class.getName());
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private WorkManager workManager;
    @Autowired
    private CompanyStatusManager companyStatusManager;
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
        CompanyStatus companyStatus = companyStatusManager.getCompanyStatus(company, campaign.getCurrentCampaignDate());
        Map<String, List<CompanyStatusPropertyValue>> deptPropertyMap = workManager.partCompanyStatusPropertyByDept(companyStatus.getCompanyStatusPropertyValueList());
        List<OfficeChoice> officeChoiceList = choiceManager.listOfficeChoice(campaign);
        List<Human> humanList = choiceManager.listHuman(campaign);
        List<MarketActivityChoice> marketActivityChoiceList = choiceManager.listMarketActivityChoice(campaign);
        List<ProductStudy> productStudyList = choiceManager.listProductStudy(campaign);
        List<OperationChoice> operationChoiceList = choiceManager.listOperationChoice(campaign);
        Integer companyCash = accountManager.getCompanyCash(company);
        Integer campaignDateInCash = accountManager.countAccountEntryFee(
                company, campaign.getCurrentCampaignDate(), EAccountEntityType.COMPANY_CASH.name(), "1");
        Integer campaignDateOutCash = accountManager.countAccountEntryFee(
                company, campaign.getCurrentCampaignDate(), EAccountEntityType.COMPANY_CASH.name(), "-1");
        List<HrInstruction> hrInstructionList = instructionManager.listHrInstruction(company);
        List<OfficeInstruction> officeInstructionList = instructionManager.listOfficeInstruction(company);
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
        String entity = request.getParameter("entity");
        String fields = request.getParameter("fields");
        Map<String, String> fieldMap = new HashMap<>();
        for (String field : fields.split(";")) {
            fieldMap.put(field.split("=")[0], field.split("=")[1]);
        }
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);

        switch (entity) {
            case "officeInstruction":
                OfficeChoice officeChoice = (OfficeChoice) baseManager.getObject(OfficeChoice.class.getName(), choiceId);
                instructionManager.saveOrUpdateOfficeInstruction(company, officeChoice, fieldMap);
                break;
            case "hrInstruction":
                Human human = (Human) baseManager.getObject(Human.class.getName(), choiceId);
                instructionManager.saveOrUpdateHrInstruction(company, human, fieldMap);
                break;
            case "marketInstruction":
                MarketActivityChoice marketActivityChoice = (MarketActivityChoice) baseManager.getObject(MarketActivityChoice.class.getName(), choiceId);
                instructionManager.saveOrUpdateMarketInstruction(company, marketActivityChoice, fieldMap);
                break;
            case "productStudyInstruction":
                ProductStudy productStudy = (ProductStudy) baseManager.getObject(ProductStudy.class.getName(), choiceId);
                instructionManager.saveOrUpdateProductStudyInstruction(company, productStudy, fieldMap);
                break;
            case "operationInstruction":
                OperationChoice operationChoice = (OperationChoice) baseManager.getObject(OperationChoice.class.getName(), choiceId);
                instructionManager.saveOrUpdateOperationInstruction(company, operationChoice, fieldMap);
                break;
        }

        return "success";
    }
}
