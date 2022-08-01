var responseData;
$("#submitLogs").click(function(e){
  e.preventDefault();
  $.ajax({
    url: "/optimalRoute",
    type: "POST",
    dataType: 'json',
    data: {"logs" : $("#inputLogs").val()},
    success: function(result){
     var commonList = initLogs(result); //logs without teleports
     formAreaToDefault();
     $('.logsWrapperPanel').show();
     initLogsPanel(commonList); //full response list with teleports, always temporary variable
     repaint(canvasPanel);
     createCanvas(result);
  }});
});

function initLogs(result){ //init response data ignoring teleports
    responseData = [];
    commonList = [];
    for(listId in result){
        let list = JSON.parse(result[listId].logs);
        for(linkedListId in list){
            let linkedList = list[linkedListId];
            for (logId in linkedList){
                if(linkedList[logId].teleport == false)
                    responseData.push(linkedList[logId]);
                commonList.push(linkedList[logId]);
            }
        }
    }
    return commonList;
}

function formAreaToDefault(){
    $("#inputLogs").val('');
    resize(document.getElementById("inputLogs"));
    $('.logsAdjust').attr('disabled', false);
    $('#submitLogs').attr('disabled', true);
    $('.logsAdd').attr('disabled', true);
}

$('.logsReset').click(function(e){
    e.preventDefault();
    responseData = [];
    repaint(document.getElementById('logsPanelId'));
    $('.logsWrapperPanel').hide();
    repaint(canvasPanel);
    formAreaToDefault();
});

$('.logsAdd').click(function(e){
    $.ajax({
        url: "/optimalRoute",
        type: "POST",
        dataType: 'json',
        data: {"inputLogs" : $("#inputLogs").val()},
        success: function(response){
         formAreaToDefault();
         if(jQuery.isEmptyObject(response)) return;
         let result = JSON.parse(JSON.stringify(response));
         for(logId in result){
             responseData.push(result[logId]);
         }
         addToLogsPanel(result);
      }});
});

$('.logsAdjust').click(function(e){
    $.ajax({
        url: "/optimalRoute",
        type: "POST",
        dataType: 'json',
        data: {"editedLogs" : JSON.stringify(responseData)},
        success: function(result){
         var commonList = initLogs(result);
         initLogsPanel(commonList);
         repaint(canvasPanel);
         createCanvas(result);
      }});
});

$('.chatLogs').on('input', function(e){
    resize(this);
    if(/^\s*$/.test($(this).val())) return;
    if(!jQuery.isEmptyObject(responseData))
        $('.logsAdd').attr('disabled', false);
    $('#submitLogs').attr('disabled', false);
});

$('.chatLogs').on('keydown', function(e){
  var ctrl = e.ctrlKey ? e.ctrlKey : ((e.keyCode === 17) ? true : false);
  if (e.keyCode === 86 && ctrl || e.keyCode === 67 && ctrl || e.keyCode === 88 && ctrl) {
    return true;
  } else {
    return false;
  }
});

function resize(textArea){
 textArea.style.height = '';
 textArea.style.height = textArea.scrollHeight +'px';
}

/*
var isTooltipEnabled = false;
$(".tooltipCheckBox").click(function(e){
isTooltipEnabled = !isTooltipEnabled;
revalidateCanvas();
});
*/