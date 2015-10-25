<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<%--
  Created by IntelliJ IDEA.
  User: pgwt
  Date: 10/3/15
  Time: 4:32 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <script>
        function removeC(cid) {
            jQuery.ajax({
                type: "GET",
                url: '<c:url value="/basic/xmj.do?qm=removeCampaign"/>',
                data: {id: cid},
                dataType: "json",
                success: function (data) {
                    $("#" + cid).remove();
                }
            });
        }
    </script>

</head>
<body>
    <div class="am-g">
        <div class="am-u-sm-12 am-u-md-6">
        </div>
        <div class="am-u-sm-12">
            <table class="am-table am-table-striped am-table-hover table-main">
                <thead>
                <tr>
                    <th class="table-set">操作</th>
                    <th class="table-title">比赛名称</th>
                    <th class="table-title">比赛状态</th>
                    <th class="table-title">所属行业</th>
                    <th class="table-title">当前进度</th>
                    <th class="table-title">游戏开始时间</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${requestScope.pageInfo.list}" var="campaign">
                    <tr id="${campaign.id}">
                        <td>
                            <div class="am-btn-toolbar">
                                <div class="am-btn-group am-btn-group-xs">
                                    <button class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only"
                                            onclick="showConfirm('提示','确认删除？',function(){removeC('${campaign.id}')})"><span
                                            class="am-icon-trash-o">删除</span>
                                    </button>

                                    <a href="<c:url value="/basic/xm.do?qm=plistCompany_campaign&conditions=campaign.id:${campaign.id}"/> " class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only"><span
                                            class="am-icon-trash-o">参与企业</span>
                                    </a>
                                </div>
                            </div>
                        </td>
                        <td class="am-hide-sm-only"><a
                                href="<c:url value='/basic/xm.do?qm=viewCampaign&view=index&id=${campaign.id}'/>">${campaign.name}</a>
                        </td>
                        <td class="am-hide-sm-only"><ming800:status name="status" dataType="Campaign.status"
                                                                    checkedValue="${campaign.status}"
                                                                    type="normal"/></td>
                        <td class="am-hide-sm-only">${campaign.industry.name}</td>
                        <td class="am-hide-sm-only">${campaign.currentCampaignDate}</td>
                        <td class="am-hide-sm-only">${campaign.startDatetime}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <div style="clear: both">
        <c:url value="/basic/xm.do" var="url"/>
        <ming800:pcPageList bean="${requestScope.pageInfo.pageEntity}" url="${url}">
            <ming800:pcPageParam name="qm" value="${requestScope.qm}"/>
            <ming800:pcPageParam name="conditions" value="${requestScope.conditions}"/>
        </ming800:pcPageList>
    </div>
</body>
</html>
