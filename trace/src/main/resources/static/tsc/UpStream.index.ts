class UpStreamIndex extends ListPage {
    constructor(grid: any, queryform: any) {
        super(grid, queryform, queryform.find('#query'), "/upStream/listPage.action");
        this.grid.on('check.bs.table', async () => await this.rowClick());
        this.grid.on('uncheck.bs.table', async () => await this.rowClick());
        window['UpStreamIndexObj']=this;

        $('#btn_add').on('click', async () => this.openAddPage());
        $('#edit-btn').on('click', async () => this.openEditPage());
        // $('#export').on('click', async () => this.openExportPage());
    }
    //在增加或修改成功结束时由子页面调用到此方法
    public async editSuccess() {
        super.removeAllDialog();
        await popwrapper.alert('操作成功', {type: 'info', autoClose: 800});
        await this.queryGridData();
    }

    public removeAllAndLoadData() {
        //@ts-ignore
        bs4pop.removeAll();
        //@ts-ignore
        $(this).closest("body").removeClass("modal-open");
        (async () => {
            await this.queryGridData();
        })();
    }

    private async openAddPage() {

        let url = this.toUrl("/upStream/edit.html");
        //@ts-ignore
        window.dia = bs4pop.dialog({
            title: '上游新增',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '70%',
            height: '700',
            btns: []
        });
    }

    private async openEditPage() {
        if (this.rows.length == 0) {
            //@ts-ignore
            bs4pop.alert('请选择要修改的数据', {type: 'error'});
            return;
        }
        var select = this.rows[0];
        let url = this.toUrl("/upStream/edit.html?id=" + select.id);

        //@ts-ignore
        window.dia = bs4pop.dialog({
            title: '上游企业修改',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '70%',
            height: '700',
            btns: []
        });
    }


    private async rowClick() {
        this.resetButtons();
         if(this.rows.length==0){
             return;
         }
         if(this.rows.length==1){
             $('#edit-btn').show();
             $('#delete-btn').show();
         }
        $('#export').show();
    }
    public async resetButtons(){
        $('#edit-btn').hide();
        $('#delete-btn').hide();
        $('#export').hide();
    }

}