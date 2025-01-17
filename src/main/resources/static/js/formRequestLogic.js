var logsMap = new Map();
var logsToRemoveMap = new Map();

function addLogs(response){
    let addedLogs = new Map();
    response.forEach(item => {
        let area = JSON.parse(item.area);
        let logs = JSON.parse(item.logs);

        let existingArea = Array.from(logsMap.keys()).find(key => key.name === area.name);

        if (existingArea) {
            let existingLogs = logsMap.get(existingArea);
            logsMap.set(existingArea, [...existingLogs, ...logs]);
        } else {
            logsMap.set(area, logs);
        }
        addedLogs.set(area, logs);
    });

    console.log('Updated logsMap:', logsMap);
    return addedLogs;
}

$("#submitLogs").click(function(e) {
    e.preventDefault();

    const serializedLogs = {};

    // Remove unwanted logs from logsMap
    logsMap.forEach((logs, area) => {
        if (logsToRemoveMap.has(area)) {
            const logsToRemove = logsToRemoveMap.get(area);
            logs = logs.filter(log => !logsToRemove.some(logToRemove => JSON.stringify(log) === JSON.stringify(logToRemove)));
        }
        logs = logs.filter(log => log.teleport == false);
        serializedLogs[JSON.stringify(area)] = logs;
    });

    $.ajax({
        url: "/optimalRoute/calculate",
        type: "POST",
        dataType: 'json',
        data: {
            "logs" : JSON.stringify(serializedLogs)
        },
        success: function(result) {
            formAreaToDefault();
            if (jQuery.isEmptyObject(result)) return;

            logsMap.clear();
            for(id in result){
                let area = JSON.parse(result[id].area);
                let logs = JSON.parse(result[id].logs);
                logsMap.set(area, logs);
            }

            console.log('Calculated logsMap:', logsMap);
            initLogsPanel(logsMap);
            drawCanvas(logsMap);
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

function formAreaToDefault() {
    $("#inputLogs").val('');
    resize(document.getElementById("inputLogs"));
}


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

function resize(textArea) {
    textArea.style.height = '';
    textArea.style.height = textArea.scrollHeight + 'px';
}
