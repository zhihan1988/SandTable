<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html class="no-js">
<head></head>
<body>


<form class="am-form" action="<c:url value="/work/makeInstruction.do"/>" method="post">
<table class="am-table" style="padding-bottom: 200px;">
    <thead>
    <input type="hidden" name="companyId" value="${company.id}"/>
    <tr>
        <th>名字</th>
        <th>能力</th>
        <th>薪资要求</th>
        <th>出资</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${humanList}" var="human">
        <tr>
            <td><input type="hidden" name="humanId" value="${human.id}"/>${human.name}</td>
            <td>${human.hard}</td>
            <td>${human.salary}</td>
            <td>
                <select name="humanSalary" data-am-selected>
                    <option value="-1">不需要</option>
                    <option value="15000">15000</option>
                    <option value="20000">20000</option>
                    <option value="25000">25000</option>
                    <option value="30000">30000</option>
                </select>
            </td>
        </tr>
    </c:forEach>
    <tr>
        <td colspan="4"><button type="submit" class="am-btn am-btn-primary">确认保存</button></td>
    </tr>
    </tbody>
</table>
</form>

</body>
</html>