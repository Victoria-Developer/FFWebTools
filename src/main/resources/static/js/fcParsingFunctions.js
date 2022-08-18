$(document).ready(function() {
    document.getElementById("sortingMethods").value = -1;

    $(".submitSelect").on('click', function (e){
        e.preventDefault();
        var id = $('#sortingMethods').val();
        if(id == null) {
        alert("Choose sort method, please.");
        return;
        }
        callAjax({'selectedMethod' : id})
         .done(function(response){
            fillTable(response);
        });
    });

    $(".parseButton").on('click', function(){
    $(this).attr("disabled", true);
    callAjax({'parseId' : $(this).attr('index')})
            .done(function(response){
                fillTable(JSON.parse(response.charactersList));
                 $('#sortingDiv_id select option[id="' + response.relatedSortingId + '"]')
                 .prop('disabled', false);
            })
            .fail(function(){
                  $(this).removeAttr("disabled");
            });
    });

    function callAjax(data){
          return $.ajax({
          type:'GET',
          url : "/fcMembers",
          dataType: 'json',
          contentType: "application/json",
          data: data
        });
    }

    });