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
<div><h2>设置具体对象</h2></div>
<form class="am-form am-form-horizontal" action="<c:url value="/basic/xm.do"/>">

  <input type="hidden" name="id" value="${object.id}">
  <input type="hidden" name="industryResource.id" value="${pageContext.request.getParameter("irId")}" >
  <input type="hidden" name="qm" value="saveOrUpdateIndustryChoice">

  <div class="am-form-group">
    <label for="doc-ipt-3" class="am-u-sm-2 am-form-label">名称</label>
    <div class="am-u-sm-10">
      <input type="text" name="name" value="${object.name}" id="doc-ipt-3" >
    </div>
  </div>

  <div class="am-form-group">
    <label for="doc-ipt-pwd-4" class="am-u-sm-2 am-form-label">类型</label>
    <div class="am-u-sm-10">
      <input type="text" name="type" value="${object.type}" id="doc-ipt-pwd-4" >
    </div>
  </div>
  <div class="am-form-group">
    <label for="doc-ipt-pwd-7" class="am-u-sm-2 am-form-label">值</label>
    <div class="am-u-sm-10">
      <input type="text" name="value" value="${object.value}" id="doc-ipt-pwd-7" >
    </div>
  </div>

  <div class="am-form-group">
    <label for="doc-ipt-pwd-8" class="am-u-sm-2 am-form-label">值2</label>
    <div class="am-u-sm-10">
      <input type="text" name="value2" value="${object.value2}" id="doc-ipt-pwd-8" >
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
