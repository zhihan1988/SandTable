<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: pgwt
  Date: 10/17/15
  Time: 1:36 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>注册</title>
</head>
<body>

<form class="am-form" name="user" action="<c:url value="/pc/saveEnrollUser.do"/>" data-am-validator>
    <fieldset>
        <legend>注册用户</legend>

        <div class="am-form-group">
            <label for="username">手机号</label>
            <input type="text" class="" name="username" id="username" placeholder="输入手机号" required>

            <p id="tishi" style="display: none;color: red;">用户名已存在</p>
        </div>

        <div class="am-form-group">
            <label for="password">密码</label>
            <input type="password" class="" name="password" id="password" placeholder="设置个密码吧" required>
        </div>

        <div class="am-form-group">
            <label for="passwordConfirm">确认密码</label>
            <input type="password" class="" id="passwordConfirm" placeholder="请确认密码" data-equal-to="#password" required>
        </div>


        <p><a onclick="submitForm()" class="am-btn am-btn-primary">提交</a></p>

        <p>
            <button type="submit" class="am-btn am-btn-default" style="display: none" id="submit">提交</button>
        </p>
    </fieldset>
</form>

<script>

    var usernameFlag = false;


    function submitForm() {
        if (usernameFlag) {
            $("#tishi").hide();
            $("#submit").click();
        } else {
            $("#tishi").show();
        }
    }
    $().ready(function () {
        $("#username").blur(function () {
            jQuery.ajax({
                type: 'get',
                async: false,
                url: '<c:url value="/pc/checkUserName.do"/>',
                dataType: 'json',
                data: {
                    username: $(this).val()
                },
                success: function (data) {

                    if (data) {
                        console.log("用户名已经存在");
                        usernameFlag = false;
                        $("#tishi").show();
                    } else {
                        usernameFlag = true;
                        $("#tishi").hide();
                    }
                },
                error: function () {
                    console.log("数据获取失败");
                }
            });
        });
    });


</script>
</body>


</html>
