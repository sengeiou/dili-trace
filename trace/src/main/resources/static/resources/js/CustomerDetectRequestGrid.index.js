class CustomerDetectRequestGrid extends ListPage {
    constructor(grid, queryform, toolbar) {
        super(grid, queryform, queryform.find('#query'), "/customerDetectRequest/listPage.action");
        this.toolbar = toolbar;
        this.btns = this.toolbar.find('button');
        this.uid = _.uniqueId("trace_id_");
        $(window).on('resize', () => this.grid.bootstrapTable('resetView'));
        var cthis = this;
        window['CustomerDetectRequestGrid'] = this;
        $('#booking-btn').on('click', async () => await this.doBookingRequest());
        $('#assign-btn').on('click', async () => await this.openAssignPage());
        $('#sampling-btn').on('click', async () => await this.doSamplingCheck());
        $('#auto-btn').on('click', async () => await this.doAutoCheck());
        $('#manual-btn').on('click', async () => await this.doManualCheck());
        $('#review-btn').on('click', async () => await this.doReviewCheck());
        $('#undo-btn').on('click', async () => await this.doUndo());
        $('#detail-btn').on('click', async () => await this.openDetailPage());
        $('#createSheet-btn').on('click', async () => await this.openCreateSheetPage());
        $('#upload-handleresult-btn').on('click', async () => await this.openUploadHandleResultPage());
        $('select[name="detectResultSelect"]').on('change', async (o, n) => {
            var data = JSON.parse($('select[name="detectResultSelect"]').val().toString());
            $('input[name="detectType"]').val(data['detectType']);
            $('input[name="detectResult"]').val(data['detectResult']);
        });
        this.grid.on('check.bs.table uncheck.bs.table', async () => await this.resetButtons());
    }
    async openUploadHandleResultPage() {
        var row = this.rows[0];
        var url = this.toUrl('/newRegisterBill/uploadHandleResult.html?id=' + row.billId);
        var dia = bs4pop.dialog({
            title: '上传处理结果',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
    }
    async openCreateSheetPage() {
        let rows = this.rows;
        let idList = rows.map(function (v, i) { return v.billId; });
        let url = this.toUrl('/checkSheet/edit.html?' + $.param({ idList: idList }, true));
        window.dia = bs4pop.dialog({
            title: '打印检测报告',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
    }
    async doBookingRequest() {
        debugger;
        var selected = this.rows[0];
        if (selected.verifyStatus == 30) {
            bs4pop.alert('审核未通过登记单不能进行预约申请', { type: 'error' });
        }
        var url = this.toUrl("/customerDetectRequest/doBookingRequest.action?billId=" + selected.billId);
        let sure = await popwrapper.confirm('请确认是否预约检测？', undefined);
        if (!sure) {
            return;
        }
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
    async doManualCheck() {
        let selected = this.rows[0];
        bs4pop.removeAll();
        var url = this.toUrl('/customerDetectRequest/manualCheck_confirm.html?billId=' + selected.billId);
        var manual_dia = bs4pop.dialog({
            title: '人工检测',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '80%',
            height: '60%',
            btns: []
        });
    }
    removeAllAndLoadData() {
        bs4pop.removeAll();
        $("body").removeClass("modal-open");
        (async () => {
            await this.queryGridData();
        })();
    }
    async openAssignPage() {
        var row = this.rows[0];
        if (_.isUndefined(row.billId) || row.billId == null) {
            bs4pop.alert('请行进行预约检测', { type: 'error' });
            return;
        }
        var url = this.toUrl('/customerDetectRequest/confirm.html?billId=' + row.billId);
        var dia = bs4pop.dialog({
            title: '接单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '80%',
            height: '60%',
            btns: []
        });
    }
    async doAssign(billId, designatedId, designatedName, detectTime) {
        bs4pop.removeAll();
        let promise = new Promise((resolve, reject) => {
            bs4pop.confirm('是否确认接单？<br/>', { type: 'warning', btns: [
                    { label: '是', className: 'btn-primary', onClick(cb) { resolve("true"); } },
                    { label: '否', className: 'btn-cancel', onClick(cb) { resolve("cancel"); } },
                ] });
        });
        let result = await promise;
        if (result == 'cancel') {
            return;
        }
        let url = this.toUrl("/customerDetectRequest/doConfirm.action?billId=" + billId + "&designatedId=" + designatedId + "&designatedName=" + designatedName + "&detectTime=" + detectTime);
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
    async returnAssign(billId) {
        bs4pop.removeAll();
        let promise = new Promise((resolve, reject) => {
            bs4pop.prompt('请输入退回原因:', '', {
                title: '是否确认退回？',
                hideRemove: true,
                width: 500,
                className: "max20",
                onShowEnd: function () {
                    $(".max20").find("input").attr("maxlength", 20);
                },
            }, function (sure, value) {
                if (sure == true) {
                    if ($.trim(value) == '') {
                        bs4pop.alert('请输入退回原因', { type: 'error' });
                        return;
                    }
                    resolve(value);
                }
                else {
                    reject(value);
                }
            });
        });
        let data = { id: billId, returnReason: '' };
        try {
            data.returnReason = await promise;
        }
        catch (e) {
            return;
        }
        let url = this.toUrl("/customerDetectRequest/doReturn.action?billId=" + billId);
        try {
            let resp = await jq.postJsonWithProcessing(url, data);
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
    async doSamplingCheck() {
        var selected = this.rows[0];
        var url = this.toUrl("/newRegisterBill/doSamplingCheck.action?id=" + selected.billId);
        let sure = await popwrapper.confirm('请确认是否采样检测？', undefined);
        if (!sure) {
            return;
        }
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
    async doAutoCheck() {
        var selected = this.rows[0];
        let cthis = this;
        var url = this.toUrl("/newRegisterBill/doAutoCheck.action?id=" + selected.billId);
        let sure = await popwrapper.confirm('请确认是否主动送检？', undefined);
        if (!sure) {
            return;
        }
        try {
            var resp = await jq.ajaxWithProcessing({ type: "GET", url: url, processData: true, dataType: "json" });
            if (!resp.success) {
                bs4pop.alert(resp.message, { type: 'error' });
                return;
            }
            await cthis.queryGridData();
            bs4pop.removeAll();
            bs4pop.alert('操作成功', { type: 'info', autoClose: 600 });
        }
        catch (e) {
            bs4pop.alert('远程访问失败', { type: 'error' });
        }
    }
    async doReviewCheck() {
        var selected = this.rows[0];
        let url = this.toUrl("/newRegisterBill/doReviewCheck.action?id=" + selected.billId);
        let sure = await popwrapper.confirm('请确认是否复检？', undefined);
        if (!sure) {
            return;
        }
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
    async doUndo() {
        let selected = this.rows[0];
        let cthis = this;
        let url = this.toUrl("/customerDetectRequest/doUndo.action?billId=" + selected.billId);
        bs4pop.confirm('请确认是否撤销？', undefined, async function (sure) {
            if (!sure) {
                return;
            }
            try {
                var resp = await jq.ajaxWithProcessing({ type: "GET", url: url, processData: true, dataType: "json" });
                if (!resp.success) {
                    bs4pop.alert(resp.message, { type: 'error' });
                    return;
                }
                await cthis.queryGridData();
                bs4pop.removeAll();
                bs4pop.alert('操作成功', { type: 'info', autoClose: 600 });
            }
            catch (e) {
                bs4pop.alert('远程访问失败', { type: 'error' });
            }
        });
    }
    async openDetailPage() {
        var row = this.rows[0];
        var url = this.toUrl('/customerDetectRequest/view.html?billId=' + row.billId);
        var dia = bs4pop.dialog({
            title: '检测单详情',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
    }
    get rows() {
        return this.grid.bootstrapTable('getSelections');
    }
    async resetButtons() {
        var btnArray = this.btns;
        _.chain(btnArray).each((btn) => {
            $(btn).hide();
        });
        await this.queryEventAndSetBtn();
    }
    async queryEventAndSetBtn() {
        var rows = this.rows;
        try {
            var billIdList = _.chain(rows).map(v => v.billId).value();
            var resp = await jq.postJson(this.toUrl('/customerDetectRequest/queryEvents.action'), billIdList);
            resp.forEach(btnid => { $('#' + btnid).show(); });
        }
        catch (e) {
            console.error(e);
        }
    }
}
