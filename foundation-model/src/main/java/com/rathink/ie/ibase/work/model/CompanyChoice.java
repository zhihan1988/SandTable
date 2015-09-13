package com.rathink.ie.ibase.work.model;

import com.rathink.ie.foundation.campaign.model.Campaign;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "company_choice")
@Inheritance(strategy=InheritanceType.JOINED)
public class CompanyChoice {
    private String id;
    private String baseType;//表示是一个什么类型的选择 例如HumanChoice or OfficeChoice
    private String type;//提供给子类使用的类型字段 每个类型有不同的定义
    private CampaignChance campaignChance;
    private String campaignDate;
    private Campaign campaign;
    private String dept;//哪个部门的选项
    private String fees;
    private String randomHigh;
    private String randomLow;
    private String name;
    private String value;
    private String value2;
    private String description;

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

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Column(name = "fees")
    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    @Column(name = "random_high")
    public String getRandomHigh() {
        return randomHigh;
    }

    public void setRandomHigh(String randomHigh) {
        this.randomHigh = randomHigh;
    }

    @Column(name = "random_low")
    public String getRandomLow() {
        return randomLow;
    }

    public void setRandomLow(String randomLow) {
        this.randomLow = randomLow;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "value2")
    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
