<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html class="no-js">
<head></head>
<body>

<div>
    <ul class="am-nav am-nav-pills">
        <li><a href="<c:url value="/campaign/listCampaign"/>">赛事列表</a></li>
        <li class="am-active"><a href="<c:url value="/company/listCompany"/>">我的公司</a></li>
        <li><a href="#">我的资本</a></li>
    </ul>
</div>


<div class="am-margin-top">
    <table class="am-table">
        <thead>
        <tr>
            <th>名称</th>
            <th>口号</th>
            <th>开始时间</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${companyList}" var="company" varStatus="vs">
            <tr>
                <td>
                    <a href="<c:url value="/company/${company.id}"/>">${company.name}</a>
                    <a href="<c:url value="/campaign/main.do?companyId=${company.id}"/>">进入游戏</a>
                </td>
                <td>
                    ${company.slogan}
                </td>
                <td><fmt:formatDate value="${company.createDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<div>

</div>
<div>
    <ming800:pcPageList bean="${pageEntity}" url="${pageContext.request.contextPath}/company/companyList">
        <%--<ming800:page-param2 name="qm" value="${requestScope.qm}"/>--%>
        <ming800:pcPageParam name="conditions"
                             value='<%=request.getParameter("conditions")!=null ? request.getParameter("conditions") : ""%>'/>
        <ming800:pcPageParam name="sort"
                             value='<%=request.getParameter("sort")!=null ? request.getParameter("sort") : ""%>'/>
    </ming800:pcPageList>
</div>

<!--其他内容-->
</body>
</html>