$(function () {

    var base = getBaseUrl();

    var campaignId = $("#campaignId").val();
    var campaignDate = $("#campaignDate").val();
    var companyId = $("#companyId").val();
    var companyTermId = $("#companyTermId").val();
    var roundType = $("#roundType").val();

    $("div[id^='line_']").each(function(){
        var lineId = $(this).attr("id").split("_")[1];

        $.getJSON(getBaseUrl() + "/manufacturing/currentLineState.do",
            {
                lineId:lineId
            },
            function (data) {
                if(data.status==1){
                    var line = data.line;

                    refreshProduceLine(line);
                    initProduceLineButton(line);
                }
            }
        );
    });

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

    //变更生产线类型
    $("#produceLinesDiv").delegate("select[id^='lineType_']","change",function(){
        var $lineType = $(this);
        var lineId = $lineType.attr("id").split("_")[1];
        if($lineType.val()=='FLEXBILITY'){
            $("#produceType_"+lineId).find("option[value='-1']").attr("selected",true);
            $("#produceType_"+lineId).hide();
        } else {
            $("#produceType_"+lineId).show();
        }
    })

    //投资新生产线
    $("#produceLinesDiv").delegate("button[id^='build_']","click",function(){
        var $build = $(this);
        var partId = $build.attr("id").split("_")[1];
        var lineType = $("#lineType_" + partId).val();
        var produceType = $("#produceType_" + partId).val();

        if(lineType == 'FLEXBILITY'){
            produceType = '';
        }

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
                        refreshProduceLine(data.line);
                        updateProduceLineButton(data.line);
                    }
                });
        }
    });

    //继续投资生产线
    $("#produceLinesDiv").delegate("button[id^='continueBuild_']","click",function(){
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
                    refreshProduceLine(data.line);
                    updateProduceLineButton(data.line);
                }
            });
    });

    //生产产品
    $("#produceLinesDiv").delegate("button[id^='produce_']","click",function(){
        var $produce = $(this);
        var produceLineId = $produce.attr("id").split("_")[1];

        var produceType;

        var lineType = $("#lineType_"+produceLineId).val();
        if(lineType=="FLEXBILITY"){
            produceType = $("#produceType_" + produceLineId).val();
            alert(produceType);
        } else {
            produceType = $("#produceType_" + produceLineId).text();
        }

        if(produceType==-1) {//预防柔性生产线没有选择产品类型的情况
            alert("请选择生产类型");
        } else {
            //开始生产
            $.getJSON(base + "/manufacturing/produce.do",
                {
                    companyTermId: companyTermId,
                    produceLineId: produceLineId,
                    produceType: produceType
                },
                function(data){
                    //建造结果
                    var status = data.status;
                    if(status == 1) {
                        $produce.parent().text("生产中");
                        update(data.newReport);
                        refreshProduceLine(data.line);
                        updateProduceLineButton(data.line);
                    } else {
                        alert(data.message);
                    }
                });
        }


    })

})


/**
 *
 * @param line
 */
