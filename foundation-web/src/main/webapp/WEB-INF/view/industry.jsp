<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: pgwt
  Date: 10/25/15
  Time: 4:28 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>

<ul class="am-nav am-nav-pills">
    <li><h1 style="font-size: 27px;">商世界</h1></li>
    <li><a href="<c:url value="/home.do"/> ">首页</a></li>
    <li class="am-active"><a href="<c:url value="/industry.do"/>">行业</a></li>
    <li style="float: right"><a style="padding-left: 2px; padding-right: 2px" href="<c:url value="/index.jsp"/>">登录</a></li>
    <li style="float: right;"><a style="padding-left: 2px; padding-right: 2px" href="<c:url value="/pc/register"/>">注册</a></li>
</ul>

<h2 id="internet">互联网 <span style="font-size:14px;">难度 ****</span></h2>

<p>作为一个创业者，你已经得到250万元的天使投资，通过招募优秀人才，打造优秀产品，采取积极的市场活动，不断提升运营水平，打败竞争者，打造一家互联网上市公司。</p>

<h3>说明</h3>

<a id="internetIntro">查看详细说明</a>

<div id="internetContent" style="display: none;">
    <h3>1招聘</h3>

    <p>人才包括岗位和级别，获取较高级别的某岗位的人才，有助于提升相应职能的能力。</p>


    <p>每季度出现的各个人才。</p>

    <P>各家公司会去竞争所需类别的较高级别的人才，影响因素包括薪酬、运气和办公室条件。</P>

    <h3>2产品</h3>

    <p>较好的产品有助于提升市场和运营效果。</p>

    <p>较高的定位客单价较高，但如果多个公司采用相同的定位，那么会对客单价产生较多影响。</p>

    <p>较好较多的产品设计技术人才有助于提高产品研发能力。</p>

    <h3>3市场</h3>

    <p>通过开展市场活动可以获得新用户，各类市场活动的获取成本不同，但如果有较多的公司采用相同的市场活动方式，那么获取成本会提高。</p>

    <p>较好较多的市场人才有助于提高市场能力。</p>

    <h3>4运营</h3>

    <p>较好的运营水平有助于提高满意度，较高的满意度意味着较高的用户留存率。</p>

    <p>较好的运营人才和较多的运营投入有助于提升运营能力。</p>

    <p>本期总用户数和客单价决定了本期的收入。</p>
</div>


<hr>
<h3>当前竞赛</h3>
<ul class="am-list">
    <c:forEach items="${campaignList}" var="campaign">
        <li><b>${campaign.name}</b> <fmt:formatDate value="${campaign.startDatetime}" pattern="yyyy-MM-dd HH:mm"/> <a
                href="<c:url value="/campaign/${campaign.id}"/>">进入</a></li>
    </c:forEach>
</ul>

<hr>

<h2 id="manufacturing">制造业 <span style="font-size:14px;">难度 *****</span></h2>

<p>作为一名职业经理人，接手一家大型制造业，联合团队，通过有效利用资金，采取积极的市场策略和生产策略，在4-5年内把企业提升到一家较高盈利水平的企业，为股东创造价值，也为自己赢得荣誉和奖金。</p>

<h3>说明</h3>
<a id="manuIntro">查看详细说明</a>

<div id="manuContent" style="display: none;">

    <h3>1市场</h3>

    <p>每年要进行市场竞标，投标较高者优先选择市场订单。</p>


    <h3>2生产</h3>

    <p>通过建设先进的生产线，经营者可以把采购的原料加工成产品，交付订单，从而获得收入。</p>

    <h3>3财务</h3>

    <p>可以通过长期借贷、短期借贷、高利贷等方式实现债权融资。</p>
</div>


<script>
    $("#internetIntro").click(function () {
        $("#internetContent").toggle("fast", function () {
            if ($("#internetContent").css("display") == "none") {
                $("#internetIntro").html("查看详细说明");
            } else {
                $("#internetIntro").html("收起");
            }
        });
    });

    $("#manuIntro").click(function () {
        $("#manuContent").toggle("fast", function () {
            if ($("#manuContent").css("display") == "none") {
                $("#manuIntro").html("查看详细说明");
            } else {
                $("#manuIntro").html("收起");
            }
        });
    });

</script>

<hr>
<h3>当前竞赛</h3>
<ul class="am-list">
    <c:forEach items="${campaignList2}" var="campaign">
        <li><b>${campaign.name}<b>
                <fmt:formatDate value="${campaign.startDatetime}" pattern="yyyy-MM-dd HH:mm"/> <a
                href="<c:url value="/campaign/${campaign.id}"/>">进入</a></li>
    </c:forEach>
</ul>
<hr>

<h2>餐饮业 <span style="font-size:14px;">难度 ***</span></h2>

<p>作为草根创业者，以50万元资本，从经营一家小型饭店开始起步，通过有效选址、准确定位、提供较好口味、环境、服务赢得顾客，创造利润。</p>

<p><a href="#">待推出。。。</a></p>


------
</body>
</html>
