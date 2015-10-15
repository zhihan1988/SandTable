package com.rathink.ie.manufacturing.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.manufacturing.model.Product;
import com.rathink.ie.manufacturing.service.ProductManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * Created by Hean on 2015/10/15.
 */
@Service
public class ProductManagerImpl implements ProductManager {
    @Autowired
    private BaseManager baseManager;

    @Override
    public Product getProduct(Company company, String type) {
        String hql = "from Product where company.id=:companyId and type = :type";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("type", type);
        Product product = (Product) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        return product;
    }
}
