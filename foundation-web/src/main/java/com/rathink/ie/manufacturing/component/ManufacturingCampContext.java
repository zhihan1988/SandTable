package com.rathink.ie.manufacturing.component;

import com.rathink.ie.base.component.CyclePublisher;
import com.rathink.ie.base.component.DevoteCycle;
import com.rathink.ie.ibase.component.BaseCampaignContext;
import com.rathink.ie.ibase.service.CompanyTermContext;
import com.rathink.ie.ibase.work.model.CompanyPart;
import com.rathink.ie.manufacturing.service.impl.ManufacturingCompanyTermContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2016/1/30.
 */
public class ManufacturingCampContext extends BaseCampaignContext<ManufacturingCompanyTermContext>{
    private Map<String, List<CompanyPart>> companyPartMap = new HashMap<>();
    private DevoteCycle devoteCycle;


    public Map<String, List<CompanyPart>> getCompanyPartMap() {
        return companyPartMap;
    }

    public void setCompanyPartMap(Map<String, List<CompanyPart>> companyPartMap) {
        this.companyPartMap = companyPartMap;
    }

    public DevoteCycle getDevoteCycle() {
        return devoteCycle;
    }

    public void setDevoteCycle(DevoteCycle devoteCycle) {
        this.devoteCycle = devoteCycle;
    }


    public void testIfManufacturingCampContext(){
        System.out.println("this is manufacturingCampContext");
    }
}
