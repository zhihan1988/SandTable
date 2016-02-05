package com.rathink.iix.manufacturing.component;

import com.rathink.iix.ibase.component.CampaignParty;
import com.rathink.iix.ibase.component.MemoryCampaign;
import com.rathink.iix.ibase.component.MemoryCompany;
import com.rathink.ix.manufacturing.model.Market;
import com.rathink.ix.manufacturing.model.MarketOrder;

import java.util.List;
import java.util.Queue;

/**
 * Created by Hean on 2016/2/2.
 */
public class MarketOrderParty extends CampaignParty<String> {
    private List companyOrderList;
    private List<MarketOrder> marketOrderList;
    private Queue<String> marketQueue;

    public MarketOrderParty(MemoryCampaign memoryCampaign,String market) {
        super(memoryCampaign, market);

        //根据market查找MarketOrder
    }

    public void chooseOrder(String companyId, MarketOrder marketOrder) {
        super.join(companyId);
        ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) memoryCampaign.getMemoryCompany(companyId);
        memoryCompany.getMarketOrderMap().put(marketOrder.getId(), marketOrder);
    }

    public void giveUp(String companyId) {
        super.join(companyId);
    }

    @Override
    public void iNotify() {
        //进入下一周期
        marketQueue.poll();

        marketOrderList = null;//new marketOrderList

    }
}
