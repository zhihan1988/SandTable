package com.rathink.ie.internet;

/**
 * Created by Hean on 2015/8/29.
 */
public enum EAccountEntityType {
    MARKET_FEE("市场费用"),PRODUCT_FEE("产品费用"),OPERATION_FEE("运营费用"),HR_FEE("员工工资"),
    COMPANY_CASH("公司现金"),OTHER("其它");

    private EAccountEntityType(String label) {
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
