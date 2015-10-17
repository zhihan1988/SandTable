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
        .panel {
            display: none;
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
<div style="margin: 10px;">
    <div style="text-align:center">
        <h3>${campaign.name} -- ${campaign.formattedCampaignDate}</h3>
    </div>

    <div>
        <div id="panel-1" class="panel">
            <div class="am-panel am-panel-default">
                <div class="am-panel-bd">
                    <ming800:radioSet valueSet="key1:value1,key2:value2,key3:value3,key4:value4,key5:value5" name="test" onclick="test" checkedValue="value3"/>
                </div>
            </div>
        </div>
        <div id="panel-2" class="panel">
            <div class="am-panel am-panel-default">
                <div class="am-panel-bd">
                <h3>投放广告</h3>
                <table class="am-table">
                    <thead>
                    <tr>
                        <th>产品/市场</th>
                        <th>本地</th>
                        <th>区域</th>
                        <th>国内</th>
                        <th>亚洲</th>
                        <th>国际</th>
                    </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>P1</td>
                            <td>
                                <select>
                                    <option value="10000">10000</option>
                                </select>
                            </td>
                            <td>
                                <select>
                                    <option value="10000">10000</option>
                                </select>
                            </td>
                            <td>
                                <select>
                                    <option value="10000">10000</option>
                                </select>
                            </td>
                            <td>
                                <select>
                                    <option value="10000">10000</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>P2</td>
                            <td>
                                <select>
                                    <option value="10000">10000</option>
                                </select>
                            </td>
                            <td>
                                <select>
                                    <option value="10000">10000</option>
                                </select>
                            </td>
                            <td>
                                <select>
                                    <option value="10000">10000</option>
                                </select>
                            </td>
                            <td>
                                <select>
                                    <option value="10000">10000</option>
                                </select>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            </div>
            <div class="am-panel am-panel-default">
                <div class="am-panel-bd">
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
            </div>
            <div class="am-panel am-panel-default">
                <div class="am-panel-bd">
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
        </div>
        <div id="panel-3" class="panel">
          <%--  <div class="am-panel am-panel-default">
                <div class="am-panel-bd repertory">
                <ul class="am-avg-sm-2">
                    <li style="border-right: 1px solid #DDDDDD">
                        <b style="margin-left: 30px;">原料</b>
                        <ul>
                            <c:forEach items="${materialList}" var="material">
                            <li>${material.type} : ${material.amount}</li>
                            </c:forEach>
                        </ul>
                    </li>
                    <li>
                        <b style="margin-left: 30px;">产品</b>
                        <ul>
                            <c:forEach items="${productList}" var="product">
                                <li>${product.type} : ${product.amount}</li>
                            </c:forEach>
                        </ul>
                    </li>
                </ul>
                </div>
            </div>--%>

            <div class="am-panel am-panel-default">
                <div class="am-panel-bd">
                    <table class="am-table">
                        <thead>
                        <tr>
                            <th>原料类型</th>
                            <th>库存</th>
                            <th>采购数量</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${materialList}" var="material">
                        <tr>
                            <td>${material.type}</td>
                            <td>${material.amount}</td>
                            <td>
                                <select id="materialNum_${material.id}">
                                    <option value="${material.id}#-1">请选择数量</option>
                                    <option value="${material.id}#1">1</option>
                                    <option value="${material.id}#2">2</option>
                                    <option value="${material.id}#3">3</option>
                                    <option value="${material.id}#4">4</option>
                                    <option value="${material.id}#6">6</option>
                                    <option value="${material.id}#8">8</option>
                                </select>
                            </td>
                        </tr>
                        </c:forEach>
                        <tr>
                            <td colspan="3">
                                <button id="purchase" type="button" class="am-btn am-btn-secondary">采购</button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

              <div class="am-panel am-panel-default">
                  <div class="am-panel-bd">
                      <table class="am-table">
                          <thead>
                          <tr>
                              <th>产品类型</th>
                              <th>库存</th>
                              <th>剩余研发周期</th>
                              <th></th>
                          </tr>
                          </thead>
                          <tbody>
                          <c:forEach items="${productList}" var="product">
                          <tr>
                              <td>${product.type}</td>
                              <td>${product.amount}</td>
                              <td>${product.developNeedCycle}</td>
                              <td>
                                  <c:if test="${product.developNeedCycle > 0}">
                                      <input type="checkbox" name="developProduct" value="${product.id}">
                                  </c:if>
                              </td>
                          </tr>
                          </c:forEach>
                          <tr>
                              <td colspan="3">
                                  <button id="develop" type="button" class="am-btn am-btn-secondary">研发</button>
                              </td>
                          </tr>
                          </tbody>
                      </table>
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
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${produceLineList}" var="line">
                        <tr>
                            <td>${line.name}</td>
                            <td id="td_lineType_${line.id}">
                                <span <c:if test="${line.status=='UN_BUILD'}">style="display: none"</c:if>>
                                   <ming800:status type = "normal" name="lineType" dataType="ProduceLine.produceLineType" checkedValue="${line.produceLineType}"/>
                                </span>
                                <select id="lineType_${line.id}" name="lineType" <c:if test="${line.status!='UN_BUILD'}">style="display: none"</c:if>>
                                    <option value="-1">无</option>
                                    <option value="MANUAL" <c:if test="${line.produceLineType=='MANUAL'}">selected="selected"</c:if>>手工</option>
                                    <option value="HALF_AUTOMATIC" <c:if test="${line.produceLineType=='HALF_AUTOMATIC'}">selected="selected"</c:if>>半自动</option>
                                    <option value="AUTOMATIC" <c:if test="${line.produceLineType=='AUTOMATIC'}">selected="selected"</c:if>>自动</option>
                                    <option value="FLEXBILITY" <c:if test="${line.produceLineType=='FLEXBILITY'}">selected="selected"</c:if>>柔性</option>
                                </select>
                            </td>
                            <td id="td_produceType_${line.id}">
                                <span <c:if test="${line.status=='UN_BUILD'}">style="display: none"</c:if>>
                                   <ming800:status type = "normal" name="produceType" dataType="ProduceLine.produceType" checkedValue="${line.produceType}"/>
                                </span>
                                <select id="produceType_${line.id}" name="produceType" <c:if test="${line.status!='UN_BUILD'}">style="display: none;" </c:if>>
                                    <option value="-1">无</option>
                                    <c:forEach items="${productList}" var="product">
                                        <c:if test="${product.developNeedCycle == 0}">
                                            <option value="${product.type}">${product.type}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td id="operation_${line.id}" colspan="3" style="border-top: 0">
                                <c:choose>
                                    <c:when test="${line.status == 'UN_BUILD'}">
                                        <button id="build_${line.id}" type="button" class="am-btn am-btn-secondary">投资新生产线</button>
                                    </c:when>
                                    <c:when test="${line.status == 'BUILDING'}">
                                        <button id="continueBuild_${line.id}" type="button" class="am-btn am-btn-secondary">再投生产线</button>
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
        <div id="panel-4" class="panel">
            <div class="am-panel am-panel-default">
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

<div data-am-widget="navbar" class="am-navbar am-cf am-navbar-default " id="">
    <ul class="am-navbar-nav am-cf am-avg-sm-4">
        <li >
            <a id="button-1" href="#" class="">
                <span class="am-icon-home"></span>
                <span class="am-navbar-label">主页</span>
            </a>
        </li>
        <li >
            <a id="button-2" href="###" class="">
                <span class="am-icon-sign-in"></span>
                <span class="am-navbar-label">市场</span>
            </a>
        </li>
        <li>
            <a id="button-3" href="###" class="">
                <span class="am-icon-sign-in"></span>
                <span class="am-navbar-label">工厂</span>
            </a>
        </li>
        <li >
            <a id="button-4" href="#" class="">
                <span class="am-icon-sign-in"></span>
                <span class="am-navbar-label">财务</span>
            </a>
        </li>
    </ul>
</div>

<script>

    function test(element) {
        console.log($(element).val());
    }

    $(function () {

        $("#panel-1").show();
        $("a[id^='button-']").click(function(){
            var id = $(this).attr("id");
            $("div[id^='panel']").hide();
            $("#panel-"+id.split("-")[1]).show();
        })


        $("div[id^='enter-']").click(function () {
            var id = $(this).attr("id");
            $("#panel-"+id.split("-")[1]).trigger("click");
        });


        var campaignId = '${campaign.id}';
        var campaignDate = '${campaign.currentCampaignDate}';
        var companyId = '${company.id}';
        var companyTermId = '${companyTerm.id}';
        var roundType = '${roundType}';


        $("#purchase").click(function(){
            $("select[id^='materialNum_']").each(function(){
                var $material = $(this);
                var value = $material.val();
                var materialId = value.split("#")[0];
                var num = value.split("#")[1];
                if(num!=-1) {
                    $.getJSON("<c:url value="/manufacturing/purchase.do"/>",
                            {
                                companyTermId: companyTermId,
                                materialId: materialId,
                                materialNum: num
                            },
                            function(data){
                                if(data.status==1) {
                                    $material.replaceWith(num);
                                    $("#purchase").replaceWith("采购结束，原料下期入库");
                                }
                            });
                } else {
                    $material.replaceWith("0");
                }
            });
        })


        $("#develop").click(function(){
            var $developButton = $(this);
            $("input[name='developProduct']:checked").each(function(){
                var $product = $(this);
                var productId = $product.val();
                //开始建造
                $.getJSON("<c:url value="/manufacturing/develop.do"/>",
                        {
                            companyTermId: companyTermId,
                            partId: productId
                        },
                        function(data){
                            if(data.status == 1) {
                                $developButton.replaceWith("研发中");
                            }
                        });
            });
        });


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
                                $("#lineType_" + partId).replaceWith($("#lineType_" + partId).find("option:selected").text());
                                $("#produceType_" + partId).replaceWith($("#produceType_" + partId).find("option:selected").text());
                            } else {
                                $('#operation_' + partId).html("建造中");
                            }
                        });
            }
        });


        $("button[id^='continueBuild_']").click(function(){
            var $build = $(this);
            var partId = $build.attr("id").split("_")[1];
            //开始建造
            $.getJSON("<c:url value="/manufacturing/continueBuildProduceLine.do"/>",
                    {
                        companyTermId: companyTermId,
                        partId: partId
                    },
                    function(data){
                        if(data.status == 1) {
                            $build.replaceWith("建造中");
                        }
                    });
        });


        $("td[id^='operation_']").delegate("button[id^='produce_']","click",function(){
            var $produce = $(this);
            var produceLineId = $produce.attr("id").split("_")[1];

            //开始建造
            $.getJSON("<c:url value="/manufacturing/produce.do"/>",
                    {
                        companyTermId: companyTermId,
                        produceLineId: produceLineId
                    },
                    function(data){
                        //建造结果
                        var status = data.status;
                        if(status == 1) {
                            $produce.replaceWith("生产中");
                        } else {
                            alert(data.message);
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

//        setInterval(isNext, 5000);
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