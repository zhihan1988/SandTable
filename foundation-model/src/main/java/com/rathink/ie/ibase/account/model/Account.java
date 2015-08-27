package com.rathink.ie.ibase.account.model;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "account")
public class Account {
    private String id;
    private Campaign campaign;
    private Company company;
    private String campaignDate;
    private List<AccountEntry> accountEntryList;

    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    @ManyToOne(fetch = FetchType.LAZY)
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
    public List<AccountEntry> getAccountEntryList() {
        return accountEntryList;
    }

    public void setAccountEntryList(List<AccountEntry> accountEntryList) {
        this.accountEntryList = accountEntryList;
    }
}
