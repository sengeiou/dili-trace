class EcommerceBillGrid extends WebConfig{
    grid: any;
    queryform: any;
    highLightBill: any;

    constructor(grid: any, queryform: any) {
        super();
        this.grid = grid;
        this.queryform = queryform;

        this.queryform.find('#query').click(async () => await this.queryGridData());
        //load data
        (async ()=>{
            await this.queryGridData();
        })();
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
        // 查询要高亮显示的数据
        this.highLightBill = await this.findHighLightBill();

        try{
            let url = this.toUrl( "/ecommerceBill/listPage.action");
            let resp = await jq.postJson(url, this.queryform.serializeJSON(), {});
            this.grid.bootstrapTable('load',resp);
        }catch (e){
            console.error(e);
            this.grid.bootstrapTable('load',{rows:[],total:0});
        }
        this.grid.bootstrapTable('hideLoading');
        $('#toolbar button').removeAttr('disabled');
    }

    private async findHighLightBill() {
        try {
            var url=this.toUrl("/ecommerceBill/findHighLightBill.action");
            return await jq.postJson(url, {}, {});
        } catch (e) {
            console.log(e);
            return {};
        }
    }

}
