package com.rathink.ie.campaign.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;


@Entity
@Table(name = "campaign")
    public class Campaign {

    private String id;
    private String name;
    private Industry industry;
    private String mode;//private public
    private String password;
    private String currentCampaignDate;//当前进度 年/季度/月份 例如010205

    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_id")
    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    @Column(name = "mode")
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Column(name = "current_campaign_date")
    public String getCurrentCampaignDate() {
        return currentCampaignDate;
    }

    public void setCurrentCampaignDate(String currentCampaignDate) {
        this.currentCampaignDate = currentCampaignDate;
    }

}


