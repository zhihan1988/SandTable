<%--
  Created by IntelliJ IDEA.
  User: Hean
  Date: 2015/8/27
  Time: 17:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
    <style type="text/css">
        .multi-select {
            position: relative;
            /*width: 100px;*/
            /*height: 80px;*/
            /*border: 1px solid #E3E3E3;*/
        }
        .multi-select-selected {
           /*border-color: red;*/
        }
        .multi-select-icon {
            position: absolute;
            right: 5px;
            top: 5px;
            font-size: 20px;
            color: #FFFFFF;
        }
        .multi-select-icon-selected {
            color: #f37b1d;
        }
    </style>
    <script type="text/javascript">
        $(function(){
            $(".multi-select").click(function(){
                var $multiSelect = $(this);
                $multiSelect.toggleClass("multi-select-selected");
                $multiSelect.children(".multi-select-icon").toggleClass("multi-select-icon-selected");
            })
        })
    </script>
</head>
<body>
    <h3>互联网</h3>
    <div><a href="<c:url value="/flow/reset?campaignId=1"/>">重新开始</a></div>
    <div><a href="<c:url value="/flow/random?campaignId=1"/>">随机数据</a></div>
    <div><a href="<c:url value="/flow/next?campaignId=1"/>">开始下一回合</a></div>

    <h3>制造业</h3>
    <div><a href="<c:url value="/flow/reset?campaignId=2"/>">重新开始</a></div>
    <div><a href="<c:url value="/flow/random?campaignId=2"/>">随机数据</a></div>
    <div><a href="<c:url value="/flow/next?campaignId=2"/>">开始下一回合</a></div>
   <%-- <div class="multi-select am-btn am-btn-secondary">
        <i class="am-icon-check multi-select-icon"></i>
        <span style="margin-right: 25px;">3000000</span>
    </div>
    <div class="multi-select am-btn am-btn-secondary">
        <i class="am-icon-check multi-select-icon"></i>
        <span style="margin-right: 25px;">3000000</span>
    </div>
    <div class="multi-select am-btn am-btn-secondary">
        <i class="am-icon-check multi-select-icon"></i>
        <span style="margin-right: 25px;">3000000</span>
    </div>--%>
</body>
</html>
