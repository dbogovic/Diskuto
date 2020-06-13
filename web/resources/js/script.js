jsf.ajax.addOnEvent(function (data) {
    var ajaxstatus = data.status;
    var ajaxloader = document.getElementById("ajaxloader");

    switch (ajaxstatus) {
        case "begin":
            ajaxloader.style.display = 'initial';
            break;
        case "complete":
            ajaxloader.style.display = 'none';
            break;
        case "success":
            break;
    }
});