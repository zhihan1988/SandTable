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


    private String produceLineType;//��Ʒ������
    private String produceType;//��������
    private String lineBuildNeedCycle;//���������߽���������������
    private String produceNeedCycle;//����������ĳ��Ʒ���������

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
        UN_BUILD,BUILDING,FREE,PRODUCING
    }

    public enum Type {
        MANUAL(1,0,3,0,1,1),HALF_AUTOMATIC(1,1,2,1,1,2),AUTOMATIC(1,2,1,2,1,4),FLEXBILITY(1, 3, 1, 0, 1, 6),;

        Type(Integer cost, Integer installCycle, Integer produceCycle, Integer transferCycle, Integer mainternanceFee, Integer sellValue) {
            this.cost = cost;
            this.installCycle = installCycle;
            this.produceCycle = produceCycle;
            this.transferCycle = transferCycle;
            this.mainternanceFee = mainternanceFee;
            this.sellValue = sellValue;
        }
        private Integer cost;//����۸�
        private Integer installCycle;//��װ����
        private Integer produceCycle;//��������
        private Integer transferCycle;//ת������
        private Integer mainternanceFee;//ά������
        private Integer sellValue;//���۲�ֵ

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
