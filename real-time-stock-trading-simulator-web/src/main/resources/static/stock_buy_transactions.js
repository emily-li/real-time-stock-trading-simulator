function buyStock(stockSymbol, volume) {
    $.ajax({
        url: `/api/v1/buy`,
        method: `post`,
        data: {
            stockSymbol: stockSymbol,
            volume: volume
        }
    }).done((response) => {
        showMsg(response.msg);
        if (response.status === "true") {
            repopulateStocks();
        }
    });

}

// Transactional
$("#buyBtn").click(() => {
    $("#stocksTBody").find("input").each((index, element) => {
        if ($(element).val()) {
            buyStock($(element).attr("data-symbol"), $(element).val());
        }
    });
});