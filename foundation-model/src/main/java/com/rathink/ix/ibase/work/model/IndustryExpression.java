package com.rathink.ix.ibase.work.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * Created by pgwt on 10/3/15.
 */
@Entity
@Table(name = "industry_expression")
public class IndustryExpression {

    private String id;
    private String name;//公式名称
    private String expression;
    private String ability; //变量名
    private String initialValue ; //初始值
    private String step ; //步长
    private List<IndustryExpressionVariate> industryExpressionVariateList;


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

    @Column(name = "expression")
    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Column(name = "ability")
    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
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

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "industryExpression")
    public List<IndustryExpressionVariate> getIndustryExpressionVariateList() {
        return industryExpressionVariateList;
    }

    public void setIndustryExpressionVariateList(List<IndustryExpressionVariate> industryExpressionVariateList) {
        this.industryExpressionVariateList = industryExpressionVariateList;
    }
}
