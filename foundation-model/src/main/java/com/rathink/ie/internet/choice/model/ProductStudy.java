package com.rathink.ie.internet.choice.model;

import com.rathink.ie.ibase.work.model.CompanyChoice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Hean on 2015/8/24.
 */
@Entity
@Table(name = "product_study")
public class ProductStudy extends CompanyChoice {
    private String payed;

    @Column(name = "payed")
    public String getPayed() {
        return payed;
    }

    public void setPayed(String payed) {
        this.payed = payed;
    }
}
