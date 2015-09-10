package com.rathink.ie.ibase.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyStatusProperty;

/**
 * Created by Hean on 2015/9/4.
 */
public interface CompanyStatusManager {
    CompanyTerm getCompanyTerm(Company company, String campaignDate);

    CompanyStatusProperty getCompanyStatusProperty(String propertyName, CompanyTerm companyTerm);
}
