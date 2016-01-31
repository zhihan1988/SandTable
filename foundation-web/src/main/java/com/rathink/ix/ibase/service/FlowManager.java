package com.rathink.ix.ibase.service;

import com.rathink.ix.ibase.component.CampContext;

/**
 * Created by Hean on 2015/9/4.
 */
public interface FlowManager<T extends CampContext> {
    void begin(String campaignId);

    void next(String campaignId);

    void reset(String campaignId);
}
