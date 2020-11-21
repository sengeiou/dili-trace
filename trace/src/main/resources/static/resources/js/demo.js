var DemoClass = (function () {
    function DemoClass(props) {
    }
    return DemoClass;
}());
function demo() {
    console.info("abcd");
}
var Ajax = (function () {
    function Ajax(settings) {
    }
    Ajax.prototype.postForm = function (formData) {
        _.chain($.makeArray("post")).value();
    };
    Ajax.prototype.postJson = function (jsonObject) {
        console.info($.type(jsonObject));
    };
    return Ajax;
}());
//# sourceMappingURL=demo.js.map