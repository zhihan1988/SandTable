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
    private String intelligent;
    private String hard;

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

    @Column(name = "intelligent")
    public String getIntelligent() {
        return intelligent;
    }

    public void setIntelligent(String intelligent) {
        this.intelligent = intelligent;
    }

    @Column(name = "hard")
    public String getHard() {
        return hard;
    }

    public void setHard(String hard) {
        this.hard = hard;
    }
}
