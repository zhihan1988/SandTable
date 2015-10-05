package com.rathink.ie.internet.service;

import com.rathink.ie.ibase.work.model.IndustryResource;

/**
 * Created by Hean on 2015/10/5.
 */
public interface IndustryResourceManager {
    IndustryResource getUniqueIndustryResource(String industryId, String name);
}
