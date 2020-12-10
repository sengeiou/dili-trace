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
        this.grid.on('check.bs.table uncheck.bs.table', async () => await this.checkAndShowHideBtns());
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
    async openAssignPage() {
        var row = this.rows[0];
        var url = this.toUrl('/detectRequest/assign.html?id=' + row.id);
        var dia = bs4pop.dialog({
            title: '指派检测员',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '80%',
            height: '60%',
            btns: []
        });
    }
    async doAssign(id, designatedId, designatedName) {
        bs4pop.removeAll();
        let promise = new Promise((resolve, reject) => {
            bs4pop.confirm('是否把当前检测单分配给检测员【' + designatedName + '】？<br/>', { type: 'warning', btns: [
                    { label: '是', className: 'btn-primary', onClick(cb) { resolve("true"); } },
                    { label: '否', className: 'btn-cancel', onClick(cb) { resolve("cancel"); } },
                ] });
        });
        let result = await promise;
        if (result == 'cancel') {
            return;
        }
        let url = this.toUrl("/detectRequest/doAssignDetector.action?id=" + id + "&designatedId=" + designatedId + "&designatedName=" + designatedName);
        try {
            var resp = await jq.ajaxWithProcessing({ type: "GET", url: url, processData: true, dataType: "json" });
            if (!resp.success) {
                bs4pop.alert(resp.message, { type: 'error' });
                return;
            }
            await this.queryGridData();
            bs4pop.removeAll();
            bs4pop.alert('操作成功', { type: 'info', autoClose: 600 });
        }
        catch (e) {
            bs4pop.alert('远程访问失败', { type: 'error' });
        }
    }
    openConfirmPage() {
    }
    get rows() {
        return this.grid.bootstrapTable('getSelections');
    }
    resetButtons() {
        var btnArray = this.btns;
        _.chain(btnArray).each((btn) => {
            $('#' + btn).hide();
        });
    }
    async checkAndShowHideBtns() {
        debugger;
        this.resetButtons();
        let rows = this.rows;
        try {
            if (rows.length > 0) {
                let detectStatus = rows[0].detectStatus;
                if (detectStatus == 0 || detectStatus == '') {
                    $('#assign-btn').show();
                }
            }
        }
        catch (e) {
            console.error(e);
        }
    }
}
