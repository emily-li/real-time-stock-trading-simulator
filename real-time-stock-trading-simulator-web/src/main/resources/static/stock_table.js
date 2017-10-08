/*<![CDATA[*/

// Variables
let page = 0;
let size = 20;
let prevBtnDisabled = true;
let nextBtnDisabled = false;
let currSearch;

// Public Functions
function updatePageLabel() {
    $("#pageLabel").text(page);
    $("#prevBtn").prop("disabled", prevBtnDisabled);
    $("#nextBtn").prop("disabled", nextBtnDisabled);
}

function updatePageNav(numCurrentItems) {
    nextBtnDisabled = numCurrentItems < size;
    prevBtnDisabled = page === 0;
    updatePageLabel();
}

function formatCurrency(currency) {
    let curr = currency ? currency : 0;
    return curr.toFixed(2);
}

function createTableRow(stock) {
    let tr = $("<tr></tr>");

    let tdSymbol = $("<td></td>");
    tdSymbol.text(stock.symbol);
    tr.append(tdSymbol);

    let tdName = $("<td></td>");
    tdName.text(stock.name);
    tr.append(tdName);

    let tdDateTime = $("<td></td>");
    let date = stock.lastTradeDateTime ? moment(stock.lastTradeDateTime) : '';
    let formattedDate = date ? date.format("hh:mm:ss") : '';
    tdDateTime.text(formattedDate);
    tr.append(tdDateTime);

    let tdValue = $("<td></td>");
    tdValue.text(formatCurrency(stock.value));
    tr.append(tdValue);

    let tdVol = $("<td></td>");
    tdVol.text(stock.volume);
    tr.append(tdVol);

    let tdGains = $("<td></td>");
    tdGains.text(formatCurrency(stock.gains));
    tr.append(tdGains);

    let tdOpenValue = $("<td></td>");
    tdOpenValue.text(formatCurrency(stock.openValue));
    tr.append(tdOpenValue);

    let tdCloseValue = $("<td></td>");
    tdCloseValue.text(formatCurrency(stock.closeValue));
    tr.append(tdCloseValue);

    let tdPurchaseVol = $("<td><input data-symbol='" + stock.symbol + "' class='form-control' type='number'></input></td>");
    tr.append(tdPurchaseVol);

    return tr;
}

function repopulateStocks(stocks) {
    // Clear stocksTBody
    $("#stocksTBody").html("");

    // Repopulate
    stocks.forEach((stock) => {
        let tr = createTableRow(stock);
        $("#stocksTBody").append(tr);
    });

    updatePageNav(stocks.length);
}

function updateStocks() {
    let url = `/api/v1/buy?page=${page}&size=${size}`;
    url = currSearch ? `${url}&${currSearch}` : url;

    $.ajax({
        url: url,
        method: "get"
    }).done((stocks) => {
        repopulateStocks(stocks);
    });
}

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

function searchStock() {
    page = 0;
    $.ajax({
        url: `/api/v1/buy?${currSearch}`,
        method: "get"
    }).done((stocks) => {
        repopulateStocks(stocks);
    });
}

// Events

// Navigation
$("#prevBtn").click(() => {
    page--;
    updateStocks();
});

$("#nextBtn").click(() => {
    page++;
    updateStocks();
});

$("#allBtn").click(() => {
    page = 0;
    currSearch = '';

    $.ajax({
        url: `/api/v1/buy/all`,
        method: "get"
    }).done((stocks) => {
        repopulateStocks(stocks);
    });
});

// Transactional
$("#buyBtn").click(() => {
    $("#stocksTBody").find("input").each((index, element) => {
        if ($(element).val()) {
            buyStock("foo", $(element).attr("data-symbol"), $(element).val());
        }
    });
});

// Search
$("#searchSymbolLi").click(() => {
    let symbol = $("#searchText").val();
    currSearch = symbol ? `symbol=${symbol}` : '';
    searchStock();
});

$("#searchNameLi").click(() => {
    let name = $("#searchText").val();
    currSearch = name ? `name=${name}` : '';
    searchStock();

});

// Init
updateStocks();
setInterval(() => {
        updateStocks();
    },
    60000);
/*]]>*/