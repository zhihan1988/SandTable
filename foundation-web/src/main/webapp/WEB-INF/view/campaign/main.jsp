<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html class="no-js">
<head></head>
<body>
<div data-am-widget="tabs" class="am-tabs am-tabs-default" style="margin: 0;padding: 0;">
    <ul class="am-tabs-nav am-cf">
        <li class="am-active"><a href="[data-tab-panel-0]">市场营销</a></li>
        <li class=""><a href="[data-tab-panel-1]">产品研发</a></li>
        <li class=""><a href="[data-tab-panel-2]">运营</a></li>
        <li class=""><a href="[data-tab-panel-3]">人才</a></li>
    </ul>
    <div class="am-tabs-bd">
        <div data-tab-panel-0 class="am-tab-panel am-active">

            <c:forEach items="${deptPropertyMap['MARKET']}" var="property">
                <p>
                    ${property.label}:${property.value}
                </p>
            </c:forEach>
            <div class="am-panel-group" id="accordion">
                <div class="am-panel am-panel-default">
                    <div class="am-panel-hd">
                        <h4 class="am-panel-title" data-am-collapse="{parent: '#accordion', target: '#do-not-say-1'}">
                            指令栏
                        </h4>
                    </div>
                    <div id="do-not-say-1" class="am-panel-collapse am-collapse am-in">
                        <div class="am-panel-bd">
                            <a>市场投入</a>
                        </div>
                    </div>
                </div>
                <div class="am-panel am-panel-default">
                    <div class="am-panel-hd">
                        <h4 class="am-panel-title" data-am-collapse="{parent: '#accordion', target: '#do-not-say-2'}">
                            历史属性
                        </h4>
                    </div>
                    <div id="do-not-say-2" class="am-panel-collapse am-collapse am-in">
                        <div class="am-panel-bd">

                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div data-tab-panel-1 class="am-tab-panel ">
            <div class="am-panel am-panel-default">
                <div class="am-panel-hd">
                    产品研发
                    <a href="#">资金投入</a>
                </div>
                <div class="am-panel-bd">
                    <c:forEach items="${deptPropertyMap['PRODUCT']}" var="property">
                        <p>
                                ${property.label}:${property.value}
                        </p>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div data-tab-panel-2 class="am-tab-panel ">
            <div class="am-panel am-panel-default">
                <div class="am-panel-hd">
                    运营
                    <a href="#">资金投入</a>
                </div>
                <div class="am-panel-bd">
                    <c:forEach items="${deptPropertyMap['OPERATION']}" var="property">
                        <p>
                                ${property.label}:${property.value}
                        </p>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div data-tab-panel-3 class="am-tab-panel ">
            <div class="am-panel am-panel-default">
                <div class="am-panel-hd">
                    人才
                    <a href="<c:url value="/work/hrChoices?companyId=${company.id}"/>">招聘</a>
                </div>
                <div class="am-panel-bd">
                    <c:forEach items="${hrInstructionList}" var="hrInstruction">
                        <p>${hrInstruction.human.name}(${hrInstruction.statusLabel})</p>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
    <%--<h3>${campaign.name} -- ${campaign.currentCampaignDate}</h3>--%>
</body>
</html>