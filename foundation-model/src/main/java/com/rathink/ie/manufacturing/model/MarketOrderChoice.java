package com.rathink.ie.manufacturing.model;

import com.rathink.ie.ibase.work.model.IndustryResourceChoice;

/**
 * Created by Hean on 2015/10/21.
 */
public class MarketOrderChoice {

    private String name;
    private String marketArea;
    private String productType;
    private Integer amount;//数量
    private Integer unitPrice;//单价
    private Integer totalPrice;//总价格
    private Integer profit;//利润
    private Integer accountPeriod;//账期
    private String ownerCompany;

    private IndustryResourceChoice industryResourceChoice;
    private MarketOrderChoice() {}

    public MarketOrderChoice(IndustryResourceChoice industryResourceChoice) {
        this.industryResourceChoice = industryResourceChoice;
        marketArea = industryResourceChoice.getType().split("_")[0];
        productType = industryResourceChoice.getType().split("_")[1];
        name = industryResourceChoice.getName();

        String[] array = industryResourceChoice.getValue2().split(",");
        amount = Integer.valueOf(array[0]);
        unitPrice = Integer.valueOf(array[1]);
        totalPrice = Integer.valueOf(array[2]);
        profit = Integer.valueOf(array[3]);
        accountPeriod = Integer.valueOf(array[4]);
    }

    public String getMarketArea() {
        return marketArea;
    }

    public String getProductType() {
        return productType;
    }

    public String getName() {
        return name;
    }

    public Integer getAmount() {
        return amount;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getProfit() {
        return profit;
    }

    public Integer getAccountPeriod() {
        return accountPeriod;
    }

    public String getOwnerCompany() {
        return ownerCompany;
    }

    public void setOwnerCompany(String ownerCompany) {
        this.ownerCompany = ownerCompany;
    }

    public IndustryResourceChoice getIndustryResourceChoice() {
        return industryResourceChoice;
    }
}
