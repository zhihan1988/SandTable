<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<!doctype html>
<html class="no-js">
<head>
    <style type="text/css">
        .operation {
            padding-bottom: 100px;
        }
        .introduce {
            color: #999999;
            font-size:9px;
            margin-bottom: 10px;
        }
        .introduce p {
            margin: 0;
            padding: 0;
        }
        .main-panel .am-panel-group .am-panel{
            margin-bottom: 20px;
        }
        .main-panel .am-panel-group .am-panel .am-panel-hd{
            background: #EEE;
        }
        .door {
            position: relative;
            padding-left: 10px;
        }
        .door-enter {
            position: absolute;
            top: 0px;
            right: 10px;
            color:#0e90d2;
        }
    </style>
</head>
<body>
<header data-am-widget="header" class="am-header am-header-default">
    <div class="am-header-left am-header-nav">
        <a href="<c:url value="/campaign/${campaign.id}"/>" class="">
            行业
        </a>
        <a href="#" class="">
            <%--<i class="am-header-icon am-icon-phone"></i>--%>
        </a>
    </div>

    <div class="am-header-title">
        <i class="am-icon-circle-o-notch am-icon-spin" style="font-size: 20px;"></i>
        <span>进行中：</span><span id="unFinishedNum">${companyNum}</span>/${companyNum}
    </div>

    <div class="am-header-right am-header-nav">
        <%--<i class="am-icon-stop"></i>--%>
        <a href="#left-link" id="endCampaignDate" class="">
            结束回合
        </a>
    </div>
