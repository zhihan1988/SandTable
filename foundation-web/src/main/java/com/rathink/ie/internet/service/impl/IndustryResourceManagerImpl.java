package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.ibase.work.model.IndustryResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * Created by Hean on 2015/10/5.
 */
@Service
public class IndustryResourceManagerImpl implements com.rathink.ie.internet.service.IndustryResourceManager {
    @Autowired
    private BaseManager baseManager;

    @Override
    public IndustryResource getUniqueIndustryResource(String industryId, String name) {
        String hql = "from IndustryResource where industry.id=:industryId and name = :name";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("industryId", industryId);
        queryParamMap.put("name", name);
        IndustryResource industryResource = (IndustryResource) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        return industryResource;
    }

}
