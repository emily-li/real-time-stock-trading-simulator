function showMsg(body) {
    $("#msgBody").text(body);
    $("#msgAlert").alert();
}

function formatCurrency(currency) {
    let curr = currency ? currency : 0;
    return curr.toFixed(2);
}