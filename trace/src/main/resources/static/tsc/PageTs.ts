class PageTs{
    private _webConfig:WebConfig;
    constructor() {
        //@ts-ignore
        this._webConfig=(config as WebConfig);
    }
    public get webConfig():WebConfig{
        return this._webConfig;
    }
    public get contextPath():string{
        return this._webConfig.contextPath;
    }
    public get imageserverPath():string{
        return this._webConfig.imageserverPath;
    }
    public toUrl(url:string):string{
        return this.contextPath+url;
    }

}