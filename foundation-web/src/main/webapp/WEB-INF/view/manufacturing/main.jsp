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
    <script src="<c:url value='/js/manufacturing/market.js'/>"></script>
    <script src="<c:url value='/js/manufacturing/produce.js'/>"></script>
    <style type="text/css">
        .navbar-button-selected{
            /*background: orange;*/
            background: orangered;
        }
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

        .line-panel {
            border-bottom: 1px solid #DDDDDD;
            padding: 20px;
        }
        .line-innerDiv1 {
            padding: 10px 0;
        }
        .line-innerDiv2 {
            padding: 10px 0;
        }
        .line-innerDiv3 {
            padding: 10px 0;
        }
        .button-padding {
            margin: 5px;
        }
        .line-important {
            font-weight: bold;
        }
        .am-panel-hd {
            font-weight: bold;
        }
        .am-table th{
            font-weight: normal;
        }
        .homePage_ul {
            margin: 0;
            padding: 0;
        }
        .homePage_ul li{
            list-style-type:none;
        }
        #marketOrderChoicePanel_message{
            color: orangered;
        }
    </style>
</head>
<body>
<header data-am-widget="header" class="am-header am-header-default">
    <div class="am-header-left am-header-nav">
        <a href="<c:url value="/campaign/${campaign.id}"/>" class="">
            返回行业
        </a>
        <a href="#" class="">
            <%--<i class="am-header-icon am-icon-phone"></i>--%>
        </a>
    </div>

    <div class="am-header-title" style="margin: 0 20%;font-size: 1.5rem;">
       <%-- <i class="am-icon-circle-o-notch am-icon-spin" style="font-size: 20px;"></i>--%>
           ${campaign.formattedCampaignDate}(${campaign.name})

           <%--<div style="height: 50px;line-height: 30px;position: relative;">
            <span style="position: absolute;top: 0; left: 0;">
                ${campaign.formattedCampaignDate}(${campaign.name})
            </span>
            <span style="position: absolute;bottom: 0;left: 0">
                操作进度：<span id="unFinishedNum">${companyNum}</span>/${companyNum}
            </span>
            <span style="position: absolute;top: 10px; right: 0;">
                $:<span id="companyCash">${companyCash}</span>
            </span>
           </div>--%>
    </div>

    <div class="am-header-right am-header-nav">
        <%--<i class="am-icon-stop"></i>--%>
        <a href="#left-link" id="endCampaignDate" class="">
            结束本期
        </a>
    </div>
