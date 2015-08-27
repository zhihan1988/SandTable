<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html class="no-js">
<head></head>
<body>

<h3>${campaign.name} -- ${campaign.currentCampaignDate}</h3>

<div class="am-panel-group" id="accordion">
    <div class="am-panel am-panel-default">
        <div class="am-panel-hd">
            <h4 class="am-panel-title" data-am-collapse="{parent: '#accordion', target: '#do-not-say-1'}">
                市场营销
            </h4>
        </div>
        <div class="am-panel-bd"  data-am-collapse="{parent: '#accordion', target: '#do-not-say-1'}">
            <c:forEach items="${deptPropertyMap['MARKET']}" var="property">
                <p>${property.label}:${property.value}</p>
            </c:forEach>
        </div>
        <div id="do-not-say-1" class="am-panel-collapse am-collapse am-in">
            <div class="am-panel-bd">
                投入
            </div>
        </div>
    </div>
    <div class="am-panel am-panel-default">
        <div class="am-panel-hd">
            <h4 class="am-panel-title" data-am-collapse="{parent: '#accordion', target: '#do-not-say-2'}">
                产品研发
            </h4>
        </div>
        <div class="am-panel-bd"  data-am-collapse="{parent: '#accordion', target: '#do-not-say-2'}">
            <c:forEach items="${deptPropertyMap['PRODUCT']}" var="property">
                <p>${property.label}:${property.value}</p>
            </c:forEach>
        </div>
        <div id="do-not-say-2" class="am-panel-collapse am-collapse">
            <div class="am-panel-bd">
                研发投入
            </div>
        </div>
    </div>
    <div class="am-panel am-panel-default">
        <div class="am-panel-hd">
            <h4 class="am-panel-title" data-am-collapse="{parent: '#accordion', target: '#do-not-say-3'}">
                运营
            </h4>
        </div>
        <div class="am-panel-bd"  data-am-collapse="{parent: '#accordion', target: '#do-not-say-3'}">
            <c:forEach items="${deptPropertyMap['OPERATION']}" var="property">
                <p>${property.label}:${property.value}</p>
            </c:forEach>
        </div>
        <div id="do-not-say-3" class="am-panel-collapse am-collapse">
            <div class="am-panel-bd">
                定位
            </div>
        </div>
    </div>
    <div class="am-panel am-panel-default">
        <div class="am-panel-hd">
            <h4 class="am-panel-title" data-am-collapse="{parent: '#accordion', target: '#do-not-say-4'}">
                人才
            </h4>
        </div>
        <div class="am-panel-bd"  data-am-collapse="{parent: '#accordion', target: '#do-not-say-4'}">
            <c:forEach items="${hrInstructionList}" var="hrInstruction">
                <p>${hrInstruction.human.name}(${hrInstruction.statusLabel})</p>
            </c:forEach>
        </div>
        <div id="do-not-say-4" class="am-panel-collapse am-collapse">
            <div class="am-panel-bd">
                定位
            </div>
        </div>
    </div>
</div>
</body>
</html>