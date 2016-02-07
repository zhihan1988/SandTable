package com.rathink.iix.ibase.component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hean on 2016/2/2.
 */
public abstract class CampaignParty<T> {
    protected MemoryCampaign memoryCampaign;
    protected Set<T> joinedSet = new HashSet<>();
    private String type;
    private String status;

    public CampaignParty(MemoryCampaign memoryCampaign, String type) {
        this.memoryCampaign = memoryCampaign;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public synchronized void join(T o) {
        joinedSet.add(o);
        if (isAll()) {
            iNotify();
        }
    }

    /**
     * 是否全部操作完成
     * @return
     */
    public boolean isAll() {
        return joinedSet.size() >= memoryCampaign.getMemoryCompanyMap().size();
    }

    /**
     * 未完成的数量
     * @return
     */
    public Integer getUnFinishedNum() {
        Integer allNum = memoryCampaign.getMemoryCompanyMap().size();
        Integer joinedNum = joinedSet.size();
        return allNum - joinedNum;
    }


    /**
     * 全部完成后执行的操作
     */
    public abstract void iNotify();
}
