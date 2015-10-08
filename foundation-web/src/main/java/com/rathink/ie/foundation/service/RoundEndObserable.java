package com.rathink.ie.foundation.service;

import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Hean on 2015/10/8.
 */
public class RoundEndObserable extends Observable {
    String campaignId;
    private Set<String> companySet;
    private Map<String, Boolean> companyFinishedMap = new ConcurrentHashMap<>();

    private RoundEndObserable(){}

    public RoundEndObserable(String campaignId, Set<String> companySet) {
        this.campaignId = campaignId;
        this.companySet = companySet;
    }

    public Integer getUnFinishedNum() {
        int finishedCount = 0;
        Integer companyCount = companySet.size();
        for (Boolean isFinished : companyFinishedMap.values()) {
            if (isFinished) {
                finishedCount++;
            }
        }
        return companyCount - finishedCount;
    }

    public synchronized void finish(String companyId) {
        companyFinishedMap.put(companyId, true);
        if (getUnFinishedNum() == 0) {
            //全部完成
            setChanged();
            notifyObservers(campaignId);
        }
    }

}
