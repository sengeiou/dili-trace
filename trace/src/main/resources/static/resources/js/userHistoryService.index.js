class UserHistoryServiceIndex extends WebConfig {
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
        await this.queryGridData();
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
            let url = this.toUrl("/userHistory/listPage.action");
            let resp = await jq.postJson(url, this.queryform.serializeJSON(), {});
            this.grid.bootstrapTable('load', resp);
            await this.queryStatics();
        }
        catch (e) {
            console.error(e);
            this.grid.bootstrapTable('load', { rows: [], total: 0 });
        }
        this.grid.bootstrapTable('hideLoading');
        $('#toolbar button').removeAttr('disabled');
    }
    async queryStatics() {
        try {
            let url = this.toUrl("/userHistory/queryStatics.action");
            let resp = await jq.postJson(url, this.queryform.serializeJSON(), {});
            console.info("====>", resp);
            $('#plateAmount').val(resp.plateAmount);
        }
        catch (e) {
            $('#plateAmount').val('0');
        }
    }
}
