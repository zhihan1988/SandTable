package com.rathink.ie.ibase.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyStatusProperty;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.service.CompanyStatusManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * Created by Hean on 2015/8/25.
 */
@Service
public class CompanyStatusManagerImpl implements CompanyStatusManager {
    @Autowired
    private BaseManager baseManager;

    @Override
    public CompanyTerm getCompanyTerm(Company company, String campaignDate) {
        String hql = "from CompanyTerm where company.id = :companyId and campaignDate = :campaignDate";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("campaignDate", campaignDate);
        CompanyTerm companyTerm = (CompanyTerm) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        return companyTerm;
    }

    @Override
    public CompanyStatusProperty getCompanyStatusProperty(String propertyName, CompanyTerm companyTerm){
        String hql = "from CompanyStatusPropertyValue where name = :name and companyTerm.id = :companyStatusId";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("name", propertyName);
        queryParamMap.put("companyStatusId", companyTerm.getId());
        CompanyStatusProperty companyStatusProperty = (CompanyStatusProperty) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        return companyStatusProperty;
    }

}
