class RegisterBillGrid {
    constructor(grid, billStateEnums) {
        this.grid = grid;
        this.billStateEnums = billStateEnums;
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
        var url = RegisterBillGrid.getInstance().ctx + '/toll/category?name=' + productName;
        $.ajax({
            url: url,
            success: function (resp) {
                data = resp.suggestions.map(function (v, i) { v['parentId'] = ''; return v; });
                success(data);
            }
        });
    }
    isBatchSimpling() {
        if (this.filterByProp('$_state', this.billStateEnums.WAIT_SAMPLE).length > 0) {
            return true;
        }
        return false;
    }
    isBatchAudit() {
        if (this.filterByProp('$_state', this.billStateEnums.WAIT_AUDIT).length > 0) {
            return true;
        }
        return false;
    }
    isBatchAuto() {
        if (this.filterByProp('$_state', this.billStateEnums.WAIT_SAMPLE).length > 0) {
            return true;
        }
        return false;
    }
    isBatchUndo() {
        if (this.filterByProp('$_state', this.billStateEnums.WAIT_AUDIT).length > 0) {
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
        var arr = this.filterByProp("$_state", this.billStateEnums.WAIT_AUDIT);
        let promise = new Promise((resolve, reject) => {
            layer.confirm('请确认是否撤销选中数据？<br/>' + arr.map(e => e.code).join("<br\>"), { btn: ['确定', '取消'], title: "警告！！！",
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
            var _url = RegisterBillGrid.getInstance().ctx + "/registerBill/batchUndo.action";
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