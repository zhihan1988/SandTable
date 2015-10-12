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
@DiscriminatorValue(value = "MATERIAL")
public class Material extends CompanyPart {
    public enum Status {
        NORMAL
    }

    public enum Type{
        P1,P2,P3,P4
    }
    private String type;
    private String amount;

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

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
