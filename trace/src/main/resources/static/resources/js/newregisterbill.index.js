"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const jquery_1 = __importDefault(require("jquery"));
const underscore_1 = __importDefault(require("underscore"));
class NewRegisterBillGrid extends WebConfig {
    constructor(grid, queryform) {
        super();
        this.handleTimeUpdateEvent = (event) => {
        };
        this.grid = grid;
        this.queryform = queryform;
        this.uid = underscore_1.default.uniqueId("trace_id_");
        jquery_1.default(window).on('resize', () => this.grid.bootstrapTable('resetView'));
        var cthis = this;
        window['RegisterBillGridObj'] = this;
        this.btns = ['upload-detectreport-btn', 'upload-origincertifiy-btn', 'copy-btn', 'edit-btn', 'detail-btn', 'undo-btn', 'audit-btn', 'audit-withoutDetect-btn', 'auto-btn', 'sampling-btn', 'review-btn', 'upload-handleresult-btn',
            'batch-audit-btn', 'batch-sampling-btn', 'batch-auto-btn', 'batch-undo-btn', 'remove-reportAndcertifiy-btn', 'createsheet-btn'];
        jquery_1.default('#edit-btn').on('click', async () => await this.openEditPage());
        jquery_1.default('#btn_add').on('click', async () => await this.openCreatePage());
        jquery_1.default('#copy-btn').on('click', async () => await this.openCopyPage());
        jquery_1.default('#upload-origincertifiy-btn').on('click', async () => await this.openUploadOriginCertifyPage());
        jquery_1.default('#upload-detectreport-btn').on('click', async () => await this.openUploadDetectReportPage());
        jquery_1.default('#upload-handleresult-btn').on('click', async () => await this.openUploadHandleResultPage());
        jquery_1.default('#detail-btn').on('click', async () => await this.openDetailPage());
        jquery_1.default('#audit-btn').on('click', async () => await this.openAuditPage());
        jquery_1.default('#undo-btn').on('click', async () => await this.doUndo());
        jquery_1.default('#batch-undo-btn').on('click', async () => await this.doBatchUndo());
        jquery_1.default('#batch-audit-btn').on('click', async () => await this.doBatchAudit());
        jquery_1.default('#createsheet-btn').on('click', async () => await this.openCreateSheetPage());
        jquery_1.default('#sampling-btn').on('click', async () => await this.doSamplingCheck());
        jquery_1.default('#batch-auto-btn').on('click', async () => await this.doBatchAutoCheck());
        jquery_1.default('#auto-btn').on('click', async () => await this.doAutoCheck());
        jquery_1.default('#remove-reportAndcertifiy-btn').on('click', async () => await this.doRemoveReportAndCertifiy());
        jquery_1.default('#batch-sampling-btn').on('click', async () => await this.doBatchSamplingCheck());
        jquery_1.default('#audit-withoutDetect-btn').on('click', async () => await this.doAuditWithoutDetect());
        jquery_1.default('#review-btn').on('click', async () => await this.doReviewCheck());
        this.grid.on('check.bs.table uncheck.bs.table', () => this.checkAndShowHideBtns());
        this.grid.bootstrapTable('refreshOptions', { url: '/newRegisterBill/listPage.action',
            'queryParams': (params) => this.buildQueryData(params),
            'ajaxOptions': {}
        });
        this.queryform.find('#query').click(async () => await this.queryGridData());
        window.addEventListener('message', function (e) {
            var data = JSON.parse(e.data);
            if (data.obj && data.fun) {
                if (data.obj == 'RegisterBillGridObj') {
                    cthis[data.fun].call(cthis, data.args);
                }
            }
        }, false);
    }
    buildQueryData(params) {
        let temp = {
            rows: params.limit,
            page: ((params.offset / params.limit) + 1) || 1,
            sort: params.sort,
            order: params.order
        };
        let data = jquery_1.default.extend(temp, this.queryform.serializeJSON());
        let jsonData = jq.removeEmptyProperty(data);
        return JSON.stringify(jsonData);
    }
    removeAllAndLoadData() {
        bs4pop.removeAll();
        (async () => {
            await this.queryGridData();
        })();
    }
    async doAudit(billId, code) {
        bs4pop.removeAll();
        let promise = new Promise((resolve, reject) => {
            bs4pop.confirm('是否审核通过当前登记单？<br/>' + code, { type: 'warning', btns: [
                    { label: '通过', className: 'btn-primary', onClick(cb) { resolve("true"); } },
                    { label: '不通过', className: 'btn-primary', onClick(cb) { resolve("false"); } },
                    { label: '取消', className: 'btn-cancel', onClick(cb) { resolve("cancel"); } },
                ] });
        });
        let result = await promise;
        if (result == 'cancel') {
            return;
        }
        let url = this.toUrl("/newRegisterBill/doAudit.action?id=" + billId + "&pass=" + result);
        try {
            var resp = await jq.ajaxWithProcessing({ type: "GET", url: url, processData: true, dataType: "json" });
            if (!resp.success) {
                bs4pop.alert(resp.message, { type: 'error' });
                return;
            }
            await this.queryGridData();
            TLOG.component.operateLog('登记单管理', "审核", "【编号】:" + selected.code);
            bs4pop.removeAll();
            bs4pop.alert('操作成功', { type: 'info', autoClose: 600 });
        }
        catch (e) {
            bs4pop.alert('远程访问失败', { type: 'error' });
        }
    }
    async doReviewCheck() {
        var selected = this.rows[0];
        let url = this.toUrl("/newRegisterBill/doReviewCheck.action?id=" + selected.id);
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
            TLOG.component.operateLog('登记单管理', "复检", "【编号】:" + selected.code);
            bs4pop.removeAll();
            bs4pop.alert('操作成功', { type: 'info', autoClose: 600 });
        }
        catch (e) {
            bs4pop.alert('远程访问失败', { type: 'error' });
        }
    }
    async doAuditWithoutDetect() {
        var selected = this.rows[0];
        let url = this.toUrl("/newRegisterBill/doAuditWithoutDetect.action");
        let sure = await popwrapper.confirm('确认审核不检测？', undefined);
        if (!sure) {
            return;
        }
        try {
            var resp = await jq.ajaxWithProcessing({ type: "POST", data: { id: selected.id }, url: url, processData: true, dataType: "json" });
            if (!resp.success) {
                bs4pop.alert(resp.message, { type: 'error' });
                return;
            }
            await this.queryGridData();
            TLOG.component.operateLog('登记单管理', "审核不检测", "【编号】:" + selected.code);
            bs4pop.removeAll();
            bs4pop.alert('操作成功', { type: 'info', autoClose: 600 });
        }
        catch (e) {
            bs4pop.alert('远程访问失败', { type: 'error' });
        }
    }
    async doBatchSamplingCheck() {
        var rows = this.rows;
        if (this.batchSamplingRows.length == 0) {
            layer.alert('所选登记单子不能采样检测');
            return;
        }
        var codeList = [];
        var batchIdList = [];
        for (var i = 0; i < this.batchSamplingRows.length; i++) {
            batchIdList.push(this.batchSamplingRows[i].id);
            codeList.push(this.batchSamplingRows[i].code);
        }
        let sure = await popwrapper.confirm('批量采样检测' + codeList.join("<br\>"), undefined);
        if (!sure) {
            return;
        }
        let url = this.toUrl("/newRegisterBill/doBatchSamplingCheck.action");
        try {
            var resp = await jq.postJsonWithProcessing(url, batchIdList);
            if (!resp.success) {
                bs4pop.alert(resp.message, { type: 'error' });
                return;
            }
            if (!resp.success) {
                bs4pop.alert(resp.message, { type: 'error' });
                return;
            }
            var failureList = resp.data.failureList;
            if (failureList.length > 0) {
                bs4pop.alert('成功:' + resp.data.successList.join('</br>') + '失败:' + resp.data.failureList.join('</br>'), { type: 'warning' });
                return;
            }
            await this.queryGridData();
            TLOG.component.operateLog('登记单管理', "审核不检测", "【编号】:" + selected.code);
            bs4pop.removeAll();
            bs4pop.alert('操作成功', { type: 'info', autoClose: 600 });
        }
        catch (e) {
            bs4pop.alert('远程访问失败', { type: 'error' });
        }
    }
    async doRemoveReportAndCertifiy() {
        var selected = this.rows[0];
        let promise = new Promise((resolve, reject) => {
            bs4pop.confirm('所选登记单子不能主动送检', { type: 'warning', btns: [
                    { label: '全部删除', className: 'btn-primary', onClick(cb) { resolve("all"); } },
                    { label: '删除产地证明', className: 'btn-primary', onClick(cb) { resolve("originCertifiy"); } },
                    { label: '删除检测报告', className: 'btn-primary', onClick(cb) { resolve("detectReport"); } },
                    { label: '取消', className: 'btn-cancel', onClick(cb) { resolve("cancel"); } },
                ] });
        });
        let result = await promise;
        var _url = this.toUrl("/newRegisterBill/doRemoveReportAndCertifiy.action");
        if (result != 'cancel') {
            try {
                var resp = await jq.postJsonWithProcessing(_url, { id: selected.id, deleteType: result });
                if (!resp.success) {
                    bs4pop.alert(resp.message, { type: 'error' });
                    return;
                }
                await this.queryGridData();
                TLOG.component.operateLog('登记单管理', "删除产地证明和报告", '【ID】:' + selected.id);
                bs4pop.removeAll();
                bs4pop.alert('操作成功', { type: 'info', autoClose: 600 });
            }
            catch (e) {
                bs4pop.alert('远程访问失败', { type: 'error' });
            }
        }
        else {
            layer.closeAll();
        }
    }
    async doAutoCheck() {
        var selected = this.rows[0];
        let cthis = this;
        var url = this.toUrl("/newRegisterBill/doAutoCheck.action?id=" + selected.id);
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
            TLOG.component.operateLog('登记单管理', "主动送检", "【编号】:" + selected.code);
            bs4pop.removeAll();
            bs4pop.alert('操作成功', { type: 'info', autoClose: 600 });
        }
        catch (e) {
            bs4pop.alert('远程访问失败', { type: 'error' });
        }
    }
    async doBatchAutoCheck() {
        if (this.batchSamplingRows.length == 0) {
            bs4pop.alert('所选登记单子不能主动送检', { type: 'warning' });
            return;
        }
        var codeList = [];
        var batchIdList = [];
        for (var i = 0; i < this.batchSamplingRows.length; i++) {
            batchIdList.push(this.batchSamplingRows[i].id);
            codeList.push(this.batchSamplingRows[i].code);
        }
        var url = this.toUrl("/newRegisterBill/doBatchAutoCheck.action");
        let sure = await popwrapper.confirm('请确认是否主动送检？', undefined);
        if (!sure) {
            return;
        }
        try {
            var resp = await jq.postJsonWithProcessing(url, batchIdList);
            if (!resp.success) {
                bs4pop.alert(resp.message, { type: 'error' });
                return;
            }
            var failureList = resp.data.failureList;
            if (failureList.length > 0) {
                bs4pop.alert('成功:' + resp.data.successList.join('</br>') + '失败:' + resp.data.failureList.join('</br>'), { type: 'warning' });
                return;
            }
            await this.queryGridData();
            TLOG.component.operateLog('登记单管理', "批量主动送检", "【编号】:" + codeList.join(','));
            bs4pop.removeAll();
            bs4pop.alert('操作成功', { type: 'info', autoClose: 600 });
        }
        catch (e) {
            bs4pop.alert('远程访问失败', { type: 'error' });
        }
    }
    async doSamplingCheck() {
        var selected = this.rows[0];
        var url = this.toUrl("/newRegisterBill/doSamplingCheck.action?id=" + selected.id);
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
            TLOG.component.operateLog('登记单管理', "采样检测", "【编号】:" + selected.code);
            bs4pop.removeAll();
            bs4pop.alert('操作成功', { type: 'info', autoClose: 600 });
        }
        catch (e) {
            bs4pop.alert('远程访问失败', { type: 'error' });
        }
    }
    async openCreateSheetPage() {
        var rows = this.rows;
        var idList = rows.map(function (v, i) { return v.id; });
        let url = this.toUrl('/checkSheet/edit.html?' + jquery_1.default.param({ idList: idList }, true));
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
                if (!resp.success) {
                    bs4pop.alert(resp.message, { type: 'error' });
                    return;
                }
                await cthis.queryGridData();
                TLOG.component.operateLog('登记单管理', "撤销", "【编号】:" + selected.code);
                bs4pop.removeAll();
                bs4pop.alert('操作成功', { type: 'info', autoClose: 600 });
            }
            catch (e) {
                bs4pop.alert('远程访问失败', { type: 'error' });
            }
        });
    }
    async doBatchAudit() {
        var waitAudit = this.filterByProp('verifyStatus', [0]);
        if (waitAudit.length == 0) {
            bs4pop.alert('所选登记单不能进行审核', { type: 'error' });
            return;
        }
        var codeList = [];
        var batchIdList = [];
        var onlyWithOriginCertifiyUrlIdList = [];
        for (var i = 0; i < waitAudit.length; i++) {
            batchIdList.push(waitAudit[i].id);
            codeList.push(waitAudit[i].code);
            if (waitAudit[i].hasOriginCertifiy != 0 && waitAudit[i].hasDetectReport == 0) {
                onlyWithOriginCertifiyUrlIdList.push(waitAudit[i].code);
            }
            ;
        }
        let promise = new Promise((resolve, reject) => {
            bs4pop.confirm('批量审核<br\>' + codeList.join("<br\>"), undefined, async function (sure) {
                resolve(sure);
            });
        });
        let result = await promise;
        if (result == true) {
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
            jquery_1.default.ajax({
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
            width: '80%',
            height: '60%',
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
        var cthis = this;
        var dia = bs4pop.dialog({
            title: '新增报备单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: [],
            onShowEnd: function () {
                dia.$el.find('iframe')[0].contentWindow['RegisterBillGridObj'] = cthis;
            }
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
        this.grid.bootstrapTable('refresh');
    }
    resetButtons() {
        var btnArray = this.btns;
        for (var i = 0; i < btnArray.length; i++) {
            var btnId = btnArray[i];
            jquery_1.default('#' + btnId).hide();
        }
    }
    checkAndShowHideBtns() {
        var _a, _b, _c;
        this.resetButtons();
        var rows = this.rows;
        if (rows.length == 0) {
            return;
        }
        var createCheckSheet = underscore_1.default.chain(this.rows)
            .filter(item => { var _a; return 1 == ((_a = item === null || item === void 0 ? void 0 : item.detectRequest) === null || _a === void 0 ? void 0 : _a.detectResult); })
            .filter(item => underscore_1.default.isUndefined(item.checkSheetId) || item.checkSheetId == null).value().length > 0;
        createCheckSheet ? jquery_1.default('#createsheet-btn').show() : jquery_1.default('#createsheet-btn').hide();
        if (rows.length == 1) {
            var selected = rows[0];
            jquery_1.default('#copy-btn').show();
            jquery_1.default('#detail-btn').show();
            jquery_1.default('#upload-origincertifiy-btn').show();
            jquery_1.default('#upload-handleresult-btn').show();
            var waitAudit = this.waitAuditRows;
            if (waitAudit.length == 1) {
                jquery_1.default('#undo-btn').show();
                jquery_1.default('#audit-btn').show();
                jquery_1.default('#edit-btn').show();
                jquery_1.default('#upload-detectreport-btn').show();
            }
            if (selected.hasOriginCertifiy != 0) {
                jquery_1.default('#remove-reportAndcertifiy-btn').show();
            }
            if (selected.registerSource == 1) {
                if (selected.hasOriginCertifiy != 0) {
                    jquery_1.default('#audit-withoutDetect-btn').show();
                }
            }
            if (selected.detectStatus == 20) {
                jquery_1.default('#auto-btn').show();
                jquery_1.default('#undo-btn').show();
                jquery_1.default('#sampling-btn').show();
            }
            if (((_a = selected === null || selected === void 0 ? void 0 : selected.detectRequest) === null || _a === void 0 ? void 0 : _a.detectResult) == 2) {
                if (((_b = selected === null || selected === void 0 ? void 0 : selected.detectRequest) === null || _b === void 0 ? void 0 : _b.detectType) == 20) {
                    jquery_1.default('#review-btn').show();
                }
                else if (((_c = selected === null || selected === void 0 ? void 0 : selected.detectRequest) === null || _c === void 0 ? void 0 : _c.detectType) == 30 && selected.hasHandleResult == 0) {
                    jquery_1.default('#review-btn').show();
                }
            }
            return;
        }
        var batchAudit = this.filterByProp('verifyStatus', [0]).length > 0;
        batchAudit ? jquery_1.default('#batch-audit-btn').show() : jquery_1.default('#batch-audit-btn').hide();
        var batchSampling = this.batchSamplingRows.length > 0;
        batchSampling ? jquery_1.default('#batch-sampling-btn').show() : jquery_1.default('#batch-sampling-btn').hide();
        var batchAuto = batchSampling;
        batchAuto ? jquery_1.default('#batch-auto-btn').show() : jquery_1.default('#batch-auto-btn').hide();
        var batchUndo = underscore_1.default.chain(this.rows).filter(item => {
            return (0 == item.verifyStatus) || (20 == item.detectStatus);
        });
        batchUndo ? jquery_1.default('#batch-undo-btn').show() : jquery_1.default('#batch-undo-btn').hide();
    }
    get waitAuditRows() {
        return this.filterByProp('verifyStatus', [0]);
    }
    get batchSamplingRows() {
        return this.filterByProp('detectStatus', [20]);
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
    get rows() {
        let rows = this.grid.bootstrapTable('getSelections');
        return rows;
    }
    static queryProduct(param, success, error) {
        var productName = jquery_1.default('#productCombobox').combotree('getText');
        var data = [];
        var url = '/toll/category?name=' + productName;
        jquery_1.default.ajax({
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
    async doBatchUndo() {
        var cthis = this;
        var waitAudit = this.waitAuditRows;
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
            jquery_1.default('.layui-layer').width('460px');
        });
        let result = await promise;
        if (result) {
            var _url = "/registerBill/doBatchUndo.action";
            var idlist = waitAudit.map(e => e.id);
            jquery_1.default.ajax({
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
        let arrayData = jquery_1.default.makeArray(this.rows);
        let arrayValue = jquery_1.default.makeArray(propValues);
        let values = underscore_1.default.chain(arrayData).filter(element => jquery_1.default.inArray(element[prop], arrayValue) > -1).value();
        return values;
    }
}
//# sourceMappingURL=newregisterbill.index.js.map