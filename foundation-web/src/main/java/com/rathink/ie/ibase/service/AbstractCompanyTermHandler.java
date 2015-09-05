package com.rathink.ie.ibase.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.work.model.CompanyChoice;

/**
 * Created by Hean on 2015/9/4.
 */
public abstract class AbstractCompanyTermHandler {
    private CompanyTerm companyTerm;

    public void putInstruction(CompanyChoice companyChoice){

    }

    public abstract void calculateAll();

}
