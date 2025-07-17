$("#submitLogs").click(function(e) {
    e.preventDefault();
    removeLogsFromAllLogs();
    $.ajax({
        url: "/optimalRoute/calculate",
        type: "POST",
        dataType: 'json',
        data: {
            "logs" : JSON.stringify(allLogs)
        },
        success: function(response) {
            repaintTextArea();
            if (jQuery.isEmptyObject(response)) return;
            updateLogsPanel(response, true);
            updateMapCanvas(response);
        }
    });
});

function parseLogs(text){
    $.ajax({
        url: "/optimalRoute/parse",
        type: "POST",
        dataType: 'json',
        data: {
            "inputLogs": text
        },
        success: function(response) {
            repaintTextArea();
            if (jQuery.isEmptyObject(response)) return;
            $('.logsWrapperPanel').show();
            updateLogsPanel(response, false);
        }
    });
}

$('.logsReset').click(function(e) {
    e.preventDefault();
    revalidateLogsPanel();
    $('.logsWrapperPanel').hide();
    repaint(canvasPanel);
    repaintTextArea();
});


$('.chatLogs').on('input', function(e) {
    let text = $(this).val();
    resizeTextArea(this);
    if (/^\s*$/.test(text)) {
        repaintTextArea();
        return;
    }
    parseLogs(text);
});

$('.chatLogs').on('keydown', function(e) {
    var ctrl = e.ctrlKey ? e.ctrlKey : ((e.keyCode === 17) ? true : false);
    if (e.keyCode === 86 && ctrl || e.keyCode === 67 && ctrl || e.keyCode === 88 && ctrl) {
        return true;
    } else {
        return false;
    }
});

function repaintTextArea() {
    $("#inputLogs").val('');
    resizeTextArea(document.getElementById("inputLogs"));
}

function resizeTextArea(textArea) {
    textArea.style.height = '';
    textArea.style.height = textArea.scrollHeight + 'px';
}
