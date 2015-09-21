package com.rathink.ie.internet;

/**
 * Created by Hean on 2015/9/4.
 */
public enum EInstructionStatus {

    DQD("1","待确定"),WXZ("2","未选中"),YXZ("3","已选中"),YSC("0","已删除");
    private String value;
    private String label;

    private EInstructionStatus(String value, String label) {
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
