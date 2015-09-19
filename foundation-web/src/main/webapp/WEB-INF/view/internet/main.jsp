<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html class="no-js">
<head></head>
<body>

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
            <ul class="am-avg-sm-2 am-avg-md-3 am-avg-lg-4 am-thumbnails">
                <c:forEach items="${officeInstructionList}" var="instruction">
                <li>
                    <div>
                        <div><img class="am-thumbnail" src="http://s.amazeui.org/media/i/demos/bing-1.jpg" /></div>
                        <div>
                            <p>价格:${instruction.value}</p>
                            <p>简介：${instruction.companyChoice.description}<p>
                        </div>
                    </div>
                </li>
                </c:forEach>
            </ul>
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
                                ${officeChoice.description}
                            </td>
                            <td>
                                <select id="instruction_${officeChoice.id}" name="officeInstructionFee" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                    <option value="${officeChoice.id}_-1">不需要</option>
                                    <c:forEach items="${fn:split(officeChoice.fees, ',')}" var="fee">
                                        <option value="${officeChoice.id}_${fee}">${fee}</option>
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
            <ul class="am-avg-sm-2 am-avg-md-3 am-avg-lg-4 am-thumbnails">
                <c:forEach items="${hrInstructionList}" var="hrInstruction" varStatus="status">
                    <li style="border: 1px solid #DDD;padding: 5px;">
                        <div>
                            <ul class="am-avg-sm-2 am-avg-md-3 am-avg-lg-4 am-thumbnails">
                                <li><img style="margin: 0" class="am-thumbnail" src="http://s.amazeui.org/media/i/demos/bing-${status.index+1}.jpg" /></li>
                                <li>
                                    <p style="margin: 0">${hrInstruction.companyChoice.name}</p>
                                    <p style="margin: 0">
                                        <c:choose>
                                            <c:when test="${hrInstruction.companyChoice.type == 'PRODUCT'}">
                                                产品研发
                                            </c:when>
                                            <c:when test="${hrInstruction.companyChoice.type == 'MARKET'}">
                                                市场
                                            </c:when>
                                            <c:when test="${hrInstruction.companyChoice.type == 'OPERATION'}">
                                                运营
                                            </c:when>
                                        </c:choose>
                                    </p>
                                </li>

                            </ul>
                           </div>
                        <div>
                            <p style="margin: 0">能力：${hrInstruction.companyChoice.value}</p>
                            <p style="margin: 0">薪资：${hrInstruction.value}</p>
                        </div>
                    </li>
                </c:forEach>
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
                                    <c:choose>
                                        <c:when test="${human.type == 'PRODUCT'}">
                                            (产品研发部)
                                        </c:when>
                                        <c:when test="${human.type == 'MARKET'}">
                                            (市场部)
                                        </c:when>
                                        <c:when test="${human.type == 'OPERATION'}">
                                            (运营部)
                                        </c:when>
                                    </c:choose>
                            </td>
                            <td>${human.value}</td>
                            <td>
                                <select id="instruction_${human.id}" name="humanInstructionFee" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                    <option value="${human.id}_-1">不需要</option>
                                    <c:forEach items="${fn:split(human.fees, ',')}" var="fee">
                                        <option value="${human.id}_${fee}">${fee}</option>
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
                    <c:when test="${property.display == 'TEXT'}">
                        <p>${property.label}:${property.value}</p>
                    </c:when>
                    <c:otherwise>
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
                    </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <select id="productStudy" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                    <c:forEach items="${productStudyList}" var="productStudy">
                                        <option value="${productStudy.id}_${productStudy.value}" <c:if test="${preProductStudyInstruction.value==productStudy.value}">selected="selected"</c:if>>
                                            ${productStudy.name}
                                        </option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="am-panel-bd">
                <table class="am-table">
                    <thead>
                    <tr>
                        <th>投入</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${productStudyFeeList}" var="productStudyFee">
                        <tr>
                            <td>
                                <input type="hidden" name="productStudyFeeId" value="${productStudyFee.id}"/>
                                <select id="instruction_${productStudyFee.id}" name="productStudyFee"
                                        data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                    <option value="${productStudyFee.id}_-1">不需要</option>
                                    <c:forEach items="${fn:split(productStudyFee.fees, ',')}" var="fee">
                                        <option value="${productStudyFee.id}_${fee}">${fee}</option>
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
                    <c:when test="${property.display == 'TEXT'}">
                        <p>${property.label}:${property.value}</p>
                    </c:when>
                    <c:otherwise>
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
                                <select id="instruction_${marketActivityChoice.id}" name="marketActivityChoiceFee" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                    <option value="${marketActivityChoice.id}_-1">不需要</option>
                                    <c:forEach items="${fn:split(marketActivityChoice.fees, ',')}" var="fee">
                                        <option value="${marketActivityChoice.id}_${fee}">${fee}</option>
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
                    <c:when test="${property.display == 'TEXT'}">
                        <p>${property.label}:${property.value}</p>
                    </c:when>
                    <c:otherwise>
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
                                <select id="instruction_${operationChoice.id}" name="operationChoiceFee" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                    <option value="${operationChoice.id}_-1">不需要</option>
                                    <c:forEach items="${fn:split(operationChoice.fees, ',')}" var="fee">
                                        <option value="${operationChoice.id}_${fee}">${fee}</option>
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
</div>
<div class="am-panel am-panel-default am-scrollable-horizontal">

    <table class="am-table am-table-bordered am-table-compact am-text-nowrap">
        <c:forEach items="${globalReport}" var="g">
            <tr>
                <td>${g.key}:${g.value}</td>
            </tr>
        </c:forEach>
    </table>
    <table class="am-table am-table-bordered am-table-compact am-text-nowrap" style="margin-top: 20px;">
        <c:forEach items="${propertyReport}" var="r" begin="0" end="0">
            <tr>
                <td></td>
                <c:forEach items="${r.value}" var="property">
                    <td>${property.key}</td>
                </c:forEach>
            </tr>
        </c:forEach>
        <c:forEach items="${propertyReport}" var="r">
            <tr>
                <td>${r.key}</td>
                <c:forEach items="${r.value}" var="property">
                    <td>${property.value}</td>
                </c:forEach>
            </tr>
        </c:forEach>
    </table>
    <table class="am-table am-table-bordered am-table-compact am-text-nowrap" style="margin-top: 20px;">
        <c:forEach items="${accountReport}" var="acountMap" begin="0" end="0">
            <tr>
                <td></td>
                <c:forEach items="${acountMap.value}" var="account">
                    <td>${account.key}</td>
                </c:forEach>
            </tr>
        </c:forEach>
        <c:forEach items="${accountReport}" var="acountMap">
            <tr>
                <td>${acountMap.key}</td>
                <c:forEach items="${acountMap.value}" var="account">
                    <td>${account.value}</td>
                </c:forEach>
            </tr>
        </c:forEach>
    </table>
