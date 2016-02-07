package com.rathink.iix.manufacturing.component;

import com.rathink.iix.ibase.component.CampaignParty;
import com.rathink.iix.ibase.component.MemoryCampaign;
import com.rathink.ix.manufacturing.model.Market;
import com.rathink.ix.manufacturing.model.Product;

import java.util.*;

/**
 * Created by Hean on 2016/2/2.
 */
public class MarketBiddingParty extends CampaignParty<String> {
    public static String TYPE = "BIDDING";
    public enum Status {
        DOING, DONE
    }
    private Map<String, List<CompanyBidding>> marketBiddingMap = new HashMap<>();

    public MarketBiddingParty(MemoryCampaign memoryCampaign) {
        super(memoryCampaign, TYPE);
        setStatus(Status.DOING.name());
    }

    public void addBidding(String companyId, String market, Integer fee) {
        CompanyBidding companyBidding = new CompanyBidding(companyId, fee);
        if (marketBiddingMap.containsKey(market)) {
            marketBiddingMap.get(market).add(companyBidding);
        } else {
            List<CompanyBidding> companyBiddingList = new ArrayList<>();
            companyBiddingList.add(companyBidding);
            marketBiddingMap.put(market, companyBiddingList);
        }
    }

    @Override
    public void iNotify() {
        Queue<String> marketQueue = new LinkedList<>();//市场队列 例：LOCAL_P1
        for (Market.Type marketType : Market.Type.values()) {
            for (Product.Type productType : Product.Type.values()) {
                String market = marketType + "_" + productType;
                if (marketBiddingMap.containsKey(market)) {
                    marketQueue.add(market);
                }
            }
        }

        Map<String,Queue<String>> marketCompanyQueueMap = new HashMap<>();//key:market;value:companyId组成的queue
        for (String market : marketBiddingMap.keySet()) {
            List<CompanyBidding> companyBiddingList = marketBiddingMap.get(market);
            Collections.sort(companyBiddingList);
            Queue<String> companyIdQueue = new LinkedList<>();
            for (CompanyBidding companyBidding : companyBiddingList) {
                companyIdQueue.offer(companyBidding.getCompanyId());
            }
            marketCompanyQueueMap.put(market, companyIdQueue);
        }

        memoryCampaign.getCampaignPartyMap().put(MarketOrderParty.TYPE, new MarketOrderParty(memoryCampaign, marketQueue, marketCompanyQueueMap));

        setStatus(Status.DONE.name());
    }

    private class CompanyBidding implements Comparable<CompanyBidding>{
        private String companyId;
        private Integer fee;
        public CompanyBidding(String companyId, Integer fee) {
            this.companyId = companyId;
            this.fee = fee;
        }

        public String getCompanyId() {
            return companyId;
        }

        public Integer getFee() {
            return fee;
        }

        @Override
        public int compareTo(CompanyBidding o) {
            return o.fee - fee;
        }
    }
}
