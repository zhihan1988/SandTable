package com.rathink.ix.internet;

/**
 * Created by Hean on 2015/8/29.
 */
public enum EAccountEntityType {
    AD_FEE("行政费用"), HR_FEE("员工工资"), PRODUCT_FEE("产品费用"), MARKET_FEE("市场费用"), OPERATION_FEE("运营费用"),
    COMPANY_CASH("营业收入"), OTHER("其它"), FIRE_FEE("解雇补贴"),;

    EAccountEntityType(String label) {
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
