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
