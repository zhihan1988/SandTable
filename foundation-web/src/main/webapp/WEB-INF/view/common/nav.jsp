<%@ page import="com.rathink.ie.user.util.AuthorizationUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: pgwt
  Date: 11/29/15
  Time: 12:05 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<ul class="am-nav am-nav-pills">
    <li><h1 style="font-size: 27px;">商世界</h1></li>
    <c:forEach items="${jnode.children}" var="child">
        <c:if test="${!child.getState().equals('3')&&!child.getState().equals('4')}">
            <li class="${child.jnodeMatch("am-active",currentJnode)}">
                <a href="<c:url value="${child.url}"/>"
                   title="${child.text_zh_CN}">${child.text_zh_CN}</a>
            </li>
        </c:if>
        <% if (AuthorizationUtil.getMyUser().getId() == null) {%>
        <c:if test="${child.getState().equals('3')||child.getState().equals('4')}">
            <li style="float: right;" class="${child.jnodeMatch("am-active",currentJnode)}">
                <a style="padding-left: 2px; padding-right: 2px" href="<c:url value="${child.url}"/>"
                   title="${child.text_zh_CN}">${child.text_zh_CN}</a>
            </li>
        </c:if>
        <% } %>
    </c:forEach>
    <% if (AuthorizationUtil.getMyUser().getId() != null) {%>
    <li style="float: right;" class="${child.jnodeMatch("am-active",currentJnode)}">
        <a href="<c:url value="/personal/company/list"/>"
           title="个人中心">个人中心</a>
    </li>
    <% } %>
    <%--<li><a href="<c:url value="/home.do"/> ">首页</a></li>--%>
    <%--<li><a href="<c:url value="/industry.do"/>">行业</a></li>--%>
    <%--<li style="float: right;"><a style="padding-left: 2px; padding-right: 2px" href="<c:url value="/index.jsp"/>">登录</a>--%>
    <%--</li>--%>
    <%--<li style="float: right;"><a style="padding-left: 2px; padding-right: 2px"--%>
    <%--href="<c:url value="/pc/register"/>">注册</a></li>--%>
</ul>
