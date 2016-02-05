package com.rathink.iix.manufacturing.component;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.iix.ibase.component.MemoryCampaign;
import com.rathink.iix.ibase.component.MemoryCompany;
import com.rathink.ix.internet.EAccountEntityType;
import com.rathink.ix.manufacturing.EManufacturingAccountEntityType;
import com.rathink.ix.manufacturing.model.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Hean on 2016/2/2.
 */
public class ManufacturingMemoryCompany extends MemoryCompany {

    private Map<String, ProduceLine> produceLineMap = new LinkedHashMap<>();
    private Map<String, Material> materialMap = new LinkedHashMap<>();
    private Map<String, Product> productMap = new LinkedHashMap<>();
    private Map<String, Market> marketMap = new LinkedHashMap<>();
    private Map<String, Loan> loanMap = new LinkedHashMap<>();
    private Map<String, MarketOrder> marketOrderMap = new LinkedHashMap<>();

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

    public Material getMaterialByType(String type) {
        Material material = null;
        for (Material m : materialMap.values()) {
            if (m.getType().equals(type)) {
                material = m;
            }
        }
        return material;
    }

    public Map<String, Product> getProductMap() {
        return productMap;
    }

    public void setProductMap(Map<String, Product> productMap) {
        this.productMap = productMap;
    }

    public Product getProductByType(String type) {
        Product product = null;
        for (Product p : productMap.values()) {
            if (p.getType().equals(type)) {
                product = p;
            }
        }
        return product;
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

    //现金
    public Integer getCompanyCash() {
        return sumTypeAccount(EManufacturingAccountEntityType.COMPANY_CASH.name());
    }

    //长期贷款
    public Integer getLongTermLoan() {
        return -sumPrefixTypeAccount(EManufacturingAccountEntityType.LOAN_LONG_TERM.name());
    }

    //短期贷款
    public Integer getShortTermLoan() {
        return -sumPrefixTypeAccount(EManufacturingAccountEntityType.LOAN_SHORT_TERM.name());
    }

    //高利贷
    public Integer getUsuriousLoan() {
        return -sumPrefixTypeAccount(EManufacturingAccountEntityType.LOAN_USURIOUS.name());
    }

    //所有贷款贷款
    public Integer getAllLoan() {
        return -sumPrefixTypeAccount("LOAN_");
    }

    //流动资产
    public Integer getFloatingCapital() {
        return sumPrefixTypeAccount("FLOATING_CAPITAL_");
    }


}
