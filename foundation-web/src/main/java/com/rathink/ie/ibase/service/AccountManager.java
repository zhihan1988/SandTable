package com.rathink.ie.ibase.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyTerm;

import java.util.Map;

/**
 * Created by Hean on 2015/9/4.
 */
public interface AccountManager {

    Account packageAccount(String fee, String inType, String outType, CompanyTerm companyTerm);



    /**
     * 获得公司现金
     * 各行业的公司现金会计科目必须规定为COMPANY_CASH
     * @param company
     * @return
     */
    Integer getCompanyCash(Company company);

    /**
     * 获得公司贷款
     * 各行业的公司现金会计科目必须规定 以LOAN_开头
     * @param company
     * @return
     */
    Integer getLoan(Company company);

    /**
     * 获得公司流动资产
     * 各行业的公司现金会计科目必须规定  以FLOATING_CAPITAL_开头
     * @param company
     * @return
     */
    Integer getFloatingCapital(Company company);

    Integer sumLoan(Company company, String type);

    Integer countAccountEntryFee(Company company, Integer campaignDate, String type, String direction);

    Map<String, Map<String, String>> getAccountReport(Company company);
}
