var allLogs = {};
var logsToRemove = {};

const ul = document.getElementById("logsPanelId");

function revalidateLogsPanel(){
    allLogs = {};
    logsToRemove = {};
    repaint(ul);
}

function updateLogsPanel(response, shouldRepaint) {
    let liBackgroundColor;
    if(shouldRepaint == true){
        revalidateLogsPanel();
        liBackgroundColor = "#87A96B";
    } else{
        liBackgroundColor = "yellow";
    }

    response.forEach(data => {
        const areaName = data.areaName;
        const logs = JSON.parse(data.logs);

        if (!allLogs[areaName]) {
            allLogs[areaName] = [];
            createList(ul, areaName);
        }

        let innerUl = document.getElementById(areaName);

        logs.forEach(log => {
            if(log.teleport == false)
                allLogs[areaName].push(log);
        });

       logs.forEach((log, index) => {
            const logText = `${log.name} (${log.x}, ${log.y})`;
            const spanText = shouldRepaint == true? `NEW ${logText}` : `${index + 1}. ${logText}`;
            addListChild(innerUl, log, spanText, liBackgroundColor, areaName);
       });
    });
    console.log(allLogs);
}

function createList(ul, areaName) {
    if (document.getElementById(areaName)) return;

    let titleLi = document.createElement("li");
    titleLi.setAttribute("class", "logsTitleLi");
    titleLi.style.background = "#D5DBDB";

    let titleSpan = document.createElement("span");
    titleSpan.innerHTML = areaName;
    titleSpan.setAttribute("class", "logsTitleSpan");

    titleLi.appendChild(titleSpan);
    titleLi.setAttribute("id", areaName);
    ul.appendChild(titleLi);
}

function addListChild(ul, log, spanText, liBackgroundColor, areaName) {
    let li = document.createElement("li");
    li.setAttribute("class", "logsLi");
    li.style.background = liBackgroundColor;
    li.setAttribute("defaultColor", liBackgroundColor);

    let span = document.createElement("span");
    span.innerHTML = spanText;
    span.setAttribute("class", "logsSpan");

    if (!log.teleport) {
        li.appendChild(addCheckBox(areaName, log, li));
    }

    li.appendChild(span);
    ul.appendChild(li);
}

function addCheckBox(area, log, li) {
    var button = document.createElement('input');
    button.setAttribute('type', 'checkbox');
    button.setAttribute('checked', true);
    button.setAttribute('class', 'liCheck');
    button.addEventListener('click', (function(e) {
        let shouldRemove = toggleLog(area, log);
        li.style.background = shouldRemove ? "grey" : li.getAttribute('defaultColor');
    }));
    return button;
}

function toggleLog(area, log) {
    if (!logsToRemove[area]) {
        logsToRemove[area] = [];
    }

    const logIndex = logsToRemove[area].findIndex(existingLog => JSON.stringify(existingLog) === JSON.stringify(log));
    if (logIndex === -1) {
        logsToRemove[area].push(log);
        return true;
    } else {
        logsToRemove[area].splice(logIndex, 1);
        return false;
    }
}

function removeLogsFromAllLogs() {
    Object.keys(logsToRemove).forEach(areaName => {
        if (allLogs[areaName]) {
            allLogs[areaName] = allLogs[areaName].filter(log => {
                return !logsToRemove[areaName].some(
                    logToRemove => JSON.stringify(log) === JSON.stringify(logToRemove)
                );
            });

            if (allLogs[areaName].length === 0) {
                delete allLogs[areaName];
            }
        }
    });

    logsToRemove = {};
}