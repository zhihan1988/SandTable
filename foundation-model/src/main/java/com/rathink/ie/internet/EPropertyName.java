package com.rathink.ie.internet;

/**
 * Created by Hean on 2015/8/26.
 */
public enum EPropertyName{
    OFFICE_RATIO("办公室系数", Edept.AD.name(), "PERCENT"),

    PRODUCT_ABILITY("产品研发能力", Edept.PRODUCT.name(), "PERCENT"),
    PRODUCT_FEE_RATIO("产品资金投入系数", Edept.PRODUCT.name(), "HIDDEN"),
    PRODUCT_RATIO("产品系数", Edept.PRODUCT.name(), "PERCENT"),
    PER_ORDER_COST("客单价", Edept.PRODUCT.name(), "TEXT"),
    PRODUCT_COMPETITION_RATIO("产品竞争系数", Edept.PRODUCT.name(), "HIDDEN"),

    MARKET_ABILITY("市场能力", Edept.MARKET.name(), "PERCENT"),
    NEW_USER_AMOUNT("新用户数", Edept.MARKET.name(),"TEXT"),

    OPERATION_ABILITY("运营能力", Edept.OPERATION.name(), "PERCENT"),
    OPERATION_FEE_RATIO("运营资金投入系数",Edept.OPERATION.name(),"HIDDEN"),
    SATISFACTION("满意度", Edept.OPERATION.name(), "PERCENT"),
    OLD_USER_AMOUNT("老用户数量", Edept.OPERATION.name(), "TEXT"),
    USER_AMOUNT("用户数量", Edept.OPERATION.name(),"TEXT"),
    CURRENT_PERIOD_INCOME("本期收入", Edept.OPERATION.name(),"TEXT");

    private String label;
    private String dept;//所属部门
    private String display;//展现形式（例如以进度条形式展现）
    EPropertyName(String label,String dept, String display) {
        this.label = label;
        this.dept = dept;
        this.display = display;
    }

    public String getLabel() {
        return label;
    }

    public String getDept() {
        return dept;
    }

    public String getDisplay() {
        return display;
    }
}