</div>
<script>

    $(function(){
        var campaignId = ${company.campaign.id}
        var companyId = ${company.id};

        $("#endCampaignDate").click(function(){
            $.post("<c:url value="/flow/companyNext.do"/>",
                    {
                        campaignId: campaignId,
                        companyId: companyId
                    },
                    function (data) {
                         if(data=='false'){
                            alert("回合结束，等待其它企业完成操作");
                         } else {
                             alert("公司回合已全部结束，刷新进入下一回合");
                         }
                    }
            );});

        $("select[id^='instruction']")
                .change(function(){
                    var $choice = $(this);
                    var array = $choice.val().split("_");
                    var choiceId = array[0];
                    var value = array[1];
                    if (value!=-1) {
                        $.post("<c:url value="/work/makeInstruction"/>",
                                {
                                    companyId: companyId,
                                    choiceId: choiceId,
                                    value: value
                                });
                    } else {
                        $.post("<c:url value="/work/cancelInstruction"/>",
                                {
                                    companyId: companyId,
                                    choiceId: choiceId
                                });
                    }

                });
        $("select[id='productStudy']")
                .change(function(){
                    var $choice = $(this);
                    var array = $choice.val().split("_");
                    var choiceId = array[0];
                    var value = array[1];
                    if (value!=-1) {
                        $.post("<c:url value="/work/makeUniqueInstruction"/>",
                                {
                                    companyId: companyId,
                                    choiceId: choiceId,
                                    value: value
                                });
                    }
                });
    })
</script>
</body>
</html>