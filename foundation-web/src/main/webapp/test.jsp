<%--
  Created by IntelliJ IDEA.
  User: Hean
  Date: 2015/8/27
  Time: 17:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
</head>
<body>
    <div><a href="<c:url value="/work/begin?campaignId=1"/>">开始游戏</a></div>
    <div><a href="<c:url value="/work/next?campaignId=1"/>">开始下一回合</a></div>
    <div><a href="<c:url value="/work/pre?campaignId=1"/>">回到上一回合</a></div>
</body>
</html>