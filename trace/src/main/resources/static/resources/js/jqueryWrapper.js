class jq {
    static async ajax(settings) {
        const p = new Promise((resolve, reject) => {
            $.ajax(settings).done((result) => {
                resolve(result);
            }).fail((e) => {
                reject(e);
            });
        });
        return p;
    }
    static async postJson(url, data, settings = {}) {
        _.extend(settings, { method: 'post', dataType: 'json', contentType: 'application/json', data: JSON.stringify(data), url: url });
        let resp = await jq.ajax(settings);
        return resp;
    }
}
//# sourceMappingURL=jqueryWrapper.js.map