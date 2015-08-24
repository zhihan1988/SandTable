package com.rathink.ie.ibase.work.model;

import com.rathink.ie.campaign.model.Campaign;
import com.rathink.ie.team.model.Company;
import com.rathink.ie.user.model.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "company_instruction")
@Inheritance(strategy=InheritanceType.JOINED)
public class CompanyInstruction {
    private String id;
    private String type;
    private String campaignDate;
    private Campaign campaign;
    private Company company;
    private String dept;
    private CompanyChoice companyChoice;
    private String content;
    private User creator;

    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "campaign_date")
    public String getCampaignDate() {
        return campaignDate;
    }

    public void setCampaignDate(String campaignDate) {
        this.campaignDate = campaignDate;
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

    @Column(name = "dept")
    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_choice_id")
    public CompanyChoice getCompanyChoice() {
        return companyChoice;
    }

    public void setCompanyChoice(CompanyChoice companyChoice) {
        this.companyChoice = companyChoice;
    }

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
