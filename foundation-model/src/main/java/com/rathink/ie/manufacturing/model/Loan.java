package com.rathink.ie.manufacturing.model;

import com.rathink.ie.ibase.work.model.CompanyPart;

import javax.persistence.*;

/**
 * Created by Hean on 2015/10/10.
 */
@Entity
@Table(name = "company_part")
@DiscriminatorValue(value = "LOAN")
public class Loan extends CompanyPart {

    private String type;
    private String money;//金额
    private String needRepayCycle;//剩余还款周期

    @Column(name = "value")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "value2")
    public Integer getMoney() {
        return money == null ? 0 : Integer.valueOf(money);
    }

    public void setMoney(Integer money) {
        this.money = money == 0 ? null : String.valueOf(money);
    }

    @Column(name = "value3")
    public Integer getNeedRepayCycle() {
        return needRepayCycle == null ? 0 : Integer.valueOf(needRepayCycle);
    }

    public void setNeedRepayCycle(Integer needRepayCycle) {
        this.needRepayCycle = needRepayCycle == 0 ? null : String.valueOf(needRepayCycle);
    }

    public enum Status {
        NORMAL,
        //已还款
        FINISH
    }

    public enum Type{
        USURIOUS_LOAN("高利贷", 20, 4),
        SHORT_TERM_LOAN("短期贷款", 5, 4),
        LONG_TERM_LOAN("长期贷款", 10,20);

        Type(String label, Integer yearRate, Integer cycle) {
            this.label = label;
            this.yearRate = yearRate;
            this.cycle = cycle;
        }
        private String label;
        private Integer yearRate;//年利率
        private Integer cycle;//还款所需周期

        public String getLabel() {
            return label;
        }

        public Integer getYearRate() {
            return yearRate;
        }

        public Integer getCycle() {
            return cycle;
        }
    }

    @Transient
    public String getLoanTypeLable() {
        return Type.valueOf(type).getLabel();
    }
}
