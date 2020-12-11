class ApproverInfoIndex extends ListPage {
    constructor(grid, queryform) {
        super(grid, queryform, queryform.find('#query'), "/approverInfo/listPage.action");
        this.grid.on('check.bs.table', async () => await this.rowClick());
        this.grid.on('uncheck.bs.table', async () => await this.rowClick());
        $('#add-btn').on('click', async () => this.openAddPage());
        $('#edit-btn').on('click', async () => this.openEditPage());
        $('#detail-btn').on('click', async () => this.openDetailPage());
    }
    async editSuccess() {
        bs4pop.removeAll();
        await popwrapper.alert('操作成功', { type: 'info', autoClose: 800 });
        await this.queryGridData();
    }
    async openAddPage() {
        let url = this.toUrl("/approverInfo/edit.html");
        window.dia = bs4pop.dialog({
            title: '增加签名',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '40%',
            height: '400',
            btns: []
        });
    }
    async openEditPage() {
        if (this.rows.length == 0) {
            bs4pop.alert('请选择要修改的数据', { type: 'error' });
            return;
        }
        var select = this.rows[0];
        let url = this.toUrl("/approverInfo/edit.html?id=" + select.id);
        window.dia = bs4pop.dialog({
            title: '修改签名',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '40%',
            height: '60%',
            btns: []
        });
    }
    async openDetailPage() {
        if (this.rows.length == 0) {
            bs4pop.alert('请选择要查看的数据', { type: 'error' });
            return;
        }
        var select = this.rows[0];
        let url = this.toUrl("/approverInfo/view.html?id=" + select.id);
        window.dia = bs4pop.dialog({
            title: '查看签名详情',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '40%',
            height: '60%',
            btns: []
        });
    }
    async rowClick() {
        this.resetButtons();
        if (this.rows.length == 0) {
            return;
        }
        if (this.rows.length == 1) {
            $('#edit-btn').show();
            $('#detail-btn').show();
        }
    }
    resetButtons() {
        $('#edit-btn').hide();
        $('#detail-btn').hide();
    }
}
