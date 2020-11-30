class jq {
    /**
     * 对ajax异步请求转换为promise
     * @param settings
     */
    public static async ajax(settings: JQuery.AjaxSettings):Promise<any> {
        const p = new Promise<any>((resolve, reject) => {
            $.ajax(settings).done((result)=>{
                resolve(result);
            }).fail((e)=>{
                reject(e)
            })
        })
        return p
    }
    /**
     * 对ajax异步请求转换为promise
     * @param settings
     */

    public static async postJson(url:string,data:object,settings:JQuery.AjaxSettings={}):Promise<any> {
        _.extend(settings,{method:'post',dataType:'json',contentType:'application/json',data:JSON.stringify(data),url:url})
        let resp:any=await jq.ajax(settings);
        return resp;
    }
}