package com.rathink.ie.ibase.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.util.CampaignUtil;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.account.model.AccountEntry;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.AccountManager;
import com.rathink.ie.ibase.service.CompanyTermHandler;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.EPropertyName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Hean on 2015/8/29.
 */
@Service
public class AccountManagerImpl implements AccountManager {
    @Autowired
    private BaseManager baseManager;

    @Override
    public void initCompanyAccount(CompanyTerm companyTerm) {

        List<Account> accountList = new ArrayList<>();

        Account adAccount = saveAccount("0", EAccountEntityType.AD_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
        accountList.add(adAccount);
        Account humanAccount = saveAccount("0", EAccountEntityType.HR_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
        accountList.add(humanAccount);
        Account productFeeAccount = saveAccount("0", EAccountEntityType.PRODUCT_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
        accountList.add(productFeeAccount);
        Account marketFeeAccount = saveAccount("0", EAccountEntityType.MARKET_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
        accountList.add(marketFeeAccount);
        Account operationFeeAccount = saveAccount("0", EAccountEntityType.OPERATION_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
        accountList.add(operationFeeAccount);
        Account incomeAccount = saveAccount("500000", EAccountEntityType.COMPANY_CASH.name(), EAccountEntityType.OTHER.name(), companyTerm);
        accountList.add(incomeAccount);

        companyTerm.setAccountList(accountList);
        baseManager.saveOrUpdate(CompanyTerm.class.getName(), companyTerm);
    }

    @Override
    public Account saveAccount(String fee, String inType, String outType, CompanyTerm companyTerm) {
        Account account = new Account();
        account.setCampaign(companyTerm.getCampaign());
        account.setCampaignDate(companyTerm.getCampaignDate());
        account.setCompany(companyTerm.getCompany());
        account.setCompanyTerm(companyTerm);

        List<AccountEntry> accountEntryList = new ArrayList<>();
        AccountEntry inAccountEntry = new AccountEntry();
        inAccountEntry.setType(inType);
        inAccountEntry.setDirection("1");
        inAccountEntry.setValue(fee);
        inAccountEntry.setAccount(account);
        accountEntryList.add(inAccountEntry);
        AccountEntry outAccountEntity = new AccountEntry();
        outAccountEntity.setType(outType);
        outAccountEntity.setDirection("-1");
        outAccountEntity.setValue(fee);
        outAccountEntity.setAccount(account);
        accountEntryList.add(outAccountEntity);
        
        account.setAccountEntryList(accountEntryList);
        baseManager.saveOrUpdate(Account.class.getName(), account);

        return account;
    }

    @Override
    public Integer getCompanyCash(Company company) {
        Integer companyCash = 0;
        XQuery xQuery = new XQuery();
        xQuery.setHql("from AccountEntry where account.company.id = :companyId and type = :type");
        xQuery.put("companyId", company.getId());
        xQuery.put("type", EAccountEntityType.COMPANY_CASH.name());
        List<AccountEntry> accountEntryList = baseManager.listObject(xQuery);
        if (accountEntryList != null) {
            for (AccountEntry accountEntry : accountEntryList) {
                if (accountEntry.getDirection().equals("1")) {
                    companyCash += Integer.valueOf(accountEntry.getValue());
                } else if (accountEntry.getDirection().equals("-1")){
                    companyCash -= Integer.valueOf(accountEntry.getValue());
                }
            }
        }
        return companyCash;
    }

    @Override
    public Integer countAccountEntryFee(Company company, String campaignDate, String type, String direction) {
        Integer companyCash = 0;
        XQuery xQuery = new XQuery();
        xQuery.setHql("from AccountEntry where account.company.id = :companyId and account.campaignDate = :campaignDate" +
                " and type = :type and direction = :direction");
        xQuery.put("companyId", company.getId());
        xQuery.put("type", type);
        xQuery.put("direction", direction);
        xQuery.put("campaignDate", campaignDate);
        List<AccountEntry> accountEntryList = baseManager.listObject(xQuery);
        if (accountEntryList != null) {
            for (AccountEntry accountEntry : accountEntryList) {
                companyCash += Integer.valueOf(accountEntry.getValue());
            }
        }
        return companyCash;
    }

    public Map<String, Map<String, String>> getAccountReport(Company company) {
        Map<String, Map<String, String>> propertyReport = new LinkedHashMap<>();

        XQuery xQuery = new XQuery();
        xQuery.setHql("from CompanyTerm where company.id = :companyId order by campaignDate asc");
        xQuery.put("companyId", company.getId());
        List<CompanyTerm> companyTermList = baseManager.listObject(xQuery);
        for (int i = 0; i < companyTermList.size() - 1; i++) {
            CompanyTerm companyTerm = companyTermList.get(i);
            Map<String, String> accountMap = new LinkedHashMap<>();
            XQuery accountEntityQuery = new XQuery();
            accountEntityQuery.setHql("from AccountEntry where account.companyTerm.id = :companyTermId and direction = 1");
            accountEntityQuery.put("companyTermId", companyTerm.getId());
            List<AccountEntry>  accountEntryList = baseManager.listObject(accountEntityQuery);
            if (accountEntryList != null && accountEntryList.size() > 0) {
                accountEntryList.sort(new AccountEntryComparator());
                accountEntryList.forEach(accountEntry -> {
                    accountMap.put(EAccountEntityType.valueOf(accountEntry.getType()).getLabel(), accountEntry.getValue());
                });
            }
            propertyReport.put(CampaignUtil.getNextCampaignDate(companyTerm.getCampaignDate()), accountMap);
        }
        return propertyReport;
    }

    class AccountEntryComparator implements Comparator<AccountEntry> {
        @Override
        public int compare(AccountEntry o1, AccountEntry o2) {
            Integer order1 = EAccountEntityType.valueOf(o1.getType()).ordinal();
            Integer order2 = EAccountEntityType.valueOf(o2.getType()).ordinal();
            return order1 - order2;
        }
    }
}
