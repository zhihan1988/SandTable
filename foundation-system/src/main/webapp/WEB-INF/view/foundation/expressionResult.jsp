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

<c:forEach items="${result}" var="table">


<table class="am-table am-table-bordered">
    <tbody>
    <c:forEach items="${table}" var="y">
        <tr>
            <c:forEach items="${y}" var="x">
                <td>
                        ${x}
                </td>
            </c:forEach>
        </tr>
    </c:forEach>
    </tbody>
</table>
</c:forEach>
</body>
</html>
