function ajaxload1(data) {
    var ajaxstatus = data.status;
    var ajaxloader = document.getElementById("ajaxloader1");

    switch (ajaxstatus) {
        case "begin":
            ajaxloader.style.display = 'block';
            break;
        case "complete":
            ajaxloader.style.display = 'none';
            break;
        case "success":
            break;
    }
}
;
function ajaxload2(data) {
    var ajaxstatus = data.status;
    var ajaxloader = document.getElementById("ajaxloader2");

    switch (ajaxstatus) {
        case "begin":
            ajaxloader.style.display = 'block';
            break;
        case "complete":
            ajaxloader.style.display = 'none';
            break;
        case "success":
            break;
    }
}
;