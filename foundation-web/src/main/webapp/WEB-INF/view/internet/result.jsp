<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html class="no-js">
<head></head>
<body>

<h3>
   ${result}
</h3>
<div>
    <ul>
        <li>公司现金：${companyCash}</li>
        <li>上期收入：${campaignDateInCash}</li>
        <li>上期支出：${campaignDateOutCash}</li>
    </ul>
</div>

<div class="am-panel am-panel-default am-scrollable-horizontal">

    <table class="am-table am-table-bordered am-table-compact am-text-nowrap">
        <c:forEach items="${globalReport}" var="g">
            <tr>
                <td>${g.key}:${g.value}</td>
            </tr>
        </c:forEach>
    </table>
    <table class="am-table am-table-bordered am-table-compact am-text-nowrap" style="margin-top: 20px;">
        <c:forEach items="${propertyReport}" var="r" begin="0" end="0">
            <tr>
                <td></td>
                <c:forEach items="${r.value}" var="property">
                    <td>${property.key}</td>
                </c:forEach>
            </tr>
        </c:forEach>
        <c:forEach items="${propertyReport}" var="r">
            <tr>
                <td>${r.key}</td>
                <c:forEach items="${r.value}" var="property">
                    <td>${property.value}</td>
                </c:forEach>
            </tr>
        </c:forEach>
    </table>
    <table class="am-table am-table-bordered am-table-compact am-text-nowrap" style="margin-top: 20px;">
        <c:forEach items="${accountReport}" var="acountMap" begin="0" end="0">
            <tr>
                <td></td>
                <c:forEach items="${acountMap.value}" var="account">
                    <td>${account.key}</td>
                </c:forEach>
            </tr>
        </c:forEach>
        <c:forEach items="${accountReport}" var="acountMap">
            <tr>
                <td>${acountMap.key}</td>
                <c:forEach items="${acountMap.value}" var="account">
                    <td>${account.value}</td>
                </c:forEach>
            </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>