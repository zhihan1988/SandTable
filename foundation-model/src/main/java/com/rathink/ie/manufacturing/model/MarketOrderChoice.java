package com.rathink.ie.manufacturing.model;

import com.rathink.ie.ibase.work.model.IndustryResourceChoice;

/**
 * Created by Hean on 2015/10/21.
 */
public class MarketOrderChoice {

    private String name;
    private String productType;
    private Integer amount;//����
    private Integer unitPrice;//����
    private Integer totalPrice;//���
    private Integer accountPeriod;//����
    private Integer ISO;
    private Integer cost;//�ɱ�
    private Integer profit;//����

    private IndustryResourceChoice industryResourceChoice;
    private MarketOrderChoice() {}

    public MarketOrderChoice(IndustryResourceChoice industryResourceChoice) {
        this.industryResourceChoice = industryResourceChoice;

        productType = industryResourceChoice.getType();
        name = industryResourceChoice.getName();

        String[] array = industryResourceChoice.getValue2().split(",");
        amount = Integer.valueOf(array[0]);
        unitPrice = Integer.valueOf(array[1]);
        totalPrice = Integer.valueOf(array[2]);
        accountPeriod = Integer.valueOf(array[3]);
        ISO = Integer.valueOf(array[4]);
        cost = Integer.valueOf(array[5]);
        profit = Integer.valueOf(array[6]);
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

    public Integer getAccountPeriod() {
        return accountPeriod;
    }

    public Integer getISO() {
        return ISO;
    }

    public Integer getCost() {
        return cost;
    }

    public Integer getProfit() {
        return profit;
    }

}
