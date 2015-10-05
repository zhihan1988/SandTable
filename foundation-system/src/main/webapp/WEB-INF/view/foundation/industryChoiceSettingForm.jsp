<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: pgwt
  Date: 10/3/15
  Time: 7:30 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<div><h2>设置对象</h2></div>

<form class="am-form am-form-horizontal" action="<c:url value="/basic/xm.do"/>">

  <input type="hidden" name="id" value="${object.id}">
  <input type="hidden" name="industry.id" value="${pageContext.request.getParameter("industryId")}">
  <input type="hidden" name="qm" value="saveOrUpdateIndustryChoiceSetting">

  <div class="am-form-group">
    <label for="doc-ipt-3" class="am-u-sm-2 am-form-label">名称</label>
    <div class="am-u-sm-10">
      <input type="text" name="name" value="${object.name}" id="doc-ipt-3" >
    </div>
  </div>

  <div class="am-form-group">
    <label for="doc-ipt-pwd-3" class="am-u-sm-2 am-form-label">标题</label>
    <div class="am-u-sm-10">
      <input type="text" name="title" value="${object.title}" id="doc-ipt-pwd-3" >
    </div>
  </div>

  <div class="am-form-group">
    <label for="doc-ipt-pwd-4" class="am-u-sm-2 am-form-label">部门</label>
    <div class="am-u-sm-10">
      <input type="text" name="dept" value="${object.dept}" id="doc-ipt-pwd-4" >
    </div>
  </div>

  <div class="am-form-group">
    <label for="doc-ipt-pwd-5" class="am-u-sm-2 am-form-label">类型</label>
    <div class="am-u-sm-10">
      <input type="text" name="type" value="${object.type}" id="doc-ipt-pwd-5" >
    </div>
  </div>

  <div class="am-form-group">
    <label for="doc-ipt-pwd-2" class="am-u-sm-2 am-form-label">数据</label>
    <div class="am-u-sm-10">
      <input type="text" name="valueSet" value="${object.valueSet}" id="doc-ipt-pwd-2" >
    </div>
  </div>

  <div class="am-form-group">
    <div class="am-u-sm-10 am-u-sm-offset-2">
      <button type="submit" class="am-btn am-btn-default">保存</button>
    </div>
  </div>
</form>


</body>
</html>
