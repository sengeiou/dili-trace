class WebConfig {
    constructor() {
        let webGlobalConfig = window['webGlobalConfig'];
        if (_.isUndefined(webGlobalConfig)) {
            console.error("没有初始化webGlobalConfig,请先引入webGlobalConfig.tag");
        }
        this.contextPath = webGlobalConfig.contextPath;
        this.imageserverPath = webGlobalConfig.imageserverPath;
    }
    toUrl(url) {
        return this.contextPath + url;
    }
}
//# sourceMappingURL=webconfig.js.map