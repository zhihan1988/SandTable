<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/6/24
  Time: 17:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>--%>
<%String path = request.getContextPath();%>
<html class="no-js">
<head>
    <title>首页</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Amaze UI Admin index Examples</title>
    <meta name="description" content="这是一个 index 页面">
    <meta name="keywords" content="index">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="icon" type="image/png" href="<c:url value='/resources/assets/i/favicon.png'/>"/>
    <link rel="apple-touch-icon-precomposed" href="<c:url value='/resources/assets/i/app-icon72x72@2x.png'/>"/>
    <meta name="apple-mobile-web-app-title" content="Amaze UI"/>
    <link rel="stylesheet" href="<c:url value='/resources/assets/css/amazeui.min.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/resources/assets/css/admin.css'/>"/>
    <script src="<c:url value='/resources/jquery/jquery-1.11.1.min.js'/>"></script>
    <script src="<c:url value='/resources/js/alert.js'/>"></script>
    <script src="<c:url value='/resources/assets/js/amazeui.min.js'/>"></script>
    <%--<script src="<c:url value='/base_resource/p/scripts/ckeditor/ckeditor.js'/>" charset="GB2312"></script>--%>
    <script src="<c:url value='/resources/plugins/ckeditor/ckeditor.js'/>" ></script>
    <style>
        .efy-active {
            background-color: #9a9a9a;
        }
    </style>
</head>
<body>

<div class="am-g am-g-fixed" style="max-width: 550px;margin-top: 200px;">
    <div class="am-u-md-12" style="box-shadow: 0px 0px 2px #626262;">
        <div class="am-u-md-12" style="text-align: center ;"><h2 style=" margin-top: 1.6rem;">登录</h2></div>
        <form class="am-form am-form-horizontal" action="<c:url value="/j_spring_security_check"/>" method="post">
            <div class="am-form-group">
                <label for="username" class="am-u-sm-2 am-form-label">用户名</label>

                <div class="am-u-sm-10">
                    <input type="text" name="username" id="username" placeholder="输入用户名" value="user1">
                </div>
            </div>

            <div class="am-form-group">
                <label for="password" class="am-u-sm-2 am-form-label">密码</label>

                <div class="am-u-sm-10">
                    <input type="password" name="password" id="password" placeholder="输入密码" value="123456">
                </div>
            </div>

            <div class="am-form-group">
                <div class="am-u-sm-10 am-u-sm-offset-2">
                    <button type="submit" class="am-btn am-btn-default">登录</button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
