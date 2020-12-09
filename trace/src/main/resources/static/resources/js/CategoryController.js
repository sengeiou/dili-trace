class CategoryController extends WebConfig {
    async listCategories(cusCategoryQuery) {
        let url = this.toUrl("/category/listCategories.action");
        let resp = await jq.postJson(url, cusCategoryQuery);
        if (resp.code == '200') {
            return resp.data;
        }
        else {
            throw new Error(resp.message);
        }
    }
    async listSuggestionsCategories(cusCategoryQuery) {
        let url = this.toUrl("/category/listCategories.action");
        let resp = await jq.postJson(url, cusCategoryQuery);
        if (resp.code == '200') {
            return _.chain(resp.data).map(item => {
                return { "id": item.id, "value": item.name };
            }).value();
        }
        else {
            throw new Error(resp.message);
        }
    }
    lookupCategories(query, done) {
        (async () => {
            let data = await this.listSuggestionsCategories({ 'keyword': query });
            var result = { 'suggestions': data };
            done(result);
        })();
    }
}
//# sourceMappingURL=CategoryController.js.map