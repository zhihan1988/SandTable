package com.rathink.ie.ibase.work.model;

import com.rathink.ie.campaign.model.Campaign;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 机会    政策  经济形势 行业状况
 */
@Entity
@Table(name = "campaign_chance")
public class CampaignChance {
    private String id;
    private String type;                // 机会类型
    private String campaignDate;
    private Campaign campaign;
    private String dept;

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

    @Column(name = "dept")
    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }
}
