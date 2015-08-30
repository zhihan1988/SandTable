package com.rathink.ie.ibase.service;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.account.model.AccountEntry;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.Edept;
import com.rathink.ie.internet.instruction.model.HrInstruction;
import com.rathink.ie.internet.instruction.model.MarketInstruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.krb5.internal.crypto.EType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hean on 2015/8/29.
 */
@Service
public class AccountService {
    @Autowired
    private BaseManager baseManager;

    public void initCompanyAccount(Company company) {
        Account account = new Account();
        account.setCampaign(company.getCampaign());
        account.setCampaignDate(company.getCampaign().getCurrentCampaignDate());
        account.setCompany(company);
        List<AccountEntry> accountEntryList = new ArrayList<>();
        AccountEntry accountEntity = new AccountEntry();
        accountEntity.setAccount(account);
        accountEntity.setDirection("1");
        accountEntity.setValue("1000000");
        accountEntity.setType(EAccountEntityType.COMPANY_CASH.name());
        accountEntryList.add(accountEntity);
        AccountEntry accountEntity2 = new AccountEntry();
        accountEntity2.setAccount(account);
        accountEntity2.setDirection("-1");
        accountEntity2.setValue("1000000");
        accountEntity2.setType(EAccountEntityType.OTHER.name());
        accountEntryList.add(accountEntity2);
        account.setAccountEntryList(accountEntryList);
        baseManager.saveOrUpdate(Account.class.getName(), account);
    }

    public List<AccountEntry> prepareAccountEntity(List<? extends CompanyInstruction> companyInstructionList, String inType, String outType, Account account) {
        Integer fee = 0;
        if (companyInstructionList != null) {
            for (CompanyInstruction companyInstruction : companyInstructionList) {
                if (!companyInstruction.getFee() .equals("-1")) {
                    fee += Integer.parseInt(companyInstruction.getFee());
                }
            }
        }
        List<AccountEntry> accountEntryList = new ArrayList<>();
        AccountEntry inAccountEntry = new AccountEntry();
        inAccountEntry.setType(inType);
        inAccountEntry.setDirection("1");
        inAccountEntry.setValue(String.valueOf(fee));
        inAccountEntry.setAccount(account);
        accountEntryList.add(inAccountEntry);
        AccountEntry outAccountEntity = new AccountEntry();
        outAccountEntity.setType(outType);
        outAccountEntity.setDirection("-1");
        outAccountEntity.setValue(String.valueOf(fee));
        accountEntryList.add(outAccountEntity);
        return accountEntryList;
    }

    public List<AccountEntry> prepareAccountEntity(String fee, String inType, String outType, Account account) {
        List<AccountEntry> accountEntryList = new ArrayList<>();
        AccountEntry inAccountEntry = new AccountEntry();
        inAccountEntry.setType(inType);
        inAccountEntry.setDirection("1");
        inAccountEntry.setValue(String.valueOf(fee));
        inAccountEntry.setAccount(account);
        accountEntryList.add(inAccountEntry);
        AccountEntry outAccountEntity = new AccountEntry();
        outAccountEntity.setType(outType);
        outAccountEntity.setDirection("-1");
        outAccountEntity.setValue(String.valueOf(fee));
        accountEntryList.add(outAccountEntity);
        return accountEntryList;
    }

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
}
