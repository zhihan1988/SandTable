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
    <div style="text-align: left;margin-bottom: 10px">
      <a type="button" class="am-btn am-btn-default am-btn-xs" href="<c:url value="/basic/xm.do?qm=formIndustryChoice&irId=${pageContext.request.getParameter('irId')}"/>"><span class="am-icon-plus"></span>新建</a>
    </div>
    <div class="am-u-sm-12 am-u-md-6">
    </div>
    <div class="am-u-sm-12">
      <table class="am-table am-table-striped am-table-hover table-main">
        <thead>
        <tr>
          <%--<th class="table-set">操作</th>--%>
          <th class="table-title">操作</th>
          <th class="table-title">名称</th>
          <th class="table-title">类型</th>
          <th class="table-title">值</th>
          <th class="table-title">值2</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${requestScope.pageInfo.list}" var="obj">
        <tr id="${obj.id}">
          <td>
            <div class="am-btn-toolbar">
              <div class="am-btn-group am-btn-group-xs">
                <a class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only"
                   href="<c:url value="/basic/xm.do?qm=formIndustryChoiceSetting&id=${obj.id}"/>"><span
                        class="am-icon-trash-o">修改</span>
                </a>
              </div>
            </div>
          </td>
          <td class="am-hide-sm-only">${obj.name}</td>
          <td class="am-hide-sm-only">${obj.type}</td>
          <td class="am-hide-sm-only">${obj.value}</td>
          <td class="am-hide-sm-only">${obj.value2}</td>
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
