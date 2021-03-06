<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html class="no-js">
<head></head>
<body>

<h1>${campaign.name}</h1>

<c:choose>
    <c:when test="${campaign.industry.type == 'manufacturing'}">
        <a href="<c:url value="/old/manufacturing/main.do?companyId=${myCompany.id}&campaignId=${myCompany.campaign.id}"/>">开始经营</a>
    </c:when>
    <c:when test="${campaign.industry.type == 'internet'}">
        <a href="<c:url value="/internet/main.do?companyId=${myCompany.id}&campaignId=${myCompany.campaign.id}"/>">开始经营</a>
    </c:when>
</c:choose>

<hr/>

<div>
    <table class="am-table">
        <thead>
        <tr>
            <th>公司名称</th>
            <th>口号</th>
        </thead>
        <tbody>
        <c:forEach items="${companyList}" var="company" varStatus="vs">
            <tr>
                <td>
                    <a href="<c:url value="/company/${company.id}"/>">${company.name}</a>
                </td>
                <td>${company.slogan}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<c:if test="${companyStatus}">
    <a href="<c:url value="/company/form?campaignId=${campaign.id}"/>" class="am-btn am-btn-primary">创办企业</a>
</c:if>

<hr/>

<p>企业的竞争优势值：</p>

<p>勇气：越早加入一场比赛；</p>

<p>团队：公司有更多的队友参与，</p>

<p>人脉：介绍了更多的用户，</p>

<p>决心：少量的现金投入。</p>

</body>
</html>