package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.util.CampaignUtil;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.service.InternetPropertyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Hean on 2015/8/30.
 */
@Service
public class InternetPropertyManagerImpl implements InternetPropertyManager {
    @Autowired
    private BaseManager baseManager;

    public List<CompanyTermProperty> listCompanyTermProperty(CompanyTerm companyTerm) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTermProperty where companyTerm.id = :companyTermId");
        xQuery.put("companyTermId", companyTerm.getId());
        List<CompanyTermProperty> companyTermPropertyList = baseManager.listObject(xQuery);
        return companyTermPropertyList;
    }

    /**
     * 按部门分离公司属性
     * @param companyTermPropertyList
     * @return
     */
    @Override
    public Map<String, List<CompanyTermProperty>> partCompanyTermPropertyByDept(List<CompanyTermProperty> companyTermPropertyList) {
        companyTermPropertyList.sort(new CompanyTermPropertyComparator());
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
        Map<String, Map<String, Integer>> propertyReport = new LinkedHashMap<>();

        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTerm where company.id = :companyId order by campaignDate asc");
        xQuery.put("companyId", company.getId());
        List<CompanyTerm> companyTermList = baseManager.listObject(xQuery);
        for (int i = 0; i < companyTermList.size() - 1; i++) {
            CompanyTerm companyTerm = companyTermList.get(i);
            Map<String, Integer> propertyMap = new LinkedHashMap<>();
            List<CompanyTermProperty> companyTermPropertyList = companyTerm.getCompanyTermPropertyList();
            if (companyTermPropertyList != null && companyTermPropertyList.size() > 0) {
                companyTermPropertyList.sort(new CompanyTermPropertyComparator());
                for (CompanyTermProperty companyTermProperty : companyTermPropertyList) {
                    propertyMap.put(EPropertyName.valueOf(companyTermProperty.getName()).getLabel(), companyTermProperty.getValue());
                }
            }
            propertyReport.put(CampaignUtil.getNextCampaignDate(companyTerm.getCampaignDate()), propertyMap);
        }
        return propertyReport;
    }

    class CompanyTermPropertyComparator implements Comparator<CompanyTermProperty> {
        @Override
        public int compare(CompanyTermProperty o1, CompanyTermProperty o2) {
            Integer order1 = EPropertyName.valueOf(o1.getName()).ordinal();
            Integer order2 = EPropertyName.valueOf(o2.getName()).ordinal();
            return order1 - order2;
        }
    }
}
