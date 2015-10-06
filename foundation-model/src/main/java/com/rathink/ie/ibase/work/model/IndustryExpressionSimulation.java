package com.rathink.ie.ibase.work.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by pgwt on 10/5/15.
 */
public class IndustryExpressionSimulation {

    private IndustryAnalyzer industryAnalyzer;
    private List<Trituple> abilityList = new ArrayList<>();
    private List<Object> result = new ArrayList<>();
    private Integer testAmount = 10;

    public IndustryExpressionSimulation(String expression) {
        industryAnalyzer = new IndustryAnalyzer(expression);
    }


    public void add(String abilityName, Object value, Object step) {
        Trituple trituple = new Trituple();
        trituple.setAbility(abilityName);
        trituple.setInitialValue(value);
        trituple.setStep(step);
        abilityList.add(trituple);
    }

    public void add(String abilityName, String value) {
        add(abilityName, value, null);
    }

    public Integer getTestAmount() {
        return testAmount;
    }

    public void setTestAmount(Integer testAmount) {
        this.testAmount = testAmount;
    }


    //两个变量
    public int[][] getResult2() {
        return getResult2(null);
    }


    private int[][] getResult2(Integer abilityValue3) {

        if (abilityValue3 != null) {
            industryAnalyzer.add(abilityList.get(2).getAbility(), abilityValue3);
        }

        //第一个变量
        Trituple trituple = abilityList.get(0);

        int[] y1 = new int[testAmount + 1];
        y1[0] = 888;
        for (int i = 1; i < y1.length; i++) {
            int initialValue = (Integer) trituple.getInitialValue();
            int step = (Integer) trituple.getStep();
            y1[i] = initialValue + step * (i - 1);
        }

        int[][] resultTable = new int[testAmount + 1][testAmount + 1];
        resultTable[0] = y1;

        //第二个变量
        Trituple trituple2 = abilityList.get(1);
        for (int i = 0; i < testAmount; i++) {
            int[] x1 = new int[testAmount + 1];
            int initialValue2 = (Integer) trituple2.getInitialValue();
            int step = (Integer) trituple2.getStep();
            int value2 = initialValue2 + (i * step);
            x1[0] = value2;
            for (int j = 1; j < y1.length; j++) {
                industryAnalyzer.add(trituple2.getAbility(), value2);
                industryAnalyzer.add(trituple.getAbility(), y1[j]);
                x1[j] = (Integer) industryAnalyzer.getResult();
            }

            resultTable[i + 1] = x1;

        }

        return resultTable;

    }

    //三个变量
    public List<int[][]> getResult3() {

        List<int[][]> resultTableList = new ArrayList<>();
        Trituple trituple3 = abilityList.get(2);
        int[] y1 = new int[testAmount + 1];
        y1[0] = 888;
        for (int i = 1; i < y1.length; i++) {
            int initialValue = (Integer) trituple3.getInitialValue();
            int step = (Integer) trituple3.getStep();
            int value3 = initialValue + step * (i - 1);
            resultTableList.add(getResult2(value3));
        }
        return resultTableList;
    }


    public List<List<Object>> getResult() {
        int flag = 0;
        List<List<Object>> resultTable = new ArrayList<>();
        for (Trituple tritupleTemp : abilityList) {
            flag++;
            List<Trituple> abilityList2 = new ArrayList<>();
            for (Trituple tritupleTemp2 : abilityList) {
                abilityList2.add(tritupleTemp2.clone());
            }

            List<Object> resultTemp = new ArrayList<>();
            for (int i = 0; i < this.testAmount; i++) {
                if (flag > i + 1) continue;
                for (int j = 0; j < abilityList2.size(); j++) {
                    Trituple tritupleTemp3 = abilityList2.get(j);
                    if (tritupleTemp.getAbility().equals(tritupleTemp3.getAbility())) {
                        String abilityName = tritupleTemp.getAbility();
                        Integer initialValue = (Integer) (tritupleTemp.getInitialValue());
                        Integer step = (Integer) (tritupleTemp.getStep());
                        tritupleTemp3.setInitialValue(initialValue + step * i);
                        this.industryAnalyzer.add(abilityName, tritupleTemp3.getInitialValue());
                    } else {
                        this.industryAnalyzer.add(tritupleTemp3.getAbility(), tritupleTemp3.getInitialValue());
                    }
                }
                LinkedHashMap<String, Object> bmap = industryAnalyzer.getAbilityMap();
                List<Object> resultTableItem = new ArrayList<>();
                for (String key : bmap.keySet()) {
                    resultTableItem.add(bmap.get(key));
                }
                Object expressionResult = this.industryAnalyzer.getResult();
                resultTableItem.add(expressionResult);
                resultTable.add(resultTableItem);
                resultTemp.add(expressionResult);
            }
            result.add(resultTemp);

        }

        return resultTable;
    }


    class Trituple implements Cloneable {

        private String ability;
        private Object initialValue;
        private Object step;

        public String getAbility() {
            return ability;
        }

        public void setAbility(String ability) {
            this.ability = ability;
        }

        public Object getInitialValue() {
            return initialValue;
        }

        public void setInitialValue(Object initialValue) {
            this.initialValue = initialValue;
        }

        public Object getStep() {
            return step;
        }

        public void setStep(Object step) {
            this.step = step;
        }

        public Trituple clone() {
            try {
                return (Trituple) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    public static void main(String[] args) {
        String expression = "a+b";
        IndustryAnalyzer industryAnalyzer = new IndustryAnalyzer(expression);
        industryAnalyzer.add("a", 3);
        industryAnalyzer.add("b", 3);

        System.out.println(industryAnalyzer.getResult());

        IndustryExpressionSimulation industryExpressionSimulation = new IndustryExpressionSimulation(expression);
        industryExpressionSimulation.add("a", 3, 1);
        industryExpressionSimulation.add("b", 3, 2);
        //循环便利所有的变量 ，每个变量的步长是10  那应该是以变量为基准
        //如果当前便利的是第一个变量，flag ＝＝1  当flag＝j＋1 算 当第二次的是偶还是第一个变量 这个时候flag＝1
        //当flag＝2的时候 就是第二个变量 b flag > j+1(j=0) 解析器添加第一个变量

        //如果最后用数组表示的话，第一行代表y的取值范围，第二行代表当x＝某个值的时候各种结果
        industryExpressionSimulation.getResult2();

    }


}
