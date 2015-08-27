package com.rathink.ie.internet.choice.model;

/**
 * Created by Hean on 2015/8/27.
 */

import com.rathink.ie.ibase.work.model.CompanyChoice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "market_activity_choice")
public class MarketActivityChoice extends CompanyChoice{
    private String name;
    private String cost;//成本

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "cost")
    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

}
