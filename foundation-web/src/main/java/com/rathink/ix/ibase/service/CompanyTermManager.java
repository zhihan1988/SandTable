package com.rathink.ix.ibase.service;

import com.rathink.ix.ibase.property.model.CompanyTerm;

import java.util.List;

/**
 * Created by Hean on 2015/9/4.
 */
public interface CompanyTermManager {
    CompanyTerm getCompanyTerm(String companyId, Integer campaignDate);

    List<CompanyTerm> listCompanyTerm(String campaignId, Integer campaignDate);

}
