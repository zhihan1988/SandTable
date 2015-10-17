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
    <title>新建一场比赛</title>
    <link href="<c:url value="/resources/plugins/datetimepicker-master/css/amazeui.datetimepicker.css"/>" rel="stylesheet" type="text/css" >
</head>
<body>
<form class="am-form" name="user" action="<c:url value="/campaign/saveOrUpdate"/>" data-am-validator>
  <fieldset>
    <legend>新建一场比赛</legend>

    <div class="am-form-group">
      <label for="name">名字</label>
      <input type="text" class="" name="name" id="name" placeholder="输入比赛名称" required>
    </div>

    <div class="am-form-group">
      <label for="doc-select-1">选择行业</label>
      <select name="industryId" id="doc-select-1">
        <c:forEach items="${industryList}" var="industry">
        <option value="${industry.id}">${industry.name}</option>
        </c:forEach>
      </select>
      <span class="am-form-caret"></span>
    </div>

    <div class="am-form-group">
      <label for="datetimepicker">开始时间</label>
      <input type="text"  name="startDateTime" id="datetimepicker" data-date-format="yyyy-mm-dd hh:ii">
    </div>

    <%--<p><a onclick="submitForm()" class="am-btn am-btn-primary">提交</a></p>--%>

    <p>
      <button type="submit" class="am-btn am-btn-primary" style="display: block" id="submit">提交</button>
    </p>
  </fieldset>
</form>
<script src="<c:url value="/resources/plugins/datetimepicker-master/js/amazeui.datetimepicker.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/plugins/datetimepicker-master/js/locales/amazeui.datetimepicker.zh-CN.js"/>" charset="UTF-8"></script>
<script>$('#datetimepicker').datetimepicker({language:  'zh-CN'});</script>
</body>
</html>
