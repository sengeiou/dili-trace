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
    static async ajaxWithProcessing(settings) {
        bui.loading.show('努力提交中，请稍候。。。');
        const p = new Promise((resolve, reject) => {
            $.ajax(settings).done((result) => {
                resolve(result);
            }).fail((e) => {
                reject(e);
            });
        });
        try {
            var resp = await p;
            bui.loading.hide();
            return resp;
        }
        catch (e) {
            bui.loading.hide();
            throw e;
        }
    }
    static async postJson(url, data, settings = {}) {
        _.extend(settings, { method: 'post', dataType: 'json', contentType: 'application/json', data: JSON.stringify(data), url: url });
        let resp = await jq.ajax(settings);
        return resp;
    }
}
//# sourceMappingURL=jqueryWrapper.js.map