df'<%--
  Created by IntelliJ IDEA.
  User: pgwt
  Date: 10/25/15
  Time: 1:49 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<div class="am-u-md-12">
  <h3>比赛信息</h3>
    <ul class="am-list">
      <li><a href="#">比赛名称: ${object.name}</a></li>
      <li><a href="#">开始时间: ${object.startDatetime}</a></li>
      <li><a href="#">状态: ${object.status}</a></li>
      <li><a href="#">当前进度: ${object.currentCampaignDate}</a></li>
    </ul>
</div>

<div class="am-u-md-12">
  <h3>行业信息</h3>
  <ul class="am-list">
    <li><a href="#">行业名称: ${object.industry.name}</a></li>
    <li><a href="#">周期: ${object.industry.term}</a></li>
    <li><a href="#">总周期数: ${object.industry.totalTerm}</a></li>
  </ul>
</div>

<div class="am-u-md-12">
  <h3>参与的企业</h3>
  <jsp:include page="/basic/xm.do?qm=listCompany_campaign&conditions=campaign.id:${object.id}" flush="true"/>
</div>

</body>
</html>
