package com.rathink.ix.ibase.component;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ix.ibase.service.CompanyTermContext;
import com.rathink.ix.ibase.work.model.IndustryResource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hean on 2016/1/30.
 */
@Component
public class BaseCampaignContext<T extends CompanyTermContext> implements CampContext<T> {

    private Campaign campaign;
    private Map<String, T> companyTermContextMap = new HashMap<>();
    protected Map<String, IndustryResource> currentTypeIndustryResourceMap = new HashMap<>();//key:资源类型  value:资源
    private CyclePublisher cyclePublisher;

    @Override
    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Map<String, T> getCompanyTermContextMap() {
        return companyTermContextMap;
    }

    public void setCompanyTermContextMap(Map<String, T> companyTermContextMap) {
        this.companyTermContextMap = companyTermContextMap;
    }

    public T getCompanyTermContext(String companyId) {
        return companyTermContextMap.get(companyId);
    }

    public Map<String, IndustryResource> getCurrentTypeIndustryResourceMap() {
        return currentTypeIndustryResourceMap;
    }

    public void setCurrentTypeIndustryResourceMap(Map<String, IndustryResource> currentTypeIndustryResourceMap) {
        this.currentTypeIndustryResourceMap = currentTypeIndustryResourceMap;
    }

    public CyclePublisher getCyclePublisher() {
        return cyclePublisher;
    }

    public void setCyclePublisher(CyclePublisher cyclePublisher) {
        this.cyclePublisher = cyclePublisher;
    }
}
