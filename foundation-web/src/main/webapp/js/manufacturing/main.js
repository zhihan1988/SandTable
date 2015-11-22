$(function () {
    var $progress = $.AMUI.progress;
    $progress.configure({ showSpinner: false });

    var base = getBaseUrl();

    var campaignId = $("#campaignId").val();
    var campaignDate = $("#campaignDate").val();
    var companyId = $("#companyId").val();
    var companyTermId = $("#companyTermId").val();
    var roundType = $("#roundType").val();

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

    //竞单环节
    if(campaignDate%4==1) {
        $("#button-2").trigger("click");
    }

    setInterval(isNext, 1000);
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
                    $.AMUI.progress.set(data.schedule);
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
        }
    });

    //------------------------财务部分-----------------------

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

    //贷款
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

    if(P1Amount != null){ $("#productAmount_P1").text(P1Amount);}
    if(P2Amount != null){ $("#productAmount_P2").text(P2Amount); }
    if(P3Amount != null){ $("#productAmount_P3").text(P3Amount); }
    if(P4Amount != null){ $("#productAmount_P4").text(P4Amount); }
    if(R1Amount != null){ $("#materialAmount_R1").text(R1Amount); }
    if(R2Amount != null){ $("#materialAmount_R2").text(R2Amount); }
    if(R3Amount != null){ $("#materialAmount_R3").text(R3Amount); }
    if(R4Amount != null){ $("#materialAmount_R4").text(R4Amount); }
    if(companyCash != null){ $("#companyCash").text(companyCash); }
    if(longTermLoan !=null){ $("#longTermLoan").text(longTermLoan)}
    if(shortTermLoan !=null){ $("#shortTermLoan").text(shortTermLoan)}
    if(usuriousLoan !=null){ $("#usuriousLoan").text(usuriousLoan)}
}

function getBaseUrl() {
    return "";
}