function refreshProduceLine(line) {
    var lineId = line.id;
    var lineStatus = line.status;

    var lineDiv = $("<div></div>").attr("id","line_"+lineId);
    var lineInnerDiv1 = $("<div>"+line.name+"("+lineStatus+")"+"</div>");

    //生产线类型
    var produceLineType = line.produceLineType;
    var lineInnerDiv2 = $("<div></div>");
    if(produceLineType==null||produceLineType=='') {
        var $select = $("<select></select>").attr("id", "lineType_"+lineId);
        $("<option></option>").text("手工").val("MANUAL").appendTo($select);
        $("<option></option>").text("半自动").val("HALF_AUTOMATIC").appendTo($select);
        $("<option></option>").text("自动").val("AUTOMATIC").appendTo($select);
        $("<option></option>").text("柔性").val("FLEXBILITY").appendTo($select);

        lineInnerDiv2.append("生产线类型：").append($select);
    } else {
        var produceLineTypeLabel;
        if(produceLineType == "MANUAL") {
            produceLineTypeLabel = "手工";
        } else if (produceLineType == "HALF_AUTOMATIC") {
            produceLineTypeLabel = "半自动";
        } else if (produceLineType == "AUTOMATIC") {
            produceLineTypeLabel = "自动";
        } else if (produceLineType == "FLEXBILITY") {
            produceLineTypeLabel = "柔性";
        } else {
            produceLineTypeLabel = "无";
        }
        lineInnerDiv2.append("生产线类型：" + produceLineTypeLabel);
    }
    if(line.status == 'BUILDING'){
        lineInnerDiv2.append(" 剩余建设周期:"+line.lineBuildNeedCycle);
    }

    //生产产品类型
    var produceType = line.produceType;
    var lineInnerDiv3 = $("<div></div>");
    if(produceType == null || produceType == '') {

        if((lineStatus=='UN_BUILD'&&produceLineType!='FLEXBILITY') || (lineStatus=='FREE'&&produceLineType=='FLEXBILITY')){
            var $select = $("<select></select>").attr("id", "produceType_"+lineId);
            $("<option></option>").text("P1").val("P1").appendTo($select);
            $("<option></option>").text("P2").val("P2").appendTo($select);
            $("<option></option>").text("P3").val("P3").appendTo($select);
            $("<option></option>").text("P4").val("P4").appendTo($select);
            lineInnerDiv3.append("生产类型：").append($select);
        } else {
            lineInnerDiv3.append("生产类型：" + line.produceType);
        }
    } else {
        lineInnerDiv3.append("生产类型：" + line.produceType);
    }
    if(line.status == 'PRODUCING') {
        lineInnerDiv2.append(" 剩余生产周期:"+line.produceNeedCycle);
    }

    lineDiv.append(lineInnerDiv1).append(lineInnerDiv2).append(lineInnerDiv3);
    $("#line_" + lineId).replaceWith(lineDiv);
}

function initProduceLineButton(line) {
    var lineId = line.id;
    var lineStatus = line.status;
    //button
    var lineButtonDiv = $("<div></div>");
    if(lineStatus=='UN_BUILD'){
        $("<button class='am-btn am-btn-secondary'>投资新生产线</button>").attr("id","build_"+line.id).appendTo(lineButtonDiv);
    } else if (lineStatus == 'BUILDING'){
        $("<button class='am-btn am-btn-secondary'>再投生产线</button>").attr("id","continueBuild_"+line.id).appendTo(lineButtonDiv);
    } else if (lineStatus == 'FREE'){
        $("<button class='am-btn am-btn-secondary'>生产</button>").attr("id","produce_"+line.id).appendTo(lineButtonDiv);
        $("<button class='am-btn am-btn-secondary'>改造</button>").attr("id","rebuild_"+line.id).appendTo(lineButtonDiv);
    } else if (lineStatus == 'PRODUCING'){

    } else {

    }
    $("#line_" + lineId).append(lineButtonDiv)
}

function updateProduceLineButton(line) {

    var lineId = line.id;
    var lineStatus = line.status;
    //button
    var lineButtonDiv = $("<div></div>");
    if(lineStatus=='UN_BUILD'){
        $("<button class='am-btn am-btn-secondary'>投资新生产线</button>").attr("id","build_"+line.id).appendTo(lineButtonDiv);
    } else if (lineStatus == 'BUILDING'){
    } else if (lineStatus == 'FREE'){
        $("<button class='am-btn am-btn-secondary'>生产</button>").attr("id","produce_"+line.id).appendTo(lineButtonDiv);
    } else if (lineStatus == 'PRODUCING'){

    } else {

    }
    $("#line_" + lineId).append(lineButtonDiv)
}


function getBaseUrl() {
    return "";
}