class UsualAddressIndex extends ListPage {
    constructor(grid: any, queryform: any) {
        super(grid, queryform, queryform.find('#query'), "/usualAddress/listPage.action");
        this.grid.on('check.bs.table', async () => await this.rowClick());
        this.grid.on('uncheck.bs.table', async () => await this.rowClick());

        $('#add-btn').on('click', async () => this.openAddPage());
        $('#edit-btn').on('click', async () => this.openEditPage());
        $('#delete-btn').on('click', async () => this.openDeletePage())
        // $('#export').on('click', async () => this.openExportPage());
    }
    //在增加或修改成功结束时由子页面调用到此方法
    private async editSuccess() {
        super.removeAllDialog();
        await popwrapper.alert('操作成功', {type: 'info', autoClose: 800});
        await this.queryGridData();
    }

    private async openAddPage() {

        let url = this.toUrl("/usualAddress/edit.html");

        //@ts-ignore
        window.dia = bs4pop.dialog({
            title: '增加常用地址',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '40%',
            height: '300',
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
        let url = this.toUrl("/usualAddress/edit.html?id=" + select.id);

        //@ts-ignore
        window.dia = bs4pop.dialog({
            title: '增加常用地址',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '40%',
            height: '300',
            btns: []
        });
    }

    private async openDeletePage() {
        let sure=await  popwrapper.confirm("确定要删除当前选中数据吗?");
        if(!sure){
            return;
        }
        var url=this.toUrl("/usualAddress/delete.action?id="+this.rows[0].id);
        let resp = await jq.getWithProcessing(url);
        if(!resp.success){
            //@ts-ignore
            bs4pop.notice(resp.message, {type: 'warning', position: 'topleft'});
            return;
        }
        await this.editSuccess();
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