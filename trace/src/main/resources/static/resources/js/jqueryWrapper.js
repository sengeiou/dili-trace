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
        let jsonData = this.removeEmptyProperty(data);
        _.extend(settings, { method: 'post', dataType: 'json', contentType: 'application/json', data: JSON.stringify(jsonData), url: url });
        let resp = await jq.ajax(settings);
        return resp;
    }
    static async postJsonWithProcessing(url, data, settings = {}) {
        let jsonData = this.removeEmptyProperty(data);
        _.extend(settings, { method: 'post', dataType: 'json', contentType: 'application/json', data: JSON.stringify(jsonData), url: url });
        try {
            bui.loading.show('努力提交中，请稍候。。。');
            let resp = await jq.ajax(settings);
            bui.loading.hide();
            return resp;
        }
        catch (e) {
            bui.loading.hide();
            throw e;
        }
    }
    static removeEmptyProperty(data) {
        if (_.isArray(data)) {
            var jsonData = _.chain(data).filter(item => !_.isNull(item)).value();
            return jsonData;
        }
        else {
            let jsonData = _.chain(data)
                .pick((v, k) => {
                return !_.isUndefined(v);
            })
                .pick((v, k) => {
                if (_.isString(v)) {
                    if (_.isEmpty(v) || _.isNull(v)) {
                        return false;
                    }
                }
                return true;
            })
                .mapObject(v => {
                if (!_.isArray(v)) {
                    return v;
                }
                return _.chain(v).filter(v => !_.isNull(v)).value();
            })
                .value();
            return jsonData;
        }
    }
}
//# sourceMappingURL=jqueryWrapper.js.map