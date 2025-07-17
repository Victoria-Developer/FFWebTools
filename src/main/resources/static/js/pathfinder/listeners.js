$("#submitLogs").click(function(e) {
    e.preventDefault();
    removeLogsFromAllLogs();
    $("#statusLabel").text("Calculating path...");
    $.ajax({
        url: "/optimalRoute/calculate",
        type: "POST",
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify(allLogs),
        success: function(response) {
            repaintTextArea();
            $("#statusLabel").text("Path is calculated.");
            if (jQuery.isEmptyObject(response)) return;
            updateLogsPanel(response, true);
            updateMapCanvas(response);
        },
        error: function(e){
            repaintTextArea();
            $("#statusLabel").text("Path calculation error.");
        }
    });
});

function parseLogs(text){
    $("#statusLabel").text("Parsing logs...");
    $.ajax({
        url: "/optimalRoute/parse",
        type: "POST",
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify({ inputLogs: text }),
        success: function(response) {
            repaintTextArea();
            $("#statusLabel").text("Logs are parsed!");
            if (jQuery.isEmptyObject(response)) return;
            $('.logsWrapperPanel').show();
            updateLogsPanel(response, false);
        },
        error: function(e){
            repaintTextArea();
            $("#statusLabel").text("Logs parsing error.");
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
