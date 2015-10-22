package com.rathink.ie.manufacturing;

/**
 * Created by Hean on 2015/10/7.
 */
public enum EManufacturingInstructionBaseType {
    //生产线修建
    PRODUCE_LINE_BUILD,
    //生产线继续修建
    PRODUCE_LINE_BUILD_CONTINUE,
    //生产
    PRODUCE,
    //产品研发投入
    PRODUCT_DEVOTION,
    //市场区域开发投入
    MARKET_DEVOTION,
    //订单投入
    MARKET_ORDER,
    //订单交付
    ORDER_DELIVER,
    //物料采购
    MATERIAL_PURCHASE,
    LONG_TERM_LOAN, SHORT_TERM_LOAN, USURIOUS_LOAN
}
