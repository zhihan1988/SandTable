package com.rathink.ie.ibase.property.model;

import com.rathink.ie.campaign.model.Campaign;
import com.rathink.ie.team.model.Company;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "company_status")
public class CompanyStatus {
    private String id;
    private Campaign campagin;
    private Company company;
    private String campaginDate;

    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    public Campaign getCampagin() {
        return campagin;
    }

    public void setCampagin(Campaign campagin) {
        this.campagin = campagin;
    }

    @ManyToOne
    @JoinColumn(name = "company_id")
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Column(name = "campaign_date")
    public String getCampaginDate() {
        return campaginDate;
    }

    public void setCampaginDate(String campaginDate) {
        this.campaginDate = campaginDate;
    }
}
