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
<ul class="am-nav am-nav-pills am-nav-justify">
    <li ><a style="float: left;" onclick="history.go(-1)"><i class="am-icon-angle-left am-icon-sm"></i></a></li>
    <li ><a>${currentJnode.text_zh_CN}</a></li>
    <li><a style="float: right;    padding-left: 4px;
    padding-right: 4px;" href="<c:url value="/"/>">首页</a></li>
</ul>

<ul class="am-nav am-nav-pills">
    <c:forEach items="${jnode.children}" var="child">
        <li class="${child.jnodeMatch("am-active",currentJnode)}">
            <a href="<c:url value="${child.url}"/>"
               title="${child.text_zh_CN}">${child.text_zh_CN}</a>
        </li>
    </c:forEach>
</ul>
