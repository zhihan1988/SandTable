package com.rathink.ie.ibase.work.model;


import java.util.LinkedHashMap;

import org.xidea.el.Expression;
import org.xidea.el.ExpressionFactory;
import org.xidea.el.impl.ExpressionFactoryImpl;

/**
 * Created by pgwt on 10/5/15.
 */
public class IndustryAnalyzer {

    private ExpressionFactory factory = ExpressionFactoryImpl.getInstance();
    private String expressionStr;
    private Expression expression;
    private LinkedHashMap<String,Object> abilityMap = new LinkedHashMap<>();

    public IndustryAnalyzer(String expression){
        this.expressionStr = expression;
        this.expression = factory.create(expression);
    }

    public void add(String abilityName,Object value){
        abilityMap.put(abilityName,value);
    }

    public Integer getResult(){
        Double result = Double.parseDouble(expression.evaluate(abilityMap).toString());
        return result.intValue();
    }

    public void reset(){
        abilityMap.clear();
    }

    public LinkedHashMap<String, Object> getAbilityMap() {
        return abilityMap;
    }
}
