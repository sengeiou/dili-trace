
class CategoryController {

    public static async listCategories(cusCategoryQuery: any) :Promise<any>{
        let resp = await jq.postJson('../../category/listCategories.action',  cusCategoryQuery);
        if (resp.code == '200') {
            return resp.data;
        } else {
            throw new Error(resp.message);
        }
    }
    public static async listSuggestionsCategories(cusCategoryQuery: any) :Promise<any>{
        let resp = await jq.postJson('../../category/listCategories.action',  cusCategoryQuery);
        if (resp.code == '200') {
            return _.chain(resp.data).map(item => {
                return { "id": item.id, "value": item.name };
            }).value();
        } else {
            throw new Error(resp.message);
        }
    }

}