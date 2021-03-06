class UserHistoryServiceIndex extends  WebConfig{
    private grid: any;
    private queryform: any;
    constructor(grid: any,queryform:any) {
        super();
        this.grid = grid;
        this.queryform=queryform;
        (async ()=>this.init())();
    }
    private async init(){
        this.queryform.find('#query').click(async () => await this.queryGridData());

        this.grid.on('check.bs.table', async () => await this.rowClick());
        this.grid.on('uncheck.bs.table',async () => await this.rowClick());

        await this.queryGridData();
    }
    private async rowClick(){
        debugger
    }
    private async queryGridData(){
        if (!this.queryform.validate().form()) {
            //@ts-ignore
            bs4pop.notice("请完善必填项", {type: 'warning', position: 'topleft'});
            return;
        }
        await this.remoteQuery();
    }
    private async remoteQuery() {
        $('#toolbar button').attr('disabled', "disabled");
        this.grid.bootstrapTable('showLoading');
        try{
            let url = this.toUrl( "/userHistory/listPage.action");
            let resp = await jq.postJson(url, this.queryform.serializeJSON(), {});
            this.grid.bootstrapTable('load',resp);
            await this.queryStatics();
        }catch (e){
            console.error(e);
            this.grid.bootstrapTable('load',{rows:[],total:0});
        }
        this.grid.bootstrapTable('hideLoading');
        $('#toolbar button').removeAttr('disabled');
    }

    private async queryStatics() {
        try{
            let url = this.toUrl( "/userHistory/queryStatics.action");
            let resp = await jq.postJson(url, this.queryform.serializeJSON(), {});
            console.info("====>", resp)
            $('#plateAmount').val(resp.plateAmount)
        }catch (e){
            $('#plateAmount').val('0')
        }
    }

}