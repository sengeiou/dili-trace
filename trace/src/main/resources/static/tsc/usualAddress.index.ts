class UsualAddressIndex extends  WebConfig{
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

        $('#add-btn').on('click',async ()=>this.openAddPage());
        $('#edit-btn').on('click',async ()=>this.openEditPage());
        $('#delete-btn').on('click', async  ()=> this.openDeletePage())
        $('#export').on('click',async ()=>this.openExportPage());
        await this.queryGridData();
    }
    private async openAddPage() {


    }
    private async openEditPage() {

    }
    private async openDeletePage() {

    }
    private async  openExportPage() {

    }
    private async rowClick() {
    debugger
    }
    private async queryGridData() {
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
            let url = this.toUrl( "/usualAddress/listPage.action");
            let resp = await jq.postJson(url, this.queryform.serializeJSON(), {});
            this.grid.bootstrapTable('load',resp);
        }catch (e){
            console.error(e);
            this.grid.bootstrapTable('load',{rows:[],total:0});
        }
        this.grid.bootstrapTable('hideLoading');
        $('#toolbar button').removeAttr('disabled');
    }
}