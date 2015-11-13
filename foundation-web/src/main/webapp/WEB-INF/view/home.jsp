<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: pgwt
  Date: 10/25/15
  Time: 3:44 下午
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
    <li class="am-active"><a href="<c:url value="/home.do"/> ">首页</a></li>
    <li><a href="<c:url value="/industry.do"/>">行业</a></li>
    <li style="float: right;"><a style="padding-left: 2px; padding-right: 2px" href="<c:url value="/index.jsp"/>">登录</a></li>
    <li style="float: right;"><a style="padding-left: 2px; padding-right: 2px" href="<c:url value="/pc/register"/>">注册</a></li>
</ul>

<div>


    <p>商世界模拟了商业领域的创业和经营管理，在这里可以充分感受企业经营的困难和喜悦。目前商世界提供了制造和互联网两个行业，未来几个月，我们将陆续推出多个行业。</p>

    <p>在商世界，创造你的商业帝国！</p>

    <p>在商世界，锻炼你的商业思维！</p>

    <p>在商世界，淘金！</p>

    <hr/>
    <h2>互联网 <span style="font-size:14px;">难度 ****</span></h2>

    <p>作为一个创业者，你已经得到250万元的天使投资，通过招募优秀人才，打造优秀产品，采取积极的市场活动，不断提升运营水平，打败竞争者，打造一家互联网上市公司。</p>

    <a href="<c:url value="/industry.do#internet"/>">进入</a>
    <br/>

    <h2>制造业 <span style="font-size:14px;">难度 *****</span></h2>

    <p>作为一名职业经理人，接手一家大型制造业，联合团队，通过有效利用资金，采取积极的市场策略和生产策略，在4-5年内把企业提升到一家较高盈利水平的企业，为股东创造价值，也为自己赢得荣誉和奖金。</p>

    <a href="<c:url value="/industry.do#manufacturing"/>">进入</a>
    <br/>

    <h2>餐饮业 <span style="font-size:14px;">难度 ***</span></h2>

    <p>作为草根创业者，以50万元资本，从经营一家小型饭店开始起步，通过有效选址、准确定位、提供较好口味、环境、服务赢得顾客，创造利润。</p>

    <a>待推出。。。</a>

</div>


</body>
</html>
