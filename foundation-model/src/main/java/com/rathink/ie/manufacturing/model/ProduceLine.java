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
@DiscriminatorValue(value = "PRODUCE_LINE")
public class ProduceLine extends CompanyPart {


    private String produceLineType;//产品线类型
    private String produceType;//生产类型

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


    /*public String getProduceLineType() {
            return super.getValue();
        }

        public void setProduceLineType(String produceLineType) {
            super.setValue(produceLineType);
        }

        public String getProduceType() {
            return super.getValue2();
        }

        public void setProduceType(String produceType) {
            super.setValue2(produceType);
        }
    */
    public enum Status {
        NOT_OWNED,USING,FREE
    }

    enum EProduceLineType {
        MANUAL(1,0,1,1,1,1),AUTOMATIC(1,2,1,1,1,1),HALF_AUTOMATIC(1,1,1,1,1,1), FLEXBILITY(1, 3, 1, 1, 1, 1),;

        EProduceLineType(Integer cost, Integer installCycle, Integer produceCycle, Integer transferCycle, Integer mainternanceFee, Integer sellValue) {
            this.cost = cost;
            this.installCycle = installCycle;
            this.produceCycle = produceCycle;
            this.transferCycle = transferCycle;
            this.mainternanceFee = mainternanceFee;
            this.sellValue = sellValue;
        }
        private Integer cost;//购买价格
        private Integer installCycle;//安装周期
        private Integer produceCycle;//生产周期
        private Integer transferCycle;//转产周期
        private Integer mainternanceFee;//维护费用
        private Integer sellValue;//出售残值

        public Integer getCost() {
            return cost;
        }

        public void setCost(Integer cost) {
            this.cost = cost;
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

    enum EProduceType{
        P1,P2,P3,P4
    }

}
