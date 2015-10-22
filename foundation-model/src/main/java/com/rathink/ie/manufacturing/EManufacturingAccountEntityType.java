package com.rathink.ie.manufacturing;

/**
 * Created by Hean on 2015/8/29.
 */
public enum EManufacturingAccountEntityType {

    LONG_TERM_LOAN("长期贷款"), SHORT_TERM_LOAN("短期贷款"), USURIOUS_LOAN("高利贷款"), LOAN_RECEIVABLE("应收贷款"),
    COMPANY_CASH("公司现金"), OTHER("其它"),
    ORDER_FEE("订单费"),ORDER_DELAY_FEE("订单延误费"),
    MATERIAL_FEE("原料费用"),PRODUCT_DEVOTION_FEE("产品研发投入"),PRODUCT_LINE_FEE("生产线费用"),PRODUCE("生产费用"),MARKET_FEE("广告投入"),MARKET_DEVOTION_FEE("开拓市场投入");

    EManufacturingAccountEntityType(String label) {
        this.label = label;
    }

    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
