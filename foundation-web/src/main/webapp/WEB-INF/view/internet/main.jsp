<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html class="no-js">
<head></head>
<body>
<%--<header class="am-topbar am-topbar-inverse am-topbar-fixed-top">
    <div class="am-topbar-left">
        <button id="submit" class="am-btn am-btn-primary am-topbar-btn am-btn-sm">结束回合</button>
    </div>
</header>--%>
<%--<div style="position:fixed;right: 0;bottom: 50px;">
    <button class="am-btn am-btn-primary">结束回合</button>
</div>--%>
<h3>${campaign.name} -- ${campaign.formatCampaignDate}</h3>
<div>
    <button type="button" id="endCampaignDate" class="am-btn am-btn-primary">结束回合</button>
</div>
<div>
    <ul>
        <li>公司现金：${companyCash}</li>
        <li>上期收入：${campaignDateInCash}</li>
        <li>上期支出：${campaignDateOutCash}</li>
    </ul>
</div>
<div class="am-panel-group" id="accordion">
    <div class="am-panel am-panel-default">
        <div class="am-panel-hd">
            <h4 class="am-panel-title" data-am-collapse="{parent: '#accordion', target: '#do-not-say-0'}">
                办公室
            </h4>
        </div>
        <div class="am-panel-bd"  data-am-collapse="{parent: '#accordion', target: '#do-not-say-0'}">
            <table class="am-table">
                <thead>
                <tr>
                    <th>办公室描述</th>
                    <th>房租</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${officeInstructionList}" var="instruction">
                    <tr>
                        <td>
                            ${instruction.companyChoice.description}
                        </td>
                        <td>
                            ${instruction.fee}
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div id="do-not-say-0" class="am-panel-collapse am-collapse">
            <div class="am-panel-bd">
                <table class="am-table">
                    <thead>
                    <tr>
                        <th>办公室描述</th>
                        <th>房租</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${officeChoiceList}" var="officeChoice">
                        <tr>
                            <td>
                                <input type="hidden" name="officeId" value="${officeChoice.id}"/>${officeChoice.description}
                            </td>
                            <td>
                                <select id="officeInstruction_fee_${officeChoice.id}" name="officeInstructionFee" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                    <option value="-1">不需要</option>
                                    <c:forEach items="${fn:split(officeChoice.fees, ',')}" var="fee">
                                        <option value="${fee}">${fee}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="am-panel am-panel-default">
        <div class="am-panel-hd">
            <h4 class="am-panel-title" data-am-collapse="{parent: '#accordion', target: '#do-not-say-1'}">
                人才
            </h4>
        </div>
        <div class="am-panel-bd"  data-am-collapse="{parent: '#accordion', target: '#do-not-say-1'}">
            <table class="am-table">
                <thead>
                <tr>
                    <th>名字(部门)</th>
                    <th>能力</th>
                    <th>薪资</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${hrInstructionList}" var="hrInstruction">
                    <tr>
                        <td>
                                ${hrInstruction.companyChoice.name}（${hrInstruction.companyChoice.typeLabel}）
                        </td>
                        <td>${hrInstruction.companyChoice.ability}</td>
                        <td>
                                ${hrInstruction.fee}
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <ul class="am-avg-sm-2 am-thumbnails">
                <li>
                    <div>
                        <div><img class="am-thumbnail" src="http://s.amazeui.org/media/i/demos/bing-1.jpg" /></div>
                        <div>内容</div>
                    </div>
                </li>
                <li>
                    <div>
                        <div><img class="am-thumbnail" src="http://s.amazeui.org/media/i/demos/bing-2.jpg" /></div>
                        <div>内容</div>
                    </div>
                </li>
            </ul>
        </div>
        <div id="do-not-say-1" class="am-panel-collapse am-collapse">
            <div class="am-panel-bd">
                <table class="am-table">
                    <thead>
                    <tr>
                        <th>名字(部门)</th>
                        <th>能力</th>
                        <th>薪资</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${humanList}" var="human">
                        <tr>
                            <td>
                                <input type="hidden" name="humanId" value="${human.id}"/>
                                    ${human.name}
                                <%--（${human.typeLabel}）--%>
                            </td>
                            <td>${human.value}</td>
                            <td>
                                <select id="hrInstruction_fee_${human.id}" name="humanInstructionFee" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                    <option value="-1">不需要</option>
                                    <c:forEach items="${fn:split(human.fees, ',')}" var="fee">
                                        <option value="${fee}">${fee}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
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
                <c:choose>
                    <c:when test="${property.display == 'PERCENT'}">
                        <div class="am-progress">
                            <div class="am-progress-bar" style="width: ${property.value}%">${property.label}:${property.value}%</div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p>${property.label}:${property.value}</p>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <div id="do-not-say-2" class="am-panel-collapse am-collapse">
            <div class="am-panel-bd">
                <table class="am-table">
                    <thead>
                    <tr>
                        <th>定位</th>
                        <th>投入</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${productStudyList}" var="productStudy">
                        <tr>
                            <td><input type="hidden" name="productStudyId" value="${productStudy.id}"/>${productStudy.value}</td>
                            <td>
                                <select id="productStudyInstruction_fee_${productStudy.id}" name="productStudyFee"
                                        data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                    <option value="-1">不需要</option>
                                    <c:forEach items="${fn:split(productStudy.fees, ',')}" var="fee">
                                        <option value="${fee}">${fee}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
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
                <c:choose>
                    <c:when test="${property.display == 'PERCENT'}">
                        <div class="am-progress">
                            <div class="am-progress-bar" style="width: ${property.value}%">${property.label}:${property.value}%</div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p>${property.label}:${property.value}</p>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <div id="do-not-say-3" class="am-panel-collapse am-collapse">
            <div class="am-panel-bd">
                <table class="am-table">
                    <thead>
                    <tr>
                        <th>投入</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${operationChoiceList}" var="operationChoice">
                        <tr>
                            <td>
                                <input type="hidden" name="operationChoiceId" value="${operationChoice.id}"/>
                                <select id="operationInstruction_fee_${operationChoice.id}" name="operationChoiceFee" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                    <option value="-1">不需要</option>
                                    <c:forEach items="${fn:split(operationChoice.fees, ',')}" var="fee">
                                        <option value="${fee}">${fee}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="am-panel am-panel-default">
        <div class="am-panel-hd">
            <h4 class="am-panel-title" data-am-collapse="{parent: '#accordion', target: '#do-not-say-4'}">
                市场营销
            </h4>
        </div>
        <div class="am-panel-bd"  data-am-collapse="{parent: '#accordion', target: '#do-not-say-4'}">
            <c:forEach items="${deptPropertyMap['MARKET']}" var="property">
                <c:choose>
                    <c:when test="${property.display == 'PERCENT'}">
                        <div class="am-progress">
                            <div class="am-progress-bar" style="width: ${property.value}%">${property.label}:${property.value}%</div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p>${property.label}:${property.value}</p>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <div id="do-not-say-4" class="am-panel-collapse am-collapse am-in">
            <div class="am-panel-bd">
                <table class="am-table">
                    <thead>
                    <tr>
                        <th>方式</th>
                        <th>成本</th>
                        <th>投入</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${marketActivityChoiceList}" var="marketActivityChoice">
                        <tr>
                            <td><input type="hidden" name="marketActivityChoiceId" value="${marketActivityChoice.id}"/>${marketActivityChoice.name}</td>
                            <td>${marketActivityChoice.value}</td>
                            <td>
                                <select id="marketInstruction_fee_${marketActivityChoice.id}" name="marketActivityChoiceFee" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                    <option value="-1">不需要</option>
                                    <c:forEach items="${fn:split(marketActivityChoice. fees, ',')}" var="fee">
                                        <option value="${fee}">${fee}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div style="margin: 200px;">

    </div>
</div>
<script>

    $(function(){
        var companyId = ${company.id};

        $("#endCampaignDate").click(function(){
            $.post("<c:url value="/flow/companyNext.do"/>",
                    {
                        companyId: companyId
                    },
                    function (data) {
                        alert(data);
                       /* if(data=='success'){
                            alert("回合结束，等待其它企业完成操作");
                        }*/
                    }
            )});

        $("select[id^='officeInstruction_fee'],select[id^='hrInstruction_fee'],select[id^='marketInstruction_fee'],select[id^='productStudyInstruction_fee'],select[id^='operationInstruction_fee']")
                .change(function(){
                    var $fee = $(this);
                    var idArray = $fee.attr("id").split("_");
                    var value = $fee.val();
                    if(value!=-1) {
                        $.post("<c:url value="/work/makeInstruction"/>",
                                {
                                    companyId: companyId,
                                    choiceId: idArray[2],
                                    entity: idArray[0],
                                    value: value
                                });
                    } else {
                        $.post("<c:url value="/work/cancelInstruction"/>",
                                {
                                    companyId: companyId,
                                    choiceId: idArray[2],
                                    entity: idArray[0],
                                    value: value
                                });
                    }

                });
    })
</script>
</body>
</html>