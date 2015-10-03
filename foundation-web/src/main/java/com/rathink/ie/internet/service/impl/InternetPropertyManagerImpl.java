package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.util.CampaignUtil;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.service.AccountManager;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.service.InstructionManager;
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
    @Autowired
    private InstructionManager instructionManager;
    @Autowired
    private AccountManager accountManager;

    public CompanyTermProperty getCompanyTermProperty(CompanyTerm companyTerm, EPropertyName ePropertyName) {
        String hql = "from CompanyTermProperty where name = :name and companyTerm.id = :companyTermId";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyTermId", companyTerm.getId());
        queryParamMap.put("name", ePropertyName.name());
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
            List<CompanyTermProperty> companyTermPropertyList = listCompanyTermProperty(companyTerm);
            if (companyTermPropertyList != null && companyTermPropertyList.size() > 0) {
                companyTermPropertyList.sort(new CompanyTermPropertyComparator());
                for (CompanyTermProperty companyTermProperty : companyTermPropertyList) {
                    propertyMap.put(EPropertyName.valueOf(companyTermProperty.getName()).getLabel(), companyTermProperty.getValue());
                }
            }
            String formatCampaignDate = CampaignUtil.formatCampaignDate(companyTerm.getCampaignDate());
            propertyReport.put(formatCampaignDate, propertyMap);
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

    public Map<String, String> getCompanyTermReport(CompanyTerm companyTerm) {
        Map<String, String> companyTermReport = new HashMap<>();
        Company company = companyTerm.getCompany();
        List<CompanyTermInstruction> hrInstructionList = instructionManager.listCompanyInstruction(company, EChoiceBaseType.HUMAN.name());
        int count = hrInstructionList == null ? 0 : hrInstructionList.size();
        companyTermReport.put("在职人数", String.valueOf(count));

        CompanyTermInstruction productStudyInstruction = instructionManager.getUniqueInstructionByBaseType(companyTerm, EChoiceBaseType.PRODUCT_STUDY.name());
        String productName = productStudyInstruction == null ? "" : productStudyInstruction.getCampaignTermChoice().getName();
        companyTermReport.put("产品定位", productName);

        List<CompanyTermInstruction> marketInstructionList = instructionManager.listCompanyInstruction(companyTerm, EChoiceBaseType.MARKET_ACTIVITY.name());
        String marketActivitys = "";
        if (marketInstructionList != null) {
            for (CompanyTermInstruction companyTermInstruction : marketInstructionList) {
                marketActivitys += companyTermInstruction.getCampaignTermChoice().getName();
            }
        }
        companyTermReport.put("市场营销", marketActivitys);

        Integer campaignDateInCash = accountManager.countAccountEntryFee(
                company, companyTerm.getCampaignDate(), EAccountEntityType.COMPANY_CASH.name(), "1");
        companyTermReport.put("上期收入", String.valueOf(campaignDateInCash));

        //上期用户数
        CompanyTermProperty userAmountProperty = getCompanyTermProperty(companyTerm, EPropertyName.USER_AMOUNT);
        Integer oldUserAmount = userAmountProperty.getValue();
        companyTermReport.put("上期用户数", String.valueOf(oldUserAmount));
        return companyTermReport;
    }

}
