
import $ from 'jquery'
import _ from 'underscore'

class DemoClass {

    constructor(props) {
        // @ts-ignore
        $.makeArray("");
        _.chain([]).value();
        console.info("bbb")
    }
}