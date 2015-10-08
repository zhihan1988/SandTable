package com.rathink.ie.manufacturing;

import com.rathink.ie.internet.Edept;

/**
 * Created by Hean on 2015/8/26.
 */
public enum EManufacturingPropertyName {
//    OFFICE_RATIO("办公室系数", Edept.AD.name(), "PERCENT"),

    LINE1_PROCESS("1生产线距离完成的进度", Edept.PRODUCT.name(), "TEXT"),
    LINE2_PROCESS("2生产线距离完成的进度", Edept.PRODUCT.name(), "TEXT");

    private String label;
    private String dept;//所属部门
    private String display;//展现形式（例如以进度条形式展现）
    EManufacturingPropertyName(String label, String dept, String display) {
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

