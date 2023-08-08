$(document).ready(function (){
    $(".linkMinus").on("click", function (evt){
        evt.preventDefault();
        let taskId = $(this).attr("tid");
        let quantityInput = $("#quantity" + taskId);
        let newQuantity = parseInt(quantityInput.val()) - 1;

        if (newQuantity > 0){
            quantityInput.val(newQuantity);
        } else {
            showWarningModal("Minimum quantity is 1");
        }

    });

    $(".linkPlus").on("click", function (evt){
        evt.preventDefault();
        showWarningModal("clicked plus");
    });
});