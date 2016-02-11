package com.rathink.iix.ibase.controller;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.service.CampaignManager;
import com.rathink.iix.ibase.component.*;
import com.rathink.iix.manufacturing.message.IMessage;
import com.rathink.ix.ibase.component.Result;
import com.rathink.ix.ibase.service.*;
import com.rathink.ix.internet.service.InternetWorkManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hean on 2016/2/2.
 */
@Controller
@RequestMapping("/iBase")
public class IBaseController {

    @Autowired
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
    protected CompanyPartManager companyPartManager;

    @RequestMapping("/companyNext")
    @ResponseBody
    public Result companyNext(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        String partyType = request.getParameter("partyType");
        String companyId = request.getParameter("companyId");

        TermParty termParty = (TermParty)CampaignServer.getMemoryCampaign(campaignId).getCampaignPartyMap().get(partyType);
        termParty.join(companyId);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        return result;
    }

    @RequestMapping("/isCampaignNext")
    @ResponseBody
    public Result isNext(HttpServletRequest request, Model model) throws Exception {

        String campaignId = request.getParameter("campaignId");
        String partyType = request.getParameter("partyType");
        String clientCampaignDate = request.getParameter("campaignDate");

        MemoryCampaign memoryCampaign = CampaignServer.getMemoryCampaign(campaignId);
        TermParty termParty = (TermParty)memoryCampaign.getCampaignPartyMap().get(partyType);
        Integer serverCampaignDate = memoryCampaign.getCampaign().getCurrentCampaignDate();

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        if (Integer.parseInt(clientCampaignDate) == serverCampaignDate) {
            result.addAttribute("isNext", false);
            result.addAttribute("unFinishedNum", termParty.getUnFinishedNum());
            result.addAttribute("schedule", null);
        } else {
            result.addAttribute("isNext", true);
        }
        return result;
    }

    @RequestMapping("/listen")
    @ResponseBody
    public Result listen(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        MemoryCompany memoryCompany = CampaignServer.getMemoryCampaign(campaignId).getMemoryCompany(companyId);

        List<IMessage> messages = new ArrayList<>();
        while (memoryCompany.peekMessage() != null) {
            messages.add(memoryCompany.pollMessage());
        }

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.addAttribute("messages", messages);
        return result;
    }

}
