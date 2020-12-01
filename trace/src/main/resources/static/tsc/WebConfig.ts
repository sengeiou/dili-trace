class WebConfig{
    private contextPath:string
    private imageserverPath:string
    constructor() {
        let webGlobalConfig=window['webGlobalConfig'];
        if(_.isUndefined(webGlobalConfig)){
            console.error("没有初始化webGlobalConfig,请先引入webGlobalConfig.tag")
        }
        this.contextPath=webGlobalConfig.contextPath;
        this.imageserverPath=webGlobalConfig.imageserverPath;
    }
    public toUrl(url:string):string{
        return this.contextPath+url;
    }
}