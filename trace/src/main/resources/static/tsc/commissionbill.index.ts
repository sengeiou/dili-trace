class CommissionBillGrid extends WebConfig{
    grid: any;
    queryform: any;
    billStateEnums: any;
    billDetectStateEnums: any;
    highLightBill: any;

    constructor(grid: any, queryform: any, billStateEnums: any, billDetectStateEnums: any) {
        super();
        this.grid = grid;
        this.queryform = queryform;
        this.billStateEnums = billStateEnums;
        this.billDetectStateEnums = billDetectStateEnums;
        window['RegisterBillGridObj']=this;

        //this.queryform.find('#query').click(async () => await this.queryGridData());
        $('#add-btn').on('click',async ()=>await this.openCreatePage());

        this.initAutoComplete($("[name='productName']"),'/toll/category');
        this.initAutoComplete($("[name='originName']"),'/toll/city');
        //load data
        /*(async ()=>{
            await this.queryGridData();
        })();*/
    }

    public removeAllAndLoadData(){
        //@ts-ignore
        bs4pop.removeAll();
        (async ()=>{
            await this.queryGridData();
        })();
    }

    private openCreatePage() {
        let url = this.toUrl("/commissionBill/create.html");
        var cthis=this;
        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '新增委托单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: [],
            onShowEnd:function(){
                //dia.$el.find('iframe')[0].contentWindow['RegisterBillGridObj']=cthis;
            }
        });
    }

    private async queryGridData(){
        console.log("queryGridData");
        if (!this.queryform.validate().form()) {
            //@ts-ignore
            bs4pop.notice("请完善必填项", {type: 'warning', position: 'topleft'});
            return;
        }
        await this.remoteQuery();
    }
    private async remoteQuery() {
        $('#toolbar button').attr('disabled', "disabled");
        this.grid.bootstrapTable('showLoading');
        // 查询要高亮显示的数据
        this.highLightBill = await this.findHighLightBill();

        try{
            let url = this.toUrl( "/commissionBill/listPage.action");
            let resp = await jq.postJson(url, this.queryform.serializeJSON(), {});
            this.grid.bootstrapTable('load',resp);
        }catch (e){
            console.error(e);
            this.grid.bootstrapTable('load',{rows:[],total:0});
        }
        this.grid.bootstrapTable('hideLoading');
        $('#toolbar button').removeAttr('disabled');
    }

    private async findHighLightBill() {
        try {
            var url=this.toUrl("/commissionBill/findHighLightBill.action");
            return await jq.postJson(url, {}, {});
        } catch (e) {
            console.log(e);
            return {};
        }
    }

    get rows() {
        return this.grid.datagrid("getSelections");
    }

    private findReviewCheckData() {
        var detectStateArr = [this.billDetectStateEnums.NO_PASS, this.billDetectStateEnums.REVIEW_NO_PASS];
        var alreadyCheckArray = this.filterByProp('$_state', this.billStateEnums.ALREADY_CHECK);
        let values: any[] = _.chain(alreadyCheckArray).filter(element => $.inArray(element['$_detectState'], detectStateArr) > -1).value();
        return values;
    }

    public isReviewCheck() {
        return this.findReviewCheckData().length > 0;
    }

    /**
     * 复检
     */
    public async doReviewCheck() {

        if (!this.isReviewCheck()) {
            // @ts-ignore
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
            // @ts-ignore
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
        let result = await promise; // wait until the promise resolves (*)
        if (result) {
            // @ts-ignore
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
                        // @ts-ignore
                        TLOG.component.operateLog('登记单管理', "批量复检", '【IDS】:' + JSON.stringify(idlist));
                        // @ts-ignore
                        layer.alert('操作成功', {
                                title: '操作',
                                time: 600,
                                end: function () {
                                    // @ts-ignore
                                    layer.closeAll();
                                    // @ts-ignore
                                    queryRegisterBillGrid();
                                }
                            },
                            function () {
                                // @ts-ignore
                                layer.closeAll();
                                // @ts-ignore
                                queryRegisterBillGrid();
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
        let arrayData = $.makeArray(this.rows);
        let arrayValue = $.makeArray(propValues);
        let values: any[] = _.chain(arrayData).filter(element => $.inArray(element[prop], arrayValue) > -1).value();
        return values;
    }

    private  initAutoComplete(selector,url){
        $(selector).keydown(function (e){
            if(e.keyCode == 13){
                // $(selector).data('keycode',e.keyCode);
                // console.info('keydown')
            }
        });
        $(selector).data('oldvalue','');
        $(selector).on('change',function () {
            var oldvalue=$(selector).data('oldvalue');
            var val=$(this).val();
            if(oldvalue!=val){
                $(this).siblings('input').val('');
            }
        });

        //@ts-ignore
        // 产地联系输入
        $(selector).devbridgeAutocomplete({
            noCache: 1,
            serviceUrl: url,  // 数据地址
            dataType: 'json',
            onSearchComplete: function (query, suggestions) {
            },
            showNoSuggestionNotice: true,
            noSuggestionNotice: "不存在，请重输！",
            autoSelectFirst:true,
            autoFocus: true,
            onSelect: function (suggestion) {
                console.info('onSelect')
                var self = this;
                var idField = $(self).siblings('input');
                idField.val(suggestion.id);
                $(self).val(suggestion.value.trim());
                $(selector).data('oldvalue',suggestion.value);

                //@ts-ignore
                var v=$(self).valid();
            }
        });
    }
}
