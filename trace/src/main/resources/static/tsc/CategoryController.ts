
class CategoryController extends  WebConfig{

    public  async listCategories(cusCategoryQuery: any) :Promise<any>{
        let url=this.toUrl("/category/listCategories.action");
        let resp = await jq.postJson(url,  cusCategoryQuery);
        if (resp.code == '200') {
            return resp.data;
        } else {
            throw new Error(resp.message);
        }
    }
    public async listSuggestionsCategories(cusCategoryQuery: any) :Promise<any>{
        let url=this.toUrl("/category/listCategories.action");
        let resp = await jq.postJson(url,  cusCategoryQuery);
        if (resp.code == '200') {
            return _.chain(resp.data).map(item => {
                return { "id": item.id, "value": item.name };
            }).value();
        } else {
            throw new Error(resp.message);
        }
    }
    public lookupCategories(query:string,done:Function){
        (async ()=>{
            let data=await this.listSuggestionsCategories({'keyword':query});
            var result={'suggestions':data};
            done(result)
        })();
    }
}