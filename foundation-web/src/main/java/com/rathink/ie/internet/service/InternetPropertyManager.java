package com.rathink.ie.internet.service;

import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;

import java.util.List;

/**
 * Created by Hean on 2015/9/4.
 */
public interface InternetPropertyManager {
    List<CompanyTermProperty> listCompanyStatusProperty(CompanyTerm companyTerm);
}
