package com.rathink.ie.ibase.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/9/4.
 */
public interface PropertyManager {
    CompanyTermProperty getCompanyTermProperty(CompanyTerm companyTerm, String propertyName);

    List<CompanyTermProperty> listCompanyTermProperty(CompanyTerm companyTerm);

    Map<String, List<CompanyTermProperty>> partCompanyTermPropertyByDept(List<CompanyTermProperty> companyTermPropertyList);

    Map<String, List<CompanyTermProperty>> partCompanyTermPropertyByDept(List<CompanyTermProperty> companyTermPropertyList, Comparator<CompanyTermProperty> companyTermPropertyComparator);

    Map<String, Map<String, Integer>> getPropertyReport(Company company);

    Map<String, Map<String, Integer>> getPropertyReport(Company company,  Comparator<CompanyTermProperty> companyTermPropertyComparator);
}
