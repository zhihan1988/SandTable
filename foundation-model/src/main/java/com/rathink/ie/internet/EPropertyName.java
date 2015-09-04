package com.rathink.ie.internet;

/**
 * Created by Hean on 2015/8/26.
 */
public enum EPropertyName {
    OPERATION_ABILITY("运营能力", Edept.OPERATION.name(), "PERCENT"),
    SATISFACTION("满意度", Edept.OPERATION.name(), "PERCENT"),
    OLD_USER_AMOUNT("老用户数量", Edept.OPERATION.name(), ""),
    USER_AMOUNT("用户数量", Edept.OPERATION.name(),""),
//    CURRENT_PERIOD_INCOME("本期收入", Edept.OPERATION.name()),

    MARKET_ABILITY("市场能力", Edept.MARKET.name(), "PERCENT"),
    NEW_USER_AMOUNT("新用户数", Edept.MARKET.name(),""),

    PRODUCT_ABILITY("产品研发能力", Edept.PRODUCT.name(), "PERCENT"),
    PRODUCT_RATIO("产品系数", Edept.PRODUCT.name(), "PERCENT"),
    PER_ORDER_COST("客单价", Edept.PRODUCT.name(), ""),
    OFFICE_RATIO("办公室系数", Edept.AD.name(), "PERCENT"),;

    private String label;
    private String dept;
    private String display;
    private EPropertyName(String label,String dept, String display) {
        this.label = label;
        this.dept = dept;
        this.display = display;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}

