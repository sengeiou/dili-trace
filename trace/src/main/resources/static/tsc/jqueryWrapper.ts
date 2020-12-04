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
    public static async ajaxWithProcessing(settings: JQuery.AjaxSettings):Promise<any> {
        //@ts-ignore
        bui.loading.show('努力提交中，请稍候。。。');
        const p = new Promise<any>((resolve, reject) => {
            $.ajax(settings).done((result)=>{
                resolve(result);
            }).fail((e)=>{
                reject(e)
            })
        })
        try{
            var resp=await p;
            //@ts-ignore
            bui.loading.hide();
            return resp;
        }catch (e){
            //@ts-ignore
            bui.loading.hide();
            throw e
        }
    }
    /**
     * 对ajax异步请求转换为promise
     * @param settings
     */

    public static async postJson(url:string,data:object,settings:JQuery.AjaxSettings={}):Promise<any> {
        let jsonData=this.removeEmptyProperty(data);
        _.extend(settings,{method:'post',dataType:'json',contentType:'application/json',data:JSON.stringify(jsonData),url:url})
        let resp:any=await jq.ajax(settings);
        return resp;
    }

    /**
     * 对ajax异步请求转换为promise
     * @param settings
     */

    public static async postJsonWithProcessing(url:string,data:object,settings:JQuery.AjaxSettings={}):Promise<any> {
        let jsonData=this.removeEmptyProperty(data);
        _.extend(settings,{method:'post',dataType:'json',contentType:'application/json',data:JSON.stringify(jsonData),url:url})
        try{
            //@ts-ignore
            bui.loading.show('努力提交中，请稍候。。。');
            let resp:any=await jq.ajax(settings);
            //@ts-ignore
            bui.loading.hide();
            return resp;
        }catch (e){
            //@ts-ignore
            bui.loading.hide();
            throw e
        }
    }
    public  static removeEmptyProperty(data:object){

        let jsonData=_.chain(data)
            .pick((v,k)=>{
                return !_.isUndefined(v);})
            .pick((v,k)=>{
                if(_.isString(v)){
                    if(_.isEmpty(v)||_.isNull(v)){
                        return false;
                    }
                }
                return true;})
            .value();
        return jsonData;
    }
}