package com.rathink.ie.ibase.property.model;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "company_status")
public class CompanyStatus {
    private String id;
    private Campaign campaign;
    private Company company;
    private String campaignDate;
    private List<CompanyStatusPropertyValue> companyStatusPropertyValueList;

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
    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
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
    public String getCampaignDate() {
        return campaignDate;
    }

    public void setCampaignDate(String campaignDate) {
        this.campaignDate = campaignDate;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "companyStatus", cascade = CascadeType.ALL)
    @OrderBy("id asc")
    public List<CompanyStatusPropertyValue> getCompanyStatusPropertyValueList() {
        return companyStatusPropertyValueList;
    }

    public void setCompanyStatusPropertyValueList(List<CompanyStatusPropertyValue> companyStatusPropertyValueList) {
        this.companyStatusPropertyValueList = companyStatusPropertyValueList;
    }
}
