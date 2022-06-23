function createSpans(logs, isPended){
  var ul = document.getElementById("logsPanelId");
  var spanArr = [];
  var liBackgroundColor = isPended == true? "yellow" : "#2ECC40";
    for(logId in logs){
       let log = logs[logId];
       let li = document.createElement("li");
       li.setAttribute('class', 'logsLi');
       li.style.background = liBackgroundColor;
       li.setAttribute('defaultColor', liBackgroundColor);
       let span = document.createElement("span");
       span.innerHTML = getLogInfoText(log);
       span.setAttribute('class', 'logsSpan');
       if(log.teleport == false)
            li.appendChild(createRemoveRestoreButton(logs, log, li));
       li.appendChild(span);
       ul.appendChild(li);
       spanArr.push(span);
     }
  return spanArr;
}

function addToLogsPanel(currentLogs){
    var spans = createSpans(currentLogs, true);
    for (spanId in spans){
        span = spans[spanId];
        span.innerHTML = 'NEW ' + span.innerHTML;
    }
}
function initLogsPanel(result){
   var ul = document.getElementById("logsPanelId");
   repaint(ul);
   id = 1;
   let spans = createSpans(result, false);
   for (spanId in spans){
       span = spans[spanId];
       span.innerHTML = id + '. ' + span.innerHTML;
       id++;
   }
}

function createRemoveRestoreButton(logs, log, li){
    var button = document.createElement('input');
    button.setAttribute('type', 'checkbox');
    button.setAttribute('checked', true);
    button.setAttribute('isIncluded', true);
    button.setAttribute('class', 'liCheck')
    button.addEventListener('click', (function (e) {
            setIncluded(button, li, log);
          }));
    return button;
}

function setIncluded(button, li, log){
    let isIncluded = !JSON.parse(button.getAttribute('isIncluded'));
    button.setAttribute('isIncluded', isIncluded);
    let liColour, buttonColor;
    let buttonText;
    if(isIncluded == true){
       liColor = li.getAttribute('defaultColor');
       responseData.push(log);
     } else{
       liColor = "grey";
       responseData.splice(responseData.indexOf(log), 1);
     }
     li.style.background = liColor;
}