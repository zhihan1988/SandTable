package com.rathink.ie.ibase.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.ibase.service.IndustryExpressionManager;
import com.rathink.ie.ibase.work.model.IndustryAnalyzer;
import com.rathink.ie.ibase.work.model.IndustryExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * Created by pgwt on 10/6/15.
 */
@Service
public class IndustryExpressionManagerImpl implements IndustryExpressionManager {

    @Autowired
    private BaseManager baseManager;

    public IndustryAnalyzer create(String abilityName) {
        String hql = "select obj from " + IndustryExpression.class.getName() + " obj where obj.name=:name";
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("name", abilityName);
        IndustryExpression industryExpression = (IndustryExpression) baseManager.getUniqueObjectByConditions(hql, param);
        return new IndustryAnalyzer(industryExpression.getExpression());
    }

}
