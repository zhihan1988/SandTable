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
    <title>SAND</title>
    <meta name="description" content="SAND">
    <meta name="keywords" content="index">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="icon" type="image/png" href="<c:url value='/assets/i/favicon.png'/>"/>
    <link rel="apple-touch-icon-precomposed" href="<c:url value='/assets/i/app-icon72x72@2x.png'/>"/>
    <meta name="apple-mobile-web-app-title" content="SAND"/>
    <link rel="stylesheet" href="<c:url value='/assets/css/amazeui.min.css'/>"/>
    <%--<link rel="stylesheet" href="<c:url value='/assets/css/admin.css'/>"/>--%>
    <script src="<c:url value='/resources/jquery/jquery-1.11.1.min.js'/>"></script>
    <script src="<c:url value='/resources/js/alert.js'/>"></script>
    <script src="<c:url value='/resources/assets/js/amazeui.min.js'/>"></script>
    <script src="<c:url value="/resources/js/radioSet.js"/>"></script>
    <sitemesh:write property='head'/>
    <style>
        .efy-active {
            background-color: #9a9a9a;
        }
    </style>
</head>
<body>

<%--<header class="am-topbar am-topbar-inverse">--%>
    <%--<h1 class="am-topbar-brand">--%>
        <%--<a href="#">商世界</a>--%>
    <%--</h1>--%>

    <%--<button class="am-topbar-btn am-topbar-toggle am-btn am-btn-sm am-btn-success am-show-sm-only" data-am-collapse="{target: '#doc-topbar-collapse'}"><span class="am-sr-only">导航切换</span> <span class="am-icon-bars"></span></button>--%>

    <%--<div class="am-collapse am-topbar-collapse" id="doc-topbar-collapse">--%>
        <%--<ul class="am-nav am-nav-pills am-topbar-nav">--%>
            <%--<li class="am-active"><a href="<c:url value="/home.do"/> ">首页</a></li>--%>
            <%--<li><a >行业</a></li>--%>
            <%--<li><a href="#">关于</a></li>--%>

        <%--</ul>--%>



        <%--<div class="am-topbar-right">--%>
            <%--<a href="<c:url value="/pc/register"/>" class="am-btn am-btn-primary am-topbar-btn am-btn-sm">新用户注册</a>--%>
        <%--</div>--%>
        <%--<div class="am-topbar-right">--%>
            <%--<a href="<c:url value="/"/> " class="am-btn am-btn-primary am-topbar-btn am-btn-sm">老用户登录</a>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</header>--%>
<ul class="am-nav am-nav-pills">
    <li class="am-active"><a href="<c:url value="/home.do"/> ">首页</a></li>
    <li><a href="<c:url value="/industry.do"/>">行业</a></li>
    <li style="float: right"><a href="<c:url value="/pc/register"/>">注册</a></li>
    <li style="float: right;"><a href="<c:url value="/"/>">登录</a></li>
</ul>

<div class="am-cf admin-main">


<div class="admin-content" style="height: auto;">
<sitemesh:write property='body'/>
</div>

</div>




</body>

</html>
