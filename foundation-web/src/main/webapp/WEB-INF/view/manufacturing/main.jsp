<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<!doctype html>
<html class="no-js">
<head>
    <script src="<c:url value='/js/manufacturing/main.js'/>"></script>
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

        .am-nav-tabs>li>a {
            padding:.4em 0.5em
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
                    <ul>
                        <li>现金：<span id="companyCash">${companyCash}</span></li>
                    </ul>
                    <ming800:radioSet valueSet="key1:value1,key2:value2,key3:value3,key4:value4,key5:value5" name="test" onclick="testButton" checkedValue="value3"/>
                </div>
            </div>
        </div>
        <div id="panel-2" class="panel">
            <c:if test="${campaign.currentCampaignDate%4==1}">
            <div class="am-panel am-panel-default" id="devotePanel">
                <div class="am-panel-bd">
                <h3>投放广告</h3>
                    <div class="am-tabs" data-am-tabs>
                        <ul class="am-tabs-nav am-nav am-nav-tabs">
                            <li class="am-active"><a href="#tab1">本地</a></li>
                            <c:if test="${marketMap['AREA'] != null}">
                                <li><a href="#tab2">区域</a></li>
                            </c:if>
                            <c:if test="${marketMap['DOMESTIC'] != null}">
                                <li><a href="#tab3">国内</a></li>
                            </c:if>
                            <c:if test="${marketMap['ASIA'] != null}">
                                <li><a href="#tab4">亚洲</a></li>
                            </c:if>
                            <c:if test="${marketMap['INTERNATIONAL'] != null}">
                                <li><a href="#tab5">国际</a></li>
                            </c:if>
                        </ul>

                        <div class="am-tabs-bd">
                            <c:forEach items="${marketMap}" var="market" varStatus="status">
                                <div class="am-tab-panel am-fade am-in am-active" id="tab${status.index+1}">
                                    <table class="am-table">
                                        <c:forEach items="${market.value}" var="productChoiceMap">
                                            <tr>
                                                <td>${productChoiceMap.key}</td>
                                                <td>
                                                    <select id="marketFee_${productChoiceMap.value.id}">
                                                        <option value="${productChoiceMap.value.id}#-1">未投入</option>
                                                        <c:forEach items="${fn:split(productChoiceMap.value.industryResource.valueSet, ',')}" var="fee">
                                                            <option value="${productChoiceMap.value.id}#${fee}">${fee}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </c:forEach>
                        </div>
                        <button id="finishDevotion" type="button" class="am-btn am-btn-secondary">完成投放</button>
                    </div>
                </div>
            </div>
            <%--<div class="am-panel am-panel-default">
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
            </div>--%>
            <div class="am-panel am-panel-default" id="marketOrderChoicePanel" style="display: none">
                <div class="am-panel-bd">
                <h3><span id="marketOrderChoicePanel_message">正在选单</span></h3>
                <table class="am-table">
                    <thead>
                    <tr>
                        <th>订单</th>
                        <th>订单编号</th>
                        <th>产品</th>
                        <th>数量/单价/金额/账期/ISO/成本/利润</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody id="marketOrderTbody">
                    <%--<c:forEach items="${marketOrderResource.currentIndustryResourceChoiceSet}" var="choice">
                        <tr>
                            <td>${choice.name}</td>
                            <td>${choice.value}</td>
                            <td>${choice.type}</td>
                            <td>${choice.value2}</td>
                            <td><input type="radio" name="orderChoice" value="${choice.id}#${choice.value}"/></td>
                        </tr>
                    </c:forEach>--%>
                    </tbody>
                </table>
                <button id="confirmOrder" type="button" class="am-btn am-btn-secondary">确认订单</button>
                </div>
            </div>
            </c:if>
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
                            <td><span id="materialAmount_${material.type}">${material.amount}</span></td>
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
                              <td><span id="productAmount_${product.type}">${product.amount}</span></td>
                              <td>
                                  ${product.developNeedCycle}
                                  <c:if test="${product.developNeedCycle > 0}">
                                      <button id="devoteProduct_${product.id}" type="button" class="am-btn am-btn-secondary">研发</button>
                                  </c:if>
                              </td>
                          </tr>
                          </c:forEach>
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
                    <table class="am-table">
                        <thead>
                        <tr>
                            <th>贷款</th>
                            <th>金额</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${usuriousLoanResource.currentIndustryResourceChoiceSet}" var="choice">
                            <tr>
                                <td>高利贷</td>
                                <td>
                                    <select id="usuriousLoan_${choice.id}" name="usuriousLoanFee" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                        <option value="${choice.id}#-1">不需要</option>
                                        <c:forEach items="${fn:split(usuriousLoanResource.valueSet, ',')}" var="fee">
                                            <option value="${choice.id}#${fee}">${fee}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:forEach items="${shortTermLoanResource.currentIndustryResourceChoiceSet}" var="choice">
                            <tr>
                                <td>短期贷款</td>
                                <td>
                                    <select id="shortTermLoan_${choice.id}" name="shortTermLoanFee" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                        <option value="${choice.id}#-1">不需要</option>
                                        <c:forEach items="${fn:split(shortTermLoanResource.valueSet, ',')}" var="fee">
                                            <option value="${choice.id}#${fee}">${fee}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:forEach items="${longTermLoanResource.currentIndustryResourceChoiceSet}" var="choice">
                            <tr>
                                <td>长期贷款</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${campaign.currentCampaignDate%4==0}">
                                            <select id="longTermLoan_${choice.id}" name="longTermLoanFee" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                                <option value="${choice.id}#-1">不需要</option>
                                                <c:forEach items="${fn:split(longTermLoanResource.valueSet, ',')}" var="fee">
                                                    <option value="${choice.id}#${fee}">${fee}</option>
                                                </c:forEach>
                                            </select>
                                        </c:when>
                                        <c:otherwise>
                                            第四季度可贷
                                        </c:otherwise>
                                    </c:choose>
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
                <span class="am-navbar-label">制造</span>
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
<input type="hidden" id="campaignId" name="campaignId" value="${campaign.id}"/>
<input type="hidden" id="campaignDate" name="campaignDate" value="${campaign.currentCampaignDate}"/>
<input type="hidden" id="companyId" name="companyId" value="${company.id}"/>
<input type="hidden" id="companyTermId" name="companyTermId" value="${companyTerm.id}"/>
<input type="hidden" id="roundType" name="roundType" value="${roundType}"/>
<script>

    function testButton(element) {
        console.log($(element).val());
    }

</script>
</body>
</html>