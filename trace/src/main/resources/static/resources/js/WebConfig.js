class WebConfig {
    constructor() {
        let webGlobalConfig = window['webGlobalConfig'];
        if (_.isUndefined(webGlobalConfig)) {
            console.error("没有初始化webGlobalConfig,请先引入webGlobalConfig.tag");
        }
        this.contextPath = webGlobalConfig.contextPath;
        this.imageViewPathPrefix = webGlobalConfig.imageViewPathPrefix;
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
    initAutoComplete(selector, lookupFun, onSelect = this.onselectFun) {
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
}
//# sourceMappingURL=WebConfig.js.map