class CityController extends WebConfig {
    async listCities(cusCategoryQuery) {
        let url = this.toUrl("/city/listCities.action");
        let resp = await jq.postJson(url, cusCategoryQuery);
        if (resp.code == '200') {
            return resp.data;
        }
        else {
            throw new Error(resp.message);
        }
    }
    async listSuggestionsCities(cusCategoryQuery) {
        try {
            let data = await this.listCities(cusCategoryQuery);
            return _.chain(data).map(item => {
                return { "id": item.id, "value": item.mergerName, "item": item };
            }).value();
        }
        catch (e) {
            console.error(e);
            return [];
        }
    }
    lookupCities(query, done) {
        (async () => {
            let data = await this.listSuggestionsCities({ 'keyword': query });
            var result = { 'suggestions': data };
            done(result);
        })();
    }
}
//# sourceMappingURL=CityController.js.map