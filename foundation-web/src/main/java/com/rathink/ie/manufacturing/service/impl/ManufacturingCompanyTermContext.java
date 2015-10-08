package com.rathink.ie.manufacturing.service.impl;

import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.ibase.service.CompanyTermContext;
import com.rathink.ie.ibase.service.InstructionManager;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryResourceChoice;
import com.rathink.ie.manufacturing.EManufacturingChoiceBaseType;
import com.rathink.ie.manufacturing.EManufacturingPropertyName;
import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Hean on 2015/9/4.
 */
@Component
public class ManufacturingCompanyTermContext extends CompanyTermContext {
    private static Logger logger = LoggerFactory.getLogger(ManufacturingCompanyTermContext.class);

    @Override
    public Integer calculate(String key) {
        EManufacturingPropertyName ePropertyName = EManufacturingPropertyName.valueOf(key);
        Integer value = 0;
        switch (ePropertyName) {
            case LINE1_PROCESS:
                value = calculateLineProcess();
                break;
            default:
                throw new NotImplementedException();
        }
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
