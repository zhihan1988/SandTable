package com.rathink.ix.ibase.component;

import com.rathink.iix.ibase.component.CampaignServer;
import com.rathink.iix.ibase.component.MemoryCompany;
import com.rathink.iix.manufacturing.component.ManufacturingMemoryCompany;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Hean on 2015/10/31.
 */
@Aspect
@Component
public class CheckOutAspect {

    @Before("@annotation(com.rathink.ix.ibase.component.CheckOut)")
    public void checkOut2(JoinPoint point) {
        HttpServletRequest request = (HttpServletRequest) point.getArgs()[0];
        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        MemoryCompany memoryCompany = CampaignServer.getMemoryCampaign(campaignId).getMemoryCompany(companyId);
        if (memoryCompany instanceof ManufacturingMemoryCompany) {
            Integer companyCash = ((ManufacturingMemoryCompany) memoryCompany).getCompanyCash();
            if (companyCash < 0) {

            }
        }
    }
}
