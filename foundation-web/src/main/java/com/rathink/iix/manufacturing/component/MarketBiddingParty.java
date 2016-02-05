package com.rathink.iix.manufacturing.component;

import com.rathink.iix.ibase.component.CampaignParty;
import com.rathink.iix.ibase.component.MemoryCampaign;

import java.util.*;

/**
 * Created by Hean on 2016/2/2.
 */
public class MarketBiddingParty extends CampaignParty<String> {
//    private List companyBiddingPriceList;
    private Object sortedCompany;
    private Map<String, List<CompanyBidding>> marketBiddingMap = new HashMap<>();

    public MarketBiddingParty(MemoryCampaign memoryCampaign) {
        super(memoryCampaign, "BIDDING");
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
        //sort
        for (List<CompanyBidding> companyBiddingList : marketBiddingMap.values()) {
            Collections.sort(companyBiddingList);
        }
        System.out.println(123);
//        MarketOrderParty marketOrderParty = new MarketOrderParty(memoryCampaign);
    }

    class CompanyBidding implements Comparable<CompanyBidding>{
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
