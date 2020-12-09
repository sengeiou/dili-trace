class CommissionBillGrid extends WebConfig {
    constructor(grid, queryform, toolbar) {
        super();
        this.grid = grid;
        this.queryform = queryform;
        this.toolbar = toolbar;
        this.btns = this.toolbar.find('button');
        window['commissionBillGrid'] = this;
        $('#add-btn').on('click', async () => await this.openCreatePage());
        $('#detail-btn').on('click', async () => await this.doDetail());
        this.initAutoComplete($("[name='productName']"), '/toll/category');
        this.initAutoComplete($("[name='originName']"), '/toll/city');
        this.grid.on('check.bs.table uncheck.bs.table', async () => await this.checkAndShowHideBtns());
        this.grid.bootstrapTable('refreshOptions', { url: '/commissionBill/listPage.action',
            'queryParams': (params) => this.buildQueryData(params),
            'ajaxOptions': {}
        });
        this.queryform.find('#query').click(async () => await this.queryGridData());
    }
    resetButtons() {
        var btnArray = ['detail-btn', 'createsheet-btn', 'audit-btn', 'batch-reviewCheck-btn'];
        $.each(btnArray, function (i, btnId) {
            $('#' + btnId).hide();
        });
    }
    async checkAndShowHideBtns() {
        this.resetButtons();
        var rows = this.rows;
        if (rows.length == 0) {
            return;
        }
        if (this.isReviewCheck()) {
            $('#batch-reviewCheck-btn').show();
        }
        var exists_detectState_pass = _.chain(rows).filter(item => {
            return 50 == item.detectStatus;
        }).filter(item => {
            return !_.isUndefined(item.checkSheetId);
        }).filter(item => {
            return !_.isNull(item.checkSheetId);
        }).filter(item => {
            return !_.isEmpty(item.checkSheetId);
        }).value().length > 0;
        var hasName = false;
        var hasCorporateName = false;
        var rowsArray = $.makeArray(rows);
        var nameArray = _.chain(rows).map(item => item.name).filter(item => !_.isEmpty(item)).value();
        if (exists_detectState_pass) {
            var distinctNameArray = nameArray.reduce(function (accumulator, currentValue, index, array) {
                if ($.inArray(currentValue, array, index + 1) == -1) {
                    accumulator.push(currentValue);
                }
                return accumulator;
            }, []);
            var corporateNameArray = _.chain(rows).map(item => item.corporateName).filter(item => !_.isEmpty(item)).value();
            var distinctCorporateNameArray = corporateNameArray.reduce(function (accumulator, currentValue, index, array) {
                if ($.inArray(currentValue, array, index + 1) == -1) {
                    accumulator.push(currentValue);
                }
                return accumulator;
            }, []);
            $('#createsheet-btn').hide();
            if (rowsArray.length == corporateNameArray.length && distinctCorporateNameArray.length == 1) {
                $('#createsheet-btn').show();
            }
            else if (rowsArray.length == nameArray.length && distinctCorporateNameArray.length == 0 && distinctNameArray.length == 1) {
                $('#createsheet-btn').show();
            }
            else {
                $('#createsheet-btn').hide();
            }
        }
        else {
            $('#createsheet-btn').hide();
        }
        if (rows.length > 1) {
            return;
        }
        var selected = rows[0];
        if (0 == selected.verifyStatus) {
            $('#audit-btn').show();
        }
        else {
            $('#audit-btn').hide();
        }
        $('#detail-btn').show();
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
            width: '98%',
            height: '98%',
            btns: [],
            onShowEnd: function () {
            }
        });
    }
    doDetail() {
        let row = this.rows();
        if (row.length == 0) {
            return;
        }
        console.log(row);
        let selected_id = row[0].id;
        let url = this.toUrl('/commissionBill/view/' + selected_id + '/true');
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
    buildQueryData(params) {
        let temp = {
            rows: params.limit,
            page: ((params.offset / params.limit) + 1) || 1,
            sort: params.sort,
            order: params.order
        };
        let data = $.extend(temp, this.queryform.serializeJSON());
        let jsonData = jq.removeEmptyProperty(data);
        return JSON.stringify(jsonData);
    }
    async queryGridData() {
        if (!this.queryform.validate().form()) {
            bs4pop.notice("请完善必填项", { type: 'warning', position: 'topleft' });
            return;
        }
        this.grid.bootstrapTable('refresh');
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
        return _.chain(this.rows).filter(item => {
            return 50 == item.detectStatus;
        }).filter(item => !_.isUndefined(item.detectRequest)).filter(item => {
            return 2 == item.detectRequest.detectResult;
        }).value();
    }
    isReviewCheck() {
        return this.findReviewCheckData().length > 0;
    }
    async doReviewCheck() {
        if (!this.isReviewCheck()) {
            swal({
                title: '警告',
                text: '没有数据可以进行批量撤销',
                type: 'warning',
                width: 300
            });
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