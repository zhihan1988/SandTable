package com.rathink.ix.internet;

/**
 * Created by Hean on 2015/9/4.
 */
public enum EInstructionStatus {
    UN_PROCESS("1","待处理"),PROCESSED("2","已处理"),DELETE("0","已删除");
    private String value;
    private String label;

    EInstructionStatus(String value, String label) {
        this.value = value;
        this.label = label;

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
