setInterval(listen, 5000);
function listen(){
    $.getJSON(base + "/iBase/listen",
        {
            campaignId:campaignId,
            companyId: companyId
        },
        function (data) {
            var messageQueue = data.model.messages;
            if(messageQueue && messageQueue!='') {
                for(var i in messageQueue) {
                    var message = messageQueue[i];
                    if(message.type == 'NextTermMessage'){
                        location.reload();
                    } else if(message.type=='ShowUnFinishNumMessage'){
                        $("#unFinishedNum").text(message.message);
                    } else if(message.type=='RefreshChooseOrderMessage'){
                        listOrderForChoose();
                    } else if(message.type=='EndChooseOrderMessage'){
                        endChooseOrder();
                    } else{
                        alert(message.type+"!!!");
                    }
                }
            }
        }
    );

}