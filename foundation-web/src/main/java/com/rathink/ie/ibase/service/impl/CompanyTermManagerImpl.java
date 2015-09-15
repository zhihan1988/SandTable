package com.rathink.ie.ibase.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.CompanyTermManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Hean on 2015/8/25.
 */
@Service
public class CompanyTermManagerImpl implements CompanyTermManager {
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
    public CompanyTermProperty getCompanyStatusProperty(String propertyName, CompanyTerm companyTerm){
        String hql = "from CompanyStatusPropertyValue where name = :name and companyTerm.id = :companyStatusId";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("name", propertyName);
        queryParamMap.put("companyStatusId", companyTerm.getId());
        CompanyTermProperty companyTermProperty = (CompanyTermProperty) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        return companyTermProperty;
    }

    public List<CompanyTerm> listCompanyTerm(String campaignId, String campaignDate) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTerm where campaign.id = :campaignId and campaignDate = :campaignDate");
        xQuery.put("campaignId", campaignId);
        xQuery.put("campaignDate", campaignDate);
        List companyTermList = baseManager.listObject(xQuery);
        return companyTermList;
    }

}
