class UsualAddressIndex extends ListPage {
    constructor(grid, queryform) {
        super(grid, queryform, queryform.find('#query'), "/usualAddress/listPage.action");
        this.grid.on('check.bs.table', async () => await this.rowClick());
        this.grid.on('uncheck.bs.table', async () => await this.rowClick());
        $('#add-btn').on('click', async () => this.openAddPage());
        $('#edit-btn').on('click', async () => this.openEditPage());
        $('#delete-btn').on('click', async () => this.openDeletePage());
        $('#export').on('click', async () => this.openExportPage());
        window.addEventListener('message', async (e) => this.handleMessage(e), false);
    }
    handleMessage(e) {
        let data = JSON.parse(e.data);
        if (data.fun) {
            this[data.fun].call(this, data.args);
        }
    }
    async editSuccess() {
        bs4pop.removeAll();
        await popwrapper.alert('操作成功', { type: 'info', autoClose: 800 });
        await this.queryGridData();
    }
    async openAddPage() {
        let url = this.toUrl("/usualAddress/edit.html");
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
    async openEditPage() {
        if (this.rows.length == 0) {
            bs4pop.alert('请选择要修改的数据', { type: 'error' });
            return;
        }
        var select = this.rows[0];
        let url = this.toUrl("/usualAddress/edit.html?id=" + select.id);
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
    async openDeletePage() {
    }
    async openExportPage() {
    }
    async rowClick() {
        this.resetButtons();
        if (this.rows.length == 0) {
            return;
        }
        if (this.rows.length == 1) {
            $('#edit-btn').show();
            $('#delete-btn').show();
        }
        $('#export').show();
    }
    resetButtons() {
        $('#edit-btn').hide();
        $('#delete-btn').hide();
        $('#export').hide();
    }
}
