System.register(["jquery", "underscore"], function (exports_1, context_1) {
    "use strict";
    var jquery_1, underscore_1, DemoClass;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (jquery_1_1) {
                jquery_1 = jquery_1_1;
            },
            function (underscore_1_1) {
                underscore_1 = underscore_1_1;
            }
        ],
        execute: function () {
            DemoClass = class DemoClass {
                constructor(props) {
                    jquery_1.default.makeArray("");
                    underscore_1.default.chain([]).value();
                    console.info("bbb");
                }
            };
        }
    };
});
