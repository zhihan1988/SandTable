<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>--%>
<%String path = request.getContextPath();%>
<html class="no-js">
<head lang="en">
    <style>
        .header {
            text-align: center;
        }
        .header h1 {
            font-size: 200%;
            color: #333;
            margin-top: 30px;
        }
        .header p {
            font-size: 14px;
        }
    </style>
</head>
<body>
<ul class="am-nav am-nav-pills">
    <li><h1 style="font-size: 27px;">商世界</h1></li>
    <li ><a href="<c:url value="/home.do"/> ">首页</a></li>
    <li><a href="<c:url value="/industry.do"/>">行业</a></li>
    <li  style="float: right;"><a style="padding-left: 2px; padding-right: 2px" href="<c:url value="/index.jsp"/>">登录</a></li>
    <li style="float: right;"><a style="padding-left: 2px; padding-right: 2px" href="<c:url value="/pc/register"/>">注册</a></li>
</ul>
<div class="am-g">
    <div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
        <form method="post" class="am-form" action="<c:url value="/j_spring_security_check"/>">
            <label for="username">用户名:</label>
            <input type="text" name="username" id="username" placeholder="输入用户名" value="">
            <br>
            <label for="password">密码:</label>
            <input type="password" name="password" id="password" placeholder="输入密码" value="">
            <br>
            <br />
            <div class="am-cf">
                <input type="submit" name="" value="登 录" class="am-btn am-btn-primary am-btn-sm am-fl">
                <input type="submit" name="" value="忘记密码 ^_^? " class="am-btn am-btn-default am-btn-sm am-fr">
            </div>
        </form>
      <%--  <hr>
        <p>© 2015</p>--%>
    </div>
</div>
</body>
</html>