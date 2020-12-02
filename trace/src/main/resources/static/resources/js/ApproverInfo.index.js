class ApproverInfoIndex extends WebConfig {
    constructor(grid, queryform) {
        super();
        this.grid = grid;
        this.queryform = queryform;
        (async () => this.init())();
    }
    async init() {
        this.queryform.find('#query').click(async () => await this.queryGridData());
        this.grid.on('check.bs.table', async () => await this.rowClick());
        this.grid.on('uncheck.bs.table', async () => await this.rowClick());
        $('#add-btn').on('click', async () => this.openEditPage());
        $('#edit-btn').on('click', async () => this.openEditPage());
        $('#detail-btn').on('click', async () => this.openDetailPage());
        await this.queryGridData();
    }
    async openDetailPage() {
    }
    async openEditPage() {
    }
    async rowClick() {
        debugger;
    }
    async queryGridData() {
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
            let url = this.toUrl("/approverInfo/listPage.action");
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
//# sourceMappingURL=ApproverInfo.index.js.map