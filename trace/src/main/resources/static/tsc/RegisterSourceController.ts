import {WebConfig} from "WebConfig";
import _ from 'underscore'

export class RegisterSourceController extends WebConfig {
    constructor() {
        super();
    }
    public  async querySource(registerSource: number, query: string): Promise<any> {
        let url = this.toUrl("/registerSource/querySource.action");
        let resp = await jq.postJson(url, {"registerSource": registerSource, "query": query});
        if (resp.code == '200') {
            return resp.data;
        } else {
            throw new Error(resp.message);
        }
    }

    public async listSuggestionsSource(registerSource: number, query: string): Promise<any> {
        let data = await this.querySource(registerSource,query);
        return _.chain(data).map(item => {
            return {"id": item.id, "value": item.name};
        }).value();
    }

    public lookupSources(query: string, done: Function) {
        (async () => {
            let data = await this.listSuggestionsSource(0,query);
            var result = {'suggestions': data};
            done(result)
        })();
    }
}