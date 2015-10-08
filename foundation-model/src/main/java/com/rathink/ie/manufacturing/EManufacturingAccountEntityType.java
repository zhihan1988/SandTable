package com.rathink.ie.manufacturing;

/**
 * Created by Hean on 2015/8/29.
 */
public enum EManufacturingAccountEntityType {
    PRODUCE_LINE_FEE("生产线费用");

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
