class CheckSheetIndex extends ListPage {
    toolbar: any;

    constructor(grid: any, queryform: any, toolbar: any) {
        super(grid, queryform, queryform.find('#query'), "/checkSheet/listPage.action");
        this.grid = grid;
        this.queryform = queryform;
        window['checkSheetIndex'] = this;
        $('#detail-btn').on('click', async () => await this.doDetail());
        $('#reprint-btn').on('click', async () => await this.doPrint());
        //async ()=>this.init())();
    }

    private doDetail() {
        let row = this.grid.bootstrapTable("getSelections");
        if (row.length == 0) {
            //@ts-ignore
            bs4pop.alert("请选择一条数据", {type: 'warning'});
            return;
        }
        if (row.length > 1) {
            //@ts-ignore
            bs4pop.alert("请选择数据过多", {type: 'warning'});
            return;
        }
        let selected_id = row[0].id;
        let url = this.toUrl('/checkSheet/view.html?id=' + selected_id);
        //@ts-ignore
        bs4pop.dialog({
            title: '查看检测报告',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '78%',
            height: '78%',
            btns: [],
            onShowEnd: function () {
            }
        });
    }

    private doPrint(){
        let row = this.grid.bootstrapTable("getSelections");
        if (row.length == 0) {
            //@ts-ignore
            bs4pop.alert("请选择一条数据", {type: 'warning'});
            return;
        }
        if (row.length > 1) {
            //@ts-ignore
            bs4pop.alert("请选择数据过多", {type: 'warning'});
            return;
        }
        let selected_id=row[0].id;
        var resp=this.findPrintableCheckSheet(selected_id);
        //@ts-ignore
        if(resp.code=="200"){
            //@ts-ignore
            if(typeof(callbackObj)!='undefined'&&callbackObj.printDirect){
                //@ts-ignore
                callbackObj.boothPrintPreview(JSON.stringify(resp.data), 'TestReportDocument',0)
            }else{
                //@ts-ignore
                bs4pop.alert("请升级客户端或者在客户端环境运行当前程序", {type: 'error',title:'错误'});
            }
        }else{
            //@ts-ignore
            bs4pop.alert(ret.result, {type: 'error',title:'错误'});
        }
    }

    private findPrintableCheckSheet(id){
        var result={};
        $.ajax({
            type: "POST",
            url: '/checkSheet/findPrintableCheckSheet.action',
            data: JSON.stringify({id:id}),
            processData:true,
            dataType: "json",
            async : false,
            contentType: "application/json; charset=utf-8",
            success: function (ret) {
                result=ret;
            },
            error: function(){
                result={"code":"5000",result:"远程访问失败"}

            }
        });
        return result;
    }

    private async init_back() {
        this.queryform.find('#query').click(async () => await this.queryGridData());

        this.grid.on('check.bs.table', async () => await this.rowClick());
        this.grid.on('uncheck.bs.table', async () => await this.rowClick());

        $('#detail-btn').on('click', async () => this.openDetailPage());
        $('#reprint-btn').on('click', async () => this.openRePrintPage());
        //load data
        await this.queryGridData();
    }

    private async openDetailPage() {


    }

    private async openRePrintPage() {


    }

    private async rowClick() {
    }

    private async queryGridData_back() {
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
        try {
            let url = this.toUrl("/checkSheet/listPage.action");
            let resp = await jq.postJson(url, this.queryform.serializeJSON(), {});
            this.grid.bootstrapTable('load', resp);
        } catch (e) {
            console.error(e);
            this.grid.bootstrapTable('load', {rows: [], total: 0});
        }
        this.grid.bootstrapTable('hideLoading');
        $('#toolbar button').removeAttr('disabled');
    }


}