package com.rathink.ie.foundation.team.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "company")
public class Company extends Team {

    private String currentCampaignDate;
    private String slogan;
    private String memo;

    @Column(name = "current_campaign_date")
    public String getCurrentCampaignDate() {
        return currentCampaignDate;
    }

    public void setCurrentCampaignDate(String currentCampaignDate) {
        this.currentCampaignDate = currentCampaignDate;
    }

    @Column(name = "slogan")
    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    @Column(name = "memo")
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}
