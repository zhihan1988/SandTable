package com.rathink.ie.ibase.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.service.CompanyTermManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public List<CompanyTerm> listCompanyTerm(String campaignId, String campaignDate) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTerm where campaign.id = :campaignId and campaignDate = :campaignDate");
        xQuery.put("campaignId", campaignId);
        xQuery.put("campaignDate", campaignDate);
        List companyTermList = baseManager.listObject(xQuery);
        return companyTermList;
    }

    /**
     * 按部门分离公司属性
     * @param companyTermPropertyList
     * @return
     */
    @Override
    public Map<String, List<CompanyTermProperty>> partCompanyTermPropertyByDept(List<CompanyTermProperty> companyTermPropertyList) {
        Map<String, List<CompanyTermProperty>> map = new LinkedHashMap<>();
        if (companyTermPropertyList != null && !companyTermPropertyList.isEmpty()) {
            for (CompanyTermProperty companyTermProperty : companyTermPropertyList) {
                String dept = companyTermProperty.getDept();
                if (map.containsKey(dept)) {
                    map.get(dept).add(companyTermProperty);
                } else {
                    List<CompanyTermProperty> deptCompanyTermPropertyList = new ArrayList<CompanyTermProperty>();
                    deptCompanyTermPropertyList.add(companyTermProperty);
                    map.put(dept, deptCompanyTermPropertyList);
                }
            }
        }
        return map;
    }
}
