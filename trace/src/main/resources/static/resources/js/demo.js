(function (factory) {
    if (typeof module === "object" && typeof module.exports === "object") {
        var v = factory(require, exports);
        if (v !== undefined) module.exports = v;
    }
    else if (typeof define === "function" && define.amd) {
        define(["require", "exports"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.demo = exports.DemoClass = void 0;
    class DemoClass {
        constructor(props) {
            $.makeArray("");
            _.chain([]).value();
            console.info("bcd");
        }
    }
    exports.DemoClass = DemoClass;
    var demo;
    (function (demo) {
        class Test1 {
            constructor() {
            }
        }
        demo.Test1 = Test1;
        class Test2 {
            constructor() {
            }
        }
        demo.Test2 = Test2;
    })(demo = exports.demo || (exports.demo = {}));
});
