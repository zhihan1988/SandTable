package com.rathink.ie.foundation.team.model;

/**
 * Created by Hean on 2015/9/25.
 */
public enum ECompanyStatus {
    PREPARE("准备中"),NORMAL("正常进行中"),END("现金流断裂，破产"), FINISH("胜利"),;
    String label;
    ECompanyStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
