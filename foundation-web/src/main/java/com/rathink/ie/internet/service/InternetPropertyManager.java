package com.rathink.ie.internet.service;

import com.rathink.ie.ibase.property.model.CompanyStatusProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;

import java.util.List;

/**
 * Created by Hean on 2015/9/4.
 */
public interface InternetPropertyManager {
    List<CompanyStatusProperty> listCompanyStatusProperty(CompanyTerm companyTerm);
}
