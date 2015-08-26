package com.rathink.ie.internet;

/**
 * Created by Hean on 2015/8/26.
 */
public enum EPropertyName {
    OPERATION_ABILITY("运营能力", Edept.OPERATION.name()),
    SATISFACTION("满意度", Edept.OPERATION.name()),
    OLD_USER_AMOUNT("老用户数量", Edept.OPERATION.name()),
    USER_AMOUNT("用户数量", Edept.OPERATION.name()),
    CURRENT_PERIOD_INCOME("本轮收入", Edept.OPERATION.name()),

    MARKET_ABILITY("市场能力", Edept.MARKET.name()),
    NEW_USER_AMOUNT("新用户数", Edept.MARKET.name()),

    PRODUCT_ABILITY("产品研发能力", Edept.PRODUCT.name()),
    PRODUCT_RATIO("产品系数", Edept.PRODUCT.name()),
    PER_ORDER_COST("客单价", Edept.PRODUCT.name());

    private String label;
    private String dept;
    private EPropertyName(String label,String dept) {
        this.label = label;
        this.dept = dept;
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
}
