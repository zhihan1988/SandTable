$(function () {

    $("div[id^='line_']").each(function(){
        var lineId = $(this).attr("id").split("_")[1];

        $.getJSON(base + "/manufacturing/currentLineState.do",
            {
                campaignId:campaignId,
                companyId:companyId,
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
        var lineType = $lineType.val();
        var $produceTypeDiv = $("#produceTypeSpan_"+lineId);
        $produceTypeDiv.html("");

        if(lineType=='MANUAL' || lineType=='FLEXBILITY'){
            var lineProduceTypeSpan = $("<span class='line-important'>所有许可产品</span>");
            var lineProduceTypeHiddenInput = $("<input type='hidden'/>").attr("id", "produceType_" + lineId).val("");
            $produceTypeDiv.append("生产类型：").append(lineProduceTypeSpan).append(lineProduceTypeHiddenInput);
        } else {
            var $select = $("<select></select>").attr("id", "produceType_"+lineId);
            $("<option></option>").text("P1").val("P1").appendTo($select);
            $("<option></option>").text("P2").val("P2").appendTo($select);
            $("<option></option>").text("P3").val("P3").appendTo($select);
            $("<option></option>").text("P4").val("P4").appendTo($select);
            $produceTypeDiv.append("生产类型：").append($select);
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
        var produceType = $("#produceType_" + produceLineId).val();

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
                    //enableButton($produce);
                }
            });

    });

    $("#produceLinesDiv").delegate("button[id^='rebuild_']","click",function(){
        var $button = $(this);
        var lineId = $button.attr("id").split("_")[1];
        var $produceTypeDiv = $("#produceTypeSpan_"+lineId);
        $produceTypeDiv.html("");
        var $select = $("<select></select>").attr("id", "produceType_"+lineId);
        $("<option></option>").text("P1").val("P1").appendTo($select);
        $("<option></option>").text("P2").val("P2").appendTo($select);
        $("<option></option>").text("P3").val("P3").appendTo($select);
        $("<option></option>").text("P4").val("P4").appendTo($select);
        $produceTypeDiv.append("生产类型：").append($select);

        var $lineButtonDiv = $("#lineButtonDiv_" + lineId);
        $lineButtonDiv.html("");
        $("<button class='am-btn am-btn-secondary'>确认</button>").attr("id","confirmRebuild_"+lineId).appendTo($lineButtonDiv);
        $("<span class='button-padding'></span>").appendTo($lineButtonDiv);
        $("<button class='am-btn am-btn-secondary'>取消</button>").attr("id","cancelRebuild_"+lineId).appendTo($lineButtonDiv);
    });

    $("#produceLinesDiv").delegate("button[id^='confirmRebuild_']","click",function(){
        var lineId = $(this).attr("id").split("_")[1];
        var produceType = $("#produceType_" + lineId).val();
        $.getJSON(base + "/manufacturing/reBuildProduceLine.do",
            {
                companyTermId:companyTermId,
                lineId:lineId,
                produceType:produceType
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
    $("#produceLinesDiv").delegate("button[id^='cancelRebuild_']","click",function(){
        var lineId = $(this).attr("id").split("_")[1];
        $.getJSON(base + "/manufacturing/currentLineState.do",
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

})


/**
 *
 * @param line
 */
function refreshProduceLine(line) {
    var lineId = line.id;
    var lineStatus = line.status;

    var lineDiv = $("<div></div>").attr("id","line_"+lineId);

    var lineStatusSpan = $("<span></span>");
    if (lineStatus == 'UN_BUILD') {
        lineStatusSpan.text("未建造");
    } else if (lineStatus == 'BUILDING') {
        lineStatusSpan.text("建造中");
    } else if (lineStatus == 'BUILT') {
        lineStatusSpan.text("建造未完成");
    } else if (lineStatus == 'FREE') {
        lineStatusSpan.text("空闲");
    } else if (lineStatus == 'PRODUCING') {
        lineStatusSpan.text("生产中");
    } else if(lineStatus == 'REBUILDING') {
        lineStatusSpan.text("转产中");
    } else {
        lineStatusSpan.text("状态异常");
    }
    if(line.status == 'BUILDING'|| line.status == 'BUILT' || line.status == 'REBUILDING'){
        lineStatusSpan.append(" 剩余建设周期:").append("<span class='line-important'>"+line.lineBuildNeedCycle+"</span>");
    }

    if(line.status == 'PRODUCING') {
        lineStatusSpan.append(" 剩余生产周期:").append("<span class='line-important'>"+line.produceNeedCycle+"</span>");
    }


    var lineInnerDiv1 = $("<div class='line-innerDiv1 line-important'></div>")
        .append(line.name).append("：").append(lineStatusSpan);

    var lineInnerDiv2 = $("<div class='line-innerDiv2'></div>");
    //生产线类型
    var produceLineType = line.produceLineType;
    var lineInnerSpan2 = $("<span class='line-innerSpan2'></span>");
    if(produceLineType==null||produceLineType=='') {
        var $select = $("<select></select>").attr("id", "lineType_"+lineId);
        $("<option></option>").text("手工").val("MANUAL").appendTo($select);
        $("<option></option>").text("半自动").val("HALF_AUTOMATIC").appendTo($select);
        $("<option></option>").text("自动").val("AUTOMATIC").appendTo($select);
        $("<option></option>").text("柔性").val("FLEXBILITY").appendTo($select);

        lineInnerSpan2.append("生产线类型：").append($select);
    } else {
        var produceLineTypeSpan = $("<span class='line-important'></span>");
        if(produceLineType == "MANUAL") {
            produceLineTypeSpan.text("手工");
        } else if (produceLineType == "HALF_AUTOMATIC") {
            produceLineTypeSpan.text("半自动");
        } else if (produceLineType == "AUTOMATIC") {
            produceLineTypeSpan.text("自动");
        } else if (produceLineType == "FLEXBILITY") {
            produceLineTypeSpan.text("柔性");
        } else {
            produceLineTypeSpan.text("无");
        }
        lineInnerSpan2.append("生产线类型：").append(produceLineTypeSpan);
        var produceLineTypeHiddenInput = $("<input type='hidden'/>").attr("id", "lineType_" + lineId).val(produceLineType);
        lineInnerSpan2.append(produceLineTypeHiddenInput);
    }

    //生产产品类型
    var produceType = line.produceType;
    var lineInnerSpan3 = $("<span class='line-innerSpan3'></span>").attr("id", "produceTypeSpan_"+lineId);

    if((lineStatus=='UN_BUILD'&&(produceLineType=='HALF_AUTOMATIC'||produceLineType=='AUTOMATIC'))
        || (lineStatus=='FREE'&&(produceLineType=='MANUAL'||produceLineType=='FLEXBILITY'))){

        var $select = $("<select></select>").attr("id", "produceType_"+lineId);
        $("<option></option>").text("P1").val("P1").appendTo($select);
        $("<option></option>").text("P2").val("P2").appendTo($select);
        $("<option></option>").text("P3").val("P3").appendTo($select);
        $("<option></option>").text("P4").val("P4").appendTo($select);
        lineInnerSpan3.append("生产类型：").append($select);
    } else {
        //生产产品类型
        var lineProduceTypeSpan;
        var lineProduceTypeHiddenInput;
        if(produceType == null || produceType == '') {
            lineProduceTypeSpan = $("<span class='line-important'>所有许可产品</span>");
            lineProduceTypeHiddenInput = $("<input type='hidden'/>").attr("id", "produceType_" + lineId).val("");
        } else {
            lineProduceTypeSpan = $("<span class='line-important'>" + produceType + "</span>");
            lineProduceTypeHiddenInput = $("<input type='hidden'/>").attr("id", "produceType_" + lineId).val(produceType);
        }

        lineInnerSpan3.append("生产类型：").append(lineProduceTypeSpan).append(lineProduceTypeHiddenInput);
    }

    lineInnerDiv2.append(lineInnerSpan2).append("<span style='margin: 10px;'></span>").append(lineInnerSpan3);
    lineDiv.append(lineInnerDiv1).append(lineInnerDiv2);
    $("#line_" + lineId).replaceWith(lineDiv);
}

function initProduceLineButton(line) {
    var lineId = line.id;
    var lineStatus = line.status;
    var lineType = line.produceLineType;
    //button
    var lineButtonDiv = $("<div></div>").attr("id","lineButtonDiv_"+lineId);
    if(lineStatus=='UN_BUILD'){
        $("<button class='am-btn am-btn-secondary am-btn-once'>投资新生产线</button>").attr("id","build_"+line.id).appendTo(lineButtonDiv);
    } else if (lineStatus == 'BUILDING'){

    } else if(lineStatus == 'BUILT'){
        $("<button class='am-btn am-btn-secondary am-btn-once'>再投生产线</button>").attr("id","continueBuild_"+line.id).appendTo(lineButtonDiv);
    } else if (lineStatus == 'FREE'){
        $("<button class='am-btn am-btn-secondary am-btn-once'>生产</button>").attr("id","produce_"+line.id).appendTo(lineButtonDiv);
        if(lineType=='HALF_AUTOMATIC' || lineType=='AUTOMATIC'){
            $("<span class='button-padding'></span>").appendTo(lineButtonDiv);
            $("<button class='am-btn am-btn-secondary am-btn-once'>转产</button>").attr("id","rebuild_"+line.id).appendTo(lineButtonDiv);
        }
    } else if (lineStatus == 'PRODUCING'){

    } else if(lineStatus == 'REBUILDING'){

    }
    $("#line_" + lineId).append(lineButtonDiv)
}

function updateProduceLineButton(line) {

    var lineId = line.id;
    var lineStatus = line.status;
    //button
    var lineButtonDiv = $("<div></div>").attr("id","lineButtonDiv_"+lineId);
    if(lineStatus=='UN_BUILD'){
        $("<button class='am-btn am-btn-secondary am-btn-once'>投资新生产线</button>").attr("id","build_"+line.id).appendTo(lineButtonDiv);
    } else if (lineStatus == 'BUILDING'){

    } else if(lineStatus == 'BUILT'){
        $("<button class='am-btn am-btn-secondary am-btn-once'>再投生产线</button>").attr("id","continueBuild_"+line.id).appendTo(lineButtonDiv);
    } else if (lineStatus == 'FREE'){
        $("<button class='am-btn am-btn-secondary am-btn-once'>生产</button>").attr("id","produce_"+line.id).appendTo(lineButtonDiv);
    } else if (lineStatus == 'PRODUCING'){

    } else if(lineStatus == 'REBUILDING'){

    }
    $("#line_" + lineId).append(lineButtonDiv)
}