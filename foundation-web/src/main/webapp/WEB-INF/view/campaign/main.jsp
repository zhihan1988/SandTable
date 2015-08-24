<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html class="no-js">
<head></head>
<body>

    <h3>${campaign.name} -- ${campaign.currentCampaignDate}</h3>
    <div class="am-panel am-panel-default">
        <div class="am-panel-hd">市场营销</div>
        <div class="am-panel-bd">
            <p>资金投入</p>
        </div>
    </div>
    <div class="am-panel am-panel-default">
        <div class="am-panel-hd">产品研发</div>
        <div class="am-panel-bd">
            <p>资金投入</p>
        </div>
    </div>

    <div class="am-panel am-panel-default">
        <div class="am-panel-hd">运营</div>
        <div class="am-panel-bd">
            <p>资金投入</p>
            <p>用户数</p>
        </div>
    </div>

    <div class="am-panel am-panel-default">
        <div class="am-panel-hd">
            人才
            <a href="/work/">招聘</a>
        </div>
        <div class="am-panel-bd">
            <p></p>
        </div>
    </div>

</body>
</html>