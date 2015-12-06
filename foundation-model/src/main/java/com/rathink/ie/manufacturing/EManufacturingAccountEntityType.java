package com.rathink.ie.manufacturing;

/**
 * Created by Hean on 2015/8/29.
 */
public enum EManufacturingAccountEntityType {

    COMPANY_CASH("公司现金"), OTHER("其它"),

    ORDER_FEE("订单利润"), ORDER_DELAY_FEE("订单延误费"),

    //费用
    PRODUCT_DEVOTION_FEE("产品研发费用"), PRODUCT_LINE_FEE("生产线研发费"), PRODUCE_FEE("生产费"), MARKET_FEE("广告费"), MARKET_DEVOTION_FEE("市场开拓费"),
    PRODUCE_LINE_MAINTENANCE_FEE("生产线维护为"), LINE_FACTORY_RENT_FEE("工厂租赁费"), ADMINISTRATION_FEE("行政管理费用"),


    //贷款
    LOAN_LONG_TERM("长期贷款"), LOAN_SHORT_TERM("短期贷款"), LOAN_USURIOUS("高利贷款"),

    //
    RECEIVABLE("应收账款"), INTEREST("利息"),

    //流动资产
    FLOATING_CAPITAL_MATERIAL("原料"),
    FLOATING_CAPITAL_PRODUCT("产品"),;


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
