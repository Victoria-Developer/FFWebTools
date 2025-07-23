$(document).ready(function() {
    $(".mailSubmit").on('click', function(e) {
        e.preventDefault();
        var email = $("#emailFrom").val();
        var sender = $("#senderName").val();
        var message = $("#message").val()
        clearFields();
        $.ajax({
            url: "/message",
            type: "POST",
            dataType: 'json',
            data: {
                "emailFrom": email,
                "senderName": sender,
                "message": message
            },
            success: function(response) {
                console.log('ok')
                $("#statusLabel").text('Your message was sent.')
            },
            error: function(e){
                console.log(e)
                $("#statusLabel").text("Message sending error.");
            }
        });
    });

    function clearFields() {
        $("#senderName").val('');
        $("#emailFrom").val('');
        $("#message").val('');
        $("#statusLabel").text('')
    }

    $(".contactsTextField").on("keydown", function () {
        const id = this.id;
        $(`label[for='${id}']`).css("display", "none");
    });
});