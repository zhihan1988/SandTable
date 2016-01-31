package com.rathink.ix.internet.service;

import com.rathink.ix.ibase.property.model.CompanyTerm;

import java.util.Map;

/**
 * Created by Hean on 2015/10/6.
 */
public interface InternetWorkManager {
    void fireHuman(String companyInstructionId);

    Map<String, String> getCompanyTermReport(CompanyTerm companyTerm);
}
