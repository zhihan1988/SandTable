var base = "";
var campaignId;
var campaignDate;
var companyId;
var companyTermId;
var roundType;

$(function () {
    /*
    //进度条
    var $progress = $.AMUI.progress;
    $progress.configure({ showSpinner: false });*/
    campaignId = $("#campaignId").val();
    campaignDate = $("#campaignDate").val();
    companyId = $("#companyId").val();
    companyTermId = $("#companyTermId").val();
    roundType = $("#roundType").val();

    $("#panel-1").show();
    $("a[id^='button-']").click(function(){
        var $button = $(this);
        var id = $button.attr("id");
        $("div[id^='panel']").hide();
        $("#panel-"+id.split("-")[1]).show();

        $("a[id^='button-']").removeClass("navbar-button-selected");
        $button.addClass("navbar-button-selected");
    })


    $("a[id^='enter-']").click(function () {
        var id = $(this).attr("id");
        $("#button-"+id.split("-")[1]).trigger("click");
    });

    $("#homePage_intoMarket").click(function(){
        $("#button-2").trigger("click");
    });


 /*   $("#testNext").click(function(){
        isNext();
    })*/
    setInterval(isNext, 1000);
    function isNext() {
        $.getJSON(base + "/iBase/isCampaignNext",
            {
                campaignId:campaignId,
                campaignDate: campaignDate,
                partyType: "TERM"
            },
            function (data) {
                var isNext = data.map.isNext;
                if (isNext == true) {
                    location.reload();
                } else {
                    $("#unFinishedNum").text(data.map.unFinishedNum);
                    //$.AMUI.progress.set(data.schedule);
                }
            }
        );

    }


    $("#endCampaignDate").click(function () {
        if(confirm("是否结束当前回合的操作？")) {
            var $endCampaignDate = $(this);
            $.post(base + "/iBase/companyNext.do",
            {
                campaignId: campaignId,
                companyId: companyId,
                partyType: "TERM"
            },
            function (data) {
                if(data.status == 1) {
                    $endCampaignDate.hide();
                }
            }
        );
        }
    });

    //按钮点击过一次后变为无效状态  防止重复点击
    $(document).delegate("button[class*='am-btn-once']","click",function(){
        $(this).attr("disabled",true).addClass("am-disabled");
    });

    //------------------------财务部分-----------------------

    $("select[id^='usuriousLoan_']").change(function(){
        var $choice = $(this);
        var type = "LOAN_USURIOUS";
        loan($choice, type);
    });
    $("select[id^='shortTermLoan_']").change(function(){
        var $choice = $(this);
        var type = "LOAN_SHORT_TERM";
        loan($choice, type);
    });
    $("select[id^='longTermLoan_']").change(function(){
        var $choice = $(this);
        var type = "LOAN_LONG_TERM";
        loan($choice, type);
    });

    //贷款
    function loan($choice, type){
        var array = $choice.val().split("#");
        var choiceId = array[0];
        var value = array[1];
        $.getJSON(base + "/manufacturing/loan.do",
            {
                campaignId: campaignId,
                companyId: companyId,
                choiceId: choiceId,
                value: value,
                type: type
            },
            function(data){
                if(data.status == 1) {
                    $choice.parent().text("已贷款");
                    update(data.map.newReport);
                    refreshLoan();
                } else {
                    alert(data.map.message);
                    $choice.attr("disabled",false).removeClass("am-disabled");
                }
            }
        );
    }

    function refreshLoan(){
        $.getJSON(base + "/manufacturing/loanList.do",
            {
                campaignId: campaignId,
                companyId: companyId
            },
            function(data){
                if(data.status == 1) {
                    var tbody = $("#loanListTbody");
                    tbody.html("");
                    for(var i in data.map.loanList) {
                        var loan = data.map.loanList[i];
                        var tr = $("<tr></tr>");
                        tr.appendTo(tbody);
                        tr.append($("<td>" + loan.loanTypeLable + "</td>"));
                        tr.append($("<td>" + loan.money + "</td>"));
                        tr.append($("<td>" + loan.needRepayCycle + "</td>"));
                        tbody.append(tr);
                    }
                }
            }
        );
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
    var longTermLoan = newReport.longTermLoan;
    var shortTermLoan = newReport.shortTermLoan;
    var usuriousLoan = newReport.usuriousLoan;

    if(P1Amount != null){
        $("#productAmount_P1").text(P1Amount);
        $("#homePage_productAmount_P1").text(P1Amount);
    }
    if(P2Amount != null){
        $("#productAmount_P2").text(P2Amount);
        $("#homePage_productAmount_P2").text(P1Amount);
    }
    if(P3Amount != null){
        $("#productAmount_P3").text(P3Amount);
        $("#homePage_productAmount_P3").text(P3Amount);
    }
    if(P4Amount != null){
        $("#productAmount_P4").text(P4Amount);
        $("#homePage_productAmount_P4").text(P4Amount);
    }
    if(R1Amount != null){
        $("#materialAmount_R1").text(R1Amount);
        $("#homePage_materialAmount_R1").text(R1Amount);
    }
    if(R2Amount != null){
        $("#materialAmount_R2").text(R2Amount);
        $("#homePage_materialAmount_R2").text(R2Amount);
    }
    if(R3Amount != null){
        $("#materialAmount_R3").text(R3Amount);
        $("#homePage_materialAmount_R3").text(R3Amount);
    }
    if(R4Amount != null){
        $("#materialAmount_R4").text(R4Amount);
        $("#homePage_materialAmount_R4").text(R4Amount);
    }
    if(companyCash != null){
        $("#companyCash").text(companyCash);
        $("#homePage_companyCash").text(companyCash);
    }
    if(longTermLoan !=null){
        $("#longTermLoan").text(longTermLoan);
        $("#homePage_longTermLoan").text(longTermLoan);
    }
    if(shortTermLoan !=null){
        $("#shortTermLoan").text(shortTermLoan);
        $("#homePage_shortTermLoan").text(shortTermLoan);
    }
    if(usuriousLoan !=null){
        $("#usuriousLoan").text(usuriousLoan);
        $("#homePage_usuriousLoan").text(usuriousLoan);
    }
}