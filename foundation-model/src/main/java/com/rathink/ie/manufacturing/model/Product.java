package com.rathink.ie.manufacturing.model;

import com.rathink.ie.ibase.work.model.CompanyPart;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Hean on 2015/10/10.
 */
@Entity
@Table(name = "company_part")
@DiscriminatorValue(value = "PRODUCT")
public class Product extends CompanyPart {
    private String type;
    private String amount;
    private String developNeedCycle;//剩余的研发周期

    @Column(name = "value")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "value2")
    public Integer getAmount() {
        return amount == null ? 0 : Integer.valueOf(amount);
    }

    public void setAmount(Integer amount) {
        this.amount = amount == null ? null : String.valueOf(amount);
    }

    @Column(name = "value3")
    public Integer getDevelopNeedCycle() {
        return developNeedCycle == null ? 0 : Integer.valueOf(developNeedCycle);
    }

    public void setDevelopNeedCycle(Integer developNeedCycle) {
        this.developNeedCycle = developNeedCycle == null ? null : String.valueOf(developNeedCycle);
    }

    public enum Status {
        NORMAL
    }
    public enum Type {
        P1(1, 1, 2, 0), P2(1, 1, 2, 6), P3(1, 1, 2, 6), P4(1, 1, 2, 6),;

        Type(Integer cost, Integer processCost, Integer totalCost, Integer developNeedCycle) {
            this.cost = cost;
            this.processCost = processCost;
            this.totalCost = totalCost;
            this.developNeedCycle = developNeedCycle;
        }
        private Integer cost;//成本
        private Integer processCost;;//加工费
        private Integer totalCost;//合计
        private Integer developNeedCycle;

        public Integer getCost() {
            return cost;
        }

        public void setCost(Integer cost) {
            this.cost = cost;
        }

        public Integer getProcessCost() {
            return processCost;
        }

        public void setProcessCost(Integer processCost) {
            this.processCost = processCost;
        }

        public Integer getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(Integer totalCost) {
            this.totalCost = totalCost;
        }

        public Integer getDevelopNeedCycle() {
            return developNeedCycle;
        }

        public void setDevelopNeedCycle(Integer developNeedCycle) {
            this.developNeedCycle = developNeedCycle;
        }
    }

}