</header>
<div data-am-widget="tabs" class="am-tabs am-tabs-d2" style="margin: 0">
    <ul class="am-tabs-nav am-cf">
        <li class="am-active"><a id="panel-0" href="[data-tab-panel-0]">主页</a></li>
        <li class=""><a id="panel-hr" href="[data-tab-panel-1]">招聘</a></li>
        <c:if test="${campaign.currentCampaignDate != '010101'}">
            <li class=""><a id="panel-product" href="[data-tab-panel-2]">产品</a></li>
        </c:if>
        <c:if test="${campaign.currentCampaignDate != '010101' && campaign.currentCampaignDate != '010204'}">
        <li class=""><a id="panel-market" href="[data-tab-panel-3]">市场</a></li>
        <li class=""><a id="panel-operation" href="[data-tab-panel-4]">运营</a></li>
        </c:if>
    </ul>
    <h3 style="margin: 10px 0 0 20px">${campaign.name} -- ${campaign.formatCampaignDate}</h3>
    <div class="am-tabs-bd">
        <div data-tab-panel-0 class="am-tab-panel am-active">
            <div class="am-panel-bd">
                <b>财务报告</b>
                <table class="am-table am-table-bordered am-table-compact am-text-nowrap">
                    <tbody>
                    <tr>
                        <td>公司现金</td>
                        <td>上期收入</td>
                        <td>上期支出</td>
                    </tr>
                    <tr>
                        <td><f:formatNumber value="${companyCash}" pattern="#,#00.#"/></td>
                        <td><f:formatNumber value="${campaignDateInCash}" pattern="#,#00.#"/></td>
                        <td><f:formatNumber value="${campaignDateOutCash}" pattern="#,#00.#"/></td>
                    </tr>
                    </tbody>
                </table>
                <div id="enter-hr" class="am-panel am-panel-default">
                    <div class="am-panel">

                    <div id="enter-human" class="door">
                        <b>招聘</b>
                        <p>在职人数：${fn:length(hrInstructionList)}</p>
                        <span class="door-enter">进入<i class="am-icon-chevron-right"></i></span>
                    </div>

                    <c:if test="${campaign.currentCampaignDate != '010101'}">
                    <hr data-am-widget="divider" style="" class="am-divider am-divider-dashed" />
                    <div id="enter-product" class="door">
                        <b>产品</b>
                        <c:forEach items="${deptPropertyMap['PRODUCT']}" var="property">
                            <c:choose>
                                <c:when test="${property.display != 'HIDDEN'}">
                                    <p>${property.label}:${property.value}</p>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <span class="door-enter">进入<i class="am-icon-chevron-right"></i></span>
                    </div>
                    </c:if>

                    <c:if test="${campaign.currentCampaignDate != '010101' && campaign.currentCampaignDate != '010204'}">
                    <hr data-am-widget="divider" style="" class="am-divider am-divider-dashed" />
                    <div id="enter-market" class="door">
                        <b>市场</b>
                        <c:forEach items="${deptPropertyMap['MARKET']}" var="property">
                            <c:choose>
                                <c:when test="${property.display != 'HIDDEN'}">
                                    <p>${property.label}:${property.value}</p>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <span class="door-enter">进入<i class="am-icon-chevron-right"></i></span>
                    </div>

                    <hr data-am-widget="divider" style="" class="am-divider am-divider-dashed" />
                    <div class="door" id="enter-operation">
                        <b>运营</b>
                        <c:forEach items="${deptPropertyMap['OPERATION']}" var="property">
                            <c:choose>
                                <c:when test="${property.display != 'HIDDEN'}">
                                    <p>${property.label}:${property.value}</p>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <span class="door-enter">进入<i class="am-icon-chevron-right"></i></span>
                    </div>
                    </c:if>
                    </div>
                </div>

                <br/>
                <b>市场活动竞争报告</b>
                <table class="am-table am-table-bordered am-table-compact am-text-nowrap">
                    <tbody>
                    <tr>
                        <td>活动</td>
                        <c:forEach items="${marketCompetitionReport}" var="g">
                            <td>${g.key}</td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td>人数</td>
                        <c:forEach items="${marketCompetitionReport}" var="g">
                            <td>${g.value}</td>
                        </c:forEach>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div data-tab-panel-1 class="am-tab-panel ">
            <div class="am-panel am-panel-default" id="human">
                <b>已招聘人员：</b>
                <ul class="am-avg-sm-2 am-avg-md-3 am-avg-lg-4 am-thumbnails" style="margin: 10px;">
                    <c:forEach items="${hrInstructionList}" var="hrInstruction" varStatus="status">
                        <li style="border: 1px solid #DDD;padding: 5px;">
                            <div>
                                <ul class="am-avg-sm-2 am-avg-md-3 am-avg-lg-4 am-thumbnails">
                                    <li>
                                        <img style="width: 80px;margin: 0;" src="<c:url value="/img/${hrInstruction.campaignTermChoice.img}"/>"/>
                                    </li>
                                    <li>
                                        <p style="margin: 0">${hrInstruction.campaignTermChoice.name}</p>
                                        <p style="margin: 0">
                                            <c:choose>
                                                <c:when test="${hrInstruction.campaignTermChoice.type == 'PRODUCT'}">
                                                    产品研发
                                                </c:when>
                                                <c:when test="${hrInstruction.campaignTermChoice.type == 'MARKET'}">
                                                    市场
                                                </c:when>
                                                <c:when test="${hrInstruction.campaignTermChoice.type == 'OPERATION'}">
                                                    运营
                                                </c:when>
                                            </c:choose>
                                        </p>
                                    </li>

                                </ul>
                            </div>
                            <div>
                                <p style="margin: 0">能力：${hrInstruction.campaignTermChoice.value}</p>
                                <p style="margin: 0">薪资：${hrInstruction.value}</p>
                                <a id="humanInstruction_${hrInstruction.id}" href="#">辞退</a>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
                <hr data-am-widget="divider" style="" class="am-divider am-divider-dashed" />
                <b>待招聘人员：</b>
                <ul class="am-avg-sm-2 am-avg-md-3 am-avg-lg-4 am-thumbnails operation" style="margin: 10px;">
                    <c:forEach items="${humanList}" var="human">
                        <li style="border: 1px solid #DDD;padding: 5px;">
                            <div>
                                <ul class="am-avg-sm-2 am-avg-md-3 am-avg-lg-4 am-thumbnails">
                                    <li>
                                        <img style="width: 80px;margin: 0;" src="<c:url value="/img/${human.img}"/>"/>
                                    </li>
                                    <li>
                                        <p style="margin: 0">${human.name}</p>
                                        <p style="margin: 0">
                                            <c:choose>
                                                <c:when test="${human.type == 'PRODUCT'}">
                                                    产品研发
                                                </c:when>
                                                <c:when test="${human.type == 'MARKET'}">
                                                    市场
                                                </c:when>
                                                <c:when test="${human.type == 'OPERATION'}">
                                                    运营
                                                </c:when>
                                            </c:choose>
                                        </p>
                                    </li>

                                </ul>
                            </div>
                            <div>
                                <p style="margin: 0">能力：${human.value}</p>
                                <p style="margin: 0">
                                    薪资：
                                    <select id="instruction_${human.id}" name="humanInstructionFee" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                        <option value="${human.id}_-1">不需要</option>
                                        <c:forEach items="${fn:split(human.fees, ',')}" var="fee">
                                            <option value="${human.id}_${fee}">${fee}</option>
                                        </c:forEach>
                                    </select>
                                </p>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
                <hr data-am-widget="divider" style="" class="am-divider am-divider-dashed" />
                <div class="introduce">
                    <p>人才包括岗位和级别，获取较高级别的某岗位的人才，有助于提升相应职能的能力。</p>
                    <p>每季度出现的各个人才。</p>
                    <p>各家公司会去竞争所需类别的较高级别的人才，影响因素包括薪酬、运气和办公室条件。</p>
                </div>
            </div>
        </div>
        <div data-tab-panel-2 class="am-tab-panel ">
            <div class="am-panel am-panel-default" id="product">
                <div class="am-panel-bd">
                    <c:forEach items="${deptPropertyMap['PRODUCT']}" var="property">
                        <c:choose>
                            <c:when test="${property.display == 'PERCENT'}">
                                ${property.label}:
                                <div class="am-progress">
                                    <div class="am-progress-bar" style="width: ${property.value}%">${property.value}%</div>
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
                <hr data-am-widget="divider" style="" class="am-divider am-divider-dashed" />
                <div class="am-panel-bd operation">
                    <table class="am-table">
                        <thead>
                        <tr>
                            <th>产品定位</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>
                                <select id="productStudy" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                    <c:forEach items="${productStudyList}" var="productStudy">
                                        <option value="${productStudy.id}_${productStudy.value}" <c:if test="${preProductStudyInstruction.campaignTermChoice.name==productStudy.name}">selected="selected"</c:if>>
                                                ${productStudy.name}
                                        </option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        </tbody>
                    </table>

                    <table class="am-table">
                        <thead>
                        <tr>
                            <th>研发投入</th>
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
                <hr data-am-widget="divider" style="" class="am-divider am-divider-dashed" />
                <div class="introduce">
                    <p>较好的产品有助于提升市场和运营效果。</p>
                    <p>较高的定位客单价较高，但如果多个公司采用相同的定位，那么会对客单价产生较多影响。</p>
                    <p>较好较多的产品设计技术人才有助于提高产品研发能力。</p>
                </div>
            </div>
        </div>
        <div data-tab-panel-3 class="am-tab-panel ">
            <div class="am-panel am-panel-default" id="market">
                <div class="am-panel-bd">
                    <c:forEach items="${deptPropertyMap['MARKET']}" var="property">
                        <c:choose>
                            <c:when test="${property.display == 'PERCENT'}">
                                ${property.label}:
                                <div class="am-progress">
                                    <div class="am-progress-bar" style="width: ${property.value}%">${property.value}%</div>
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
                <hr data-am-widget="divider" style="" class="am-divider am-divider-dashed" />
                <div class="am-panel-bd operation">
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
                <hr data-am-widget="divider" style="" class="am-divider am-divider-dashed" />
                <div class="introduce">
                    <p>通过开展市场活动可以获得新用户，各类市场活动的获取成本不同，但如果有较多的公司采用相同的市场活动方式，那么获取成本会提高</p>
                    <p>较好较多的市场人才有助于提高市场能力。</p>
                </div>
            </div>
        </div>
        <div data-tab-panel-4 class="am-tab-panel ">
            <div class="am-panel am-panel-default" id="operation">
                <div class="am-panel-bd">
                <c:forEach items="${deptPropertyMap['OPERATION']}" var="property">
                    <c:choose>
                        <c:when test="${property.display == 'PERCENT'}">
                            ${property.label}:
                            <div class="am-progress">
                                <div class="am-progress-bar" style="width: ${property.value}%">${property.value}%</div>
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
                <hr data-am-widget="divider" style="" class="am-divider am-divider-dashed" />
                <div class="am-panel-bd operation">
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
                <hr data-am-widget="divider" style="" class="am-divider am-divider-dashed" />
                <div class="introduce">
                    <p>较好的运营水平有助于提高满意度，较高的满意度意味着较高的用户留存率。</p>
                    <p>较好的运营人才和较多的运营投入有助于提升运营能力。</p>
                    <p>本期总用户数和客单价决定了本期的收入。</p>
                </div>

            </div>
        </div>
    </div>
