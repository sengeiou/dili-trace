class DetectRequestGridGrid extends WebConfig {
    grid: any;
    uid:string;
    queryform: any;
    // highLightBill: any;
    btns:string[];

    constructor(grid: any, queryform: any) {
        super();
        this.grid = grid;
        this.queryform = queryform;
        this.uid=_.uniqueId("trace_id_");

        $(window).on('resize',()=> this.grid.bootstrapTable('resetView') );

        var cthis=this;
        window['DetectRequestGridObj']=this;

        // 绑定按钮事件
        this.btns= ['assign-btn', 'confirm-btn'];
        $('#assign-btn').on('click',async ()=>await this.openAssignPage());
        $('#confirm-btn').on('click',async ()=>await this.openConfirmPage());

        // 绑定查询按钮事件
        this.queryform.find('#query').click(async () => await this.queryGridData());

        // ？？？
        // window.addEventListener('message', function(e) {
        //     var data=JSON.parse(e.data);
        //     if(data.obj&&data.fun){
        //         if(data.obj=='DetectRequestGridObj'){
        //             cthis[data.fun].call(cthis,data.args)
        //         }
        //     }
        // }, false);
    }

    /**
     * 查询表格数据
     */
    private async queryGridData(){
        if (!this.queryform.validate().form()) {
            //@ts-ignore
            bs4pop.notice("请完善必填项", {type: 'warning', position: 'topleft'});
            return;
        }
        this.grid.bootstrapTable('refresh');
    }

    /**
     * 重置查询条件
     */
    public removeAllAndLoadData(){
        //@ts-ignore
        bs4pop.removeAll();
        (async ()=>{
            await this.queryGridData();
        })();
    }

    /**
     * 分配检测员
     */
    private openAssignPage() {

    }

    /**
     * 检测员确认接单
     */
    private openConfirmPage() {

    }

    get rows() {
        return  this.grid.bootstrapTable('getSelections');
    }
}
