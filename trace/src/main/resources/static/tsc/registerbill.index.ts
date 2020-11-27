class RegisterBillGrid extends PageTs {
    grid: any;
    highLightBill: any;

    constructor(grid: any) {
        super();
        let cthis = this;
        var onClickRow = function () {
            cthis.onClickRow()
        }
        this.grid = grid;
        this.grid.datagrid({
            onClickRow: onClickRow
            , onSelect: onClickRow
            , onSelectAll: onClickRow
            , onUnselect: onClickRow
            , onUnselectAll: onClickRow
            , loader: async function (param, success, error) {
                await cthis.loadRegisterBillGridData(param, success, error);
            }
        });

    }

    private queryRegisterBillGrid() {

        var opts = this.grid.datagrid("options");
        if (null == opts.url || "" == opts.url) {
            opts.url = this.contextPath + "/sg/registerBill/listPage.action";
        }
        //@ts-ignore
        if (!$('#queryForm').form("validate")) {
            return false;
        }

        this.grid.datagrid("reload", this.buildGridQueryData());
        this.resetButtons();

    }

    private resetButtons() {
        var btnArray = ['upload-detectreport-btn', 'upload-origincertifiy-btn', 'copy-btn', 'edit-btn', 'detail-btn', 'undo-btn', 'audit-btn', 'audit-withoutDetect-btn', 'auto-btn', 'sampling-btn', 'review-btn', 'handle-btn'
            , 'batch-audit-btn', 'batch-sampling-btn', 'batch-auto-btn', 'batch-undo-btn', 'remove-reportAndcertifiy-btn', 'createsheet-btn']
        for (var i = 0; i < btnArray.length; i++) {
            var btnId = btnArray[i];
            //@ts-ignore
            $('#' + btnId).linkbutton('enable');
            $('#' + btnId).hide();

        }
    }

    private isCreateSheet() {
        var arr = this.filterByProp('$_detectState', [BillDetectStateEnum.PASS, BillDetectStateEnum.REVIEW_PASS]).filter(function (v, i) {
            if (v.$_checkSheetId && v.$_checkSheetId != null && v.$_checkSheetId != '') {
                return false;
            } else {
                return true;
            }
        });
        return arr.length > 0;
    }

    private isSingleAudit() {
        return true;
    }

    private isSingleCopy() {
        return this.rows.length == 1;
    }

    private isSingleDetail() {
        return this.rows.length == 1;
    }

    private isSingleUploadOriginCertify() {
        return this.rows.length == 1;
    }

    private multiRows() {
        if (this.rows.length <= 1) {
            return;
        }

    }

    private singleRow() {
        if (this.rows.length != 1) {
            return;
        }

    }

    private async findHighLightBill() {
        try {
            return await jq.postJson2(this.contextPath + "/sg/registerBill/findHighLightBill.action", {}, {});
        } catch (e) {
            return {};
        }
    }

    private buildGridQueryData(): any {
        //@ts-ignore
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

    //表格查询
    private async loadRegisterBillGridData(param, success, error) {
        this.highLightBill = await this.findHighLightBill();
        $.extend(this.grid.datagrid("options").queryParams, this.buildGridQueryData());
        var datas = this.buildGridQueryData();
        var ret = await jq.postJson(this.contextPath + "/sg/registerBill/listPage.action", datas, {
            processData: true,type:'json'
        });
        if (ret && ret.rows) {
            success(ret);
        }else{
            success({rows:[],total:0});
        }

    }

    private onClickRow() {
        //@ts-ignore
        this.resetButtons();
        var rows = this.rows;
        if (rows.length == 0) {
            return;
        }

        this.isCreateSheet() ? $('#createsheet-btn').show() : $('#createsheet-btn').hide();

        //batch
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


        var waitAuditRows = this.filterByProp("$_state", [RegisterBillStateEnum.WAIT_AUDIT]);
        if (waitAuditRows.length == 1) {
            //接车状态是“已打回”,启用“撤销打回”操作
            $('#undo-btn').show();
            $('#audit-btn').show();
            $('#edit-btn').show();
            $('#upload-detectreport-btn').show();
        }
        let hasImages = _.chain(waitAuditRows).filter(item => {
            return item.originCertifiyUrl == '有' || item.detectReportUrl == '有'
        }).size().value() > 0;
        hasImages ? $('#remove-reportAndcertifiy-btn').show() : $('#remove-reportAndcertifiy-btn').hide();


        let auditWithoutDetect = _.chain(waitAuditRows)
            .filter(item => RegisterSourceEnum.TALLY_AREA == item.registerSource)
            .filter(item => {
                return item.originCertifiyUrl && item.originCertifiyUrl != null
            })
            .filter(item => {
                item.originCertifiyUrl != '' && item.originCertifiyUrl != '无'
            }).size().value() > 0;
        auditWithoutDetect ? $('#audit-withoutDetect-btn').show() : $('#audit-withoutDetect-btn').hide();


        var WAIT_SAMPLE_ROWS = this.filterByProp("$_state", [RegisterBillStateEnum.ALREADY_CHECK]);
        if (WAIT_SAMPLE_ROWS.length == 1) {
            $('#undo-btn').show();
            $('#auto-btn').show();
            $('#sampling-btn').show();
        }

        var ALREADY_CHECK_ROWS = this.filterByProp("$_state", [RegisterBillStateEnum.ALREADY_CHECK]);
        var review = _.chain(ALREADY_CHECK_ROWS).filter(item => BillDetectStateEnum.NO_PASS == item.$_detectState).size().value() > 0;
        review ? $('#review-btn').show() : $('#review-btn').hide();


        var review = _.chain(ALREADY_CHECK_ROWS)
            .filter(item => BillDetectStateEnum.REVIEW_NO_PASS == item.$_detectState)
            .filter(item => item.handleResult == null || item.handleResult == '')
            .size().value() > 0;
        review ? $('#review-btn').show() : $('#review-btn').hide();

        $('#handle-btn').show();
    }

    get rows() {
        return RegisterBillGrid.getInstance().grid.datagrid("getSelections");
    }

    public static getInstance(): RegisterBillGrid {
        return window['registerBillGrid'] as RegisterBillGrid;
    }

    private handleTimeUpdateEvent = (event) => {
        // Something
    }

    public static queryProduct(param, success, error) {
        // @ts-ignore
        var productName = $('#productCombobox').combotree('getText');
        var data = [];
        var url = RegisterBillGrid.getInstance().contextPath + '/toll/category?name=' + productName;
        $.ajax({
            url: url,
            success: function (resp) {
                data = resp.suggestions.map(function (v, i) {
                    v['parentId'] = '';
                    return v
                });
                success(data);
            }
        })


        // return data;
    }

    public isBatchSimpling() {
        if (this.filterByProp('$_state', [RegisterBillStateEnum.WAIT_SAMPLE]).length > 0) {
            return true;
        }
        return false;
    }

    public isBatchAudit() {
        if (this.filterByProp('$_state', [RegisterBillStateEnum.WAIT_AUDIT]).length > 0) {
            return true;
        }
        return false;
    }

    /**
     * 是否可以进行批量采样
     * @param applyStates
     */
    public isBatchAuto() {
        if (this.filterByProp('$_state', [RegisterBillStateEnum.WAIT_SAMPLE]).length > 0) {
            return true;
        }
        return false;

    }

    /**
     * 是否可以批量撤销
     * @param applyStates
     */
    public isBatchUndo() {
        if (this.filterByProp('$_state', [RegisterBillStateEnum.WAIT_AUDIT]).length > 0) {
            return true;
        }
        return false;
    }


    /**
     *
     * @param applyStates
     */
    public async doBatchUndo() {
        if (!this.isBatchUndo()) {
            // @ts-ignore
            swal({
                title: '警告',
                text: '没有数据可以进行批量撤销',
                type: 'warning',
                width: 300
            });
            return;
        }
        var cthis=this;
        var arr = this.filterByProp("$_state", [RegisterBillStateEnum.WAIT_AUDIT]);

        let promise = new Promise((resolve, reject) => {
            // @ts-ignore
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
        let result = await promise; // wait until the promise resolves (*)
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
                        // @ts-ignore
                        TLOG.component.operateLog('登记单管理', "批量撤销", '【IDS】:' + JSON.stringify(idlist));
                        // @ts-ignore
                        layer.alert('操作成功', {
                                title: '操作',
                                time: 600,
                                end: function () {
                                    // @ts-ignore
                                    layer.closeAll();
                                    cthis.queryRegisterBillGrid();
                                }
                            },
                            function () {
                                // @ts-ignore
                                layer.closeAll();
                                cthis.queryRegisterBillGrid();
                            }
                        );

                    } else {
                        // @ts-ignore
                        layer.closeAll();
                        // @ts-ignore
                        swal('错误', data.result, 'error');
                    }
                },
                error: function () {
                    // @ts-ignore
                    layer.closeAll();
                    // @ts-ignore
                    swal('错误', '远程访问失败', 'error');
                }
            });
        }
        // @ts-ignore
        layer.closeAll();
    }

    private filterByProp(prop: string, propValues: any[]) {
        let arrayData = $.makeArray(RegisterBillGrid.getInstance().rows);
        let arrayValue = $.makeArray(propValues);
        let values: any[] = _.chain(arrayData).filter(element => $.inArray(element[prop], arrayValue) > -1).value();
        return values;
    }

}
