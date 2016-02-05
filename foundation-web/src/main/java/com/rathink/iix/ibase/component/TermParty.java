package com.rathink.iix.ibase.component;

import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.iix.manufacturing.service.ManufacturingService;

/**
 * Created by Hean on 2016/2/2.
 */
public class TermParty extends CampaignParty<String> {
    public TermParty(MemoryCampaign memoryCampaign) {
        super(memoryCampaign ,"TERM");
    }

    public Integer getUnFinishedNum() {
        Integer allNum = memoryCampaign.getMemoryCompanyMap().size();
        Integer joinedNum = joinedSet.size();
        return allNum - joinedNum;
    }

    @Override
    public void iNotify() {
        String type = memoryCampaign.getCampaign().getIndustry().getType();
        ManufacturingService manufacturingService = (ManufacturingService) ApplicationContextUtil.getBean(type + "Service");
        manufacturingService.next(memoryCampaign.getCampaign().getId());
    }
}
