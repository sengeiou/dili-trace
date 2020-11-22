var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var CommissionBillGrid = (function () {
    function CommissionBillGrid(grid, billStateEnums, billDetectStateEnums) {
        this.grid = grid;
        this.billStateEnums = billStateEnums;
        this.billDetectStateEnums = billDetectStateEnums;
    }
    Object.defineProperty(CommissionBillGrid.prototype, "rows", {
        get: function () {
            return this.grid.datagrid("getSelections");
        },
        enumerable: false,
        configurable: true
    });
    CommissionBillGrid.prototype.findReviewCheckData = function () {
        var detectStateArr = [this.billDetectStateEnums.NO_PASS, this.billDetectStateEnums.REVIEW_NO_PASS];
        var alreadyCheckArray = this.filterByProp('$_state', this.billStateEnums.ALREADY_CHECK);
        var values = _.chain(alreadyCheckArray).filter(function (element) { return $.inArray(element['$_detectState'], detectStateArr) > -1; }).value();
        return values;
    };
    CommissionBillGrid.prototype.isReviewCheck = function () {
        return this.findReviewCheckData().length > 0;
    };
    CommissionBillGrid.prototype.doReviewCheck = function () {
        return __awaiter(this, void 0, void 0, function () {
            var arr, promise, result, _url, idlist;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        if (!this.isReviewCheck()) {
                            swal({
                                title: '警告',
                                text: '没有数据可以进行批量撤销',
                                type: 'warning',
                                width: 300
                            });
                            return [2];
                        }
                        arr = this.findReviewCheckData();
                        promise = new Promise(function (resolve, reject) {
                            layer.confirm('请确认是否复检选中数据？<br/>' + arr.map(function (e) { return e.code; }).join("<br\>"), {
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
                        return [4, promise];
                    case 1:
                        result = _a.sent();
                        if (result) {
                            _url = ctx + "/commissionBill/doBatchReviewCheck.action";
                            idlist = arr.map(function (e) { return e.id; });
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
                        return [2];
                }
            });
        });
    };
    CommissionBillGrid.prototype.filterByProp = function (prop, propValues) {
        var arrayData = $.makeArray(this.rows);
        var arrayValue = $.makeArray(propValues);
        var values = _.chain(arrayData).filter(function (element) { return $.inArray(element[prop], arrayValue) > -1; }).value();
        return values;
    };
    return CommissionBillGrid;
}());
//# sourceMappingURL=commissionbill.index.js.map