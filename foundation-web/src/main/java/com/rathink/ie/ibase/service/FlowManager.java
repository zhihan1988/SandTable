package com.rathink.ie.ibase.service;

/**
 * Created by Hean on 2015/9/4.
 */
public interface FlowManager {
    void begin(String campaignId);

    void next(String campaignId);

    void reset(String campaignId);
}
