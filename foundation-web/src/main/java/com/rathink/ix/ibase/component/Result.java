package com.rathink.ix.ibase.component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hean on 2016/1/31.
 */
public class Result {
    public static final Integer SUCCESS =  1;
    public static final Integer FAILED =  0;

    private Integer status;
    private String message;
    private Map model = new HashMap<>();

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

    public void addAttribute(String key, Object value) {
        model.put(key, value);
    }

    public Object getAttribute(String key) {
        return model.get(key);
    }

    public Map getModel() {
        return model;
    }
}
