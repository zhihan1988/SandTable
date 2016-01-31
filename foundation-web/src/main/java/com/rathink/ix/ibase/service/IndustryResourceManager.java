package com.rathink.ix.ibase.service;

import com.rathink.ix.ibase.work.model.IndustryResource;

/**
 * Created by Hean on 2015/10/5.
 */
public interface IndustryResourceManager {
    IndustryResource getUniqueIndustryResource(String industryId, String name);
}
