package com.rathink.ie.ibase.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.service.CompanyPartManager;
import com.rathink.ie.ibase.work.model.CompanyPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hean on 2015/10/9.
 */
@Service
public class CompanyPartManagerImpl implements CompanyPartManager {
    @Autowired
    private BaseManager baseManager;

    @Override
    public List<CompanyPart> listCompanyPart(Company company, String baseType) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyPart where company.id = :companyId and baseType = :baseType");
        xQuery.put("companyId", company.getId());
        xQuery.put("baseType", baseType);
        List<CompanyPart> companyPartList = baseManager.listObject(xQuery);
        return companyPartList;
    }
}
