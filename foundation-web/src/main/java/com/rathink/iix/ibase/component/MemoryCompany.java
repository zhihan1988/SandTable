package com.rathink.iix.ibase.component;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.iix.manufacturing.message.IMessage;
import com.rathink.ix.ibase.account.model.Account;
import com.rathink.ix.ibase.account.model.AccountEntry;
import com.rathink.ix.ibase.work.model.CompanyTermInstruction;

import java.util.ArrayList;
import java.util.LinkedList;
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
    private Queue<IMessage> messageQueue = new LinkedList<>();

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

    public void offerMessage(IMessage message) {
        messageQueue.offer(message);
    }

    public IMessage pollMessage() {
        return messageQueue.poll();
    }

    public IMessage peekMessage(){
        return messageQueue.peek();
    }

    public void addAccount(Account account){
        accountList.add(account);
    }

    public Integer sumTypeAccount(String type) {
        Integer sum = 0;
        for (Account account : accountList) {
            for (AccountEntry accountEntry : account.getAccountEntryList()) {
                if (accountEntry.getType().equals(type)) {
                    if (accountEntry.getDirection().equals("1")) {
                        sum += Integer.valueOf(accountEntry.getValue());
                    } else if (accountEntry.getDirection().equals("-1")) {
                        sum -= Integer.valueOf(accountEntry.getValue());
                    }
                }
            }
        }
        return sum;
    }

    public Integer sumPrefixTypeAccount(String prefix) {
        Integer sum = 0;
        for (Account account : accountList) {
            for (AccountEntry accountEntry : account.getAccountEntryList()) {
                if (accountEntry.getType().startsWith(prefix)) {
                    if (accountEntry.getDirection().equals("1")) {
                        sum += Integer.valueOf(accountEntry.getValue());
                    } else if (accountEntry.getDirection().equals("-1")) {
                        sum -= Integer.valueOf(accountEntry.getValue());
                    }
                }
            }
        }
        return sum;
    }
}
