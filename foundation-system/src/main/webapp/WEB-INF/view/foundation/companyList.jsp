<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<%--
  Created by IntelliJ IDEA.
  User: pgwt
  Date: 10/3/15
  Time: 4:32 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title></title>
  <script>
    function removeC(cid) {
      jQuery.ajax({
        type: "GET",
        url: '<c:url value="/basic/xmj.do?qm=removeCompanyTerm"/>',
        data: {id: cid},
        dataType: "json",
        success: function (data) {
          $("#" + cid).remove();
        }
      });
    }
  </script>

</head>
<body>
  <div class="am-g">
    <div class="am-u-sm-12 am-u-md-6">
    </div>
    <div class="am-u-sm-12">
      <table class="am-table am-table-striped am-table-hover table-main">
        <thead>
        <tr>
          <%--<th class="table-set">操作</th>--%>
          <th class="table-title">企业名称</th>
          <th class="table-title">老板</th>
          <th class="table-title">注册资本</th>
          <th class="table-title">当前现金</th>
          <th class="table-title">创建时间</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${requestScope.pageInfo.list}" var="company">
          <tr id="${company.id}">
            <%--<td>--%>
              <%--<div class="am-btn-toolbar">--%>
                <%--<div class="am-btn-group am-btn-group-xs">--%>
                  <%--<button class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only"--%>
                          <%--onclick="showConfirm('提示','确认删除？',function(){removeC('${company.id}')})"><span--%>
                          <%--class="am-icon-trash-o">删除</span>--%>
                  <%--</button>--%>
                <%--</div>--%>
              <%--</div>--%>
            <%--</td>--%>
            <td class="am-hide-sm-only"><a>${company.name}</a>
            </td>
            <td class="am-hide-sm-only">${company.director.name}</td>
            <td class="am-hide-sm-only">${company.capital}</td>
            <td class="am-hide-sm-only">${company.deposit}</td>
            <td class="am-hide-sm-only">${company.createDatetime}</td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
  <div style="clear: both">
    <c:url value="/basic/xm.do" var="url"/>
    <ming800:pcPageList bean="${requestScope.pageInfo.pageEntity}" url="${url}">
      <ming800:pcPageParam name="qm" value="${requestScope.qm}"/>
      <ming800:pcPageParam name="conditions" value="${requestScope.conditions}"/>
    </ming800:pcPageList>
  </div>
</body>
</html>
