package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.ibase.property.model.CompanyStatusProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.CompanyTermManager;
import com.rathink.ie.internet.service.InternetPropertyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hean on 2015/8/30.
 */
@Service
public class InternetPropertyManagerImpl implements InternetPropertyManager {
    @Autowired
    private BaseManager baseManager;

    public List<CompanyStatusProperty> listCompanyStatusProperty(CompanyTerm companyTerm) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyStatusProperty where companyTerm.id = :companyTermId");
        xQuery.put("companyTermId", companyTerm.getId());
        List<CompanyStatusProperty> companyStatusPropertyList = baseManager.listObject(xQuery);
        return companyStatusPropertyList;
    }
}
