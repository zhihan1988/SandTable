package com.rathink.ix.manufacturing.model;

import com.rathink.ix.ibase.work.model.CompanyPart;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Hean on 2015/10/10.
 */
@Entity
@Table(name = "company_part")
@DiscriminatorValue(value = "MARKET")
public class Market extends CompanyPart {
    private String type;
    private String devotionNeedCycle;//剩余开发周期

    @Column(name = "value")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "value2")
    public Integer getDevotionNeedCycle() {
        return devotionNeedCycle == null ? 0 : Integer.valueOf(devotionNeedCycle);
    }

    public void setDevotionNeedCycle(Integer devotionNeedCycle) {
        this.devotionNeedCycle = devotionNeedCycle == null ? null : String.valueOf(devotionNeedCycle);
    }

    public enum Status {
        UNDEVELOPED, DEVELOPING, DEVELOPED,
    }

    public enum Type {
        LOCAL(0,0),AREA(1,1),DOMESTIC(2,1),INTERNATIONAL(4, 1);

        Type(Integer devotionNeedCycle, Integer perDevotion) {
            this.devotionNeedCycle = devotionNeedCycle;
            this.perDevotion = perDevotion;
        }

        private Integer devotionNeedCycle;//开发周期
        private Integer perDevotion;//每期的投入

        public Integer getDevotionNeedCycle() {
            return devotionNeedCycle;
        }

        public void setDevotionNeedCycle(Integer devotionNeedCycle) {
            this.devotionNeedCycle = devotionNeedCycle;
        }

        public Integer getPerDevotion() {
            return perDevotion;
        }

        public void setPerDevotion(Integer perDevotion) {
            this.perDevotion = perDevotion;
        }
    }

}
