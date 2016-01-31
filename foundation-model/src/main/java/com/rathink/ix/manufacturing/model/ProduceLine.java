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
@DiscriminatorValue(value = "PRODUCE_LINE")
public class ProduceLine extends CompanyPart {


    private String produceLineType;//产品线类型
    private String produceType;//生产类型
    private String lineBuildNeedCycle;//距离生产线建设完成所需的周期
    private String produceNeedCycle;//距离生产完某产品所需的周期

    @Column(name = "value")
    public String getProduceLineType() {
        return produceLineType;
    }

    public void setProduceLineType(String produceLineType) {
        this.produceLineType = produceLineType;
    }

    @Column(name = "value2")
    public String getProduceType() {
        return produceType;
    }

    public void setProduceType(String produceType) {
        this.produceType = produceType;
    }

    @Column(name = "value3")
    public Integer getLineBuildNeedCycle() {
        return lineBuildNeedCycle == null ? 0 : Integer.valueOf(lineBuildNeedCycle);
    }

    public void setLineBuildNeedCycle(Integer lineBuildNeedCycle) {
        this.lineBuildNeedCycle = lineBuildNeedCycle == null ? null : String.valueOf(lineBuildNeedCycle);
    }

    @Column( name = "value4")
    public Integer getProduceNeedCycle() {
        return produceNeedCycle == null ? 0 : Integer.valueOf(produceNeedCycle);
    }

    public void setProduceNeedCycle(Integer produceNeedCycle) {
        this.produceNeedCycle = produceNeedCycle == null ? null : String.valueOf(produceNeedCycle);
    }

    public enum Status {
        UN_BUILD,BUILDING,BUILT,FREE,PRODUCING, REBUILDING
    }

    public enum Type {
        MANUAL(5,0,3,0,1,1),HALF_AUTOMATIC(4,2,2,1,1,2),AUTOMATIC(4,4,1,2,1,4),FLEXBILITY(6, 4, 1, 0, 1, 6),;

        Type(Integer perBuildDevotion, Integer installCycle, Integer produceCycle, Integer transferCycle, Integer mainternanceFee, Integer sellValue) {
            this.perBuildDevotion = perBuildDevotion;
            this.installCycle = installCycle;
            this.produceCycle = produceCycle;
            this.transferCycle = transferCycle;
            this.mainternanceFee = mainternanceFee;
            this.sellValue = sellValue;
        }
        private Integer perBuildDevotion;//每期建造成本
        private Integer installCycle;//安装周期
        private Integer produceCycle;//生产周期
        private Integer transferCycle;//转产周期
        private Integer mainternanceFee;//维护费用
        private Integer sellValue;//出售残值

        public Integer getPerBuildDevotion() {
            return perBuildDevotion;
        }

        public void setPerBuildDevotion(Integer perBuildDevotion) {
            this.perBuildDevotion = perBuildDevotion;
        }

        public Integer getInstallCycle() {
            return installCycle;
        }

        public void setInstallCycle(Integer installCycle) {
            this.installCycle = installCycle;
        }

        public Integer getProduceCycle() {
            return produceCycle;
        }

        public void setProduceCycle(Integer produceCycle) {
            this.produceCycle = produceCycle;
        }

        public Integer getTransferCycle() {
            return transferCycle;
        }

        public void setTransferCycle(Integer transferCycle) {
            this.transferCycle = transferCycle;
        }

        public Integer getMainternanceFee() {
            return mainternanceFee;
        }

        public void setMainternanceFee(Integer mainternanceFee) {
            this.mainternanceFee = mainternanceFee;
        }

        public Integer getSellValue() {
            return sellValue;
        }

        public void setSellValue(Integer sellValue) {
            this.sellValue = sellValue;
        }
    }

}
