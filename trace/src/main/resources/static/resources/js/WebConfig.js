class WebConfig {
    constructor() {
        let webGlobalConfig = window['webGlobalConfig'];
        if (_.isUndefined(webGlobalConfig)) {
            console.error("没有初始化webGlobalConfig,请先引入webGlobalConfig.tag");
        }
        this.contextPath = webGlobalConfig.contextPath;
        this.imageViewPathPrefix = webGlobalConfig.imageViewPathPrefix;
        window.addEventListener('message', async (e) => this.handleMessage(e), false);
    }
    handleMessage(e) {
        let cthis = this;
        let data = JSON.parse(e.data);
        if (data.type == 'call' && data.calls) {
            _.chain(data.calls).each(async (item) => {
                let fun = item.funName;
                let args = item.args;
                await cthis[fun].call(cthis, args);
            });
        }
        if (data.type == 'apply' && data.calls) {
            _.chain(data.calls).each(async (item) => {
                let fun = item.funName;
                let args = item.args;
                await cthis[fun].apply(cthis, args);
            });
        }
    }
    toUrl(url) {
        return this.contextPath + url;
    }
    onselectFun(suggestion) {
        var self = this;
        var forId = $(self).attr("for");
        if (!_.isEmpty(forId)) {
            var idField = $('#' + forId);
            idField.val(suggestion.id);
        }
        $(self).val(suggestion.value.trim());
        $(self).data('oldvalue', suggestion.value);
        var v = $(self).valid();
    }
    initTraceAutoComplete(selector, lookupFun, onSelect = this.onselectFun) {
        $(selector).keydown(function (e) {
            if (e.keyCode == 13) {
            }
        });
        $(selector).data('oldvalue', '');
        $(selector).on('change', function () {
            var oldvalue = $(selector).data('oldvalue');
            var val = $(this).val();
            if (oldvalue != val) {
                $(this).siblings('input').val('');
            }
        });
        $(selector).devbridgeAutocomplete({
            noCache: 1,
            lookup: lookupFun,
            dataType: 'json',
            onSearchComplete: function (query, suggestions) {
            },
            showNoSuggestionNotice: true,
            noSuggestionNotice: "不存在，请重输！",
            autoSelectFirst: true,
            autoFocus: true,
            onSelect: onSelect
        });
    }
    serializeJSON(form) {
        let data = form.serializeJSON({ useIntKeysAsArrayIndex: true });
        return data;
    }
}
class ListPage extends WebConfig {
    constructor(grid, queryform, queryBtn, listPageUrl, otherParams = {}) {
        super();
        this.grid = grid;
        this.queryform = queryform;
        this.queryBtn = queryBtn;
        this.listPageUrl = listPageUrl;
        this.otherParams = otherParams;
        (async () => {
            await this.init();
        })();
    }
    async resetButtons() { }
    get rows() {
        let rows = this.grid.bootstrapTable('getSelections');
        return rows;
    }
    async init() {
        this.refreshTableOptions();
        await this.resetButtons();
        this.queryBtn.click(async () => await this.queryGridData());
    }
    async queryGridData() {
        if (!this.queryform.validate().form()) {
            bs4pop.notice("请完善必填项", { type: 'warning', position: 'topleft' });
            return;
        }
        await this.resetButtons();
        this.grid.bootstrapTable('refresh');
    }
    removeAllDialog() {
        bs4pop.removeAll();
    }
    async alert(msg, cfg) {
        await popwrapper.alert(msg, cfg);
    }
    buildMetaData() {
        let metadata = bui.util.bindGridMeta2Form(this.grid.attr('id'), this.queryform.attr('id')).metadata;
        if (_.isUndefined(metadata)) {
            return {};
        }
        return metadata;
    }
    refreshTableOptions() {
        let url = this.toUrl(this.listPageUrl);
        let metadata = this.buildMetaData();
        let buildQueryData = (params) => {
            let temp = {
                rows: params.limit,
                page: ((params.offset / params.limit) + 1) || 1,
                sort: params.sort,
                order: params.order,
                metadata: metadata
            };
            $.extend(temp, this.queryform.serializeJSON());
            $.extend(temp, this.otherParams);
            let jsonData = jq.removeEmptyProperty(temp);
            return JSON.stringify(jsonData);
        };
        this.grid.bootstrapTable('refreshOptions', {
            url: url,
            'queryParams': (params) => buildQueryData(params),
            'contentType': 'application/json',
            'ajaxOptions': { contentType: 'application/json', dataType: 'json' }
        });
    }
}
