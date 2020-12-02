class popwrapper {
    /**
     * 对ajax异步请求转换为promise
     * @param settings
     */
    public static async confirm(msg:string,config:any={} ):Promise<any> {
        const p = new Promise<any>((resolve, reject) => {
            //@ts-ignore
            bs4pop.confirm(msg, config, async function (sure) {
                resolve(sure);
            })
        })
        return await p;
    }
}