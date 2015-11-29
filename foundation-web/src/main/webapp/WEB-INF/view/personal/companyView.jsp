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
    <title>个人中心－企业详情</title>
</head>
<body>
<ul class="am-list">
    <li><a>企业名称:${company.name}</a></li>
    <li><a>企业口号:${company.slogan}</a></li>
    <li><a>当前季度:<c:if test="${empty company.currentCampaignDate}">0</c:if><c:if test="${not empty company.currentCampaignDate}">${company.currentCampaignDate}</c:if>/${company.campaign.industry.totalTerm}</a></li>
    <li>
        <a href="<c:url value="/manufacturing/main.do?companyId=${company.id}&campaignId=${company.campaign.id}"/>">开始经营</a>
    </li>
</ul>
</body>
</html>
