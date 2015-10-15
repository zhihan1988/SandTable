package com.rathink.ie.manufacturing.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.manufacturing.model.Material;
import com.rathink.ie.manufacturing.service.MaterialManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * Created by Hean on 2015/10/11.
 */
@Service
public class MaterialManagerImpl implements MaterialManager {

    @Autowired
    private BaseManager baseManager;

    @Override
    public Material getMateral(Company company, String type) {
        String hql = "from Material where company.id=:companyId and type = :type";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("type", type);
        Material material = (Material) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        return material;
    }

    @Override
    public Integer getMateralAmount(Company company, String type) {
        Integer amount = 0;
        String hql = "from Material where company.id=:companyId and type = :type";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("type", type);
        Material material = (Material) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        if (material != null) {
            amount = material.getAmount();
        }
        return amount;
    }
}
