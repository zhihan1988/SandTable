<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html class="no-js">
<head>
    <style type="text/css">
        .introduce {
            color: #999999;
            font-size:9px;
            margin-bottom: 10px;
        }
        .introduce p {
            margin: 0;
            padding: 0;
        }
    </style>
</head>
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
            <div class="introduce">
                <p>
                    选用较好的办公室有利于招聘人才，但这个因素是有限的。
                </p>
            </div>
            <ul class="am-avg-sm-2 am-avg-md-3 am-avg-lg-4 am-thumbnails">
                <c:if test="${preOfficeInstruction.id != null}">
                    <li style="border: 1px solid #DDD;padding: 5px;">
                        <div>
                            <ul class="am-avg-sm-2 am-avg-md-3 am-avg-lg-4 am-thumbnails">
                                <li><img style="margin: 0" class="am-thumbnail" src="http://s.amazeui.org/media/i/demos/bing-${status.index+1}.jpg" /></li>
                                <li>
                                    <p style="margin: 0">${preOfficeInstruction.companyChoice.name}</p>
                                </li>
                            </ul>
                        </div>
                        <div>
                            <p style="margin: 0">价格:${preOfficeInstruction.value}</p>
                            <p style="margin: 0">简介：${preOfficeInstruction.companyChoice.description}<p>
                        </div>
                    </li>
                </c:if>
            </ul>
        </div>
        <div id="do-not-say-0" class="am-panel-collapse am-collapse">
            <div class="am-panel-bd">
                <table class="am-table">
                    <thead>
                    <tr>
                        <th>地点</th>
                        <th>描述</th>
                        <th>房租</th>
                        <th>选择</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${officeChoiceList}" var="officeChoice">
                        <tr>
                            <td>
                                ${officeChoice.name}
                            </td>
                            <td>
                                ${officeChoice.description}
                            </td>
                            <td>
                                ${officeChoice.fees}
                            </td>
                            <td>
                                <input type="radio" name="officeInstructionFee" value="${officeChoice.id}_${officeChoice.value}"
                                    <c:if test="${preOfficeInstruction.companyChoice.name == officeChoice.name}">checked="checked"</c:if>>
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
            <div class="introduce">
                <p>人才包括岗位和级别，获取较高级别的某岗位的人才，有助于提升相应职能的能力。</p>
                <p>每季度出现的各个人才。</p>
                <p>各家公司会去竞争所需类别的较高级别的人才，影响因素包括薪酬、运气和办公室条件。</p>
            </div>
            <ul class="am-avg-sm-2 am-avg-md-3 am-avg-lg-4 am-thumbnails">
                <c:forEach items="${hrInstructionList}" var="hrInstruction" varStatus="status">
                    <li style="border: 1px solid #DDD;padding: 5px;">
                        <div>
                            <ul class="am-avg-sm-2 am-avg-md-3 am-avg-lg-4 am-thumbnails">
                                <li>
                                    <img style="width: 80px;margin: 0;" src="<c:url value="/img/${hrInstruction.companyChoice.img}"/>"/>
                                </li>
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
                            <a id="humanInstruction_${hrInstruction.id}" href="#">解雇</a>
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
            <div class="introduce">
                <p>较好的产品有助于提升市场和运营效果。</p>
                <p>较高的定位客单价较高，但如果多个公司采用相同的定位，那么会对客单价产生较多影响。</p>
                <p>较好较多的产品设计技术人才有助于提高产品研发能力。</p>
            </div>
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
                                        <option value="${productStudy.id}_${productStudy.value}" <c:if test="${preProductStudyInstruction.companyChoice.name==productStudy.name}">selected="selected"</c:if>>
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
            <div class="introduce">
                <p>通过开展市场活动可以获得新用户，各类市场活动的获取成本不同，但如果有较多的公司采用相同的市场活动方式，那么获取成本会提高</p>
                <p>较好较多的市场人才有助于提高市场能力。</p>
            </div>
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
            <div class="introduce">
                <p>较好的运营水平有助于提高满意度，较高的满意度意味着较高的用户留存率。</p>
                <p>较好的运营人才和较多的运营投入有助于提升运营能力。</p>
                <p>本期总用户数和客单价决定了本期的收入。</p>
            </div>
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

    $(function () {
        var campaignId = '${campaign.id}';
        var campaignDate = '${campaign.currentCampaignDate}';
        var companyId = '${company.id}';

        //办公室初始值
        var isOfficeCheck = $('input:radio[name="officeInstructionFee"]').is(":checked");
        if (!isOfficeCheck) {
            $("input:radio[name=officeInstructionFee]:eq(0)").attr("checked", 'checked');
        }
        var $choice = $("input:radio[name=officeInstructionFee]:checked");
        var array = $choice.val().split("_");
        var choiceId = array[0];
        var value = array[1];
        makeUniqueInstruction(companyId, choiceId, value);

        $("select[id^='instruction']")
                .change(function () {
                    var $choice = $(this);
                    var array = $choice.val().split("_");
                    var choiceId = array[0];
                    var value = array[1];
                    if (value != -1) {
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
        //产品定位
        $("select[id='productStudy']")
                .change(function () {
                    var $choice = $(this);
                    var array = $choice.val().split("_");
                    var choiceId = array[0];
                    var value = array[1];
                    if (value != -1) {
                        makeUniqueInstruction(companyId, choiceId, value);
                    }
                });
        //办公室
        $("input:radio[name='officeInstructionFee']").change(function () {
            var $choice = $(this);
            var array = $choice.val().split("_");
            var choiceId = array[0];
            var value = array[1];
            if (value != -1) {
                makeUniqueInstruction(companyId, choiceId, value);
            }
        });

        $("a[id^='humanInstruction_']").click(function() {
            if(confirm("解雇员工将扣除两个月薪水作为补贴，是否继续？")) {
                var $humanInstruction = $(this);
                var instructionId = $humanInstruction.attr("id").split("_")[1];
                $.post("<c:url value="/work/fire"/>",
                        {
                            instructionId: instructionId
                        },function(data) {
                            $humanInstruction.after("<span>已解雇</span>");
                            $humanInstruction.remove();
                        });
                }
        })


        function makeUniqueInstruction(companyId,choiceId,value) {
            $.post("<c:url value="/work/makeUniqueInstruction"/>",
                    {
                        companyId: companyId,
                        choiceId: choiceId,
                        value: value
                    });
        }

        $("#endCampaignDate").click(function () {
            if(confirm("是否结束当前回合的操作？")) {
                var $endCampaignDate = $(this);
                $.post("<c:url value="/flow/companyNext.do"/>",
                        {
                            campaignId: campaignId,
                            companyId: companyId
                        },
                        function (data) {
                            setInterval(isNext, 5000);
                            $endCampaignDate.addClass("am-disabled");
                            $endCampaignDate.attr("disabled", "disabled");
                            $endCampaignDate.text("回合结束，等待其他公司完成操作");
                            $endCampaignDate.after("<img width='38px' src='<c:url value="/img/loading.gif"/>'/>");
                        }
                );
            }
        });

        function isNext() {
            $.post("<c:url value="/flow/isNext"/>",
                    {
                        campaignId:campaignId,
                        campaignDate: campaignDate
                    },
                    function (data) {
                        if (data == 0) {
                            location.reload();
                        } else {
                            $("#endCampaignDate").text(data + '家公司没有完成操作');
                        }
                    });

        }
    });
</script>
</body>
</html>