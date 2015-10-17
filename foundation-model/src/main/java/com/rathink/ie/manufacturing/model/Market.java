package com.rathink.ie.manufacturing.model;

import com.rathink.ie.ibase.work.model.CompanyPart;

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
    private String devotionNeedCycle;//ʣ��Ŀ�������

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
        NORMAL
    }

    public enum Type {
        LOCAL(0),AREA(1),DOMESTIC(2),ASIA(3),INTERNATIONAL(4);

        Type(Integer devotionNeedCycle) {
            this.devotionNeedCycle = devotionNeedCycle;
        }

        private Integer devotionNeedCycle;

        public Integer getDevotionNeedCycle() {
            return devotionNeedCycle;
        }

        public void setDevotionNeedCycle(Integer devotionNeedCycle) {
            this.devotionNeedCycle = devotionNeedCycle;
        }
    }

}