package com.rathink.ie.ibase.service;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.account.model.AccountEntry;
import com.rathink.ie.internet.EAccountEntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        baseManager.saveOrUpdate(Account.class.getName(), account);

    }
}
