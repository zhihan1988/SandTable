package com.rathink.ie.manufacturing.controller;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.*;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryResource;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.internet.ERoundType;
import com.rathink.ie.internet.service.FlowManager;
import com.rathink.ie.internet.service.InternetWorkManager;
import com.rathink.ie.manufacturing.EManufacturingChoiceBaseType;
import com.rathink.ie.manufacturing.EManufacturingRoundType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Hean on 2015/10/7.
 */
@Controller
@RequestMapping("/manufacturing")
public class ManufacturingController {
    private static Logger logger = LoggerFactory.getLogger(ManufacturingController.class);
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private CompanyTermManager companyTermManager;
    @Autowired
    private InstructionManager instructionManager;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private PropertyManager propertyManager;
    @Autowired
    private InternetWorkManager internetWorkManager;

    @RequestMapping("/main")
    public String main(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        CampaignContext campaignContext = CampaignCenter.getCampaignHandler(campaignId);
        Campaign campaign = campaignContext.getCampaign();
        CompanyTermContext companyTermContext = campaignContext.getCompanyTermContextMap().get(companyId);
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
        Company company = companyTerm.getCompany();
        CompanyTerm preCompanyTerm = companyTermContext.getPreCompanyTermContext().getCompanyTerm();
        Integer currentCampaignDate = campaign.getCurrentCampaignDate();
        Map<String, IndustryResource> industryResourceMap = campaignContext.getCurrentTypeIndustryResourceMap();

        model.addAttribute("company", company);
        model.addAttribute("campaign", campaign);
        model.addAttribute("companyTerm", companyTerm);
        model.addAttribute("industryResourceMap", industryResourceMap);
        model.addAttribute("companyNum", campaignContext.getCompanyTermContextMap().size());
        if (currentCampaignDate % 4 == 1) {
            //进入抢订单环节
            model.addAttribute("marketFeeResource", industryResourceMap.get(EManufacturingChoiceBaseType.MARKET_FEE.name()));
            model.addAttribute("roundType", EManufacturingRoundType.MARKET_PAY_ROUND.name());
            return "/manufacturing/marketPay";
        }
        model.addAttribute("produceLineResource", industryResourceMap.get(EManufacturingChoiceBaseType.PRODUCE_LINE.name()));
        model.addAttribute("roundType", EManufacturingRoundType.DATE_ROUND.name());
        return "/manufacturing/main";
    }

    /*@RequestMapping("/confirmMarketPay")
    @ResponseBody
    public Boolean confirmMarketPay(HttpServletRequest request, Model model) throws Exception {
        String companyTermId = request.getParameter("companyTermId");
        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        CompanyTermInstruction companyTermInstruction = instructionManager.getUniqueInstructionByBaseType(companyTerm, EManufacturingChoiceBaseType.MARKET_FEE.name());
        companyTermInstruction.setStatus(EInstructionStatus.YXZ.getValue());
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);

        return true;
    }

    @RequestMapping("/isAllConfirmedMarketPay")
    @ResponseBody
    public synchronized Integer isAllConfirmedMarketPay(HttpServletRequest request, Model model) throws Exception {
        String companyTermId = request.getParameter("companyTermId");
        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        List<CompanyTermInstruction> marketFeeInstructionList = instructionManager.listCompanyInstruction(companyTerm.getCampaign()
                , companyTerm.getCampaignDate(), EManufacturingChoiceBaseType.MARKET_FEE.name());
        CampaignContext campaignContext = CampaignCenter.getCampaignHandler(companyTerm.getCampaign().getId());
        Integer unConfirmedNum = campaignContext.getCompanyTermContextMap().size();
        if (marketFeeInstructionList != null) {
            unConfirmedNum = unConfirmedNum - marketFeeInstructionList.size();
        }
        return unConfirmedNum;
    }
*/
    @RequestMapping("/order")
    public String order(HttpServletRequest request, Model model) throws Exception {
        String companyTermId = request.getParameter("companyTermId");
        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        List<CompanyTermInstruction> marketFeeInstructionList = instructionManager.listCompanyInstruction(companyTerm.getCampaign()
                , companyTerm.getCampaignDate(), EManufacturingChoiceBaseType.MARKET_FEE.name());
        marketFeeInstructionList.sort((o1, o2) -> Integer.valueOf(o1.getValue()) - Integer.valueOf(o2.getValue()));
        model.addAttribute("marketFeeInstructionList", marketFeeInstructionList);

        CampaignContext campaignContext = CampaignCenter.getCampaignHandler(companyTerm.getCampaign().getId());
        Map<String, IndustryResource> industryResourceMap = campaignContext.getCurrentTypeIndustryResourceMap();
        model.addAttribute("marketOrderResource", industryResourceMap.get(EManufacturingChoiceBaseType.MARKET_ORDER.name()));
        model.addAttribute("company", companyTerm.getCompany());
        model.addAttribute("campaign", companyTerm.getCampaign());
        model.addAttribute("companyTerm", companyTerm);
        model.addAttribute("companyNum", campaignContext.getCompanyTermContextMap().size());
        model.addAttribute("roundType", EManufacturingRoundType.ORDER_ROUND.name());
        return "/manufacturing/order";
    }

}
