<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
            font-size: 9px;
            margin-bottom: 10px;
        }

        .introduce p {
            margin: 0;
            padding: 0;
        }

        .main-panel .am-panel-group .am-panel {
            margin-bottom: 20px;
        }

        .main-panel .am-panel-group .am-panel .am-panel-hd {
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
            color: #0e90d2;
        }
    </style>
    <script src="/js/test/model.js"></script>
    <script src="/js/test/doT.js"></script>
    <script src="/js/test/main.js"></script>
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
        <%--<c:if test="${campaign.currentCampaignDate > 1}">--%>
        <li class=""><a id="panel-product" href="[data-tab-panel-2]">产品</a></li>
        <li class=""><a id="panel-market" href="[data-tab-panel-3]">市场</a></li>
        <li class=""><a id="panel-operation" href="[data-tab-panel-4]">运营</a></li>
        <%--</c:if>--%>
    </ul>
    <h3 style="margin: 10px 0 0 20px">${campaign.name} -- ${campaign.formattedCampaignDate}</h3>

    <div class="am-tabs-bd">
        <div data-tab-panel-0 class="am-tab-panel am-active">
            <div class="am-panel-bd">
                <b>财务报告</b>
                <table class="am-table am-table-bordered am-table-compact am-text-nowrap" id="cash">
                </table>
                <div id="enter-hr" class="am-panel am-panel-default">
                    <div class="am-panel">

                        <div id="enter-human" class="door">
                            <b>招聘</b>

                            <p>在职人数：<p id="human"></p></p>
                            <span class="door-enter">进入<i class="am-icon-chevron-right"></i></span>
                        </div>
                        <hr data-am-widget="divider" style="" class="am-divider am-divider-dashed"/>
                        <div id="enter-product" class="door">
                            <b>产品</b>

                            <p id="product"></p>
                            <span class="door-enter">进入<i class="am-icon-chevron-right"></i></span>
                        </div>

                        <hr data-am-widget="divider" style="" class="am-divider am-divider-dashed"/>
                        <div id="enter-market" class="door">
                            <b>市场</b>

                            <p id="market"></p>
                            <span class="door-enter">进入<i class="am-icon-chevron-right"></i></span>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>


<script>

    var classNameList = ["CompanyPart"];//页面传递过来的对象，作为init初始化的参数
    //测试数据
    var testData = {
        "CompanyPart": [{
            "id": "id:1",
            "serial": "1",
            "dept": "1",
            "campaign": "1",
            "company": "1",
            "name": "product",
            "status": "1"
        }, {
            "id": "id:2",
            "serial": "1",
            "dept": "1",
            "campaign": "1",
            "company": "1",
            "name": "market",
            "status": "1"
        }, {
            "id": "id:3",
            "serial": "1",
            "dept": "1",
            "campaign": "1",
            "company": "1",
            "name": "human",
            "status": "1"
        }, {
            "id": "id:4",
            "serial": "1",
            "dept": "1",
            "campaign": "1",
            "company": "1",
            "name": "human",
            "status": "1"
        }]
    };

    var productTemplate = '{{ for (var i=0; i<it.length;i++){ }}<p>{{=it[i].name}}:{{=it[i].serial}}</p>{{}}}'
    var marketTemplate = '{{for (var i=0; i<it.length;i++){}}<p>{{=it[i].name}}:{{=it[i].serial}}</p>{{}}}'
    var humanTemplate = '<p>{{=it.length}}</p>'
    var totalCashTemplate = '<tbody><tr> <td>公司现金</td> </tr> <tr> <td>{{=it.value}}</td> </tr> </tbody>'

    var logicModel = {
        totalCash: "}",//template
        totalDebt: "",
        debtList: "",
        humanList: "",
        market: "",
        product: ""
    };

    initModel(classNameList, testData);
    logicModel.totalCash = generateHtml(totalCashTemplate, {"value":"10000"});
    logicModel.product = generateHtml(productTemplate,getCurrentObject("product"));
    logicModel.market = generateHtml(marketTemplate,getCurrentObject("market"));
    logicModel.humanList = generateHtml(humanTemplate,getCurrentObject("human"));

    function getCurrentObject(name) {
        var list = new Array();
        for (var i = 0; i < modelMap["CompanyPart"].length; i++) {
            if (modelMap["CompanyPart"][i].name == name) {
                list.push(modelMap["CompanyPart"][i]);
            }
        }
        return list;
    }

    $("#human").html(logicModel.humanList);
    $("#product").html(logicModel.product);
    $("#market").html(logicModel.market);
    $("#cash").html(logicModel.totalCash);


</script>
</body>
</html>