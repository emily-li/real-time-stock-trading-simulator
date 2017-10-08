function buyStock(username, stockSymbol, volume) {
    $.ajax({
        url: `/api/v1/buy`,
        method: "post",
        data: {
            username: username,
            stockSymbol: stockSymbol,
            volume: volume
        }
    });
}

// Transactional
$("#buyBtn").click(() => {
    $("#stocksTBody").find("input").each((index, element) => {
        if ($(element).val()) {
            buyStock("foo", $(element).attr("data-symbol"), $(element).val());
        }
    });
});