</header>
<div style="margin: 10px;">
    <div>
        <div id="panel-1" class="panel">
            <div class="am-panel am-panel-default">

                <div class="am-panel-bd">
                    <h3>财务</h3>
                    <ul class="homePage_ul">
                        <li>公司现金：<span id="homePage_companyCash">${companyCash}</span>M</li>
                        <li>高利贷：<span id="homePage_usuriousLoan">${usuriousLoan}</span>M</li>
                        <li>短期贷款：<span id="homePage_shortTermLoan">${shortTermLoan}</span>M</li>
                        <li>长期贷款：<span id="homePage_longTermLoan">${longTermLoan}</span>M</li>
                    </ul>
                </div>

                <div class="am-panel-bd">
                    <h3>市场</h3>
                    <c:if test="${currentSeason==1}">
                        <a id="homePage_intoMarket" class="am-btn am-btn-warning" href="#">进入市场竞标</a>
                        <br/>
                        <br/>
                    </c:if>

                    未交付订单数量: <span id="unDeliveredOrderSize">${fn:length(marketOrderList)}</span>
                </div>

                <div class="am-panel-bd">
                    <h3>生产</h3>

                    原料库存<br/>
                    R1:<span id="homePage_materialAmount_R1">${R1.amount}</span>
                    R2:<span id="homePage_materialAmount_R2">${R2.amount}</span>
                    R3:<span id="homePage_materialAmount_R3">${R3.amount}</span>
                    R4:<span id="homePage_materialAmount_R4">${R4.amount}</span>
                    <br/>
                    <br/>
                    产品库存<br/>
                    P1:<span id="homePage_materialAmount_P1">${P1.amount}</span>
                    P2:<span id="homePage_materialAmount_P2">${P2.amount}</span>
                    P3:<span id="homePage_materialAmount_P3">${P3.amount}</span>
                    P4:<span id="homePage_materialAmount_P4">${P4.amount}</span>
                </div>

            </div>
        </div>
        <div id="panel-2" class="panel">
            <c:if test="${currentSeason == 1}">
            <div class="am-panel am-panel-default" id="devotePanel">
                <div class="am-panel-hd">广告投放</div>
                <div class="am-panel-bd">

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
                                        <tr>
                                            <th>产品类型</th>
                                            <th>投放金额(M)</th>
                                        </tr>
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
                        <button id="finishDevotion" type="button" class="am-btn am-btn-secondary am-btn-once">完成投放</button>
                    </div>
                </div>
            </div>

            <div class="am-panel am-panel-default" id="companyOrderPanel" style="display: none">
                <div class="am-panel-hd">公司排名</div>
                <div class="am-panel-bd">
                    <table class="am-table">
                        <thead>
                        <tr>
                            <th>排名</th>
                            <th>公司(投入)</th>
                        </tr>
                        </thead>
                        <tbody id="companyOrderTbody">
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="am-panel am-panel-default" id="marketOrderChoicePanel" style="display: none">
                <div class="am-panel-hd">选单</div>
                <div class="am-panel-bd">
                <h3><span id="marketOrderChoicePanel_message">正在选单</span></h3>
                <table class="am-table">
                    <thead>
                    <tr>
                        <th>订单</th>
                        <%--<th>订单编号</th>--%>
                        <th>产品</th>
                        <th>数量/单价(M)/金额(M)/利润(M)/账期</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody id="marketOrderTbody">
                    </tbody>
                </table>
                <button id="confirmOrder" type="button" class="am-btn am-btn-secondary am-disabled am-btn-once" disabled="disabled">等待选单</button>
                </div>
            </div>

            </c:if>
            <div class="am-panel am-panel-default">
                <div class="am-panel-hd">未交付订单</div>
                <div class="am-panel-bd">
                    <table class="am-table" id="marketOrderListTable">
                        <thead>
                        <tr>
                            <th>订单</th>
                            <th>产品</th>
                            <th>数量/金额(M)/账期</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody id="marketOrderListTbody">
                        <c:forEach items="${marketOrderList}" var="marketOrder">
                            <tr>
                                <td>${marketOrder.name}</td>
                                <td>${marketOrder.productType}</td>
                                <td>${marketOrder.amount}/${marketOrder.totalPrice}/${marketOrder.needAccountCycle}</td>
                                <td>
                                    <button id="deliver_${marketOrder.id}" type="button" class="am-btn am-btn-secondary am-btn-once">交付</button>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="am-panel am-panel-default">
                <div class="am-panel-hd">市场开拓</div>
                <div class="am-panel-bd">
                    <table class="am-table">
                        <thead>
                        <tr>
                            <th>市场</th>
                            <th colspan="2">剩余开发周期</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${marketList}" var="market">
                            <tr>
                                <td>${market.name}</td>
                                <td>${market.devotionNeedCycle}</td>
                                <td>
                                    <c:if test="${market.devotionNeedCycle > 0}">
                                        <c:choose>
                                            <c:when test="${currentSeason == 4}">
                                                <button id="devoteMarket_${market.id}" type="button" class="am-btn am-btn-secondary am-btn-once">开发</button>
                                            </c:when>
                                            <c:otherwise>
                                                第四季度可开发
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>
        <div id="panel-3" class="panel">
            <div class="am-panel am-panel-default">
                <div class="am-panel-hd am-panel-hd-module">原料库</div>
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
                  <div class="am-panel-hd am-panel-hd-module">产品库</div>
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
                                      <button id="devoteProduct_${product.id}" type="button" class="am-btn am-btn-secondary am-btn-once">研发</button>
                                  </c:if>
                              </td>
                          </tr>
                          </c:forEach>
                          </tbody>
                      </table>
                  </div>
              </div>
            <%--生产线--%>
            <div class="am-panel am-panel-default" id="produceLinesDiv">
                <c:forEach items="${produceLineList}" var="line" varStatus="status">
                    <c:if test="${status.index==0}"><div class="am-panel-hd am-panel-hd-module" style="border-top: 0">厂房A</div></c:if>
                    <c:if test="${status.index==4}"><div class="am-panel-hd am-panel-hd-module">厂房B</div></c:if>
                    <c:if test="${status.index==7}"><div class="am-panel-hd am-panel-hd-module">厂房C</div></c:if>
                    <div class="am-panel-bd line-panel">
                        <div id="line_${line.id}"></div>
                    </div>
                </c:forEach>
            </div>
        </div>
        <div id="panel-4" class="panel">
            <div class="am-panel am-panel-default">
                <div class="am-panel-hd">公司现金</div>
                <div class="am-panel-bd">
                    <span id="companyCash">${companyCash}</span>M
                </div>
            </div>
            <div class="am-panel am-panel-default">
                <div class="am-panel-hd">贷款</div>
                <div class="am-panel-bd">
                    <table class="am-table">
                        <thead>
                        <tr>
                            <th>贷款类型</th>
                            <th>已贷总额(M)</th>
                            <th>本期贷款(M)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${usuriousLoanResource.currentIndustryResourceChoiceSet}" var="choice">
                            <tr>
                                <td>高利贷</td>
                                <td><span id="usuriousLoan">${usuriousLoan}</span></td>
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
                                <td><span id="shortTermLoan">${shortTermLoan}</span></td>
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
                                <td><span id="longTermLoan">${longTermLoan}</span></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${currentSeason == 4}">
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

            <div class="am-panel am-panel-default">
                <div class="am-panel-hd">待还款记录</div>
                <div class="am-panel-bd">
                    <table class="am-table">
                        <thead>
                        <tr>
                            <th>贷款类型</th>
                            <th>金额(M)</th>
                            <th>剩余还款周期</th>
                        </tr>
                        </thead>
                        <tbody id="loanListTbody">
                        <c:forEach items="${loanList}" var="loan">
                            <tr>
                                <td>${loan.loanTypeLable}</td>
                                <td>${loan.money}</td>
                                <td>${loan.needRepayCycle}</td>
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
            <a id="button-1" href="#" class="navbar-button-selected">
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
                <span class="am-navbar-label">生产</span>
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

</body>
</html>