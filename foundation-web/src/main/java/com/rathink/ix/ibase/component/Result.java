package com.rathink.ix.ibase.component;

import org.jgroups.protocols.pbcast.STABLE;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hean on 2016/1/31.
 */
public class Result {
    public static final Integer SUCCESS =  1;
    public static final Integer ERROR =  0;

    private Integer status;
    private String message;
    private Map map = new HashMap<>();

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAttribute(String key, Object value) {
        map.put(key, value);
    }

    public Object getAttribute(String key) {
        return map.get(key);
    }

}
