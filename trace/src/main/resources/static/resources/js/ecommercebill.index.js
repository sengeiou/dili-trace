class EcommerceBillGrid extends WebConfig {
    constructor(grid, queryform) {
        super();
        this.grid = grid;
        this.queryform = queryform;
        let categoryController = new CategoryController();
        let cityController = new CityController();
        window['ECommerceBillGrid'] = this;
        $('#add-btn').on('click', async () => await this.openCreatePage());
        $('#audit-btn').on('click', async () => await this.openAudit());
        $('#delete-btn').on('click', async () => await this.openDelete());
        $('#print-btn').on('click', async () => await this.openPrint());
        $('#printSeperatePrintReport-btn').on('click', async () => await this.openPrintSeperatePrintReport());
        $('#detail-btn').on('click', async () => await this.doDetail());
        this.initAutoComplete($("[name='productId']"), function (query, done) {
            categoryController.lookupCategories(query, done);
        });
        this.initAutoComplete($("[name='originId']"), function (query, done) {
            cityController.lookupCities(query, done);
        });
        this.queryform.find('#query').click(async () => await this.queryGridData());
        (async () => {
            await this.queryGridData();
        })();
    }
    async openAudit() {
        var rows = this.grid.bootstrapTable("getSelections");
        if (rows.length == 0) {
            bs4pop.alert("请选择一条数据", { type: "warning" });
            return;
        }
        if (rows.length > 1) {
            bs4pop.alert("请选择数据过多", { type: "warning" });
            return;
        }
        var rowsArray = $.makeArray(rows);
        var waitAuditRowArray = rowsArray.filter(function (v, i) {
            return v.verifyStatus == 0;
        });
        if (waitAuditRowArray.length != 1) {
            return;
        }
        var hasOriginCertifiyUrlRowArray = waitAuditRowArray.filter(function (v, i) {
            return $.trim(v.originCertifiyUrl) != '';
        });
        var hasDetectReportUrlRowArray = waitAuditRowArray.filter(function (v, i) {
            return $.trim(v.detectReportUrl) != '';
        });
        var reconfirmPromise = new Promise((resolve, reject) => {
            resolve('');
        });
        if (hasDetectReportUrlRowArray.length == 1) {
            reconfirmPromise = new Promise((resolve, reject) => {
                bs4pop.confirm('登记单:<br/>' + waitAuditRowArray[0].code, {
                    type: 'warning', btns: [
                        {
                            label: '合格', className: 'btn-primary', onClick(cb) {
                                resolve({
                                    id: waitAuditRowArray[0].id, verifyStatus: 20,
                                    detectStatus: 50,
                                    detectState: 1
                                });
                            }
                        },
                        {
                            label: '取消', className: 'btn-cancel', onClick(cb) {
                                resolve("cancel");
                            }
                        },
                    ]
                });
            });
        }
        else {
            if (hasOriginCertifiyUrlRowArray.length == 1) {
                reconfirmPromise = new Promise((resolve, reject) => {
                    bs4pop.confirm('登记单:<br/>' + waitAuditRowArray[0].code, {
                        type: 'warning', btns: [
                            {
                                label: '检测', className: 'btn-primary', onClick(cb) {
                                    resolve({
                                        id: waitAuditRowArray[0].id, verifyStatus: 20,
                                        detectStatus: 50,
                                        detectState: 1
                                    });
                                }
                            },
                            {
                                label: '合格不检测', className: 'btn-primary', onClick(cb) {
                                    resolve({
                                        id: waitAuditRowArray[0].id,
                                        state: 20,
                                        detectState: 2,
                                        detectStatus: 40
                                    });
                                }
                            },
                            {
                                label: '取消', className: 'btn-cancel', onClick(cb) {
                                    resolve("cancel");
                                }
                            },
                        ]
                    });
                });
            }
            else {
                reconfirmPromise = new Promise((resolve, reject) => {
                    bs4pop.confirm('登记单:<br/>' + waitAuditRowArray[0].code, {
                        type: 'warning',
                        btns: [
                            {
                                label: '检测', className: 'btn-primary', onClick(cb) {
                                    resolve({
                                        id: waitAuditRowArray[0].id, verifyStatus: 20,
                                        detectStatus: 50,
                                        detectState: 1
                                    });
                                }
                            },
                            {
                                label: '取消', className: 'btn-cancel', onClick(cb) {
                                    resolve("cancel");
                                }
                            },
                        ]
                    });
                });
            }
        }
        var ret = await reconfirmPromise;
        if ($.type(ret) == 'object') {
            var result = {};
            $.ajax({
                type: "POST",
                url: '/ecommerceBill/doAudit.action',
                data: JSON.stringify(ret),
                processData: true,
                dataType: "json",
                async: false,
                contentType: "application/json; charset=utf-8",
                success: function (ret) {
                    result = ret;
                },
                error: function () {
                    result = { "code": "5000", result: "远程访问失败" };
                }
            });
            if (result.code == '200') {
                console.log(result);
                bs4pop.alert("操作成功", { type: 'success' }, function () {
                    window['ECommerceBillGrid'].removeAllAndLoadData();
                });
            }
            else {
                bs4pop.alert("操作" + result.result, { type: 'info' });
            }
        }
    }
    removeAllAndLoadData() {
        bs4pop.removeAll();
        (async () => {
            await this.queryGridData();
        })();
    }
    openCreatePage() {
        let url = this.toUrl("/ecommerceBill/create.html");
        var dia = bs4pop.dialog({
            title: '创建登记单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '90%',
            height: '78%',
            btns: [],
            onShowEnd: function () {
            }
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
    async openDelete() {
        let row = this.grid.bootstrapTable("getSelections");
        if (row.length == 0) {
            bs4pop.alert("请选择一条数据", { type: 'warning' });
            return;
        }
        var rowsArray = $.makeArray(row);
        var waitAuditRowArray = rowsArray.filter(function (v, i) {
            return v.verifyStatus == 0;
        });
        if (waitAuditRowArray.length != 1) {
            bs4pop.alert("单据状态不是待审核", { type: 'warning' });
            return;
        }
        let confirmPromise = new Promise((resolve, reject) => {
            bs4pop.confirm('确定要撤销登记单:' + waitAuditRowArray[0].code + " ?", {
                type: 'warning', btns: [
                    {
                        label: '撤销', className: 'btn-primary', onClick(cb) {
                            resolve(waitAuditRowArray[0].id);
                        }
                    },
                    {
                        label: '取消', className: 'btn-cancel', onClick(cb) {
                            resolve("cancel");
                        }
                    },
                ]
            });
        });
        var ret = await confirmPromise;
        if (ret == 'cancel') {
            return;
        }
        let url = this.toUrl("/ecommerceBill/doDelete.action");
        try {
            let param = { id: ret };
            var resp = await jq.ajaxWithProcessing({
                type: "POST",
                data: JSON.stringify(param),
                url: url,
                processData: true,
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            });
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
    async openPrint() {
        let row = this.grid.bootstrapTable("getSelections");
        if (row.length == 0) {
            bs4pop.alert("请选择一条数据", { type: 'warning' });
            return;
        }
        if (row.length > 1) {
            bs4pop.alert("请选择数据过多", { type: 'warning' });
            return;
        }
        var result = {};
        $.ajax({
            type: "POST",
            url: '/ecommerceBill/prePrint.action',
            data: JSON.stringify({ id: row[0].id }),
            processData: true,
            dataType: "json",
            async: false,
            contentType: "application/json; charset=utf-8",
            success: function (ret) {
                result = ret;
            },
            error: function () {
                result = { "code": "5000", result: "远程访问失败" };
            }
        });
        console.log(result);
        if (typeof (callbackObj) != 'undefined' && callbackObj.printDirect) {
            callbackObj.printDirect(JSON.stringify(result), "StickerDocument");
        }
        else {
            bs4pop.alert("请升级客户端或者在客户端环境运行当前程序", { type: 'error' });
        }
    }
    async openPrintSeperatePrintReport() {
        let row = this.grid.bootstrapTable("getSelections");
        if (row.length == 0) {
            bs4pop.alert("请选择一条数据", { type: 'warning' });
            return;
        }
        if (row.length > 1) {
            bs4pop.alert("请选择数据过多", { type: 'warning' });
            return;
        }
        let url = this.toUrl("/ecommerceBill/prePrintSeperatePrintReport.html?billId=" + row[0].id);
        var dia = bs4pop.dialog({
            title: '打印分销报告',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '90%',
            height: '78%',
            btns: [],
            onShowEnd: function () {
            }
        });
    }
    async doDetail() {
        let row = this.grid.bootstrapTable("getSelections");
        if (row.length == 0) {
            bs4pop.alert("请选择一条数据", { type: 'warning' });
            return;
        }
        if (row.length > 1) {
            bs4pop.alert("请选择数据过多", { type: 'warning' });
            return;
        }
        let selected_id = row[0].id;
        let url = this.toUrl('/ecommerceBill/view/' + selected_id);
        var dia = bs4pop.dialog({
            title: '查看委托单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: [],
            onShowEnd: function () {
            }
        });
    }
}
