package com.rathink.ie.internet.instruction.model;

import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.choice.model.Human;

import javax.persistence.*;

/**
 * Created by Hean on 2015/8/24.
 */
@Entity
@Table(name = "hr_instruction")
public class HrInstruction extends CompanyInstruction {
    private Human human;
    private String status;// 见页面下方枚举定义

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "human_id")
    public Human getHuman() {
        return human;
    }

    public void setHuman(Human human) {
        this.human = human;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   /* @Column(name = "stock")
    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }*/

    public enum Status {
        DQD("1","待确定"),WXZ("2","未选中"),YXZ("3","已选中");
        private String value;
        private String label;

        private Status(String value, String label) {
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

    @Transient
    public String getStatusLabel() {
        String label = "";
        for (Status status: Status.values()) {
            if (status.getValue().equals(getStatus())) {
                label = status.getLabel();
            }
        }
        return label;
    }

}
