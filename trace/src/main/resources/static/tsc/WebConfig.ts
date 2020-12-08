class WebConfig{
    private contextPath:string
    private imageViewPathPrefix:string
    constructor() {
        let webGlobalConfig=window['webGlobalConfig'];
        if(_.isUndefined(webGlobalConfig)){
            console.error("没有初始化webGlobalConfig,请先引入webGlobalConfig.tag")
        }
        this.contextPath=webGlobalConfig.contextPath;
        this.imageViewPathPrefix=webGlobalConfig.imageViewPathPrefix;
    }
    public toUrl(url:string):string{
        return this.contextPath+url;
    }

    public initAutoComplete(selector,url){
        $(selector).keydown(function (e){
            if(e.keyCode == 13){
                // $(selector).data('keycode',e.keyCode);
                // console.info('keydown')
            }
        });
        $(selector).data('oldvalue','');
        $(selector).on('change',function () {
            var oldvalue=$(selector).data('oldvalue');
            var val=$(this).val();
            if(oldvalue!=val){
                $(this).siblings('input').val('');
            }
        });

        //@ts-ignore
        // 联想输入
        $(selector).devbridgeAutocomplete({
            noCache: 1,
            serviceUrl: url,  // 数据地址
            dataType: 'json',
            onSearchComplete: function (query, suggestions) {
            },
            showNoSuggestionNotice: true,
            noSuggestionNotice: "不存在，请重输！",
            autoSelectFirst:true,
            autoFocus: true,
            onSelect: function (suggestion) {
                console.info('onSelect')
                var self = this;
                var idField = $(self).siblings('input');
                idField.val(suggestion.id);
                $(self).val(suggestion.value.trim());
                $(selector).data('oldvalue',suggestion.value);

                //@ts-ignore
                var v=$(self).valid();
            }
        });
    }

}