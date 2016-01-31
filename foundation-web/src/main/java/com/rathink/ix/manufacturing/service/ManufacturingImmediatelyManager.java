package com.rathink.ix.manufacturing.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ix.ibase.property.model.CompanyTerm;
import com.rathink.ix.manufacturing.model.ProduceLine;

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

    //最大的可贷金额
    Integer getMaxLoanMoney(Company company, String loanType);

    //贷款
    Map loan(String companyTermId, String choiceId, String fee, String type);
}
