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

    <h1>${campaign.name}</h1>

    <hr/>

    <div>
        <table class="am-table">
            <thead>
            <tr>
                <th>名称</th>
                <th>口号</th>
            </thead>
            <tbody>
            <c:forEach items="${companyList}" var="company" varStatus="vs">
                <tr>
                    <td>
                        <a href="<c:url value="#"/>">${company.name}</a>
                        <a href="<c:url value="/campaign/main.do?companyId=${company.id}"/>">进入游戏</a>
                    </td>
                    <td>${company.slogan}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <button type="button" class="am-btn am-btn-primary">创办企业</button>

    <hr/>

    <p>企业的竞争优势值：</p>
    <p>勇气：越早加入一场比赛；</p>
    <p>团队：公司有更多的队友参与，</p>
    <p>人脉：介绍了更多的用户，</p>
    <p>决心：少量的现金投入。</p>

</body>
</html>