class UsualAddressIndex extends ListPage {
    constructor(grid, queryform) {
        super(grid, queryform, queryform.find('#query'), "/usualAddress/listPage.action");
        this.grid.on('check.bs.table', async () => await this.rowClick());
        this.grid.on('uncheck.bs.table', async () => await this.rowClick());
        $('#add-btn').on('click', async () => this.openAddPage());
        $('#edit-btn').on('click', async () => this.openEditPage());
        $('#delete-btn').on('click', async () => this.openDeletePage());
        $('#export').on('click', async () => this.openExportPage());
    }
    async openAddPage() {
        let url = this.toUrl("/usualAddress/edit.html");
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
    async openEditPage() {
        if (this.rows.length == 0) {
            bs4pop.alert('请选择要修改的数据', { type: 'error' });
            return;
        }
        var select = this.rows[0];
        let url = this.toUrl("/usualAddress/edit.html?id=" + select.id);
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
    async openDeletePage() {
    }
    async openExportPage() {
    }
    async rowClick() {
        debugger;
    }
}
