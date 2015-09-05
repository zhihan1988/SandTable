package com.rathink.ie.internet.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hean on 2015/9/4.
 */
public class PropertyRepertory {
    InternetPropertyManager internetPropertyManager;
    Map<String, String> map = new HashMap<>();

    public String getVaue(String key) {
        String value = map.get(key);
        if (value == null) {
            switch (key) {
                case "aaa":
//                    String bbb = internetPropertyManager.getAbilityValue("");
                    value = "";
            }
        }

        return value;
    }
}
