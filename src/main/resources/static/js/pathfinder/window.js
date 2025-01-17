function formAreaToDefault() {
    $("#inputLogs").val('');
    resize(document.getElementById("inputLogs"));
}

function resize(textArea) {
    textArea.style.height = '';
    textArea.style.height = textArea.scrollHeight + 'px';
}