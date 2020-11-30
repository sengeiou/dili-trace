class PageTs {
    constructor() {
        this._webConfig = webGlobalConfig;
    }
    get webConfig() {
        return this._webConfig;
    }
    get contextPath() {
        return this._webConfig.contextPath;
    }
    get imageserverPath() {
        return this._webConfig.imageserverPath;
    }
    toUrl(url) {
        return this.contextPath + url;
    }
}
//# sourceMappingURL=PageTs.js.map