</div>

<b>调试信息</b>
<div class="am-scrollable-horizontal">
    <table class="am-table am-table-bordered am-table-compact am-text-nowrap" style="margin-top: 20px;">
        <c:forEach items="${propertyReport}" var="r" begin="0" end="0">
            <tr>
                <td></td>
                <c:forEach items="${r.value}" var="property">
                    <td>${property.key}</td>
                </c:forEach>
            </tr>
        </c:forEach>
        <c:forEach items="${propertyReport}" var="r" begin="1">
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
        <c:forEach items="${accountReport}" var="acountMap" begin="1">
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
        $("div[id^='enter-']").click(function () {
            var id = $(this).attr("id");
            $("#panel-"+id.split("-")[1]).trigger("click");
        });


        var campaignId = '${campaign.id}';
        var campaignDate = '${campaign.currentCampaignDate}';
        var companyId = '${company.id}';

        //产品定位初始值
        var productStudy = $("select[id='productStudy']").val();
        if(productStudy){
            var productStudyArray = productStudy.split("_");
            makeUniqueInstruction(companyId, productStudyArray[0], productStudyArray[1]);
        }

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

        $("a[id^='humanInstruction_']").click(function() {
            if(confirm("辞退员工将扣除两个月薪水作为补贴，是否继续？")) {
                var $humanInstruction = $(this);
                var instructionId = $humanInstruction.attr("id").split("_")[1];
                $.post("<c:url value="/work/fire"/>",
                        {
                            instructionId: instructionId
                        },function(data) {
                            $humanInstruction.after("<span>已辞退</span>");
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
//                            $endCampaignDate.addClass("am-disabled");
                            $endCampaignDate.hide();
                        }
                );
            }
        });

        setInterval(isNext, 5000);
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
                            $("#unFinishedNum").text(data);
                        }
                    });

        }
    });
</script>
</body>
</html>