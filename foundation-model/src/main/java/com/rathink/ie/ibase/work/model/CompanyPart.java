package com.rathink.ie.ibase.work.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Hean on 2015/10/8.
 */
@JsonIgnoreProperties(value={"hibernateLazyInitializer"})
@Entity
@Table(name = "company_part")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "base_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "COMPANY_PART")
public class CompanyPart {
    private String id;
//    private String baseType;
    private String serial;
    private String dept;
    private Campaign campaign;
    private Company company;
    private String name;
    private String status;
//    private String value;
//    private String value2;
//    private String value3;
//    private String value4;
//    private String value5;


    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "serial")
    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

/*    @Column(name = "base_type")
    public String getBaseType() {
        return baseType;
    }

    public void setBaseType(String baseType) {
        this.baseType = baseType;
    }*/

    @Column(name = "dept")
    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   /* @Column(name = "value")
    protected String getValue() {
        return value;
    }

    protected void setValue(String value) {
        this.value = value;
    }

    @Column(name = "value2")
    protected String getValue2() {
        return value2;
    }

    protected void setValue2(String value2) {
        this.value2 = value2;
    }

    @Column(name = "value3")
    protected String getValue3() {
        return value3;
    }

    protected void setValue3(String value3) {
        this.value3 = value3;
    }

    @Column(name = "value4")
    protected String getValue4() {
        return value4;
    }

    protected void setValue4(String value4) {
        this.value4 = value4;
    }

    @Column(name = "value5")
    protected String getValue5() {
        return value5;
    }

    protected void setValue5(String value5) {
        this.value5 = value5;
    }*/
}
