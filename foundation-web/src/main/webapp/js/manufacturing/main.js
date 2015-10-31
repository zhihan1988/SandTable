$(function () {

    var base = getBaseUrl();

    var campaignId = $("#campaignId").val();
    var campaignDate = $("#campaignDate").val();
    var companyId = $("#companyId").val();
    var companyTermId = $("#companyTermId").val();
    var roundType = $("#roundType").val();

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

    //完成市场投放
    $("#finishDevotion").click(function(){

    });

    //交付
    $("button[id^='deliver']").click(function () {
        var $order = $(this);
        var orderId = $order.attr("id").split("_")[1];
        $.getJSON(base + "/manufacturing/deliverOrder.do",
        {
            companyTermId: companyTermId,
            orderId: orderId
        },
            function(data){
                if(data.status == 1) {
                    $order.replaceWith("已交付");
                    update(data.newReport);
                } else {
                    alert(data.message);
                }
            });
    })

    $("input[name='orderChoice']").click(function(){
        var $choice = $(this);
        var array = $choice.val().split("#");
        var choiceId = array[0];
        var value = array[1];
        if (value != -1) {
            $.post(base + "/manufacturing/chooseOrder",
            {
                companyTermId: companyTermId,
                choiceId: choiceId,
                value: value
            },function(){

            });
        }
    })

    //采购原料
    $("select[id^='materialNum_']").change(function(){
        var $material = $(this);
        var value = $material.val();
        var materialId = value.split("#")[0];
        var num = value.split("#")[1];
        if(num!=-1) {
            $.getJSON(base + "/manufacturing/purchase.do",
            {
                companyTermId: companyTermId,
                materialId: materialId,
                materialNum: num
            },
                function(data){
                    if(data.status==1) {
                        $material.replaceWith(num);
                        update(data.newReport);
                    }
                });
        }
    })

    //研发投入
    $("button[id^='devoteProduct_']").click(function(){
        var $developButton = $(this);
        var productId = $developButton.attr("id").split("_")[1];
        //开始建造
        $.getJSON(base + "/manufacturing/devoteProduct.do",
        {
            companyTermId: companyTermId,
            partId: productId
        },
            function(data){
                if(data.status == 1) {
                    $developButton.replaceWith("研发中");
                    update(data.newReport);
                }
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
            $.getJSON(base + "/manufacturing/buildProduceLine.do",
            {
                companyTermId: companyTermId,
                partId: partId,
                lineType: lineType,
                produceType: produceType
            },
                function(data){
                    //建造结果
                    if(data.status == 1) {
                        update(data.newReport);
                        $("#lineType_" + partId).replaceWith($("#lineType_" + partId).find("option:selected").text());
                        $("#produceType_" + partId).replaceWith($("#produceType_" + partId).find("option:selected").text());
                        var installCycle = data.installCycle;
                        if(installCycle == 0) {
                            //建造完成
                            var $operationDiv = $('#operation_' + partId);
                            var $button = $('<button type="button">生产</button>').attr('id','produce_'+partId).addClass('am-btn am-btn-secondary');
                            $operationDiv.html($button);
                        } else {
                            $('#operation_' + partId).html("建造中");
                        }
                    }
                });
        }
    });


    $("button[id^='continueBuild_']").click(function(){
        var $build = $(this);
        var partId = $build.attr("id").split("_")[1];
        //继续建造
        $.getJSON(base + "/manufacturing/continueBuildProduceLine.do",
        {
            companyTermId: companyTermId,
            partId: partId
        },
            function(data){
                if(data.status == 1) {
                    $build.replaceWith("建造中");
                    update(data.newReport);
                }
            });
    });


    $("td[id^='operation_']").delegate("button[id^='produce_']","click",function(){
        var $produce = $(this);
        var produceLineId = $produce.attr("id").split("_")[1];

        //开始生产
        $.getJSON(base + "/manufacturing/produce.do",
        {
            companyTermId: companyTermId,
            produceLineId: produceLineId
        },
            function(data){
                //建造结果
                var status = data.status;
                if(status == 1) {
                    $produce.parent().text("生产中");
                    update(data.newReport);
                } else {
                    alert(data.message);
                }
            });
    });

    $("select[id^='usuriousLoan_']").change(function(){
        var $choice = $(this);
        var type = "USURIOUS_LOAN";
        loan($choice, type);
    });
    $("select[id^='shortTermLoan_']").change(function(){
        var $choice = $(this);
        var type = "SHORT_TERM_LOAN";
        loan($choice, type);
    });
    $("select[id^='longTermLoan_']").change(function(){
        var $choice = $(this);
        var type = "LONG_TERM_LOAN";
        loan($choice, type);
    });

    function loan($choice, type){
        var array = $choice.val().split("#");
        var choiceId = array[0];
        var value = array[1];
        $.getJSON(base + "/manufacturing/loan.do",
        {
            companyTermId: companyTermId,
            choiceId: choiceId,
            value: value,
            type: type
        },
            function(data){
                if(data.status == 1) {
                    $choice.parent().text("已贷款");
                    update(data.newReport);
                }
            }
    );
    }

    $("#endCampaignDate").click(function () {
        if(confirm("是否结束当前回合的操作？")) {
            var $endCampaignDate = $(this);
            $.post(base + "/flow/companyNext.do",
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
            setInterval(isNext, 5000);
        }
    });

    function isNext() {
        $.getJSON(base + "/flow/isCampaignNext",
        {
            campaignId:campaignId,
            campaignDate: campaignDate,
            roundType: roundType
        },
            function (data) {
                var isNext = data.isNext;
                if (isNext == true) {
                    location.reload();
                } else {
                    $("#unFinishedNum").text(data.unFinishedNum);
                }
            });

    }
})

function update(newReport){
    var P1Amount = newReport.p1Amount;
    var P2Amount = newReport.p2Amount;
    var P3Amount = newReport.p3Amount;
    var P4Amount = newReport.p4Amount;
    var R1Amount = newReport.r1Amount;
    var R2Amount = newReport.r2Amount;
    var R3Amount = newReport.r3Amount;
    var R4Amount = newReport.r4Amount;
    var companyCash = newReport.companyCash;
    if(P1Amount != null){ $("#productAmount_P1").text(P1Amount);}
    if(P2Amount != null){ $("#productAmount_P2").text(P2Amount); }
    if(P3Amount != null){ $("#productAmount_P3").text(P3Amount); }
    if(P4Amount != null){ $("#productAmount_P4").text(P4Amount); }
    if(R1Amount != null){ $("#materialAmount_R1").text(R1Amount); }
    if(R2Amount != null){ $("#materialAmount_R2").text(R2Amount); }
    if(R3Amount != null){ $("#materialAmount_R3").text(R3Amount); }
    if(R4Amount != null){ $("#materialAmount_R4").text(R4Amount); }
    if(companyCash != null){ $("#companyCash").text(companyCash); }
}

function getBaseUrl() {
    return "";
}