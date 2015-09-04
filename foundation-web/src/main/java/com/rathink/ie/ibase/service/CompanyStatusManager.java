package com.rathink.ie.ibase.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyStatus;
import com.rathink.ie.ibase.property.model.CompanyStatusPropertyValue;

/**
 * Created by Hean on 2015/9/4.
 */
public interface CompanyStatusManager {
    CompanyStatus getCompanyStatus(Company company, String campaignDate);

    CompanyStatusPropertyValue getCompanyStatusProperty(String propertyName, CompanyStatus companyStatus);
}
