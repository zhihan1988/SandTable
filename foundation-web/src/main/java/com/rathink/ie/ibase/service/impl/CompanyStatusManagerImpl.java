package com.rathink.ie.ibase.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.ibase.property.model.CompanyStatus;
import com.rathink.ie.ibase.property.model.CompanyStatusPropertyValue;
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
    public CompanyStatus getCompanyStatus(Company company, String campaignDate) {
        String hql = "from CompanyStatus where company.id = :companyId and campaignDate = :campaignDate";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("campaignDate", campaignDate);
        CompanyStatus companyStatus = (CompanyStatus) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        return companyStatus;
    }

    @Override
    public CompanyStatusPropertyValue getCompanyStatusProperty(String propertyName, CompanyStatus companyStatus){
        String hql = "from CompanyStatusPropertyValue where name = :name and companyStatus.id = :companyStatusId";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("name", propertyName);
        queryParamMap.put("companyStatusId", companyStatus.getId());
        CompanyStatusPropertyValue companyStatusPropertyValue = (CompanyStatusPropertyValue) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        return companyStatusPropertyValue;
    }

}
