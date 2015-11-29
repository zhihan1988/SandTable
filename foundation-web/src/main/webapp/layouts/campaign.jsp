<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/6/24
  Time: 17:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>--%>
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

<div class="am-cf admin-main">
    <div class="admin-content" style="height: auto;padding:10px">
        <ul class="am-nav am-nav-pills am-nav-justify">
            <li><a style="float: left;" href="<c:url value="/campaign/listCampaign"/>"><i class="am-icon-angle-left am-icon-sm"></i></a></li>
            <li><a>竞赛</a></li>
            <li><a style="float: right  ;  padding-left: 4px;
    padding-right: 4px;;" href="<c:url value="/personal/company/list"/>">我的</a><a style="float: right;    padding-left: 4px;
    padding-right: 4px;" href="<c:url value="/"/>">首页</a></li>
        </ul>
        <br>
        <sitemesh:write property='body'/>
    </div>

</div>


</body>

</html>
