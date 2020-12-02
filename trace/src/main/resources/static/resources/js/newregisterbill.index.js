var Table = WebAssembly.Table;
class NewRegisterBillGrid extends WebConfig {
    constructor(grid, queryform) {
        super();
        this.handleTimeUpdateEvent = (event) => {
        };
        this.grid = grid;
        this.queryform = queryform;
        this.grid.on('check.bs.table uncheck.bs.table', () => this.onClickRow());
        this.queryform.find('#query').click(async () => await this.queryGridData());
        $(window).on('resize', () => this.grid.bootstrapTable('resetView'));
        $('#edit-btn').on('click', async () => await this.openEditPage());
        $('#btn_add').on('click', async () => await this.openCreatePage());
        $('#copy-btn').on('click', async () => await this.openCopyPage());
        $('#upload-origincertifiy-btn').on('click', async () => await this.openUploadOriginCertifyPage());
        $('#upload-detectreport-btn').on('click', async () => await this.openUploadDetectReportPage());
        $('#upload-handleresult-btn').on('click', async () => await this.openUploadHandleResultPage());
        $('#detail-btn').on('click', async () => await this.openDetailPage());
        $('#audit-btn').on('click', async () => await this.openAuditPage());
        $('#undo-btn').on('click', async () => await this.doUndo());
        $('#batch-undo-btn').on('click', async () => await this.doBatchUndo());
        $('#batch-audit-btn').on('click', async () => await this.doBatchAudit());
        $('#createsheet-btn').on('click', async () => await this.openCreateSheetPage());
        $('#sampling-btn').on('click', async () => await this.doSamplingCheck());
        $('#batch-auto-btn').on('click', async () => await this.doBatchAutoCheck());
        $('#auto-btn').on('click', async () => await this.doAutoCheck());
        $('#remove-reportAndcertifiy-btn').on('click', async () => await this.doRemoveReportAndCertifiy());
        $('#batch-sampling-btn').on('click', async () => await this.doBatchSamplingCheck());
        $('#audit-withoutDetect-btn').on('click', async () => await this.doAuditWithoutDetect());
        $('#review-btn').on('click', async () => await this.doReviewCheck());
        (async () => {
            await this.queryGridData();
        })();
    }
    async doReviewCheck() {
        var selected = this.rows[0];
        let cthis = this;
        let url = this.toUrl("/newRegisterBill/doReviewCheck.action?id=" + selected.id);
        bs4pop.confirm('请确认是否复检？', undefined, async function (sure) {
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
                TLOG.component.operateLog('登记单管理', "复检", "【编号】:" + selected.code);
                layer.alert('操作成功', { title: '操作', time: 600 });
            }
            catch (e) {
                bs4pop.alert('远程访问失败', { type: 'error' });
            }
        });
    }
    async doAuditWithoutDetect() {
        var selected = this.rows[0];
        let cthis = this;
        let url = this.toUrl("/newRegisterBill/doAuditWithoutDetect.action");
        bs4pop.confirm('确认审核不检测？', undefined, async function (sure) {
            if (!sure) {
                return;
            }
            try {
                var resp = await jq.ajaxWithProcessing({ type: "POST", data: { id: selected.id }, url: url, processData: true, dataType: "json" });
                if (!resp.success) {
                    bs4pop.alert(resp.message, { type: 'error' });
                    return;
                }
                await cthis.queryGridData();
                TLOG.component.operateLog('登记单管理', "审核不检测", "【编号】:" + selected.code);
                layer.alert('操作成功', { title: '操作', time: 600 });
            }
            catch (e) {
                bs4pop.alert('远程访问失败', { type: 'error' });
            }
        });
    }
    async doBatchSamplingCheck() {
        var rows = this.rows;
        if (rows.length == 0) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
        var codeList = [];
        var batchIdList = [];
        for (var i = 0; i < rows.length; i++) {
            if (rows[i].$_state == 2) {
                batchIdList.push(rows[i].id);
                codeList.push(rows[i].code);
            }
        }
        if (codeList.length == 0) {
            layer.alert('所选登记单子不能采样检测');
            return;
        }
        var cthis = this;
        layer.confirm(codeList.join("<br\>"), { btn: ['确定', '取消'], title: "批量采样检测" }, function () {
            $.ajax({
                type: "POST",
                url: "${contextPath}/newRegisterBill/doBatchSamplingCheck.action",
                processData: true,
                contentType: 'application/json;charset=utf-8',
                data: JSON.stringify(batchIdList),
                dataType: "json",
                async: true,
                success: function (ret) {
                    if (ret.success) {
                        var failureList = ret.data.failureList;
                        if (failureList.length == 0) {
                            cthis.queryGridData();
                            TLOG.component.operateLog('登记单管理', "批量采样检测", "【编号】:" + codeList.join(','));
                            layer.alert('操作成功', { title: '操作', time: 600 });
                        }
                        else {
                            swal('操作', '成功:' + ret.data.successList.join('</br>') + '失败:' + ret.data.failureList.join('</br>'), 'info');
                        }
                    }
                    else {
                        swal('错误', ret.result, 'error');
                    }
                },
                error: function () {
                    swal('错误', '远程访问失败', 'error');
                }
            });
        });
    }
    async doRemoveReportAndCertifiy() {
        var selected = this.rows[0];
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
        let promise = new Promise((resolve, reject) => {
            layer.confirm('请确认是否删除产地证明和报告？', { btn: ['全部删除', '删除产地证明', '删除检测报告', '取消'], title: "警告！！！",
                btn1: function () {
                    resolve("all");
                    return false;
                },
                btn2: function () {
                    resolve("originCertifiy");
                    return false;
                },
                btn3: function () {
                    resolve("detectReport");
                    return false;
                },
                btn4: function () {
                    resolve("cancel");
                    return false;
                }
            });
            $('.layui-layer').width('460px');
        });
        var cthis = this;
        let result = await promise;
        if (result != 'cancel') {
            var _url = "${contextPath}/newRegisterBill/doRemoveReportAndCertifiy.action";
            $.ajax({
                type: "POST",
                url: _url,
                data: { id: selected.id, deleteType: result },
                processData: true,
                dataType: "json",
                async: true,
                success: function (data) {
                    if (data.code == "200") {
                        TLOG.component.operateLog('登记单管理', "删除产地证明和报告", '【ID】:' + selected.id);
                        layer.alert('操作成功', {
                            title: '操作',
                            time: 600,
                            end: async function () {
                                layer.closeAll();
                                await cthis.queryGridData();
                            }
                        }, async function () {
                            layer.closeAll();
                            await cthis.queryGridData();
                        });
                    }
                    else {
                        layer.closeAll();
                        swal('错误', data.result, 'error');
                    }
                },
                error: function () {
                    layer.closeAll();
                    swal('错误', '远程访问失败', 'error');
                }
            });
        }
        else {
            layer.closeAll();
        }
    }
    async doAutoCheck() {
        var selected = this.rows[0];
        let cthis = this;
        var url = this.toUrl("/newRegisterBill/doAutoCheck.action?id=" + selected.id);
        bs4pop.confirm('请确认是否主动送检？', undefined, async function (sure) {
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
                TLOG.component.operateLog('登记单管理', "主动送检", "【编号】:" + selected.code);
                layer.alert('操作成功', { title: '操作', time: 600 });
            }
            catch (e) {
                bs4pop.alert('远程访问失败', { type: 'error' });
            }
        });
    }
    async doBatchAutoCheck() {
        var rows = this.rows;
        if (rows.length == 0) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
        var codeList = [];
        var batchIdList = [];
        for (var i = 0; i < rows.length; i++) {
            if (rows[i].$_state == 2) {
                batchIdList.push(rows[i].id);
                codeList.push(rows[i].code);
            }
        }
        if (codeList.length == 0) {
            layer.alert('所选登记单子不能主动送检');
            return;
        }
        var cthis = this;
        layer.confirm(codeList.join("<br\>"), { btn: ['确定', '取消'], title: "批量主动送检" }, function () {
            $.ajax({
                type: "POST",
                url: "${contextPath}/newRegisterBill/doBatchAutoCheck.action",
                processData: true,
                dataType: "json",
                contentType: 'application/json;charset=utf-8',
                data: JSON.stringify(batchIdList),
                async: true,
                success: async function (ret) {
                    if (ret.success) {
                        var failureList = ret.data.failureList;
                        if (failureList.length == 0) {
                            await cthis.queryGridData();
                            TLOG.component.operateLog('登记单管理', "批量主动送检", "【编号】:" + codeList.join(','));
                            layer.alert('操作成功', { title: '操作', time: 600 });
                        }
                        else {
                            swal('操作', '成功:' + ret.data.successList.join('</br>') + '失败:' + ret.data.failureList.join('</br>'), 'info');
                        }
                    }
                    else {
                        swal('错误', ret.result, 'error');
                    }
                },
                error: function () {
                    swal('错误', '远程访问失败', 'error');
                }
            });
        });
    }
    async doSamplingCheck() {
        var selected = this.rows[0];
        let cthis = this;
        var url = this.toUrl("/newRegisterBill/doSamplingCheck.action?id=" + selected.id);
        bs4pop.confirm('请确认是否采样检测？', undefined, async function (sure) {
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
                TLOG.component.operateLog('登记单管理', "采样检测", "【编号】:" + selected.code);
                layer.alert('操作成功', { title: '操作', time: 600 });
            }
            catch (e) {
                bs4pop.alert('远程访问失败', { type: 'error' });
            }
        });
    }
    async openCreateSheetPage() {
        var rows = this.rows;
        var idList = rows.map(function (v, i) { return v.id; });
        let url = this.toUrl('/checkSheet/edit.html?' + $.param({ idList: idList }, true));
        var dia = bs4pop.dialog({
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
    async doUndo() {
        let selected = this.rows[0];
        let cthis = this;
        let url = this.toUrl("/newRegisterBill/doUndo.action?id=" + selected.id);
        bs4pop.confirm('请确认是否撤销？', undefined, async function (sure) {
            if (!sure) {
                return;
            }
            try {
                var resp = await jq.ajaxWithProcessing({ type: "GET", url: url, processData: true, dataType: "json" });
                debugger;
                if (!resp.success) {
                    bs4pop.alert(resp.message, { type: 'error' });
                    return;
                }
                await cthis.queryGridData();
                TLOG.component.operateLog('登记单管理', "撤销", "【编号】:" + selected.code);
                layer.alert('操作成功', { title: '操作', time: 600 });
            }
            catch (e) {
                bs4pop.alert('远程访问失败', { type: 'error' });
            }
        });
    }
    async doBatchAudit() {
        var rows = this.rows;
        if (rows.length == 0) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
        var codeList = [];
        var batchIdList = [];
        var onlyWithOriginCertifiyUrlIdList = [];
        for (var i = 0; i < rows.length; i++) {
            if (rows[i].$_state == 1) {
                batchIdList.push(rows[i].id);
                codeList.push(rows[i].code);
                if (rows[i].originCertifiyUrl == '有' && rows[i].detectReportUrl == '无') {
                    onlyWithOriginCertifiyUrlIdList.push(rows[i].code);
                }
                ;
            }
        }
        if (codeList.length == 0) {
            layer.alert('所选登记单不能进行审核');
            return;
        }
        let promise = new Promise((resolve, reject) => {
            layer.confirm(codeList.join("<br\>"), { btn: ['确定', '取消'], title: "批量审核" }, function () {
                resolve("yes");
            }, function () {
                resolve("cancel");
            });
        });
        let result = await promise;
        if (result == 'yes') {
            var passWithOriginCertifiyUrl = null;
            if (onlyWithOriginCertifiyUrlIdList.length > 0) {
                let reconfirmPromise = new Promise((resolve, reject) => {
                    layer.confirm('只有产地证明登记单:<br/>' + onlyWithOriginCertifiyUrlIdList.join("<br\>"), { btn: ['不检测', '检测'], title: "是否不再进行检测" }, function () {
                        resolve(true);
                    }, function () {
                        resolve(false);
                    });
                });
                passWithOriginCertifiyUrl = await reconfirmPromise;
            }
            layer.closeAll();
            var cthis = this;
            $.ajax({
                type: "POST",
                url: "${contextPath}/newRegisterBill/doBatchAudit.action",
                processData: true,
                contentType: 'application/json;charset=utf-8',
                data: JSON.stringify({ registerBillIdList: batchIdList, pass: true, passWithOriginCertifiyUrl: passWithOriginCertifiyUrl }),
                dataType: "json",
                async: true,
                success: function (ret) {
                    if (ret.success) {
                        var failureList = ret.data.failureList;
                        if (failureList.length == 0) {
                            cthis.queryGridData();
                            TLOG.component.operateLog('登记单管理', "批量审核", "【编号】:" + codeList.join(','));
                            layer.alert('操作成功：</br>' + ret.data.successList.join('</br>'), { title: '操作', time: 3000 });
                        }
                        else {
                            swal('操作', '成功:' + ret.data.successList.join('</br>') + '失败:' + ret.data.failureList.join('</br>'), 'info');
                        }
                    }
                    else {
                        swal('错误', ret.result, 'error');
                    }
                },
                error: function () {
                    swal('错误', '远程访问失败', 'error');
                }
            });
        }
    }
    async openAuditPage() {
        var row = this.rows[0];
        var url = this.toUrl('/newRegisterBill/audit.html?id=' + row.billId);
        var dia = bs4pop.dialog({
            title: '进场审核',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
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
    async openUploadDetectReportPage() {
        var row = this.rows[0];
        var url = this.toUrl('/newRegisterBill/uploadDetectReport.html?id=' + row.billId);
        var dia = bs4pop.dialog({
            title: '上传检测报告',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
    }
    async openUploadOriginCertifyPage() {
        var row = this.rows[0];
        var url = this.toUrl('/newRegisterBill/uploadOrigincertifiy.html?id=' + row.billId);
        var dia = bs4pop.dialog({
            title: '上传产地证明',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
    }
    openCopyPage() {
        var row = this.rows[0];
        let url = this.toUrl("/newRegisterBill/copy.html?id=" + row.billId);
        var dia = bs4pop.dialog({
            title: '复制报备单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
    }
    openCreatePage() {
        let url = this.toUrl("/newRegisterBill/create.html");
        var dia = bs4pop.dialog({
            title: '新增报备单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
    }
    openEditPage() {
        var row = this.rows[0];
        let url = this.toUrl("/newRegisterBill/edit.html?id=" + row.billId);
        var dia = bs4pop.dialog({
            title: '修改报备单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
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
        this.resetButtons();
        this.highLightBill = await this.findHighLightBill();
        try {
            let url = this.toUrl("/newRegisterBill/listPage.action");
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
    resetButtons() {
        var btnArray = ['upload-detectreport-btn', 'upload-origincertifiy-btn', 'copy-btn', 'edit-btn', 'detail-btn', 'undo-btn', 'audit-btn', 'audit-withoutDetect-btn', 'auto-btn', 'sampling-btn', 'review-btn', 'upload-handleresult-btn',
            'batch-audit-btn', 'batch-sampling-btn', 'batch-auto-btn', 'batch-undo-btn', 'remove-reportAndcertifiy-btn', 'createsheet-btn'];
        for (var i = 0; i < btnArray.length; i++) {
            var btnId = btnArray[i];
            $('#' + btnId).show();
        }
    }
    isCreateSheet() {
        var arr = this.filterByProp('$_detectState', ["PASS", "REVIEW_PASS"]).filter(function (v, i) {
            if (v.$_checkSheetId && v.$_checkSheetId != null && v.$_checkSheetId != '') {
                return false;
            }
            else {
                return true;
            }
        });
        return arr.length > 0;
    }
    isSingleAudit() {
        return true;
    }
    isSingleCopy() {
        return this.rows.length == 1;
    }
    isSingleDetail() {
        return this.rows.length == 1;
    }
    isSingleUploadOriginCertify() {
        return this.rows.length == 1;
    }
    multiRows() {
        if (this.rows.length <= 1) {
            return;
        }
    }
    singleRow() {
        if (this.rows.length != 1) {
            return;
        }
    }
    async findHighLightBill() {
        try {
            var url = this.toUrl("/newRegisterBill/findHighLightBill.action");
            return await jq.postJson(url, {}, {});
        }
        catch (e) {
            console.log(e);
            return {};
        }
    }
    buildGridQueryData() {
        var formdata = bindGridMeta2Form("registerBillGrid", "queryForm");
        delete formdata['productCombobox'];
        var rows = this.grid.datagrid("getRows");
        var options = this.grid.datagrid("options");
        formdata['rows'] = options.pageSize;
        formdata['page'] = options.pageNumber;
        formdata['sort'] = options.sortName;
        formdata['order'] = options.sortOrder;
        return formdata;
    }
    onClickRow() {
        this.resetButtons();
        var rows = this.rows;
        if (rows.length == 0) {
            return;
        }
        this.isCreateSheet() ? $('#createsheet-btn').show() : $('#createsheet-btn').hide();
        this.isBatchAudit() ? $('#batch-audit-btn').show() : $('#batch-audit-btn').hide();
        this.isBatchSimpling() ? $('#batch-sampling-btn').show() : $('#batch-sampling-btn').hide();
        this.isBatchAuto() ? $('#batch-auto-btn').show() : $('#batch-auto-btn').hide();
        this.isBatchUndo() ? $('#batch-undo-btn').show() : $('#batch-undo-btn').hide();
        if (rows.length > 1) {
            return;
        }
        this.isSingleCopy() ? $('#copy-btn').show() : $('#copy-btn').hide();
        this.isSingleDetail() ? $('#detail-btn').show() : $('#detail-btn').hide();
        this.isSingleUploadOriginCertify() ? $('#upload-origincertifiy-btn').show() : $('#upload-origincertifiy-btn').hide();
        var waitAuditRows = this.filterByProp("$_state", [1]);
        if (waitAuditRows.length == 1) {
            $('#undo-btn').show();
            $('#audit-btn').show();
            $('#edit-btn').show();
            $('#upload-detectreport-btn').show();
        }
        let hasImages = _.chain(waitAuditRows).filter(item => {
            return item.originCertifiyUrl == '有' || item.detectReportUrl == '有';
        }).size().value() > 0;
        hasImages ? $('#remove-reportAndcertifiy-btn').show() : $('#remove-reportAndcertifiy-btn').hide();
        let auditWithoutDetect = _.chain(waitAuditRows)
            .filter(item => 1 == item.registerSource)
            .filter(item => {
            return item.originCertifiyUrl && item.originCertifiyUrl != null;
        })
            .filter(item => {
            item.originCertifiyUrl != '' && item.originCertifiyUrl != '无';
        }).size().value() > 0;
        auditWithoutDetect ? $('#audit-withoutDetect-btn').show() : $('#audit-withoutDetect-btn').hide();
        var WAIT_SAMPLE_ROWS = this.filterByProp("$_state", [6]);
        if (WAIT_SAMPLE_ROWS.length == 1) {
            $('#undo-btn').show();
            $('#auto-btn').show();
            $('#sampling-btn').show();
        }
        var ALREADY_CHECK_ROWS = this.filterByProp("$_state", [6]);
        var review = _.chain(ALREADY_CHECK_ROWS).filter(item => "NO_PASS" == item.$_detectState).size().value() > 0;
        review ? $('#review-btn').show() : $('#review-btn').hide();
        var review = _.chain(ALREADY_CHECK_ROWS)
            .filter(item => "REVIEW_NO_PASS" == item.$_detectState)
            .filter(item => item.handleResult == null || item.handleResult == '')
            .size().value() > 0;
        review ? $('#review-btn').show() : $('#review-btn').hide();
        $('#upload-handleresult-btn').show();
    }
    get rows() {
        let rows = this.grid.bootstrapTable('getSelections');
        return rows;
    }
    static getInstance() {
        return window['registerBillGrid'];
    }
    static queryProduct(param, success, error) {
        var productName = $('#productCombobox').combotree('getText');
        var data = [];
        var url = '/toll/category?name=' + productName;
        $.ajax({
            url: url,
            success: function (resp) {
                data = resp.suggestions.map(function (v, i) {
                    v['parentId'] = '';
                    return v;
                });
                success(data);
            }
        });
    }
    isBatchSimpling() {
        if (this.filterByProp('$_state', [2]).length > 0) {
            return true;
        }
        return false;
    }
    isBatchAudit() {
        if (this.filterByProp('$_state', [1]).length > 0) {
            return true;
        }
        return false;
    }
    isBatchAuto() {
        if (this.filterByProp('$_state', [2]).length > 0) {
            return true;
        }
        return false;
    }
    isBatchUndo() {
        if (this.filterByProp('$_state', [1]).length > 0) {
            return true;
        }
        return false;
    }
    async doBatchUndo() {
        if (!this.isBatchUndo()) {
            swal({
                title: '警告',
                text: '没有数据可以进行批量撤销',
                type: 'warning',
                width: 300
            });
            return;
        }
        var cthis = this;
        var arr = this.filterByProp("$_state", [1]);
        let promise = new Promise((resolve, reject) => {
            layer.confirm('请确认是否撤销选中数据？<br/>' + arr.map(e => e.code).join("<br\>"), {
                btn: ['确定', '取消'], title: "警告！！！",
                btn1: function () {
                    resolve(true);
                    return false;
                },
                btn2: function () {
                    resolve(false);
                    return false;
                }
            });
            $('.layui-layer').width('460px');
        });
        let result = await promise;
        if (result) {
            var _url = "/registerBill/doBatchUndo.action";
            var idlist = arr.map(e => e.id);
            $.ajax({
                type: "POST",
                url: _url,
                data: JSON.stringify(idlist),
                processData: true,
                dataType: "json",
                contentType: 'application/json;charset=utf-8',
                async: true,
                success: function (data) {
                    if (data.code == "200") {
                        TLOG.component.operateLog('登记单管理', "批量撤销", '【IDS】:' + JSON.stringify(idlist));
                        layer.alert('操作成功', {
                            title: '操作',
                            time: 600,
                            end: async function () {
                                layer.closeAll();
                                await cthis.queryGridData();
                            }
                        }, async function () {
                            layer.closeAll();
                            await cthis.queryGridData();
                        });
                    }
                    else {
                        layer.closeAll();
                        swal('错误', data.result, 'error');
                    }
                },
                error: function () {
                    layer.closeAll();
                    swal('错误', '远程访问失败', 'error');
                }
            });
        }
        layer.closeAll();
    }
    filterByProp(prop, propValues) {
        let arrayData = $.makeArray(this.rows);
        let arrayValue = $.makeArray(propValues);
        let values = _.chain(arrayData).filter(element => $.inArray(element[prop], arrayValue) > -1).value();
        return values;
    }
}
//# sourceMappingURL=newregisterbill.index.js.map