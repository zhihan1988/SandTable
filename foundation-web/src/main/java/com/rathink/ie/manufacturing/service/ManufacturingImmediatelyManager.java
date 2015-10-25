package com.rathink.ie.manufacturing.service;

import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.manufacturing.model.ProduceLine;

import java.util.Map;

/**
 * Created by Hean on 2015/10/25.
 */
public interface ManufacturingImmediatelyManager {

    Map processProductLineBuild(String companyTermId, String partId, String produceType, String lineType);


    Map processProductLineContinueBuild(String companyTermId, String partId);

    //开始生产
    Map processProduce(CompanyTerm companyTerm, ProduceLine produceLine);
}
