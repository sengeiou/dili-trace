class CategoryController {
    static async listCategories(cusCategoryQuery) {
        let resp = await jq.postJson2('../../category/listCategories.action', cusCategoryQuery);
        if (resp.code == '200') {
            return resp.data;
        }
        else {
            throw new Error(resp.message);
        }
    }
    static async listSuggestionsCategories(cusCategoryQuery) {
        let resp = await jq.postJson2('../../category/listCategories.action', cusCategoryQuery);
        if (resp.code == '200') {
            return _.chain(resp.data).map(item => {
                return { "id": item.id, "value": item.name };
            }).value();
        }
        else {
            throw new Error(resp.message);
        }
    }
}
//# sourceMappingURL=CategoryController.js.map