<%--
  Created by IntelliJ IDEA.
  User: pgwt
  Date: 10/5/15
  Time: 6:04 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
    <div class="am-u-md-12">
        <ul class="am-list">
            <li><a href="#">公式名称: ${object.name}</a></li>
            <li><a href="#">表达式: ${object.expression}</a></li>
        </ul>
    </div>

    <jsp:include page="/basic/xm.do?qm=listIndustryExpressionVariate_defaut&industryExpressionId=${object.id}&conditions=industryExpression.id:${object.id}" flush="true"></jsp:include>

</body>
</html>
