$(function () {

    var base = getBaseUrl();

    var campaignId = $("#campaignId").val();
    var campaignDate = $("#campaignDate").val();
    var companyId = $("#companyId").val();
    var companyTermId = $("#companyTermId").val();
    var roundType = $("#roundType").val();

    //市场投放
    $("select[id^='marketFee_'],select[id^='instruction_']").change(function(){
        var $choice = $(this);
        var array = $choice.val().split("#");
        var choiceId = array[0];
        var value = array[1];
        if (value != -1) {
            $.post(base+"/work/makeInstruction",
            {
                companyTermId: companyTermId,
                choiceId: choiceId,
                value: value
            });
        } else {
            $.post(base + "/work/cancelInstruction",
            {
                companyId: companyId,
                choiceId: choiceId
            });
        }
    })

    var intervalFlag;
    //完成市场投放
    $("#finishDevotion").click(function(){
        var $finishDevotionButton = $(this);
        $.getJSON(base + "/manufacturing/finishDevotion.do",
            {
                campaignId: campaignId,
                companyId: companyId,
                companyTermId: companyTermId
            }, function(data){
                if(data.status == 1) {
                    $finishDevotionButton.replaceWith("等待竞标结果");
                    intervalFlag = setInterval(refreshDevoteCycleStatus, 5000);

                    update(data.newReport);
                } else {
                    alert(data.message);
                }
            }
        );
    });

    function refreshDevoteCycleStatus(){

        $.getJSON(base + "/manufacturing/refreshDevoteCycleStatus.do",
            {
                campaignId: campaignId
            }, function(data){
                if(data.status==1){
                    if(data.cycleStatus==1) {
                        //投标进行中
                    } else if(data.cycleStatus==2) {

                        //投标完成进入选单阶段
                        var company = data.company;
                        var market = data.market;
                        var marketOrderChoiceList = data.marketOrderChoiceList;
                        var companyOrderList = data.companyOrderList;

                        var companyOrderTbody = $("<tbody id='companyOrderTbody'></tbody>");
                        for(var i in companyOrderList){
                            var index = Number(i) + 1;
                            var companyOrder = companyOrderList[i];
                            var tr = $("<tr></tr>");
                            tr.appendTo(companyOrderTbody);
                            tr.append($("<td>" + index + "</td>"));
                            tr.append($("<td>" + companyOrder + "</td>"));
                        }
                        $("#companyOrderTbody").replaceWith(companyOrderTbody);

                        var tbody = $("<tbody id='marketOrderTbody'></tbody>");
                        for(var i in marketOrderChoiceList){
                            var marketOrderChoice = marketOrderChoiceList[i];
                            var tr = $("<tr></tr>");
                            tr.appendTo(tbody);

                            tr.append($("<td>" + marketOrderChoice.name + "</td>"));
                            //tr.append($("<td>" + marketOrderChoice.industryResourceChoice.value + "</td>"));
                            tr.append($("<td>" + marketOrderChoice.productType + "</td>"));
                            tr.append($("<td>" + marketOrderChoice.industryResourceChoice.value2 + "</td>"));

                            if( marketOrderChoice.ownerCompany == null){
                                if(company == companyId){
                                    var $choiceRadio = $('<input type="radio" name="orderChoice"/>')
                                        .val(marketOrderChoice.industryResourceChoice.id+'#'+marketOrderChoice.industryResourceChoice.value);
                                    $("<td></td>").append($choiceRadio).appendTo(tr);
                                } else {
                                    tr.append($("<td></td>"));
                                }
                            } else {
                                tr.append($("<td>" + marketOrderChoice.ownerCompany + "</td>"));
                            }
                        }

                        $("#marketOrderTbody").replaceWith(tbody);


                        $("#devotePanel").hide();
                        $("#marketOrderChoicePanel").show();
                        $("#companyOrderPanel").show();

                        if(company == companyId){
                            clearInterval(intervalFlag);
                            $("#marketOrderChoicePanel_message").html(market+":请选单");
                            $("#confirmOrder").text("确认订单").attr("disabled",false).removeClass("am-disabled");
                        } else {
                            $("#marketOrderChoicePanel_message").html(market+":"+company+"正在选单，请等待");
                            $("#confirmOrder").text("等待选单").attr("disabled", true).addClass("am-disabled");
                        }

                    } else if(data.cycleStatus==3) {
                        //结束
                        clearInterval(intervalFlag);
                        //alert("选单环节结束");

                        $("#marketOrderChoicePanel").hide();
                        $("#companyOrderPanel").hide();

                        //加载目前所有的订单
                        $.getJSON(base + "/manufacturing/listCurrentMarketOrder.do",
                            {
                                companyId: companyId
                            } ,function(data){
                                if(data.status==1) {
                                    var tbody = $("#marketOrderListTbody");
                                    for(var i in data.marketOrderList) {
                                        var marketOrder = data.marketOrderList[i];
                                        var tr = $("<tr></tr>");
                                        tr.appendTo(tbody);

                                        tr.append($("<td>" + marketOrder.name + "</td>"));
                                        tr.append($("<td>" + marketOrder.productType + "</td>"));
                                        tr.append($("<td>" + marketOrder.amount+"/" + marketOrder.totalPrice+"/" + marketOrder.needAccountCycle + "</td>"));
                                        var $button = $('<button type="button" class="am-btn am-btn-secondary">交付</button>')
                                            .attr("id","deliver_" + marketOrder.id);
                                        $("<td></td>").append($button).appendTo(tr);
                                        tbody.append(tr);
                                    }
                                }
                            }
                        );
                    }
                } else {
                    alert(data.message);
                }
            }
        );
    }

    //确认订单
    $("#confirmOrder").click(function(){
        var $choice = $("input[name='orderChoice']:checked");
        var array = $choice.val().split("#");
        var choiceId = array[0];
        var value = array[1];
        if (value != -1) {

            $.getJSON(base + "/manufacturing/chooseOrder.do",
                {
                    companyTermId: companyTermId,
                    choiceId: choiceId,
                    value: value
                }, function(data){
                    if(data.status == 1) {
                        intervalFlag = setInterval(refreshDevoteCycleStatus, 5000);
                        $("#confirmOrder").text("选单完成").attr("disabled",true).addClass("am-disabled");
                        //
                    } else {
                        alert(data.message);
                    }
                }
            );
        }
    })

    //市场投入
    $("button[id^='devoteMarket_']").click(function(){
        var $marketButton = $(this);
        var marketId = $marketButton.attr("id").split("_")[1];
        //开始建造
        $.getJSON(base + "/manufacturing/devoteMarket.do",
            {
                companyTermId: companyTermId,
                partId: marketId
            },
            function(data){
                if(data.status == 1) {
                    $marketButton.replaceWith("开发中");
                    update(data.newReport);
                }
            });
    });

    //交付
    $("#marketOrderListTable").delegate("button[id^='deliver_']","click",function(){
        var $order = $(this);
        var orderId = $order.attr("id").split("_")[1];
        $.getJSON(base + "/manufacturing/deliverOrder.do",
            {
                companyTermId: companyTermId,
                orderId: orderId
            }, function(data){
                if(data.status == 1) {
                    $order.replaceWith("已交付");
                    update(data.newReport);
                } else {
                    alert(data.message);
                }
            }
        );
    });

})

function getBaseUrl() {
    return "";
}