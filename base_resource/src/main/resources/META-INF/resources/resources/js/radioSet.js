/**
 * Created by pgwt on 10/17/15.
 */
function buttonGroupAction(element, callback) {
    var groupName = $(element).attr("name");
    var currentClass = $(element).attr("class");
    if ($(element).attr("status") == "unActive") {
        $("button[name=" + groupName + "]").each(function () {
            $(this).attr("class",currentClass);
            $(this).attr("status","unActive");
        });
        $(element).attr("status","active")
        $(element).attr("class", $(element).attr("class") + " am-active");
        if (typeof callback != "undefined") {
            callback(element);
        }
    }
}