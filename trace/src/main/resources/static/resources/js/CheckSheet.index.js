class CheckSheetIndex extends ListPage {
    constructor(grid, queryform, toolbar, billType) {
        super(grid, queryform, queryform.find('#query'), "/checkSheet/listPage.action", { 'billType': billType });
        this.grid = grid;
        this.queryform = queryform;
        window['checkSheetIndex'] = this;
        $('#detail-btn').on('click', async () => await this.doDetail());
        $('#reprint-btn').on('click', async () => await this.doPrint());
        $('#export').on('click', async () => await this.exportGrid());
    }
    exportGrid() {
        bui.util.doExport(this.grid.attr('id'), this.queryform.attr('id'), '', this.otherParams);
    }
    removeAllAndLoadData() {
        bs4pop.removeAll();
        $("body").removeClass("modal-open");
        (async () => {
            await this.queryGridData();
        })();
    }
    doDetail() {
        let row = this.grid.bootstrapTable("getSelections");
        if (row.length == 0) {
            bs4pop.alert("请选择一条数据", { type: 'warning' });
            return;
        }
        if (row.length > 1) {
            bs4pop.alert("请选择数据过多", { type: 'warning' });
            return;
        }
        let selected_id = row[0].id;
        let url = this.toUrl('/checkSheet/view.html?id=' + selected_id);
        $(window.document.body).append('<iframe id="checksheet_view_win" name="view_win" src="' + url + '" style="border:0px;width:100%;height:100%;position:fixed;left:0;top:0;z-index:99999"></iframe>');
    }
    doPrint() {
        let row = this.grid.bootstrapTable("getSelections");
        if (row.length == 0) {
            bs4pop.alert("请选择一条数据", { type: 'warning' });
            return;
        }
        if (row.length > 1) {
            bs4pop.alert("请选择数据过多", { type: 'warning' });
            return;
        }
        let selected_id = row[0].id;
        var resp = this.findPrintableCheckSheet(selected_id);
        if (resp.code == "200") {
            if (typeof (callbackObj) != 'undefined' && callbackObj.printDirect) {
                callbackObj.boothPrintPreview(JSON.stringify(resp.data), 'TestReportDocument', 0);
            }
            else {
                bs4pop.alert("请升级客户端或者在客户端环境运行当前程序", { type: 'error', title: '错误' });
            }
        }
        else {
            bs4pop.alert(ret.result, { type: 'error', title: '错误' });
        }
    }
    findPrintableCheckSheet(id) {
        var result = {};
        $.ajax({
            type: "POST",
            url: '/checkSheet/findPrintableCheckSheet.action',
            data: JSON.stringify({ id: id }),
            processData: true,
            dataType: "json",
            async: false,
            contentType: "application/json; charset=utf-8",
            success: function (ret) {
                result = ret;
            },
            error: function () {
                result = { "code": "5000", result: "远程访问失败" };
            }
        });
        return result;
    }
    async init_back() {
        this.queryform.find('#query').click(async () => await this.queryGridData());
        this.grid.on('check.bs.table', async () => await this.rowClick());
        this.grid.on('uncheck.bs.table', async () => await this.rowClick());
        $('#detail-btn').on('click', async () => this.openDetailPage());
        $('#reprint-btn').on('click', async () => this.openRePrintPage());
        await this.queryGridData();
    }
    async openDetailPage() {
    }
    async openRePrintPage() {
    }
    async rowClick() {
    }
    async queryGridData_back() {
        if (!this.queryform.validate().form()) {
            bs4pop.notice("请完善必填项", { type: 'warning', position: 'topleft' });
            return;
        }
        await this.remoteQuery();
    }
    async remoteQuery() {
        $('#toolbar button').attr('disabled', "disabled");
        this.grid.bootstrapTable('showLoading');
        try {
            let url = this.toUrl("/checkSheet/listPage.action");
            let resp = await jq.postJson(url, this.queryform.serializeJSON(), {});
            this.grid.bootstrapTable('load', resp);
        }
        catch (e) {
            console.error(e);
            this.grid.bootstrapTable('load', { rows: [], total: 0 });
        }
        this.grid.bootstrapTable('hideLoading');
        $('#toolbar button').removeAttr('disabled');
    }
}
