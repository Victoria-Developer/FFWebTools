var logsMap = new Map();
var logsToRemoveMap = new Map();

function initializeLogsMap(result) {
    logsMap.clear();
    for (const id in result) {
        const area = JSON.parse(result[id].area);
        const logs = JSON.parse(result[id].logs);
        logsMap.set(area, logs);
    }
    console.log('Initialized logsMap:', logsMap);
}

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

function toggleLog(area, log) {
    let shouldRemove = true;
    if (logsToRemoveMap.has(area)) {
        let logs = logsToRemoveMap.get(area);
        let logIndex = logs.findIndex(existingLog => JSON.stringify(existingLog) === JSON.stringify(log));
        if (logIndex !== -1) {
            logs.splice(logIndex, 1);
            shouldRemove = false;
        } else {
            logs.push(log);
        }
        logsToRemoveMap.set(area, logs);
    } else {
        logsToRemoveMap.set(area, [log]);
    }
    return shouldRemove;
}

function serializeLogsMap() {
    const serializedLogs = {};
    logsMap.forEach((logs, area) => {
        if (logsToRemoveMap.has(area)) {
            const logsToRemove = logsToRemoveMap.get(area);
            logs = logs.filter(log => !logsToRemove.some(logToRemove => JSON.stringify(log) === JSON.stringify(logToRemove)));
        }
        logs = logs.filter(log => log.teleport == false);
        serializedLogs[JSON.stringify(area)] = logs;
    });
    return serializedLogs;
}