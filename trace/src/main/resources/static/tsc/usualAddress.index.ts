class UsualAddressIndex extends  ListPage{
    constructor(grid: any,queryform:any) {
        super(grid,queryform,queryform.find('#query'),"/usualAddress/listPage.action");
        this.grid.on('check.bs.table', async () => await this.rowClick());
        this.grid.on('uncheck.bs.table',async () => await this.rowClick());

        $('#add-btn').on('click',async ()=>this.openAddPage());
        $('#edit-btn').on('click',async ()=>this.openEditPage());
        $('#delete-btn').on('click', async  ()=> this.openDeletePage())
        $('#export').on('click',async ()=>this.openExportPage());
    }

    private async openAddPage() {

        let url = this.toUrl("/usualAddress/edit.html");

        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '增加常用地址',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '30%',
            height: '60%',
            btns: []
        });
    }
    private async openEditPage() {

        if(this.rows.length==0){
            //@ts-ignore
            bs4pop.alert('请选择要修改的数据', {type: 'error'});
            return;
        }
        var select=this.rows[0];
        let url = this.toUrl("/usualAddress/edit.html?id="+select.id);

        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '增加常用地址',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '60%',
            height: '98%',
            btns: []
        });
    }
    private async openDeletePage() {

    }
    private async  openExportPage() {

    }
    private async rowClick() {
    debugger
    }

}