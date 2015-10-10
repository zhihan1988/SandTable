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
        <li class="am-active"><a id="panel-0" href="[data-tab-panel-0]">工厂</a></li>
    </ul>
    <h3 style="margin: 10px 0 0 20px">${campaign.name} -- ${campaign.formattedCampaignDate}</h3>
    <div class="am-tabs-bd">
        <div data-tab-panel-0 class="am-tab-panel am-active">
            <div class="am-panel am-panel-default">
            <div class="am-panel-bd operation">
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
                    <c:forEach items="${companyPartList}" var="line">
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
                                    <c:when test="${line.status == 'FREE'}">
                                        <button id="produce_${line.id}" type="button" class="am-btn am-btn-secondary">生产</button>
                                    </c:when>
                                    <c:when test="${line.status == 'USING'}">
                                        <button id="rebuild_${line.id}" type="button" class="am-btn am-btn-secondary">改造</button>
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
                <div class="am-panel-bd operation">
                    <h3>工厂2</h3>
                    <table class="am-table">
                        <thead>
                        <tr>
                            <th>生产线</th>
                            <th>生产线类型</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${produceLineResource.currentIndustryResourceChoiceSet}" var="choice" begin="4" end="6">
                            <tr>
                                <td>${choice.name}</td>
                                <td>
                                    <select id="instruction_${choice.id}" name="produceLine">
                                        <option value="-1">无</option>
                                        <option value="${choice.id}_MANUAL">手工</option>
                                        <option value="${choice.id}_HALF_AUTOMATIC">半自动</option>
                                        <option value="${choice.id}_AUTOMATIC">自动</option>
                                        <option value="${choice.id}_FLEXBILITY">柔性</option>
                                    </select>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="am-panel-bd operation">
                    <h3>工厂3</h3>
                    <table class="am-table">
                        <thead>
                        <tr>
                            <th>生产线</th>
                            <th>生产线类型</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${produceLineResource.currentIndustryResourceChoiceSet}" var="choice" begin="7" end="7">
                            <tr>
                                <td>${choice.name}</td>
                                <td>
                                    <select id="instruction_${choice.id}" name="produceLine">
                                        <option value="-1">无</option>
                                        <option value="${choice.id}_MANUAL">手工</option>
                                        <option value="${choice.id}_HALF_AUTOMATIC">半自动</option>
                                        <option value="${choice.id}_AUTOMATIC">自动</option>
                                        <option value="${choice.id}_FLEXBILITY">柔性</option>
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
        })

        $("button[id^='produce_']").click(function(){
            alert(123);
        })

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