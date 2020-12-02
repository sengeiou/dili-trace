class EcommerceBillGrid extends WebConfig {
    constructor(grid, queryform) {
        super();
        this.grid = grid;
        this.queryform = queryform;
        this.queryform.find('#query').click(async () => await this.queryGridData());
        (async () => {
            await this.queryGridData();
        })();
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
        this.highLightBill = await this.findHighLightBill();
        try {
            let url = this.toUrl("/ecommerceBill/listPage.action");
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
    async findHighLightBill() {
        try {
            var url = this.toUrl("/ecommerceBill/findHighLightBill.action");
            return await jq.postJson(url, {}, {});
        }
        catch (e) {
            console.log(e);
            return {};
        }
    }
}
//# sourceMappingURL=ecommercebill.index.js.map