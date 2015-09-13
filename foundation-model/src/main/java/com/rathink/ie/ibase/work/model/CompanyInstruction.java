package com.rathink.ie.ibase.work.model;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.user.model.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "company_instruction")
@Inheritance(strategy=InheritanceType.JOINED)
public class CompanyInstruction {
    private String id;
    private String status;
    private String baseType;
    private String campaignDate;
    private Campaign campaign;
    private Company company;
    private String dept;
    private CompanyChoice companyChoice;
    private String value;
    private User creator;
    private CompanyTerm companyTerm;

    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "base_type")
    public String getBaseType() {
        return baseType;
    }

    public void setBaseType(String baseType) {
        this.baseType = baseType;
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


    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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
    public String getStatusLabel() {
        String label = "";
        for (EInstructionStatus status: EInstructionStatus.values()) {
            if (status.getValue().equals(getStatus())) {
                label = status.getLabel();
            }
        }
        return label;
    }
}
