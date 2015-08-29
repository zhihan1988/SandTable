package com.rathink.ie.internet.choice.model;

import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.internet.Edept;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by Hean on 2015/8/24.
 */
@Entity
@Table(name = "human")
public class Human extends CompanyChoice {
    private String name;
    private String ability;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "ability")
    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    @Transient
    public String getTypeLabel(){
        String label = "";
        for (Edept edept: Edept.values()) {
            if (edept.name().equals(getType())) {
                label = edept.getLabel();
            }
        }
        return label;
    }
}
