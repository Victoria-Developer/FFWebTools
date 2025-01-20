$(document).ready(function() {
    $(".mailSubmit").on('click', function(e) {
        e.preventDefault();
        clearFields();
        $.ajax({
            url: "/contact/message",
            type: "POST",
            dataType: 'json',
            data: {
                "emailFrom": $("#emailFrom").val(),
                "senderName": $("#senderName").val(),
                "message": $("#message").val()
            },
            success: function(response) {
                // show success dialog
            }
        });
    });

    function clearFields() {
        $("#senderName").val('');
        $("#emailFrom").val('');
        $("#message").val('');
    }
});