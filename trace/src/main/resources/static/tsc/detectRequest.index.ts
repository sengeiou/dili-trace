class DetectRequestGridGrid extends ListPage {
    uid:string;
    btns:string[];
    toolbar:any;

    constructor(grid: any, queryform: any, toolbar: any) {
        super(grid,queryform,queryform.find('#query'),"/detectRequest/listPage.action");
        this.toolbar=toolbar;
        this.btns=this.toolbar.find('button');
        this.uid=_.uniqueId("trace_id_");

        $(window).on('resize',()=> this.grid.bootstrapTable('resetView') );

        var cthis=this;
        window['DetectRequestGridObj']=this;

        // 绑定按钮事件
        $('#assign-btn').on('click',async ()=>await this.openAssignPage());
        $('#confirm-btn').on('click',async ()=>await this.openConfirmPage());

        this.grid.on('check.bs.table uncheck.bs.table', async () => await this.resetButtons());
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
     * 进入分配检测员页面
     */
    private async  openAssignPage(){
        var row=this.rows[0]
        var url=this.toUrl('/detectRequest/assign.html?id='+row.id);
        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '指派检测员',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '80%',
            height: '60%',
            btns: []
        });
    }

    /**
     * 分配检测员
     * @param id
     * @param designatedId
     * @param designatedName
     */
    public async doAssign(id:string, designatedId:string, designatedName:string){
        //@ts-ignore
        bs4pop.removeAll();
        let promise = new Promise((resolve, reject) => {
            //@ts-ignore
            bs4pop.confirm('是否把当前检测单分配给检测员【'+designatedName+'】？<br/>',
                {type: 'warning',btns: [
                        {label: '是', className: 'btn-primary',onClick(cb){  resolve("true");}},
                        {label: '否', className: 'btn-cancel',onClick(cb){resolve("cancel");}},
                    ]});

        });
        let result = await promise;
        if(result=='cancel'){
            return;
        }

        let url= this.toUrl("/detectRequest/doAssignDetector.action?id="+id+"&designatedId="+designatedId+"&designatedName="+designatedName);
        try{
            var resp=await jq.ajaxWithProcessing({type: "GET",url: url,processData:true,dataType: "json"});
            if(!resp.success){
                //@ts-ignore
                bs4pop.alert(resp.message, {type: 'error'});
                return;
            }
            await this.queryGridData();
            //@ts-ignore
            bs4pop.removeAll()
            //@ts-ignore
            bs4pop.alert('操作成功', {type: 'info',autoClose: 600});
        }catch (e){
            //@ts-ignore
            bs4pop.alert('远程访问失败', {type: 'error'});
        }
    }

    /**
     * 检测员确认接单
     */
    private openConfirmPage() {

    }

    get rows() {
        return  this.grid.bootstrapTable('getSelections');
    }

    public async resetButtons() {
        var btnArray=this.btns;
        // _.chain(btnArray).each((btn)=> {
        //     $(btn).hide();
        // });
        // await this.queryEventAndSetBtn();
    }

}
