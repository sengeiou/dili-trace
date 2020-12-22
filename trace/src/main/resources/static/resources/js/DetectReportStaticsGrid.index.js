class DetectReportStaticsGrid extends ListPage {
    constructor(grid, queryform, toolbar) {
        super(grid, queryform, queryform.find('#query'), "/newRegisterBill/listStaticsPage.action");
        this.toolbar = toolbar;
        this.btns = this.toolbar.find('button');
        this.uid = _.uniqueId("trace_id_");
        $(window).on('resize', () => this.grid.bootstrapTable('resetView'));
        let cthis = this;
        window['DetectReportStaticsGridObj'] = this;
        $('#detail-btn').on('click', async () => await this.openDetailPage());
        window.addEventListener('message', function (e) {
            var data = JSON.parse(e.data);
            if (data.obj && data.fun) {
                if (data.obj == 'DetectReportStaticsGridObj') {
                    cthis[data.fun].call(cthis, data.args);
                }
            }
        }, false);
    }
    removeAllAndLoadData() {
        bs4pop.removeAll();
        (async () => {
            await super.queryGridData();
        })();
    }
    async openDetailPage() {
        var row = this.rows[0];
        var url = this.toUrl('/newRegisterBill/view.html?id=' + row.billId);
        var dia = bs4pop.dialog({
            title: '报备单详情',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
    }
}
