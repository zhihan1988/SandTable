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

<div class="am-panel am-panel-default">
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
</div>
<script>

    $(function () {

        var campaignId = '${campaign.id}';
        var campaignDate = ${campaign.currentCampaignDate};
        var companyId = '${company.id}';
        var companyTermId = '${companyTerm.id}';
        var roundType = '${roundType}';

        $("select[id^='instruction']")
                .change(function () {
                    var $choice = $(this);
                    var array = $choice.val().split("#");
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

        $("#endCampaignDate").click(function () {
            if(confirm("是否确认市场投入？")) {
                var $endCampaignDate = $(this);
                $.post("<c:url value="/flow/companyNext"/>",
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
                     campaignId: campaignId,
                     companyId: companyId,
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