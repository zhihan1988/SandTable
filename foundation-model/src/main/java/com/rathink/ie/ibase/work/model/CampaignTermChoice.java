package com.rathink.ie.ibase.work.model;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.campaign.model.Industry;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "campaign_term_choice")
public class CampaignTermChoice {
    private String id;
    private String baseType;
    private CampaignChance campaignChance;
    private Integer campaignDate;
    private Campaign campaign;
    private IndustryChoice industryChoice;

    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "base_type")
    public String getBaseType() {
        return baseType;
    }

    public void setBaseType(String baseType) {
        this.baseType = baseType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_chance_id")
    public CampaignChance getCampaignChance() {
        return campaignChance;
    }

    public void setCampaignChance(CampaignChance campaignChance) {
        this.campaignChance = campaignChance;
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
    @JoinColumn(name = "industry_choice_id")
    public IndustryChoice getIndustryChoice() {
        return industryChoice;
    }

    public void setIndustryChoice(IndustryChoice industryChoice) {
        this.industryChoice = industryChoice;
    }

    @Transient
    public String getType() {
        return industryChoice.getType();
    }

    @Transient
    public String getDept() {
        return industryChoice.getDept();
    }

    @Transient
    public String getFees() {
        return industryChoice.getFees();
    }

    @Transient
    public String getName() {
        return industryChoice.getName();
    }

    @Transient
    public String getValue() {
        return industryChoice.getValue();
    }

    @Transient
    public String getValue2() {
        return industryChoice.getValue2();
    }

    @Transient
    public String getDescription() {
        return industryChoice.getDescription();
    }

    @Transient
    public String getImg() {
        return industryChoice.getImg();
    }
}
