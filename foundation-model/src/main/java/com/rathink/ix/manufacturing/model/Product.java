package com.rathink.ix.manufacturing.model;

import com.rathink.ix.ibase.work.model.CompanyPart;

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
    private String developNeedCycle;//ʣ����з�����

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
        P1(1, 0, 0), P2(1, 6, 4), P3(1, 6, 8), P4(1, 6, 12),;

        Type(Integer workFee, Integer developNeedCycle, Integer perDevotion) {
            this.workFee = workFee;
            this.developNeedCycle = developNeedCycle;
            this.perDevotion = perDevotion;
        }
        private Integer workFee;//�ӹ���
        private Integer developNeedCycle;//�з�����
        private Integer perDevotion;//�����з�Ͷ��

        public Integer getWorkFee() {
            return workFee;
        }

        public void setWorkFee(Integer workFee) {
            this.workFee = workFee;
        }

        public Integer getDevelopNeedCycle() {
            return developNeedCycle;
        }

        public void setDevelopNeedCycle(Integer developNeedCycle) {
            this.developNeedCycle = developNeedCycle;
        }

        public Integer getPerDevotion() {
            return perDevotion;
        }

        public void setPerDevotion(Integer perDevotion) {
            this.perDevotion = perDevotion;
        }
    }

}
