<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html class="no-js">
<head></head>
<body>
<header class="am-topbar am-topbar-inverse am-topbar-fixed-top">
    <div class="am-topbar-left">
        <button id="submit" class="am-btn am-btn-primary am-topbar-btn am-btn-sm">结束回合</button>
    </div>
</header>
<%--<div style="position:fixed;right: 0;bottom: 50px;">
    <button class="am-btn am-btn-primary">结束回合</button>
</div>--%>
<h3>${campaign.name} -- ${campaign.currentCampaignDate}</h3>
<div class="am-panel-group" id="accordion">
    <div class="am-panel am-panel-default">
        <div class="am-panel-hd">
            <h4 class="am-panel-title" data-am-collapse="{parent: '#accordion', target: '#do-not-say-1'}">
                市场营销
            </h4>
        </div>
        <div class="am-panel-bd"  data-am-collapse="{parent: '#accordion', target: '#do-not-say-1'}">
            <c:forEach items="${deptPropertyMap['MARKET']}" var="property">
                <p>${property.label}:${property.value}</p>
            </c:forEach>
        </div>
        <div id="do-not-say-1" class="am-panel-collapse am-collapse am-in">
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
                            <td>${marketActivityChoice.cost}</td>
                            <td>
                                <select name="marketActivityChoiceFee_${marketActivityChoice.id}" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                    <option value="-1">不需要</option>
                                    <c:forEach items="${fn:split(marketActivityChoice. fees, ',')}" var="fee">
                                        <option value="${fee}">${fee}</option>
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
            <c:forEach items="${deptPropertyMap['PRODUCT']}" var="property">
                <p>${property.label}:${property.value}</p>
            </c:forEach>
        </div>
        <div id="do-not-say-2" class="am-panel-collapse am-collapse">
            <div class="am-panel-bd">
                研发投入
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
            <c:forEach items="${deptPropertyMap['OPERATION']}" var="property">
                <p>${property.label}:${property.value}</p>
            </c:forEach>
        </div>
        <div id="do-not-say-3" class="am-panel-collapse am-collapse">
            <div class="am-panel-bd">
                定位
            </div>
        </div>
    </div>
    <div class="am-panel am-panel-default">
        <div class="am-panel-hd">
            <h4 class="am-panel-title" data-am-collapse="{parent: '#accordion', target: '#do-not-say-4'}">
                人才
            </h4>
        </div>
        <div id="do-not-say-4" class="am-panel-collapse am-collapse">
            <div class="am-panel-bd">
                <table class="am-table">
                    <thead>
                    <tr>
                        <th>名字</th>
                        <th>能力</th>
                        <th>薪资</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${humanList}" var="human">
                        <tr>
                            <td><input type="hidden" name="humanId" value="${human.id}"/>${human.name}</td>
                            <td>${human.ability}</td>
                            <td>
                                <select name="humanFee_${human.id}" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}">
                                    <option value="-1">不需要</option>
                                    <c:forEach items="${fn:split(human. fees, ',')}" var="fee">
                                        <option value="${fee}">${fee}</option>
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
<script>
   /* $('#submit').click(function(){
       var aaa = collectHrInstruction();
        alert(aaa);
    });*/
    var companyId = ${company.id};
    $(function(){
        $("select[name^='humanFee_']").change(function(){
            var humanFee = $(this);
            var id = humanFee.attr("name").split("_")[0];
            var value = humanFee.val();
            if(value != -1) {
                $.post("<c:url value="/work/makeInstruction"/>", {companyId:companyId, entity: "HrInstruction", id: id, value:value } );
            }
        });
    })
</script>
</body>
</html>