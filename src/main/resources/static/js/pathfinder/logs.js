function initLogsPanel(result) {
    const ul = document.getElementById("logsPanelId");
    repaint(ul);
    addSpans(result, false).forEach((span, index) => {
        span.innerHTML = `${index + 1}. ${span.innerHTML}`;
    });
}

function addToLogsPanel(currentLogs) {
    addSpans(currentLogs, true).forEach(span => {
        span.innerHTML = `NEW ${span.innerHTML}`;
    });
}

function addSpans(logsMap, isPended) {
    var ul = document.getElementById("logsPanelId");
    var spanArr = [];
    var liBackgroundColor = isPended == true ? "yellow" : "#2ECC40";

    logsMap.forEach((logs, area) => {
        let titleLi = document.createElement("li");
        titleLi.setAttribute('class', 'logsTitleLi');
        titleLi.style.background = "#D5DBDB";
        let titleSpan = document.createElement("span");
        titleSpan.innerHTML = area.name;
        titleSpan.setAttribute('class', 'logsTitleSpan');
        titleLi.appendChild(titleSpan);
        ul.appendChild(titleLi);

        logs.forEach(log => {
            let li = document.createElement("li");
            li.setAttribute('class', 'logsLi');
            li.style.background = liBackgroundColor;
            li.setAttribute('defaultColor', liBackgroundColor);
            let span = document.createElement("span");
            span.innerHTML = getLogInfoText(log);
            span.setAttribute('class', 'logsSpan');
            if(log.teleport == false)
                li.appendChild(addCheckBox(area, log, li));
            li.appendChild(span);
            ul.appendChild(li);
            spanArr.push(span);
        });
    });

    return spanArr;
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
