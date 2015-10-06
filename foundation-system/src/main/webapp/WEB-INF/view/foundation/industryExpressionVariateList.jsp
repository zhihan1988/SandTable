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


<div class="am-g">
    <div style="text-align: left;margin-bottom: 10px">
        <a type="button" class="am-btn am-btn-default am-btn-xs"
           href="<c:url value="/basic/xm.do?qm=formIndustryExpressionVariate&industryExpressionId=${pageContext.request.getParameter('industryExpressionId')}"/>"><span
                class="am-icon-plus"></span>新建变量</a>
        <a type="button" class="am-btn am-btn-default am-btn-xs"
           href="javascript:void(0)" onclick="simulate()"><span
                class="am-icon-plus"></span>仿真</a>
    </div>
    <div class="am-u-sm-12 am-u-md-6">
    </div>
    <div class="am-u-sm-12">
        <table class="am-table am-table-striped am-table-hover table-main">
            <thead>
            <tr>
                <%--<th class="table-set">操作</th>--%>
                <th class="table-title">操作</th>
                <th class="table-title">变量名</th>
                <th class="table-title">初始值</th>
                <th class="table-title">步长</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${objectList}" var="obj">
            <tr id="${obj.id}">
                <td>
                    <div class="am-btn-toolbar">
                        <div class="am-btn-group am-btn-group-xs">
                            <a class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only"
                               href="<c:url value="/basic/xm.do?qm=removeIndustryExpressionVariate&id=${obj.id}"/>"><span
                                    class="am-icon-trash-o">删除</span>
                            </a>
                        </div>
                    </div>
                </td>
                <td class="am-hide-sm-only">${obj.name}</td>
                <td class="am-hide-sm-only">${obj.initialValue}</td>
                <td class="am-hide-sm-only">${obj.step}</td>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <div id="iframe" class="am-u-md-12">

    </div>
</div>
<script>
    var resultUrl = "<c:url value="/result.do?expressionId=${pageContext.request.getParameter('industryExpressionId')}"/>";

    function simulate() {
        var iframe = '<iframe style="width:100%;height:100%" src="' + resultUrl + '"></iframe>'
        $("#iframe").html(iframe);
    }

</script>

