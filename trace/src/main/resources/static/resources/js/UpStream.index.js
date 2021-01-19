class UpStreamIndex extends ListPage {
    constructor(grid, queryform) {
        super(grid, queryform, queryform.find('#query'), "/upStream/listPage.action");
        this.grid.on('check.bs.table', async () => await this.rowClick());
        this.grid.on('uncheck.bs.table', async () => await this.rowClick());
        window['UpStreamIndexObj'] = this;
        $('#btn_add').on('click', async () => this.openAddPage());
        $('#edit-btn').on('click', async () => this.openEditPage());
    }
    async editSuccess() {
        super.removeAllDialog();
        await popwrapper.alert('操作成功', { type: 'info', autoClose: 800 });
        await this.queryGridData();
    }
    removeAllAndLoadData() {
        bs4pop.removeAll();
        $(this).closest("body").removeClass("modal-open");
        (async () => {
            await this.queryGridData();
        })();
    }
    async openAddPage() {
        let url = this.toUrl("/upStream/edit.html");
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
    async openEditPage() {
        if (this.rows.length == 0) {
            bs4pop.alert('请选择要修改的数据', { type: 'error' });
            return;
        }
        var select = this.rows[0];
        let url = this.toUrl("/upStream/edit.html?id=" + select.id);
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
    async resetButtons() {
        $('#edit-btn').hide();
        $('#delete-btn').hide();
        $('#export').hide();
    }
}
