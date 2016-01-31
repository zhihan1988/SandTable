package com.rathink.ix.ibase.work.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by pgwt on 10/3/15.
 */

@Entity
@Table(name = "industry_choice_setting")
public class IndustryChoiceSetting {

    private String id ;
    private String name;
    private String valueSet;


    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "value_set")
    public String getValueSet() {
        return valueSet;
    }

    public void setValueSet(String valueSet) {
        this.valueSet = valueSet;
    }
}
