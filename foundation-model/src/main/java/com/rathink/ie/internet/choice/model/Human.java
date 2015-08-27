package com.rathink.ie.internet.choice.model;

import com.rathink.ie.ibase.work.model.CompanyChoice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Hean on 2015/8/24.
 */
@Entity
@Table(name = "human")
public class Human extends CompanyChoice {
    private String name;
    private String salary;
    private String ability;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "salary")
    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    @Column(name = "ability")
    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }
}
