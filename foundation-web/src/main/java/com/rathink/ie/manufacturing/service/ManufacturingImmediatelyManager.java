package com.rathink.ie.manufacturing.service;

import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.manufacturing.model.ProduceLine;

import java.util.Map;

/**
 * Created by Hean on 2015/10/25.
 */
public interface ManufacturingImmediatelyManager {

    //建造生产线
    Map processProductLineBuild(String companyTermId, String partId, String produceType, String lineType);

    //继续建造生产线
    Map processProductLineContinueBuild(String companyTermId, String partId);

    //开始生产
    Map processProduce(CompanyTerm companyTerm, ProduceLine produceLine);

    //交付订单
    Map processDeliveredOrder(String companyTermId, String orderId);

    //贷款
    Map loan(String companyTermId, String choiceId, String fee, String type);
}
