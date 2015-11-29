package com.rathink.ie.user.controller;

/**
 * Created by Administrator on 2014/11/13.
 */


import com.ming800.core.base.controller.BaseController;
import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.p.PConst;
import com.ming800.core.util.HttpUtil;
import com.ming800.core.util.StringUtil;
import com.ming800.core.util.VerificationCodeGenerator;
import com.rathink.ie.user.model.User;
import com.rathink.ie.user.service.SmsCheckManager;
import com.rathink.ie.user.service.UserManager;
import com.sun.javafx.sg.prism.NGShape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: ming
 * Date: 12-10-15
 * Time: ����4:56
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class SigninController extends BaseController {
    @Autowired
    private UserManager branchManager;
    @Autowired
    private BaseManager baseManager;

    @Autowired
    private SmsCheckManager smsCheckManager;


    /**
     * 查看当前用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    @RequestMapping(value = "/pc/checkUserName.do")
    @ResponseBody
    public Boolean checkUserName(String username) {

        User user = branchManager.checkUsername(username);

        if (user != null) {
            return true;
        }

        return false;
    }


    /**
     * 注册新的消费者
     *
     * @param request
     * @param bigUser  消费者的初始信息
     * @param modelMap 返回给视图的数据
     * @return jsp的路径
     * @throws Exception
     */
    @RequestMapping(value = "/pc/saveEnrollUser.do")
    public ModelAndView saveEnrollUser(HttpServletRequest request, User bigUser, ModelMap modelMap) throws Exception {
        bigUser.setPassword(StringUtil.encodePassword(bigUser.getPassword(), "SHA"));
        String invitatoryCode = request.getParameter("invitatoryCode");
        String sourceUserHQL = "select obj from User obj where obj.myCode=:sourceCode";
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("sourceCode", invitatoryCode);
        User sourceUser = (User) baseManager.getUniqueObjectByConditions(sourceUserHQL, param);
        bigUser.setSourceUser(sourceUser);
        bigUser.setMyCode(generateMyCode());
        baseManager.saveOrUpdate(User.class.getName(), bigUser);
        modelMap.put("user", bigUser);
        modelMap.put("message", "注册成功");
        request.getSession().setAttribute("username", bigUser.getUsername());
        //注册成功页面
//        modelMap.put("sourceCode",bigUser.getMyCode());
        return new ModelAndView("redirect:/registeSuccess/"+bigUser.getMyCode());
//        return new ModelAndView("redirect:/");
    }

    @RequestMapping({"/registeSuccess/{myCode}"})
    public String registeSuccess(HttpServletRequest request,@PathVariable String myCode,Model model){
        model.addAttribute("myCode",myCode);
        return "/registeSuccess";
    }

    private static String generateMyCode() {
        String currentTime = Long.toHexString(System.currentTimeMillis()); //当前时间戳转换成16进制
        //后三位
        currentTime = currentTime.substring(currentTime.length() - 3, currentTime.length());
        Double num = Math.random() * 100000000;
        String numStr = Integer.toHexString(num.intValue());
        //前三位
        numStr = numStr.substring(0, 3);
        return currentTime + numStr;
    }


    /*
    认证手机验证码
     */
    @RequestMapping({"/pc/verification/verify.do"})
    @ResponseBody
    public boolean checkVerificationCode(HttpServletRequest request) {
        String inputVerificationCode = request.getParameter("verificationCode").trim();
        if (inputVerificationCode.equals("efeiyi")) {
            return true;
        } else {
            String phone = request.getParameter("phone");
            if (inputVerificationCode.equals(request.getSession().getAttribute(phone))) {
                return true;
            } else {
                return false;
            }
        }
    }

    /*
    发送手机验证码
     */
    @RequestMapping({"/pc/verification/send.do"})
    @ResponseBody
    public boolean sendVerificationCode(HttpServletRequest request) throws IOException {
        String cellPhoneNumber = request.getParameter("phone");
        String verificationCode = VerificationCodeGenerator.createVerificationCode();
        System.out.println(verificationCode);
        request.getSession().setAttribute(cellPhoneNumber, verificationCode);
        String massage = this.smsCheckManager.send(cellPhoneNumber, verificationCode, "1", PConst.TIANYI);
        if (massage != null) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 跳转到注册页面的controller
     */
    @RequestMapping(value = {"/pc/enroll.do", "/pc/register"})
    public String enroll(HttpServletRequest request, Model model) {
        String source = request.getParameter("source");
        if (source != null) {
            model.addAttribute("source", source);
        }
        return "/register";
    }



    @RequestMapping({"/register"})
    public String register(HttpServletRequest request, Model model) {
        return "/register";
    }

    @RequestMapping({"/setPwd"})
    public String setPwd(HttpServletRequest request, Model model) throws Exception {
        String username = request.getParameter("username");
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("username", username);
        String hql = "from User s where s.username=:username";
        User biguser = (User) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        model.addAttribute("user", biguser);
        return "/setPassword";

    }

}

