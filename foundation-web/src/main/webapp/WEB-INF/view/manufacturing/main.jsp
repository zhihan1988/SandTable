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
                        <th>生产线类型</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${produceLineResource.currentIndustryResourceChoiceSet}" var="choice" begin="0" end="3">
                        <tr>
                            <td>${choice.name}</td>
                            <td>
                                <select id="instruction_${choice.id}" name="produceLine">
                                    <option value="-1">无</option>
                                    <option value="1">手工</option>
                                    <option value="2">半自动</option>
                                    <option value="3">自动</option>
                                    <option value="4">柔性</option>
                                </select>
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
                                    <select>
                                        <option>手工</option>
                                        <option>半自动</option>
                                        <option>自动</option>
                                        <option>柔性</option>
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
                                    <select>
                                        <option>手工</option>
                                        <option>半自动</option>
                                        <option>自动</option>
                                        <option>柔性</option>
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

        //产品定位初始值
        var productStudy = $("select[id='productStudy']").val();
        if(productStudy){
            var productStudyArray = productStudy.split("_");
            makeUniqueInstruction(companyTermId, productStudyArray[0], productStudyArray[1]);
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
                                    companyTermId: companyTermId,
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
                        makeUniqueInstruction(companyTermId, choiceId, value);
                    }
                });

        function makeUniqueInstruction(companyTermId,choiceId,value) {
            $.post("<c:url value="/work/makeUniqueInstruction"/>",
                    {
                        companyTermId: companyTermId,
                        choiceId: choiceId,
                        value: value
                    });
        }

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
//                            $endCampaignDate.addClass("am-disabled");
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