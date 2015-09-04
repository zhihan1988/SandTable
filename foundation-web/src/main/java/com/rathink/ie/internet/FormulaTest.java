package com.rathink.ie.internet;

import com.rathink.ie.foundation.util.RandomUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Hean on 2015/9/4.
 */
public class FormulaTest {

    /**
     * 新用户数
     * @param marketFee  市场营销投入
     * @param marketCost 用户获取成本
     * @return
     */
    public Integer getNewUserAmount(Integer marketFee, Integer marketCost) {
        Integer randomRatio = RandomUtil.random(60, 80);
        Integer newUserAmount = marketFee * randomRatio / marketCost / 100;
        System.out.println("----------------新用户数计算");
        System.out.println("市场投入：" + marketFee + "；用户获取成本：" + marketCost + ";随机系数：" + randomRatio + "新用户数：" + newUserAmount);
        return newUserAmount;
    }


    /**
     * 满意度
     * @param operationFee 运营系数
     * @param operationAbility  运营能力
     * @return
     */
    public Integer getSatisfaction(Integer operationFee, Integer operationAbility){
        System.out.println("----------------满意度计算");
        Integer operationFeeRatio = operationFee / 10000 + 80;
        Integer satisfaction = operationAbility * operationFeeRatio / 100 + 30;
        System.out.println("运营费用" + operationFee + "；运营费用系数" + operationFeeRatio + "；运营能力："+ operationAbility + ";满意度：" + satisfaction);
        return satisfaction;
    }

    /**
     * 老用户数
     * @param preUserAmount 上一期用户数
     * @param satisfaction 满意度
     * @return
     */
    public Integer getOldUserAmount(Integer preUserAmount, Integer satisfaction) {
        System.out.println("----------------老用户数计算");
        Integer oldUserAmount = preUserAmount * satisfaction / 100;
        System.out.println("上一期老用户数：" + preUserAmount + ";满意度：" + satisfaction + ";本轮老用户数：" + oldUserAmount);
        return oldUserAmount;
    }

    /**
     * 产品系数
     * @param productFee 产品研发投入
     * @param productAbility 产品能力
     * @param preProductRatio 上一轮产品系数
     * @return
     */
    public Integer getProductRadio(Integer productFee, Integer productAbility, Integer preProductRatio){
        System.out.println("----------------产品系数计算");
        //资金投入系数
        Integer feeRatio = productFee / 1000 + 50;
        Integer productRadio = (productAbility * feeRatio) / 500 + preProductRatio;
        System.out.println("产品投入" + productFee + "；上一轮产品系数：" + preProductRatio + ";产品能力：" + productAbility + ";资金投入系数：" + feeRatio + "；本轮产品系数：" + productRadio);
        return productRadio;
    }

    /**
     * 客单价
     * @param grade 产品定位
     * @return
     */
    public Integer getPerOrderCost(Integer grade){
        System.out.println("----------------客单价计算");
        Integer perOrderCost = Integer.valueOf(grade) * 10 + 150;
        System.out.println("产品定位：" + grade + ";客单价：" + perOrderCost);
        return perOrderCost;
    }

    /**
     * 本轮收入
     * @param userAmount 用户数
     * @param perOrderCost 客单价
     * @return
     */
    public Integer getCurrentPeriodIncome(Integer userAmount, Integer perOrderCost) {
        System.out.println("----------------本轮收入计算");
        Integer currentPeriodIncome = userAmount * perOrderCost / 10;
        System.out.println("本轮收入：" + currentPeriodIncome);
        return currentPeriodIncome;
    }

    /**
     * 本轮支出
     * @param marketFee
     * @param productFee
     * @param operationFee
     * @param humanFee
     * @param humanCount
     * @return
     */
    public Integer getOut(Integer marketFee, Integer productFee, Integer operationFee,Integer humanFee, Integer humanCount) {
        int out = marketFee + productFee + operationFee + humanFee * humanCount;
        System.out.println("本轮支出：" + out);
        return out;
    }

    /**
     * 现金总额
     * @param currentIncome 本轮收入
     * @param currentOut 本轮支出
     * @param preTotalCash 上一轮的现金总额
     * @return
     */
    public Integer getTotalCash(Integer currentIncome,Integer currentOut, Integer preTotalCash){
        int totalCash = preTotalCash - currentOut + currentIncome;
        System.out.println("现金总额：" + totalCash);
        return totalCash;
    }

    private int random(int low, int high){
        int returnValue = 100;
        Random random = new Random();
        int randomNum = random.nextInt(high - low);
        returnValue = randomNum + low + 1;
        return returnValue;
    }



    public static void main(String[] args) {
        //每轮的部门能力固定增长8
        //部门的资金投入不变

        //初始值map
        Map<String, Integer> map = new HashMap<>();
        map.put("marketAbility", 60);//市场能力
        map.put("productAbility", 60);//产品研发能力
        map.put("operationAbility", 60);//运营能力
        map.put("marketFee", 10000);//市场投入
        map.put("marketCost", 20);//用户获取成本
        map.put("productFee", 10000);//产品研发投入
        map.put("operationFee", 10000);//运营投入
        map.put("productGrade", 2);//产品定位
        map.put("userAmount", 500);//用户数初始值
        map.put("productRatio", 60);//产品系数初始值
        map.put("humanFee",15000);//各部门 员工工资（单人）
        map.put("humanCount",0);//人员数量
        map.put("totalCash",1000000);//现金总额

        FormulaTest formulaTest = new FormulaTest();
        for (int i = 1; i <= 10; i++) {
            map.put("i", i);
            map.put("humanCount", i * 3);
            formulaTest.process(map);
        }

    }

    public void process(Map<String, Integer> map) {
        System.out.printf("##########第" + map.get("i") + "轮开始\n\r");
        FormulaTest formulaTest = new FormulaTest();
        Integer newUserAmount = formulaTest.getNewUserAmount(map.get("marketFee"), map.get("marketCost"));
        Integer satisfaction = formulaTest.getSatisfaction(map.get("operationFee"), map.get("operationAbility"));
        Integer oldUserAmount = formulaTest.getOldUserAmount(map.get("userAmount"), satisfaction);
        Integer productRatio = formulaTest.getProductRadio(map.get("productFee"), map.get("productAbility"), map.get("productRatio"));
        Integer perOrderCost = formulaTest.getPerOrderCost(2);
        Integer userAmount = oldUserAmount + newUserAmount;
        System.out.println("----------------总用户数：" + userAmount);
        Integer currentPeriodIncome = formulaTest.getCurrentPeriodIncome(userAmount, perOrderCost);
        Integer currentPeriodOut = formulaTest.getOut(map.get("marketFee"), map.get("productFee"), map.get("operationFee"), map.get("humanFee"), map.get("humanCount"));
        Integer totalCash = getTotalCash(currentPeriodIncome, currentPeriodOut, map.get("totalCash"));

        map.put("newUserAmount", newUserAmount);
        map.put("satisfaction", satisfaction);
        map.put("oldUserAmount", oldUserAmount);
        map.put("userAmount", userAmount);
        map.put("productRatio", productRatio);
        map.put("perOrderCost", perOrderCost);
        map.put("currentPeriodIncome", currentPeriodIncome);
        map.put("totalCash", totalCash);
        map.put("marketAbility", map.get("marketAbility") + 8);
        map.put("productAbility", map.get("productAbility") + 8);
        map.put("operationAbility", map.get("operationAbility") + 8);
        System.out.printf("##########第" + map.get("i") + "轮结束\n\r\n\r");
    }


}