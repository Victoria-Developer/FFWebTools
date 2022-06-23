$(document).ready(function() {
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

    $(".submitParse").on('click', function (e){
        e.preventDefault();
        var booleanArray = [];
        $(":checkbox").each(function () {
               var isChecked = $(this).is(":checked");
               var isDisabled = $(this).is(":disabled");
               booleanArray.push(isDisabled);
               if (isChecked && !isDisabled) {
                  $(this).attr("disabled", true);
                  callAjax({'parseId' : $(this).val()})
                  .done(function(response){
                  fillTable(JSON.parse(response.charactersList));
                  $('#sortingDiv_id select option[id="' + response.relatedSortingId + '"]')
                        .prop('disabled', false);
                  })
                  .fail(function(){
                  $(this).removeAttr("disabled");
                  });
                }
        });
        if(booleanArray.every(el=> el == true) == true)
            alert('You parsed all data~');
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