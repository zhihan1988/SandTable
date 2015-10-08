package com.rathink.ie.manufacturing;

/**
 * Created by Hean on 2015/10/8.
 */
public enum EProduceLineType {
    MANUAL,AUTOMATIC,HALF_AUTOMATIC,FLEXBILITY;

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
