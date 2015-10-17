<%--
  Created by IntelliJ IDEA.
  User: pgwt
  Date: 10/17/15
  Time: 10:41 上午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
</head>
<body>
<ming800:radioSet valueSet="key1:value1,key2:value2,key3:value3,key4:value4,key5:value5" name="test" onclick="test" checkedValue="value3"/>

<script src="<c:url value="/resources/js/radioSet.js"/>"></script>
<script>
    function test(element) {
        console.log("测试函数");
    }
</script>

</body>
</html>
