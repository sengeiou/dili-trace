class CustomerController extends WebConfig {

    public async listSeller(query: any): Promise<any> {

        var url = await super.toUrl("/customer/listSeller.action");
        let resp = await jq.postJson(url, query);
        if (resp.code === '200') {
            return resp.data;
        } else {
            throw new Error(resp.message);
        }
    }

    public async listSellerSuggestions(query: any): Promise<any> {
        try {
            let data = await this.listSeller(query);

            return _.chain(data).map(item => {
                return { "id": item.id, "value": item.name + ' | ' + item.phone + ' | ' + item.tradePrintingCard + ' | ' + item.marketName, "item": item };
            }).value();
        } catch (e) {
            console.error(e);
            return [];
        }

    }
    public lookupSeller(query:string, done:Function){
        (async () => {
            let data = await this.listSellerSuggestions({'keyword': query});
            let result = {'suggestions': data};
            done(result);
        })();
    }

}