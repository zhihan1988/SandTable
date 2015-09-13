package com.rathink.ie.ibase.property.model;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "company_term")
public class CompanyTerm {
    private String id;
    private Campaign campaign;
    private Company company;
//    private CompanyTerm preCompanyTerm;
    private String campaignDate;//哪一季度展示（不是指在哪一季度产生的数据）
    private List<CompanyInstruction> companyInstructionList;
    private List<CompanyStatusProperty> companyStatusPropertyList;
    private List<Account> accountList;

    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    @ManyToOne
    @JoinColumn(name = "company_id")
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

  /*  @ManyToOne
    @JoinColumn(name = "pre_company_term_id")
    public CompanyTerm getPreCompanyTerm() {
        return preCompanyTerm;
    }

    public void setPreCompanyTerm(CompanyTerm preCompanyTerm) {
        this.preCompanyTerm = preCompanyTerm;
    }
*/
    @Column(name = "campaign_date")
    public String getCampaignDate() {
        return campaignDate;
    }

    public void setCampaignDate(String campaignDate) {
        this.campaignDate = campaignDate;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "companyTerm", cascade = CascadeType.ALL)
    @OrderBy("id asc")
    public List<CompanyInstruction> getCompanyInstructionList() {
        return companyInstructionList;
    }

    public void setCompanyInstructionList(List<CompanyInstruction> companyInstructionList) {
        this.companyInstructionList = companyInstructionList;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "companyTerm", cascade = CascadeType.ALL)
    @OrderBy("id asc")
    public List<CompanyStatusProperty> getCompanyStatusPropertyList() {
        return companyStatusPropertyList;
    }

    public void setCompanyStatusPropertyList(List<CompanyStatusProperty> companyStatusPropertyList) {
        this.companyStatusPropertyList = companyStatusPropertyList;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "companyTerm", cascade = CascadeType.ALL)
    @OrderBy("id asc")
    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }
}
