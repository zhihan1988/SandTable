<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: pgwt
  Date: 11/29/15
  Time: 2:00 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>个人中心－企业列表</title>
</head>
<body>
<h2>企业列表</h2>



<ul class="am-list">
    <c:forEach items="${companyList}" var="company">
        <li><a href="<c:url value="/personal/company/${company.id}"/>">${company.name}[${company.slogan}]</a></li>
    </c:forEach>
</ul>
</body>
</html>
