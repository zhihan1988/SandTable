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
@DiscriminatorValue(value = "MARKET_ORDER")
public class MarketOrder extends CompanyPart {

    private String amount;//����
    private String unitPrice;//����
    private String totalPrice;//���
    private String needAccountCycle;//ʣ������
    private String productType;//P1,P2,P3,P4

    @Column(name = "value")
    public Integer getAmount() {
        return amount == null ? 0 : Integer.valueOf(amount);
    }

    public void setAmount(Integer amount) {
        this.amount = amount == 0 ? null : String.valueOf(amount);
    }

    @Column(name = "value2")
    public Integer getUnitPrice() {
        return unitPrice == null ? 0 : Integer.valueOf(unitPrice);
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice == 0 ? null : String.valueOf(unitPrice);
    }

    @Column(name = "value3")
    public Integer getTotalPrice() {
        return totalPrice == null ? 0 : Integer.valueOf(totalPrice);
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice == 0 ? null : String.valueOf(totalPrice);
    }

    @Column(name = "value4")
    public Integer getNeedAccountCycle() {
        return needAccountCycle == null ? 0 : Integer.valueOf(needAccountCycle);
    }

    public void setNeedAccountCycle(Integer needAccountCycle) {
        this.needAccountCycle = needAccountCycle == 0 ? null : String.valueOf(needAccountCycle);
    }

    @Column(name = "value5")
    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public enum Status {
        NORMAL,
        //�ѽ���
        DELIVERED,
        //�˿��ѵ�����������
        FINISH
    }

}
