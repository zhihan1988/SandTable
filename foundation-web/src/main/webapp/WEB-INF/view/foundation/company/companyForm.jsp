<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: pgwt
  Date: 10/17/15
  Time: 3:30 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>新建企业</title>
</head>
<body>
<form class="am-form" name="user" action="<c:url value="/company/saveOrUpdate"/>" data-am-validator>
  <fieldset>
    <legend>新建企业</legend>

    <input type="hidden" name="campaignId" value="${pageContext.request.getParameter("campaignId")}">
    <div class="am-form-group">
      <label for="name">名字</label>
      <input type="text" class="" name="name" id="name" placeholder="输入企业名称" required>
    </div>

    <div class="am-form-group">
      <label for="slogan">口号</label>
      <input type="text" class="" name="slogan" id="slogan" placeholder="输入企业的口号" required>
    </div>

    <div class="am-form-group">
      <label for="slogan">简介</label>
      <textarea type="text" class="" name="memo" id="memo" placeholder="输入企业的口号" 输入企业的简介></textarea>
    </div>

    <p>
      <button type="submit" class="am-btn am-btn-primary" style="display: block" id="submit">提交</button>
    </p>
  </fieldset>
</form>
</body>
</html>
