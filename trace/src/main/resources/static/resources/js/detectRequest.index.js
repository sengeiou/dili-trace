class DetectRequestGridGrid extends WebConfig {
    constructor(grid, queryform) {
        super();
        this.grid = grid;
        this.queryform = queryform;
        this.uid = _.uniqueId("trace_id_");
        $(window).on('resize', () => this.grid.bootstrapTable('resetView'));
        var cthis = this;
        window['DetectRequestGridObj'] = this;
        this.btns = ['assign-btn', 'confirm-btn'];
        $('#assign-btn').on('click', async () => await this.openAssignPage());
        $('#confirm-btn').on('click', async () => await this.openConfirmPage());
        this.queryform.find('#query').click(async () => await this.queryGridData());
    }
    async queryGridData() {
        if (!this.queryform.validate().form()) {
            bs4pop.notice("请完善必填项", { type: 'warning', position: 'topleft' });
            return;
        }
        this.grid.bootstrapTable('refresh');
    }
    removeAllAndLoadData() {
        bs4pop.removeAll();
        (async () => {
            await this.queryGridData();
        })();
    }
    openAssignPage() {
    }
    openConfirmPage() {
    }
    get rows() {
        return this.grid.bootstrapTable('getSelections');
    }
}
//# sourceMappingURL=detectRequest.index.js.map