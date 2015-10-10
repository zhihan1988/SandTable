package com.rathink.ie.internet.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.service.AccountManager;
import com.rathink.ie.ibase.service.CompanyTermManager;
import com.rathink.ie.ibase.service.InstructionManager;
import com.rathink.ie.ibase.service.PropertyManager;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.service.InternetWorkManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/10/6.
 */
@Service
public class InternetWorkManagerImpl implements InternetWorkManager {
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private CompanyTermManager companyTermManager;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private InstructionManager instructionManager;
    @Autowired
    private PropertyManager propertyManager;

    public void fireHuman(String companyInstructionId) {
        CompanyTermInstruction companyTermInstruction = (CompanyTermInstruction) baseManager.getObject(CompanyTermInstruction.class.getName(), companyInstructionId);
        Integer fee = Integer.valueOf(companyTermInstruction.getValue()) * 2;
        Company company = companyTermInstruction.getCompany();
        CompanyTerm companyTerm = companyTermManager.getCompanyTerm(company.getId(), company.getCurrentCampaignDate());
        Account account = accountManager.packageAccount(String.valueOf(fee), EAccountEntityType.HR_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
        baseManager.saveOrUpdate(Account.class.getName(), account);
        companyTermInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);
    }

    public Map<String, String> getCompanyTermReport(CompanyTerm companyTerm) {
        Map<String, String> companyTermReport = new HashMap<>();
        Company company = companyTerm.getCompany();
        List<CompanyTermInstruction> hrInstructionList = instructionManager.listCompanyInstruction(company, EChoiceBaseType.HUMAN.name());
        int count = hrInstructionList == null ? 0 : hrInstructionList.size();
        companyTermReport.put("在职人数", String.valueOf(count));

        CompanyTermInstruction productStudyInstruction = instructionManager.getUniqueInstructionByBaseType(companyTerm, EChoiceBaseType.PRODUCT_STUDY.name());
        String productName = productStudyInstruction == null ? "" : productStudyInstruction.getIndustryResourceChoice().getName();
        companyTermReport.put("产品定位", productName);

        List<CompanyTermInstruction> marketInstructionList = instructionManager.listCompanyInstruction(companyTerm, EChoiceBaseType.MARKET_ACTIVITY.name());
        String marketActivities = "";
        if (marketInstructionList != null) {
            for (CompanyTermInstruction companyTermInstruction : marketInstructionList) {
                marketActivities += companyTermInstruction.getIndustryResourceChoice().getName();
            }
        }
        companyTermReport.put("市场营销", marketActivities);

        Integer campaignDateInCash = accountManager.countAccountEntryFee(
                company, companyTerm.getCampaignDate(), EAccountEntityType.COMPANY_CASH.name(), "1");
        companyTermReport.put("上期收入", String.valueOf(campaignDateInCash));

        //上期用户数
        CompanyTermProperty userAmountProperty = propertyManager.getCompanyTermProperty(companyTerm, EPropertyName.USER_AMOUNT.name());
        Integer oldUserAmount = userAmountProperty.getValue();
        companyTermReport.put("上期用户数", String.valueOf(oldUserAmount));
        return companyTermReport;
    }
}
