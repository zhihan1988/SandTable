package com.rathink.ie.ibase.work.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by pgwt on 10/5/15.
 */
@Entity
@Table(name = "industry_expression_variate")
public class IndustryExpressionVariate {

    private String id;
    private String name;
    private IndustryExpression industryExpression;
    private String initialValue ; //初始值
    private String step ; //步长
    private String status;

    @Id
        @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_expression_id")
    public IndustryExpression getIndustryExpression() {
        return industryExpression;
    }

    public void setIndustryExpression(IndustryExpression industryExpression) {
        this.industryExpression = industryExpression;
    }

    @Column(name = "initial_value")
    public String getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(String initialValue) {
        this.initialValue = initialValue;
    }

    @Column(name = "step")
    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
