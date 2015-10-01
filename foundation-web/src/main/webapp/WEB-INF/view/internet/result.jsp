<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<!doctype html>
<html class="no-js">
<head></head>
<body>
<header data-am-widget="header" class="am-header am-header-default">
    <div class="am-header-left am-header-nav">
        <a href="<c:url value="/campaign/${campaign.id}"/>" class="">
            行业
        </a>
        <a href="#" class="">
            <%--<i class="am-header-icon am-icon-phone"></i>--%>
        </a>
    </div>

    <div class="am-header-title">
        ${result}
    </div>

    <div class="am-header-right am-header-nav">
        <a href="#" class="">
        </a>
    </div>
</header>

<div>
    <b>财务报告</b>
    <table class="am-table am-table-bordered am-table-compact am-text-nowrap">
        <tbody>
        <tr>
            <td>公司现金</td>
            <td>上期收入</td>
            <td>上期支出</td>
        </tr>
        <tr>
            <td><f:formatNumber value="${companyCash}" pattern="#,#00.#"/></td>
            <td><f:formatNumber value="${campaignDateInCash}" pattern="#,#00.#"/></td>
            <td><f:formatNumber value="${campaignDateOutCash}" pattern="#,#00.#"/></td>
        </tr>
        </tbody>
    </table>
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
        <c:forEach items="${propertyReport}" var="r" begin="1">
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
        <c:forEach items="${accountReport}" var="acountMap" begin="1">
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