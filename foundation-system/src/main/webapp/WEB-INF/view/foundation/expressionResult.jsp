<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: pgwt
  Date: 10/5/15
  Time: 3:50 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>

<c:forEach items="${result}" var="table" varStatus="index">

    <c:if test="${!empty ability3}">
        <h3>${ability3.name}=${ability3.initialValue*ability3.step*index.index}</h3>
    </c:if>

    <table class="am-table am-table-bordered">
        <tbody>
        <c:forEach items="${table}" var="y" varStatus="head">
            <tr>
                <c:forEach items="${y}" var="x" varStatus="head2">
                    <td>
                        <c:if test="${head.index==0&&head2.index==0}">
                            ${ability1.name}/${ability2.name}
                        </c:if>

                        <c:if test="${head.index!=0||head2.index!=0}">
                            ${x}
                        </c:if>
                    </td>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:forEach>
</body>
</html>
