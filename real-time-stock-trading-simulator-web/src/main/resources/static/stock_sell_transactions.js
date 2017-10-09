function sellStock(stockSymbol, volume) {
    $.ajax({
        url: `/api/v1/sell`,
        method: "post",
        data: {
            stockSymbol: stockSymbol,
            volume: volume
        }
    }).done((response) => {
        showMsg(response.msg);
    });
}

// Transactional
$("#sellBtn").click(() => {
    $("#stocksTBody").find("input").each((index, element) => {
        if ($(element).val()) {
            sellStock($(element).attr("data-symbol"), $(element).val());
        }
    });
});