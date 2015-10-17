package com.rathink.ie.system.basic.controller;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.ibase.work.model.IndustryAnalyzer;
import com.rathink.ie.ibase.work.model.IndustryExpression;
import com.rathink.ie.ibase.work.model.IndustryExpressionSimulation;
import com.rathink.ie.ibase.work.model.IndustryExpressionVariate;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by pgwt on 10/3/15.
 */
@Controller
public class HomeController {

    @Autowired
    private BaseManager baseManager;

    @RequestMapping({"/home.do"})
    public String home() {
        return "/home";
    }


    @RequestMapping({"/result.do"})
    public String getResult(Model model, HttpServletRequest request) {
        String expressionId = request.getParameter("expressionId");
        IndustryExpression industryExpression = (IndustryExpression) baseManager.getObject(IndustryExpression.class.getName(), expressionId);
        List<IndustryExpressionVariate> industryExpressionVariateList = industryExpression.getIndustryExpressionVariateList();
        IndustryExpressionSimulation industryExpressionSimulation = new IndustryExpressionSimulation(industryExpression.getExpression());
        for (IndustryExpressionVariate industryExpressionVariateTemp : industryExpressionVariateList) {
            industryExpressionSimulation.add(industryExpressionVariateTemp.getName(), Integer.parseInt(industryExpressionVariateTemp.getInitialValue()), Integer.parseInt(industryExpressionVariateTemp.getStep()));
        }
        if (industryExpressionVariateList.size() == 2) {
            List<int[][]> result = new ArrayList<>();
            result.add(industryExpressionSimulation.getResult2());
            model.addAttribute("result", result);
        } else {
            model.addAttribute("result", industryExpressionSimulation.getResult3());
        }

        model.addAttribute("ability1", industryExpressionVariateList.get(0));
        model.addAttribute("ability2", industryExpressionVariateList.get(1));
        if (industryExpressionVariateList.size() == 3) {
            model.addAttribute("ability3", industryExpressionVariateList.get(2));
        }
//        String expression = "a+b+c";
//        IndustryAnalyzer industryAnalyzer = new IndustryAnalyzer(expression);
//        industryAnalyzer.add("a", 3);
//        industryAnalyzer.add("b", 3);
//        industryAnalyzer.add("c", 3);
//
//        System.out.println(industryAnalyzer.getResult());
//
//        industryExpressionSimulation.add("a", 3, 1);
//        industryExpressionSimulation.add("b", 3, 2);
//        industryExpressionSimulation.add("c", 3, 2);
        //循环便利所有的变量 ，每个变量的步长是10  那应该是以变量为基准
        //如果当前便利的是第一个变量，flag ＝＝1  当flag＝j＋1 算 当第二次的是偶还是第一个变量 这个时候flag＝1
        //当flag＝2的时候 就是第二个变量 b flag > j+1(j=0) 解析器添加第一个变量

        //如果最后用数组表示的话，第一行代表y的取值范围，第二行代表当x＝某个值的时候各种结果
//        model.addAttribute("result",industryExpressionSimulation.getResult2());

        return "/foundation/expressionResult";
    }

    @RequestMapping("/test.do")
    public String test() {
        String hql = "select obj from " + IndustryExpression.class.getName() + " obj where obj.name=:name";
//        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
//        param.put("name", "测试数据");
//        IndustryExpression industryExpression = (IndustryExpression) baseManager.getUniqueObjectByConditions(hql, param);
//        IndustryAnalyzer industryAnalyzer = new IndustryAnalyzer(industryExpression.getExpression());
        return "/test";

    }

}
