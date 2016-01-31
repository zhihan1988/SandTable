package com.rathink.ix.ibase.service;

import com.rathink.ix.ibase.work.model.IndustryResourceChoice;

import java.util.List;
import java.util.Set;

/**
 * Created by Hean on 2015/10/5.
 */
public interface IndustryResourceChoiceManager {
    List<IndustryResourceChoice> listIndustryResourceChoice(String industryResourceId);

    List<IndustryResourceChoice> listIndustryResourceChoice(String industryResourceId, Set<String> notInIds);
}
