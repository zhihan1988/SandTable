package com.rathink.ie.ibase.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.account.model.AccountEntry;
import com.rathink.ie.ibase.work.model.CompanyInstruction;

import java.util.List;

/**
 * Created by Hean on 2015/9/4.
 */
public interface AccountManager {
    void initCompanyAccount(Company company);

    List<AccountEntry> prepareAccountEntity(List<? extends CompanyInstruction> companyInstructionList, String inType, String outType, Account account);

    List<AccountEntry> prepareAccountEntity(String fee, String inType, String outType, Account account);

    Integer getCompanyCash(Company company);

    Integer countAccountEntryFee(Company company, String campaignDate, String type, String direction);
}
