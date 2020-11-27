class RegisterBillGrid extends PageTs {
    constructor(grid) {
        super();
        this.handleTimeUpdateEvent = (event) => {
        };
        let cthis = this;
        this.grid = grid;
        this.grid.datagrid({
            onClickRow: function () {
                cthis.onClickRow();
            },
            onSelect: function () {
                cthis.onClickRow();
            },
            onSelectAll: function () {
                cthis.onClickRow();
            },
            onUnselect: function () {
                cthis.onClickRow();
            },
            onUnselectAll: function () {
                cthis.onClickRow();
            }
        });
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
    onClickRow() {
        initBtnStatus();
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
        let hasImages = _.chain(waitAuditRows).filter(item => { return item.originCertifiyUrl == '有' || item.detectReportUrl == '有'; }).size().value() > 0;
        hasImages ? $('#remove-reportAndcertifiy-btn').show() : $('#remove-reportAndcertifiy-btn').hide();
        let auditWithoutDetect = _.chain(waitAuditRows)
            .filter(item => 1 == item.registerSource)
            .filter(item => { return item.originCertifiyUrl && item.originCertifiyUrl != null; })
            .filter(item => { item.originCertifiyUrl != '' && item.originCertifiyUrl != '无'; }).size().value() > 0;
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
        $('#handle-btn').show();
    }
    get rows() {
        return RegisterBillGrid.getInstance().grid.datagrid("getSelections");
    }
    static getInstance() {
        return window['registerBillGrid'];
    }
    static queryProduct(param, success, error) {
        var productName = $('#productCombobox').combotree('getText');
        var data = [];
        var url = RegisterBillGrid.getInstance().contextPath + '/toll/category?name=' + productName;
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
            var _url = RegisterBillGrid.getInstance().contextPath + "/registerBill/batchUndo.action";
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
                            end: function () {
                                layer.closeAll();
                                queryRegisterBillGrid();
                            }
                        }, function () {
                            layer.closeAll();
                            queryRegisterBillGrid();
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
        let arrayData = $.makeArray(RegisterBillGrid.getInstance().rows);
        let arrayValue = $.makeArray(propValues);
        let values = _.chain(arrayData).filter(element => $.inArray(element[prop], arrayValue) > -1).value();
        return values;
    }
}
//# sourceMappingURL=registerbill.index.js.map