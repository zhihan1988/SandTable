package com.rathink.ix.internet.controller;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.team.model.ECompanyStatus;
import com.rathink.ix.ibase.controller.BaseIndustryController;
import com.rathink.ix.ibase.property.model.CompanyTerm;
import com.rathink.ix.ibase.property.model.CompanyTermProperty;
import com.rathink.ix.ibase.service.CampaignCenter;
import com.rathink.ix.ibase.service.CompanyTermContext;
import com.rathink.ix.ibase.work.model.CompanyTermInstruction;
import com.rathink.ix.ibase.work.model.IndustryResource;
import com.rathink.ix.internet.EAccountEntityType;
import com.rathink.ix.internet.EChoiceBaseType;
import com.rathink.ix.internet.EPropertyName;
import com.rathink.ix.internet.ERoundType;
import com.rathink.ix.internet.component.InternetCampContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/8/24.
 */
@Controller
@RequestMapping("/internet")
public class InternetController extends BaseIndustryController {
    private static Logger logger = LoggerFactory.getLogger(InternetController.class);

/*    @Autowired
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
    protected CompanyPartManager companyPartManager;*/

    @RequestMapping("/main")
    public String main(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        InternetCampContext campaignContext = (InternetCampContext) CampaignCenter.getCampaignHandler(campaignId);
        Campaign campaign = campaignContext.getCampaign();
        CompanyTermContext companyTermContext = campaignContext.getCompanyTermContextMap().get(companyId);
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
        Company company = companyTerm.getCompany();
        CompanyTerm preCompanyTerm = companyTermContext.getPreCompanyTermContext().getCompanyTerm();
//        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);
//        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), company.getCampaign().getId());
//        Integer currentCampaignDate = campaign.getCurrentCampaignDate();
        Integer preCampaignDate = campaign.getPreCampaignDate();
//        CompanyTerm preCompanyTerm = companyTermManager.getCompanyTerm(company.getId(), preCampaignDate);


        //上一期的属性数据
        List<CompanyTermProperty> preCompanyTermPropertyList = propertyManager.listCompanyTermProperty(preCompanyTerm);
        Map<String, List<CompanyTermProperty>> deptPropertyMap = propertyManager.partCompanyTermPropertyByDept(preCompanyTermPropertyList);

        Map<String, IndustryResource> industryResourceMap = campaignContext.getCurrentTypeIndustryResourceMap();

        Integer companyCash = accountManager.getCompanyCash(company);
        Integer campaignDateInCash = accountManager.countAccountEntryFee(
                company, preCompanyTerm.getCampaignDate(), EAccountEntityType.COMPANY_CASH.name(), "1");
        Integer campaignDateOutCash = accountManager.countAccountEntryFee(
                company, preCompanyTerm.getCampaignDate(), EAccountEntityType.COMPANY_CASH.name(), "-1");
        List<CompanyTermInstruction> hrInstructionList = instructionManager.listCompanyInstruction(company, EChoiceBaseType.HUMAN.name());

        CompanyTermInstruction preProductStudyInstruction = preCompanyTerm == null ? null : instructionManager.getUniqueInstructionByBaseType(preCompanyTerm, EChoiceBaseType.PRODUCT_STUDY.name());
        model.addAttribute("company", company);
        model.addAttribute("campaign", campaign);
        model.addAttribute("companyTerm", companyTerm);
        model.addAttribute("deptPropertyMap", deptPropertyMap);
        model.addAttribute("industryResourceMap", industryResourceMap);
        model.addAttribute("companyCash", companyCash);
        model.addAttribute("campaignDateInCash", campaignDateInCash);
        model.addAttribute("campaignDateOutCash", campaignDateOutCash);
        model.addAttribute("hrInstructionList", hrInstructionList);
        model.addAttribute("preProductStudyInstruction", preProductStudyInstruction);
        model.addAttribute("companyNum", campaignContext.getCompanyTermContextMap().size());
        model.addAttribute("humanResource", industryResourceMap.get(EChoiceBaseType.HUMAN.name()));
        model.addAttribute("productStudyResource", industryResourceMap.get(EChoiceBaseType.PRODUCT_STUDY.name()));
        model.addAttribute("productStudyFeeResource", industryResourceMap.get(EChoiceBaseType.PRODUCT_STUDY_FEE.name()));
        model.addAttribute("marketActivityResource", industryResourceMap.get(EChoiceBaseType.MARKET_ACTIVITY.name()));
        model.addAttribute("operationResource", industryResourceMap.get(EChoiceBaseType.OPERATION.name()));
        model.addAttribute("roundType", ERoundType.DATE_ROUND.name());

        //公司的竞争报告
        Map<String, Map<String, String>> competitionMap = new HashMap<>();
        List<Company> companyList = campaignManager.listCompany(campaign);
        for (Company c : companyList) {
            CompanyTerm pct = companyTermManager.getCompanyTerm(c.getId(), preCampaignDate);
            Map<String, String> map = internetWorkManager.getCompanyTermReport(pct);
            competitionMap.put(c.getName(), map);
        }
        model.addAttribute("competitionMap", competitionMap);

        //调试报告
        Map<String, Map<String, Integer>> propertyReport = propertyManager.getPropertyReport(company, new CompanyTermPropertyComparator());
        Map<String, Map<String, String>> accountReport = accountManager.getAccountReport(company);
        model.addAttribute("propertyReport", propertyReport);
        model.addAttribute("accountReport", accountReport);

        if (!company.getStatus().equals(ECompanyStatus.NORMAL.name())) {
            model.addAttribute("result", ECompanyStatus.valueOf(company.getStatus()).getLabel());
            return "/internet/result";
        }
        return "/internet/main";
    }

    @RequestMapping("/fire")
    @ResponseBody
    public String fire(HttpServletRequest request, Model model) throws Exception {
        String instructionId = request.getParameter("instructionId");
        internetWorkManager.fireHuman(instructionId);
        return "success";
    }

    class CompanyTermPropertyComparator implements Comparator<CompanyTermProperty> {
        @Override
        public int compare(CompanyTermProperty o1, CompanyTermProperty o2) {
            Integer order1 = EPropertyName.valueOf(o1.getName()).ordinal();
            Integer order2 = EPropertyName.valueOf(o2.getName()).ordinal();
            return order1 - order2;
        }
    }
}
