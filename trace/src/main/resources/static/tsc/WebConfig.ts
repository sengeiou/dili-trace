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
        window.addEventListener('message', async (e) => this.handleMessage(e), false);
    }

    protected handleMessage(e: MessageEvent) {
        let cthis=this;
        let data = JSON.parse(e.data);
        if (data.type=='call'&&data.calls) {
            _.chain(data.calls).each(async (item)=>{
                let fun=item.funName;
                let args=item.args;
                await cthis[fun].call(cthis, args)
            });

        }if (data.type=='apply'&&data.calls) {
            _.chain(data.calls).each(async (item)=>{
                let fun=item.funName;
                let args=item.args;
                await cthis[fun].apply(cthis, args)
            });

        }
    }
    public toUrl(url:string):string{
        return this.contextPath+url;
    }

    private onselectFun(suggestion){
        // console.info('onSelect')
        var self = this;
        var forId=$(self).attr("for");
        if(!_.isEmpty(forId)){
            var idField = $('#'+forId);
            idField.val(suggestion.id);
        }
        $(self).val(suggestion.value.trim());
        $(self).data('oldvalue',suggestion.value);
        //@ts-ignore
        var v=$(self).valid();

    }
    public initAutoComplete(selector,lookupFun:Function,onSelect:Function=this.onselectFun){
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
            //serviceUrl: url,  // 数据地址
            lookup: lookupFun,
            dataType: 'json',
            onSearchComplete: function (query, suggestions) {
            },
            showNoSuggestionNotice: true,
            noSuggestionNotice: "不存在，请重输！",
            autoSelectFirst:true,
            autoFocus: true,
            onSelect: onSelect
        });
    }

}
class ListPage extends WebConfig {
    protected grid: any;
    protected queryform: any;
    protected  queryBtn: any;
    protected listPageUrl: string;

    constructor(grid: any, queryform: any, queryBtn: any, listPageUrl: string) {
        super();
        this.grid = grid;
        this.queryform = queryform;
        this.queryBtn = queryBtn;
        this.listPageUrl = listPageUrl;
        this.init();
    }
    protected async resetButtons() {}
    get rows() {
        let rows = this.grid.bootstrapTable('getSelections');
        return rows;
    }
    private  init(){
        this.refreshTableOptions();
        this.queryBtn.click(async () => await this.queryGridData());
    }
    protected async queryGridData() {
        if (!this.queryform.validate().form()) {
            //@ts-ignore
            bs4pop.notice("请完善必填项", {type: 'warning', position: 'topleft'});
            return;
        }
        await this.resetButtons();
        this.grid.bootstrapTable('refresh');
    }
    private removeAllDialog(){
        //@ts-ignore
        bs4pop.removeAll();
    }
    private async notice(msg:string,cfg:any){
        debugger
       await popwrapper.alert(msg,cfg);
    }

    // private get columns(){
    //     var cols=this.grid.bootstrapTable('getOptions')?.columns;
    //     if(_.isUndefined(cols)){
    //         return [];
    //     }
    //     return cols;
    //
    // }
    private buildMetaData(){
        //@ts-ignore
        let metadata=bui.util.bindGridMeta2Form(this.grid.attr('id'),this.queryform.attr('id'))?.metadata;
        if(_.isUndefined(metadata)){
            return {}
        }
        return metadata;
    }
    protected refreshTableOptions(){
        let url=this.toUrl(this.listPageUrl)
        let metadata=this.buildMetaData();

        let buildQueryData=(params)=>{
            let temp = {
                rows: params.limit,   //页面大小
                page: ((params.offset / params.limit) + 1) || 1, //页码
                sort: params.sort,
                order: params.order,
                metadata:metadata
            }

            let data = $.extend(temp, this.queryform.serializeJSON());
            let jsonData = jq.removeEmptyProperty(data);

            return JSON.stringify(jsonData);
        }
        this.grid.bootstrapTable('refreshOptions', {
            url: url
            , 'queryParams': (params) => buildQueryData(params)
            ,'contentType':'application/json'
            , 'ajaxOptions': {contentType: 'application/json', dataType: 'json'}

        });
    }


}