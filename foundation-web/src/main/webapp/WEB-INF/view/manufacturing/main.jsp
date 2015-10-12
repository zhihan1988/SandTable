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
        .repertory ul li{
            list-style-type:none;
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
        <li><a id="panel-1" href="[data-tab-panel-1]">主页</a></li>
        <li class="am-active"><a id="panel-2" href="[data-tab-panel-2]">市场</a></li>
        <li><a id="panel-3" href="[data-tab-panel-3]">工厂</a></li>
        <li><a id="panel-4" href="[data-tab-panel-4]">财务</a></li>
    </ul>
    <h3 style="margin: 10px 0 0 20px">${campaign.name} -- ${campaign.formattedCampaignDate}</h3>
    <div class="am-tabs-bd">
        <div data-tab-panel-1 class="am-tab-panel am-active"></div>
        <div data-tab-panel-2 class="am-tab-panel">
            <div class="am-panel-bd operation">
                <h3>投放广告</h3>
                <table class="am-table">
                    <thead>
                    <tr>
                        <th>产品</th>
                        <th>市场</th>
                        <th>投入</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${marketFeeResource.currentIndustryResourceChoiceSet}" var="choice">
                        <tr>
                            <td>${choice.type}</td>
                            <td>${choice.value}</td>
                            <td>
                                <select id="instruction_${choice.id}" name="market" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                    <option value="${choice.id}#-1">未选择</option>
                                    <c:forEach items="${fn:split(marketFeeResource.valueSet, ',')}" var="fee">
                                        <option value="${choice.id}#${fee}">${fee}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="am-panel-bd operation">
                <h3>公司投放列表</h3>
                <table class="am-table">
                    <thead>
                    <tr>
                        <th>排名</th>
                        <th>公司</th>
                        <th>投入</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${marketFeeInstructionList}" var="marketFeeInstruction" varStatus="status">
                        <tr>
                            <td>${status.index+1}</td>
                            <td>${marketFeeInstruction.company.name}</td>
                            <td>${marketFeeInstruction.value}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="am-panel-bd operation">
                <h3>市场订单</h3>
                <table class="am-table">
                    <thead>
                    <tr>
                        <th>订单</th>
                        <th>产品</th>
                        <th>金额/成本/利润</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${marketOrderResource.currentIndustryResourceChoiceSet}" var="choice">
                        <tr>
                            <td>${choice.name}</td>
                            <td>${choice.type}</td>
                            <td>${choice.value2}</td>
                            <td><input type="radio" name="orderChoice" value="${choice.id}#${choice.value}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div data-tab-panel-3 class="am-tab-panel">
            <div class="am-panel am-panel-default">
                <div class="am-panel-bd repertory">
                    <ul class="am-avg-sm-2">
                        <li>
                            <b style="margin-left: 30px;">原料</b>
                            <ul>
                                <li>R1 : 0</li>
                                <li>R2 : 0</li>
                                <li>R3 : 0</li>
                                <li>R4 : 0</li>
                            </ul>
                        </li>
                        <li style="border-right: 1px solid #DDDDDD">
                            <b style="margin-left: 30px;">产品</b>
                            <ul>
                                <li>P1 : 0</li>
                                <li>P2 : 0</li>
                                <li>P3 : 0</li>
                                <li>P4 : 0</li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="am-panel am-panel-default">
            <div class="am-panel-bd">
                <h3>工厂1</h3>
                <table class="am-table">
                    <thead>
                    <tr>
                        <th>生产线</th>
                        <th>类型</th>
                        <th>产品型号</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${produceLineList}" var="line">
                        <tr>
                            <td>${line.name}</td>
                            <td id="td_lineType_${line.id}">
                                <select id="lineType_${line.id}" name="lineType">
                                    <option value="-1">无</option>
                                    <option value="MANUAL">手工</option>
                                    <option value="HALF_AUTOMATIC">半自动</option>
                                    <option value="AUTOMATIC">自动</option>
                                    <option value="FLEXBILITY">柔性</option>
                                </select>
                            </td>
                            <td id="td_produceType_${line.id}">
                                <select id="produceType_${line.id}" name="produceType">
                                    <option value="-1">无</option>
                                    <option value="P1">P1</option>
                                    <option value="P1">P2</option>
                                    <option value="P3">P3</option>
                                    <option value="P4">P4</option>
                                </select>
                            </td>
                            <td id="operation_${line.id}">
                                <c:choose>
                                    <c:when test="${line.status == 'NOT_OWNED'}">
                                        <button id="build_${line.id}" type="button" class="am-btn am-btn-secondary">建造</button>
                                    </c:when>
                                    <c:when test="${line.status == 'BUILDING'}">
                                        建造中
                                    </c:when>
                                    <c:when test="${line.status == 'FREE'}">
                                        <button id="produce_${line.id}" type="button" class="am-btn am-btn-secondary">生产</button>
                                        <button id="rebuild_${line.id}" type="button" class="am-btn am-btn-secondary">改造</button>
                                    </c:when>
                                    <c:when test="${line.status == 'PRODUCING'}">
                                        生产中
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            </div>
        </div>
        <div data-tab-panel-4 class="am-tab-panel operation">
            <div class="am-panel am-panel-default" id="account">
                <div class="am-panel-bd">
                    <ul>
                        <li>高利贷</li>
                        <li>短期贷款</li>
                        <li>长期贷款</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
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
        var companyTermId = '${companyTerm.id}';
        var roundType = '${roundType}';


        $("button[id^='build_']").click(function(){
            var $build = $(this);
            var partId = $build.attr("id").split("_")[1];
            var lineType = $("#lineType_" + partId).val();
            var produceType = $("#produceType_" + partId).val();
            if(lineType == -1) {
                alert("请选择生产线类型");
            } else if(produceType == -1) {
                alert("请选择生产的产品类型");
            } else {

                //开始建造
                $.getJSON("<c:url value="/manufacturing/buildProduceLine.do"/>",
                        {
                            companyTermId: companyTermId,
                            partId: partId,
                            lineType: lineType,
                            produceType: produceType
                        },
                        function(data){
                            //建造结果
                            var installCycle = data.installCycle;
                            if(installCycle == 0) {
                                //建造完成
                               var $operationDiv = $('#operation_' + partId);
                                var $button = $('<button type="button">生产</button>').attr('id','produce_'+partId).addClass('am-btn am-btn-secondary');
                                $operationDiv.html($button);
                                $("#lineType_" + partId).attr("disabled",true);
                                $("#produceType_" + partId).attr("disabled",true);
                            } else {
                                $('#operation_' + partId).html("建造中");
                            }
                        });
            }
        });

        $("td[id^='operation_']").delegate("button[id^='produce_']","click",function(){
            var $produce = $(this);
            var produceLineId = $produce.attr("id").split("_")[1];

            //开始建造
            $.getJSON("<c:url value="/manufacturing/buildProduceLine.do"/>",
                    {
                        companyTermId: companyTermId,
                        produceLineId: produceLineId
                    },
                    function(data){
                        //建造结果
                        var installCycle = data.installCycle;
                        if(installCycle == 0) {
                            //建造完成
                            var $operationDiv = $('#operation_' + partId);
                            var $button = $('<button type="button">生产</button>').attr('id','produce_'+partId).addClass('am-btn am-btn-secondary');
                            $operationDiv.html($button);
                            $("#lineType_" + partId).attr("disabled",true);
                            $("#produceType_" + partId).attr("disabled",true);
                        } else {
                            $('#operation_' + partId).html("生产中");
                        }
                    });
        });


        $("#endCampaignDate").click(function () {
            if(confirm("是否结束当前回合的操作？")) {
                var $endCampaignDate = $(this);
                $.post("<c:url value="/flow/companyNext.do"/>",
                        {
                            campaignId: campaignId,
                            companyId: companyId,
                            campaignDate: campaignDate,
                            roundType: roundType
                        },
                        function (data) {
                            $endCampaignDate.hide();
                        }
                );
            }
        });

        setInterval(isNext, 5000);
        function isNext() {
            $.post("<c:url value="/flow/isCampaignNext"/>",
                    {
                        campaignId:campaignId,
                        campaignDate: campaignDate,
                        roundType: roundType
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