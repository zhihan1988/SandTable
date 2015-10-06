package com.rathink.ie.ibase.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.CampaignDateUtil;
import com.rathink.ie.foundation.campaign.model.Industry;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.service.PropertyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Hean on 2015/10/6.
 */
@Service
public class PropertyManagerImpl implements PropertyManager {

    @Autowired
    private BaseManager baseManager;

    public CompanyTermProperty getCompanyTermProperty(CompanyTerm companyTerm, String propertyName) {
        String hql = "from CompanyTermProperty where name = :name and companyTerm.id = :companyTermId";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyTermId", companyTerm.getId());
        queryParamMap.put("name", propertyName);
        CompanyTermProperty companyTermProperty = (CompanyTermProperty) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        return companyTermProperty;
    }

    public List<CompanyTermProperty> listCompanyTermProperty(CompanyTerm companyTerm) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTermProperty where companyTerm.id = :companyTermId");
        xQuery.put("companyTermId", companyTerm.getId());
        List<CompanyTermProperty> companyTermPropertyList = baseManager.listObject(xQuery);
        return companyTermPropertyList;
    }

    public Map<String, List<CompanyTermProperty>> partCompanyTermPropertyByDept(List<CompanyTermProperty> companyTermPropertyList) {
        return this.partCompanyTermPropertyByDept(companyTermPropertyList, null);
    }

    public Map<String, List<CompanyTermProperty>> partCompanyTermPropertyByDept(List<CompanyTermProperty> companyTermPropertyList, Comparator<CompanyTermProperty> companyTermPropertyComparator) {
        if( companyTermPropertyComparator!=null) companyTermPropertyList.sort(companyTermPropertyComparator);
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

    public Map<String, Map<String, Integer>> getPropertyReport(Company company) {
        return this.getPropertyReport(company, null);
    }

    public Map<String, Map<String, Integer>> getPropertyReport(Company company, Comparator<CompanyTermProperty> companyTermPropertyComparator) {
        Map<String, Map<String, Integer>> propertyReport = new LinkedHashMap<>();
        Industry industry = (Industry) baseManager.getObject(Industry.class.getName(), company.getCampaign().getIndustry().getId());
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTerm where company.id = :companyId order by campaignDate asc");
        xQuery.put("companyId", company.getId());
        List<CompanyTerm> companyTermList = baseManager.listObject(xQuery);
        for (int i = 0; i < companyTermList.size() - 1; i++) {
            CompanyTerm companyTerm = companyTermList.get(i);
            Map<String, Integer> propertyMap = new LinkedHashMap<>();
            List<CompanyTermProperty> companyTermPropertyList = listCompanyTermProperty(companyTerm);
            if (companyTermPropertyList != null && companyTermPropertyList.size() > 0) {
                if(companyTermPropertyComparator!=null) companyTermPropertyList.sort(companyTermPropertyComparator);
                for (CompanyTermProperty companyTermProperty : companyTermPropertyList) {
                    propertyMap.put(companyTermProperty.getName(), companyTermProperty.getValue());
                }
            }
            String formatCampaignDate = CampaignDateUtil.formatCampaignDate(companyTerm.getCampaignDate(), industry.getTerm());
            propertyReport.put(formatCampaignDate, propertyMap);
        }
        return propertyReport;
    }
}
