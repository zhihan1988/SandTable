package com.rathink.iix.ibase.component;

import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.iix.manufacturing.message.NextTermMessage;
import com.rathink.iix.manufacturing.message.ShowUnFinishNumMessage;
import com.rathink.iix.manufacturing.service.ManufacturingService;

/**
 * Created by Hean on 2016/2/2.
 */
public class TermParty extends CampaignParty<String> {
    public static String TYPE = "TERM";
    public TermParty(MemoryCampaign memoryCampaign) {
        super(memoryCampaign , TYPE);
    }

    @Override
    public synchronized void join(String o) {
        joinedSet.add(o);
        memoryCampaign.broadcast(new ShowUnFinishNumMessage(getUnFinishedNum()));
        if (isAll()) {
            iNotify();
            memoryCampaign.broadcast(new NextTermMessage(true));
        }
    }

    @Override
    public void iNotify() {
        String type = memoryCampaign.getCampaign().getIndustry().getType();
        ManufacturingService manufacturingService = (ManufacturingService) ApplicationContextUtil.getBean(type + "Service");
        manufacturingService.next(memoryCampaign.getCampaign().getId());
    }
}
