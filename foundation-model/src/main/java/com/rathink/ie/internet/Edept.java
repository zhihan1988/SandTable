package com.rathink.ie.internet;

/**
 * Created by Hean on 2015/8/24.
 */
public enum Edept {
    PRODUCT("产品研发"), OPERATION("运营"), MARKET("市场营销"), HR("人力资源");
    private Edept(String label){
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
