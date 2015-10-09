package com.rathink.ie.ibase.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.work.model.CompanyPart;

import java.util.List;

/**
 * Created by Hean on 2015/10/9.
 */
public interface CompanyPartManager {
    List<CompanyPart> companyPartList(Company company);
}
