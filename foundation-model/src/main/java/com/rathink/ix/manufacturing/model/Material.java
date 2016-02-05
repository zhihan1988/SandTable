package com.rathink.ix.manufacturing.model;

import com.rathink.ix.ibase.work.model.CompanyPart;
import org.omg.PortableInterceptor.INACTIVE;

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
        NORMAL,PURCHASING
    }

    public enum Type{
        R1,R2,R3,R4
    }
    private String type;
    private String amount;
    private String purchasingAmount;

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
    public Integer getPurchasingAmount() {
        return purchasingAmount == null ? 0 : Integer.valueOf(purchasingAmount);
    }

    public void setPurchasingAmount(Integer purchasingAmount) {
        this.purchasingAmount = purchasingAmount == null ? null : String.valueOf(purchasingAmount);
    }
}
