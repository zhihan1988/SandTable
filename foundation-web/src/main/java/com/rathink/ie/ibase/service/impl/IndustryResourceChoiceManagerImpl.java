package com.rathink.ie.ibase.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.ibase.work.model.IndustryResourceChoice;
import com.rathink.ie.ibase.service.IndustryResourceChoiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by Hean on 2015/10/5.
 */
@Service
public class IndustryResourceChoiceManagerImpl implements IndustryResourceChoiceManager {
    @Autowired
    private BaseManager baseManager;

    @Override
    public List<IndustryResourceChoice> listIndustryResourceChoice(String industryResourceId) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from IndustryResourceChoice where industryResource.id = :industryResourceId");
        xQuery.put("industryResourceId",industryResourceId);
        List<IndustryResourceChoice> industryResourceChoiceList = baseManager.listObject(xQuery);
        return industryResourceChoiceList;
    }

    public List<IndustryResourceChoice> listIndustryResourceChoice(String industryResourceId, Set<String> notInIds) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from IndustryResourceChoice where industryResource.id = :industryResourceId and id not in (:notInIds)");
        xQuery.put("industryResourceId", industryResourceId);
        xQuery.put("notInIds",notInIds);
        List<IndustryResourceChoice> industryResourceChoiceList = baseManager.listObject(xQuery);
        return industryResourceChoiceList;
    }

}
