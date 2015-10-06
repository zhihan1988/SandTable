package com.rathink.ie.foundation.campaign.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "campaign")
    public class Campaign {

    private String id;
    private String name;
    private String status; //1 未开始 2进行中  3一结束  0 删除
    private Industry industry;
    private String mode;//private public
    private String password;
    private Date createDatetime;// 创建时间
    private Date startDatetime;// 游戏开始时间
    private Integer currentCampaignDate;//当前进度 年/季度/月份 例如010205

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

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ManyToOne(fetch = FetchType.EAGER)
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

    @Column(name = "create_datetime")
    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    @Column(name = "start_datetime")
    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    @Column(name = "current_campaign_date")
    public Integer getCurrentCampaignDate() {
        return currentCampaignDate;
    }

    public void setCurrentCampaignDate(Integer currentCampaignDate) {
        this.currentCampaignDate = currentCampaignDate;
    }

    public enum Status {
        PREPARE("1","未开始"),RUN("2","进行中"), EDN("3","已结束");
        private String value;
        private String label;
        private Status(String value, String label){
            this.value = value;
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    @Transient
    public String getStatusLabel(){
        String label = "";
        for (Status status: Status.values()) {
            if (status.getValue().equals(getStatus())) {
                label = status.getLabel();
            }
        }
        return label;
    }

    @Transient
    public Integer getPreCampaignDate() {
        return CampaignDateUtil.getPreCampaignDate(currentCampaignDate);
    }

    @Transient
    public Integer getNextCampaignDate() {
        return CampaignDateUtil.getNextCampaignDate(currentCampaignDate);
    }

    @Transient
    public String getFormattedCampaignDate() {
        return CampaignDateUtil.formatCampaignDate(currentCampaignDate, industry.getTerm());
    }

}


