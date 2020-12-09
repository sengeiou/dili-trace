class CommissionBillGrid extends WebConfig {
    constructor(grid, queryform, billStateEnums, billDetectStateEnums) {
        super();
        this.grid = grid;
        this.queryform = queryform;
        this.billStateEnums = billStateEnums;
        this.billDetectStateEnums = billDetectStateEnums;
        window['commissionBillGrid'] = this;
        $('#add-btn').on('click', async () => await this.openCreatePage());
        $('#detail-btn').on('click', async () => await this.doDetail());
        $('#audit-btn').on('click', async () => await this.audit());
        $('#createsheet-btn').on('click', async () => await this.doCreateCheckSheet());
        $('#batch-reviewCheck-btn').on('click', async () => await this.doReviewCheck());
        this.initAutoComplete($("[name='productName']"), '/toll/category');
        this.initAutoComplete($("[name='originName']"), '/toll/city');
    }
    doCreateCheckSheet() {
        let row = this.grid.bootstrapTable("getSelections");
        if (row.length == 0) {
            bs4pop.alert("请选择一条数据", { type: 'warning' });
            return;
        }
        var idList = row.map(function (v, i) { return v.id; });
        let param = $.param({ idList: idList }, true);
        let url = this.toUrl("/checkSheet/edit.html?" + param);
        var audit_dia = bs4pop.dialog({
            title: '创建打印报告单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '50%',
            height: '70%',
            btns: [],
            onShowEnd: function () {
            }
        });
    }
    audit() {
        let row = this.grid.bootstrapTable("getSelections");
        if (row.length == 0) {
            bs4pop.alert("请选择一条数据", { type: 'warning' });
            return;
        }
        if (row.length > 1) {
            bs4pop.alert("请选择数据过多", { type: 'warning' });
            return;
        }
        console.log(row);
        let url = this.toUrl("/commissionBill/audit.html?billId=" + row[0].id);
        var audit_dia = bs4pop.dialog({
            title: '审核',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '50%',
            height: '51%',
            btns: [],
            onShowEnd: function () {
            }
        });
    }
    doAudit(id) {
        let url = this.toUrl("/commissionBill/doAuditCommissionBillByManager.action");
        $.ajax({
            type: "POST",
            data: { billId: id },
            url: url,
            processData: true,
            dataType: "json",
            async: true,
            success: function (ret) {
                if (ret.success) {
                    bs4pop.alert("操作成功", { type: 'success' }, function () {
                        try {
                            window['commissionBillGrid'].removeAllAndLoadData();
                        }
                        finally {
                        }
                    });
                }
                else {
                    bs4pop.alert("操作失败", { type: 'warning' });
                }
            },
            error: function () {
                bs4pop.alert("操作失败", { type: 'error' });
            }
        });
    }
    removeAllAndLoadData() {
        bs4pop.removeAll();
        (async () => {
            await this.queryGridData();
        })();
    }
    openCreatePage() {
        let url = this.toUrl("/commissionBill/create.html");
        var dia = bs4pop.dialog({
            title: '新增委托单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '70%',
            height: '70%',
            btns: [],
            onShowEnd: function () {
            }
        });
    }
    doDetail() {
        let row = this.grid.bootstrapTable("getSelections");
        if (row.length == 0) {
            bs4pop.alert("请选择一条数据", { type: 'warning' });
            return;
        }
        console.log(row);
        let selected_id = row[0].id;
        let url = this.toUrl('/commissionBill/view/' + selected_id + '/true');
        var detail_dia = bs4pop.dialog({
            title: '查看委托单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '70%',
            height: '75%',
            btns: [],
            onShowEnd: function () {
            }
        });
    }
    async queryGridData() {
        console.log("queryGridData");
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
            let url = this.toUrl("/commissionBill/listPage.action");
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
            var url = this.toUrl("/commissionBill/findHighLightBill.action");
            return await jq.postJson(url, {}, {});
        }
        catch (e) {
            console.log(e);
            return {};
        }
    }
    get rows() {
        return this.grid.bootstrapTable("getSelections");
    }
    findReviewCheckData() {
        var detectStateArr = [this.billDetectStateEnums.NO_PASS, this.billDetectStateEnums.REVIEW_NO_PASS];
        var alreadyCheckArray = this.filterByProp('$_state', this.billStateEnums.ALREADY_CHECK);
        let values = _.chain(alreadyCheckArray).filter(element => $.inArray(element['$_detectState'], detectStateArr) > -1).value();
        return values;
    }
    isReviewCheck() {
        return this.findReviewCheckData().length > 0;
    }
    async doReviewCheck() {
        if (!this.isReviewCheck()) {
            bs4pop.alert("请选择一条数据", { type: 'warning' });
            return;
        }
        var arr = this.findReviewCheckData();
        let promise = new Promise((resolve, reject) => {
            layer.confirm('请确认是否复检选中数据？<br/>' + arr.map(e => e.code).join("<br\>"), {
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
            var _url = ctx + "/commissionBill/doBatchReviewCheck.action";
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
                        TLOG.component.operateLog('登记单管理', "批量复检", '【IDS】:' + JSON.stringify(idlist));
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
        let arrayData = $.makeArray(this.rows);
        let arrayValue = $.makeArray(propValues);
        let values = _.chain(arrayData).filter(element => $.inArray(element[prop], arrayValue) > -1).value();
        return values;
    }
}
//# sourceMappingURL=commissionbill.index.js.map