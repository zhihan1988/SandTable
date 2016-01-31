package com.rathink.ix.ibase.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ix.ibase.work.model.CompanyPart;

import java.util.List;

/**
 * Created by Hean on 2015/10/9.
 */
public interface CompanyPartManager {
    List<CompanyPart> listCompanyPart(Company company, String baseType);

    Object getUniqueCompanyPart(Company company, String name, String entityName);
}
