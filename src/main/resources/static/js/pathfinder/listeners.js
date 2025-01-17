$("#submitLogs").click(function(e) {
    e.preventDefault();
    $.ajax({
        url: "/optimalRoute/calculate",
        type: "POST",
        dataType: 'json',
        data: {
            "logs" : JSON.stringify(serializeLogsMap())
        },
        success: function(result) {
            formAreaToDefault();
            if (jQuery.isEmptyObject(result)) return;

            logsMap.clear();
            initializeLogsMap(result)

            console.log('Calculated logsMap:', logsMap);
            initLogsPanel(logsMap);
            drawCanvas(logsMap);
        }
    });
});

$('.logsAdd').click(function(e) {
    $.ajax({
        url: "/optimalRoute/parse",
        type: "POST",
        dataType: 'json',
        data: {
            "inputLogs": $("#inputLogs").val()
        },
        success: function(response) {
            formAreaToDefault();
            if (jQuery.isEmptyObject(response)) return;
            $('.logsWrapperPanel').show();
            let addedLogs = addLogs(response);
            addToLogsPanel(addedLogs);
        }
    });
});

$('.logsReset').click(function(e) {
    e.preventDefault();
    logsMap = {};
    repaint(document.getElementById('logsPanelId'));
    $('.logsWrapperPanel').hide();
    repaint(canvasPanel);
    formAreaToDefault();
});


$('.chatLogs').on('input', function(e) {
    resize(this);
    if (/^\s*$/.test($(this).val())) return;
});

$('.chatLogs').on('keydown', function(e) {
    var ctrl = e.ctrlKey ? e.ctrlKey : ((e.keyCode === 17) ? true : false);
    if (e.keyCode === 86 && ctrl || e.keyCode === 67 && ctrl || e.keyCode === 88 && ctrl) {
        return true;
    } else {
        return false;
    }
});
