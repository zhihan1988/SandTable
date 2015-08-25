package com.rathink.ie.ibase.property.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "company_status_property_value")
public class CompanyStatusPropertyValue {
    private String id;
    private String name;
    private String value;
    private String dept;
    private CompanyStatus companyStatus;

    public CompanyStatusPropertyValue(){}

    public CompanyStatusPropertyValue(String dept, String name, String value, CompanyStatus companyStatus) {
        this.name = name;
        this.value = value;
        this.dept = dept;
        this.companyStatus = companyStatus;
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
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_status_id")
    public CompanyStatus getCompanyStatus() {
        return companyStatus;
    }

    public void setCompanyStatus(CompanyStatus companyStatus) {
        this.companyStatus = companyStatus;
    }
}
