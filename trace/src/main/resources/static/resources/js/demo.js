"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const jquery_1 = __importDefault(require("jquery"));
const underscore_1 = __importDefault(require("underscore"));
class DemoClass {
    constructor(props) {
    }
}
function demo() {
    console.info("bbb");
}
class Ajax {
    constructor(settings) {
    }
    postForm(formData) {
        underscore_1.default.chain(jquery_1.default.makeArray("post")).value();
    }
    postJson(jsonObject) {
        console.info(jquery_1.default.type(jsonObject));
    }
}
//# sourceMappingURL=demo.js.map