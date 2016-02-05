package com.rathink.iix.ibase.component;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ix.ibase.account.model.Account;
import com.rathink.ix.ibase.account.model.AccountEntry;
import com.rathink.ix.ibase.property.model.CompanyTerm;
import com.rathink.ix.ibase.work.model.CompanyTermInstruction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Hean on 2016/2/2.
 */
public class MemoryCompany {
    private Company company;
    private MemoryCampaign memoryCampaign;
    private List<Account> accountList = new ArrayList<>();
    private List<CompanyTermInstruction> instructionList = new ArrayList<>();
    //instructionlist
    private Queue messageQueue;

    public MemoryCompany(Company company, MemoryCampaign memoryCampaign) {
        this.company = company;
        this.memoryCampaign = memoryCampaign;
    }

    public Company getCompany() {
        return company;
    }

    public MemoryCampaign getMemoryCampaign() {
        return memoryCampaign;
    }

    public void broadcast() {

    }



    public void addAccount(Account account){
        accountList.add(account);
    }

    public Integer getCompanyCash() {
        Integer companyCash = 0;
        for (Account account : accountList) {
            for (AccountEntry accountEntry : account.getAccountEntryList()) {
                if (accountEntry.getDirection().equals("1")) {
                    companyCash += Integer.valueOf(accountEntry.getValue());
                } else if (accountEntry.getDirection().equals("-1")) {
                    companyCash -= Integer.valueOf(accountEntry.getValue());
                }
            }
        }
        return companyCash;
    }

    public Integer getLoan(String type) {
        Integer companyCash = 0;
        for (Account account : accountList) {
            for (AccountEntry accountEntry : account.getAccountEntryList()) {
                if (accountEntry.getType().equals(type)) {
                    if (accountEntry.getDirection().equals("1")) {
                        companyCash += Integer.valueOf(accountEntry.getValue());
                    } else if (accountEntry.getDirection().equals("-1")) {
                        companyCash -= Integer.valueOf(accountEntry.getValue());
                    }
                }
            }
        }
        return companyCash;
    }
}
