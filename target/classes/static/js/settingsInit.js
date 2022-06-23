$(document).ready(function() {
    $("#sortingMethods option").each(function () {
        var isAccessible = $(this).attr("isAccessible");
        $(this).attr("disabled", !JSON.parse(isAccessible));
        });

    $("input[type='checkbox']").each(function () {
    var box = $(this);
    var value = $(this).attr("isParsed");
    value = JSON.parse(value);
    $(this).prop('checked', value);
    $(this).prop('disabled', value);
    });
    });
