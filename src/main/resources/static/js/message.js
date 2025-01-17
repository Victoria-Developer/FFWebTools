$(document).ready(function() {
    $(".mailSubmit").on('click', function(e) {
        e.preventDefault();
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
                clearFields();
            }
        });
    });

    function clearFields() {
        $("#senderName").val('');
        $("#emailFrom").val('');
        $("#message").val('');
    }
});