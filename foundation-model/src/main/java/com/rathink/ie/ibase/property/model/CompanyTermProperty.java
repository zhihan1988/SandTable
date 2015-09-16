package com.rathink.ie.ibase.property.model;

import com.rathink.ie.internet.EPropertyName;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "company_term_property")
public class CompanyTermProperty {
    private String id;
    private String name;
    private Double value;
    private String dept;
    private CompanyTerm companyTerm;

    public CompanyTermProperty(){}

    public CompanyTermProperty(EPropertyName ePropertyName, Double value, CompanyTerm companyTerm) {
        this.name = ePropertyName.name();
        this.value = value;
        this.dept = ePropertyName.getDept();
        this.companyTerm = companyTerm;
    }

    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "dept")
    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "value")
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_term_id")
    public CompanyTerm getCompanyTerm() {
        return companyTerm;
    }

    public void setCompanyTerm(CompanyTerm companyTerm) {
        this.companyTerm = companyTerm;
    }

    @Transient
    public String getLabel() {
        String label = "";
        for (EPropertyName ePropertyName: EPropertyName.values()) {
            if (ePropertyName.name().equals(getName())) {
                label = ePropertyName.getLabel();
            }
        }
        return label;
    }

    @Transient
    public String getDisplay() {
        String display = "";
        for (EPropertyName ePropertyName: EPropertyName.values()) {
            if (ePropertyName.name().equals(getName())) {
                display = ePropertyName.getDisplay();
            }
        }
        return display;
    }

}
