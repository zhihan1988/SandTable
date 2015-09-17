package com.rathink.ie.internet.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;

import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/9/4.
 */
public interface InternetPropertyManager {
    List<CompanyTermProperty> listCompanyTermProperty(CompanyTerm companyTerm);

    Map<String, List<CompanyTermProperty>> partCompanyTermPropertyByDept(List<CompanyTermProperty> companyTermPropertyList);

    Map<String, Map<String, Double>> getPropertyReport(Company company);
}
