package com.rathink.ie.foundation.service;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;

import com.rathink.ie.campaign.model.Campaign;
import com.rathink.ie.foundation.util.CampaignUtil;
import com.rathink.ie.ibase.property.model.CompanyStatus;
import com.rathink.ie.ibase.property.model.CompanyStatusPropertyValue;
import com.rathink.ie.internet.Edept;
import com.rathink.ie.team.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Created by Hean on 2015/8/24.
 */
@Service
public class WorkService {

    @Autowired
    private BaseManager baseManager;
    @Autowired
    private ChoiceService choiceService;

    public void initCampaign(Campaign campaign) {
        //1.比赛开始
        campaign.setCurrentCampaignDate(CampaignUtil.getCurrentCampaignDate());
        campaign.setStatus(Campaign.Status.RUN.getValue());
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);
        //2.初始化各公司基本属性
        XQuery xQuery = new XQuery();
        xQuery.setHql("from Company where campaign.id = :campaignId");
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<String, Object>();
        queryParamMap.put("campaignId", campaign.getId());
        xQuery.setQueryParamMap(queryParamMap);
        List<Company> companyList = baseManager.listObject(xQuery);
        for (Company company : companyList) {
            initCompanyStatus(company);
        }
        //3.准备供用户决策用的随机数据
        choiceService.produceChoice(campaign);

    }

    public void initCompanyStatus(Company company) {
        CompanyStatus companyStatus = new CompanyStatus();
        companyStatus.setCampaign(company.getCampaign());
        companyStatus.setCompany(company);
        companyStatus.setCampaignDate(company.getCampaign().getCurrentCampaignDate());
        List<CompanyStatusPropertyValue> companyStatusPropertyValueList = prepareCompanyStatusProperty(companyStatus);
        companyStatus.setCompanyStatusPropertyValueList(companyStatusPropertyValueList);
        baseManager.saveOrUpdate(CompanyStatus.class.getName(), companyStatus);
    }

    public List<CompanyStatusPropertyValue> prepareCompanyStatusProperty(CompanyStatus companyStatus) {
        List<CompanyStatusPropertyValue> companyStatusPropertyValueList = new ArrayList<CompanyStatusPropertyValue>();
        companyStatusPropertyValueList.add(new CompanyStatusPropertyValue("USER_AMOUNT", "2000", Edept.OPERATION.name(), companyStatus));
        companyStatusPropertyValueList.add(new CompanyStatusPropertyValue("OPERATION_ABILITY", "2000", Edept.OPERATION.name(), companyStatus));
        companyStatusPropertyValueList.add(new CompanyStatusPropertyValue("MARKET_ABILITY", "2000", Edept.MARKET.name(), companyStatus));
        companyStatusPropertyValueList.add(new CompanyStatusPropertyValue("PRODUCT_ABILITY", "2000", Edept.PRODUCT.name(), companyStatus));
        return companyStatusPropertyValueList;
    }

    /**
     * 按部门分离公司属性
     * @param companyStatusPropertyValueList
     * @return
     */
    public Map<String, List<CompanyStatusPropertyValue>> partCompanyStatusPropertyByDept(List<CompanyStatusPropertyValue> companyStatusPropertyValueList) {
        Map<String, List<CompanyStatusPropertyValue>> map = new HashMap();
        if (companyStatusPropertyValueList != null && !companyStatusPropertyValueList.isEmpty()) {
            for (CompanyStatusPropertyValue companyStatusPropertyValue : companyStatusPropertyValueList) {
                String dept = companyStatusPropertyValue.getDept();
                if (map.containsKey(dept)) {
                    map.get(dept).add(companyStatusPropertyValue);
                } else {
                    List<CompanyStatusPropertyValue> deptCompanyStatusPropertyValueList = new ArrayList<CompanyStatusPropertyValue>();
                    deptCompanyStatusPropertyValueList.add(companyStatusPropertyValue);
                    map.put(dept, deptCompanyStatusPropertyValueList);
                }
            }
        }
        return map;
    }

    public void nextCampaign(Campaign campaign) {
        //回合结束
        String oldCampaignDate = campaign.getCurrentCampaignDate();
        campaign.setCurrentCampaignDate(CampaignUtil.getNextCampaignDate(oldCampaignDate));
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);

        //计算这一回合各公司属性
        XQuery xQuery = new XQuery();
        xQuery.setHql("from Company where campaign.id = :campaignId");
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<String, Object>();
        queryParamMap.put("campaignId", campaign.getId());
        xQuery.setQueryParamMap(queryParamMap);
        List<Company> companyList = baseManager.listObject(xQuery);
        for (Company company : companyList) {

        }
    }


}
