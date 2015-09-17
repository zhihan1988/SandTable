package com.rathink.ie.ibase.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.work.model.CompanyInstruction;

import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/9/4.
 */
public interface AccountManager {
    void initCompanyAccount(CompanyTerm companyTerm);

    Account saveAccount(List<CompanyInstruction> companyInstructionList, String inType, String outType, CompanyTerm companyTerm);

    Account saveAccount(String fee, String inType, String outType, CompanyTerm companyTerm);

    Integer getCompanyCash(Company company);

    Integer countAccountEntryFee(Company company, String campaignDate, String type, String direction);

    Map<String, Map<String, String>> getAccountReport(Company company);
}
