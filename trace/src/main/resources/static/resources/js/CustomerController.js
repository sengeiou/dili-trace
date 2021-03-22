class CustomerController extends WebConfig {
    async listSeller(query) {
        var url = await super.toUrl("/customer/listSeller.action");
        let resp = await jq.postJson(url, query);
        if (resp.code === '200') {
            return resp.data;
        }
        else {
            throw new Error(resp.message);
        }
    }
    async listSellerSuggestions(query) {
        try {
            let data = await this.listSeller(query);
            return _.chain(data).map(item => {
                return { "id": item.id, "value": item.name + ' | ' + item.phone + ' | ' + item.tradePrintingCard + ' | ' + item.marketName, "item": item };
            }).value();
        }
        catch (e) {
            console.error(e);
            return [];
        }
    }
    lookupSeller(query, done) {
        (async () => {
            let data = await this.listSellerSuggestions({ 'keyword': query });
            let result = { 'suggestions': data };
            done(result);
        })();
    }
}
