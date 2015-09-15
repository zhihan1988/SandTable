package com.rathink.ie.ibase.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;

import java.util.List;

/**
 * Created by Hean on 2015/9/4.
 */
public interface CompanyTermManager {
    CompanyTerm getCompanyTerm(Company company, String campaignDate);

    CompanyTermProperty getCompanyStatusProperty(String propertyName, CompanyTerm companyTerm);

    List<CompanyTerm> listCompanyTerm(String campaignId, String campaignDate);
}
