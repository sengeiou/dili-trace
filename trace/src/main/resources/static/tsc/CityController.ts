class CityController extends WebConfig {

    public async listCities(cusCategoryQuery: any): Promise<any> {
        let url = this.toUrl("/city/listCities.action");
        let resp = await jq.postJson(url, cusCategoryQuery);
        if (resp.code == '200') {
            return resp.data;
        } else {
            throw new Error(resp.message);
        }
    }

    public async listSuggestionsCities(cusCategoryQuery: any): Promise<any> {
        try{
            let data = await this.listCities(cusCategoryQuery);

            return _.chain(data).map(item => {
                return {"id": item.id, "value": item.mergerName,"item":item};
            }).value();
        }catch (e){
            console.error(e);
            return [];
        }

    }
    public lookupCities(query:string,done:Function){
        (async ()=>{
            let data=await this.listSuggestionsCities({'keyword':query});
            var result={'suggestions':data};
            done(result)
        })();
    }

}