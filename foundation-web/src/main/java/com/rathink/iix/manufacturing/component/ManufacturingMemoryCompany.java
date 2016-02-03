package com.rathink.iix.manufacturing.component;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.iix.ibase.component.MemoryCampaign;
import com.rathink.iix.ibase.component.MemoryCompany;
import com.rathink.ix.manufacturing.model.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hean on 2016/2/2.
 */
public class ManufacturingMemoryCompany extends MemoryCompany {

    private Map<String, ProduceLine> produceLineMap = new HashMap<>();
    private Map<String, Material> materialMap = new HashMap<>();
    private Map<String, Product> productMap = new HashMap<>();
    private Map<String, Market> marketMap = new HashMap<>();
    private Map<String, Loan> loanMap = new HashMap<>();
    private Map<String, MarketOrder> marketOrderMap = new HashMap<>();

    public ManufacturingMemoryCompany(Company company, MemoryCampaign memoryCampaign) {
        super(company, memoryCampaign);
    }

    public Map<String, ProduceLine> getProduceLineMap() {
        return produceLineMap;
    }

    public void setProduceLineMap(Map<String, ProduceLine> produceLineMap) {
        this.produceLineMap = produceLineMap;
    }

    public Map<String, Material> getMaterialMap() {
        return materialMap;
    }

    public void setMaterialMap(Map<String, Material> materialMap) {
        this.materialMap = materialMap;
    }

    public Map<String, Product> getProductMap() {
        return productMap;
    }

    public void setProductMap(Map<String, Product> productMap) {
        this.productMap = productMap;
    }

    public Map<String, Market> getMarketMap() {
        return marketMap;
    }

    public void setMarketMap(Map<String, Market> marketMap) {
        this.marketMap = marketMap;
    }

    public Map<String, Loan> getLoanMap() {
        return loanMap;
    }

    public void setLoanMap(Map<String, Loan> loanMap) {
        this.loanMap = loanMap;
    }

    public Map<String, MarketOrder> getMarketOrderMap() {
        return marketOrderMap;
    }

    public void setMarketOrderMap(Map<String, MarketOrder> marketOrderMap) {
        this.marketOrderMap = marketOrderMap;
    }
}
