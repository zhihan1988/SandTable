package com.rathink.ie.ibase.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyStatusProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;

import java.util.List;

/**
 * Created by Hean on 2015/9/4.
 */
public interface CompanyTermManager {
    CompanyTerm getCompanyTerm(Company company, String campaignDate);

    CompanyStatusProperty getCompanyStatusProperty(String propertyName, CompanyTerm companyTerm);

    public List<CompanyTerm> listCompanyTerm(String campaignId);
}
