$(document).ready(function() {
    $(".mailSubmit").on('click', function(e) {
        e.preventDefault();
        var email = $("#emailFrom").val();
        var sender = $("#senderName").val();
        var message = $("#message").val()
        clearFields();
        $.ajax({
            url: "/contact/message",
            type: "POST",
            dataType: 'json',
            data: {
                "emailFrom": email,
                "senderName": sender,
                "message": message
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