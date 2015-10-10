package com.rathink.ie.manufacturing.service.impl;

import com.rathink.ie.ibase.service.CompanyTermContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Hean on 2015/9/4.
 */
@Component
public class ManufacturingCompanyTermContext extends CompanyTermContext {
    private static Logger logger = LoggerFactory.getLogger(ManufacturingCompanyTermContext.class);

    @Override
    public Integer calculate(String key) {
        Integer value = 0;
        return value;
    }


    /**
     * 计算部门能力
     * @param type 部门类型
     * @return 部门能力
     */
    public Integer calculateLineProcess() {
       return 0;
    }



}
