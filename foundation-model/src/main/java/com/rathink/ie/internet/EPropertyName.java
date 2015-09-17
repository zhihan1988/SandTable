package com.rathink.ie.internet;

/**
 * Created by Hean on 2015/8/26.
 */
public enum EPropertyName{
    OFFICE_RATIO("办公室系数", Edept.AD.name(), "PERCENT",1),

    PRODUCT_ABILITY("产品研发能力", Edept.PRODUCT.name(), "PERCENT",2),
    PRODUCT_FEE_RATIO("产品资金投入系数", Edept.PRODUCT.name(), "HIDDEN",3),
    PRODUCT_RATIO("产品系数", Edept.PRODUCT.name(), "PERCENT" ,4),
    PER_ORDER_COST("客单价", Edept.PRODUCT.name(), "", 5),
    PRODUCT_COMPETITION_RATIO("产品竞争系数", Edept.PRODUCT.name(), "HIDDEN", 6),

    MARKET_ABILITY("市场能力", Edept.MARKET.name(), "PERCENT", 7),
    NEW_USER_AMOUNT("新用户数", Edept.MARKET.name(),"", 8),

    OPERATION_ABILITY("运营能力", Edept.OPERATION.name(), "PERCENT", 9),
    SATISFACTION("满意度", Edept.OPERATION.name(), "PERCENT", 10),
    OLD_USER_AMOUNT("老用户数量", Edept.OPERATION.name(), "", 11),
    USER_AMOUNT("用户数量", Edept.OPERATION.name(),"", 12),
    OPERATION_FEE_RATIO("运营资金投入系数",Edept.OPERATION.name(),"HIDDEN", 13),
    CURRENT_PERIOD_INCOME("本期收入", Edept.OPERATION.name(),"", 14);

    private String label;
    private String dept;//所属部门
    private String display;//展现形式（例如以进度条形式展现）
    private Integer order;
    EPropertyName(String label,String dept, String display, Integer order) {
        this.label = label;
        this.dept = dept;
        this.display = display;
        this.order = order;
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

    public Integer getOrder() {
        return order;
    }
}

