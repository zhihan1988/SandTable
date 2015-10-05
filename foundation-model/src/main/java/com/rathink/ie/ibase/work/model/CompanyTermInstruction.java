package com.rathink.ie.ibase.work.model;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.user.model.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "company_term_instruction")
public class CompanyTermInstruction {
    private String id;
    private String status;
    private String baseType;
    private Integer campaignDate;
    private Campaign campaign;
    private Company company;
    private String dept;
    private CampaignTermChoice campaignTermChoice;
    private IndustryChoice industryChoice;
    private IndustryResource industryResource;
    private IndustryResourceChoice industryResourceChoice;
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
    public Integer getCampaignDate() {
        return campaignDate;
    }

    public void setCampaignDate(Integer campaignDate) {
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
    @JoinColumn(name = "campaign_term_choice_id")
    public CampaignTermChoice getCampaignTermChoice() {
        return campaignTermChoice;
    }

    public void setCampaignTermChoice(CampaignTermChoice campaignTermChoice) {
        this.campaignTermChoice = campaignTermChoice;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_choice_id")
    public IndustryChoice getIndustryChoice() {
        return industryChoice;
    }

    public void setIndustryChoice(IndustryChoice industryChoice) {
        this.industryChoice = industryChoice;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_resource_id")
    public IndustryResource getIndustryResource() {
        return industryResource;
    }

    public void setIndustryResource(IndustryResource industryResource) {
        this.industryResource = industryResource;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_resource_choice_id")
    public IndustryResourceChoice getIndustryResourceChoice() {
        return industryResourceChoice;
    }

    public void setIndustryResourceChoice(IndustryResourceChoice industryResourceChoice) {
        this.industryResourceChoice = industryResourceChoice;
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
