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
        window['commissionBillGrid']=this;
        //this.queryform.find('#query').click(async () => await this.queryGridData());
        $('#add-btn').on('click',async ()=>await this.openCreatePage());
        $('#detail-btn').on('click',async ()=>await this.doDetail());
        $('#audit-btn').on('click',async ()=>await this.audit());
        $('#createsheet-btn').on('click',async ()=>await this.doCreateCheckSheet());
        $('#batch-reviewCheck-btn').on('click',async ()=>await this.doReviewCheck());

        this.initAutoComplete($("[name='productName']"),'/toll/category');
        this.initAutoComplete($("[name='originName']"),'/toll/city');
        //load data
        /*(async ()=>{
            await this.queryGridData();
        })();*/
    }
    private doCreateCheckSheet(){
        let  row= this.grid.bootstrapTable("getSelections");
        if (row.length == 0) {
            //@ts-ignore
            bs4pop.alert("请选择一条数据", {type: 'warning'});
            return;
        }

        var idList=row.map(function(v,i){return v.id});
        let param = $.param({idList:idList},true);
        let url = this.toUrl("/checkSheet/edit.html?" + param);
        //@ts-ignore
        var audit_dia = bs4pop.dialog({
            title: '创建打印报告单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '50%',
            height: '70%',
            btns: [],
            onShowEnd:function(){
            }
        });
    }

    private  audit(){
        let  row= this.grid.bootstrapTable("getSelections");
        if (row.length == 0) {
            //@ts-ignore
            bs4pop.alert("请选择一条数据", {type: 'warning'});
            return;
        }
        if (row.length > 1) {
            //@ts-ignore
            bs4pop.alert("请选择数据过多", {type: 'warning'});
            return;
        }
        console.log(row);
        let url = this.toUrl("/commissionBill/audit.html?billId=" + row[0].id);
        //@ts-ignore
        var audit_dia = bs4pop.dialog({
            title: '审核',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '50%',
            height: '51%',
            btns: [],
            onShowEnd:function(){
                //dia.$el.find('iframe')[0].contentWindow['RegisterBillGridObj']=cthis;
            }
        });

    }
    public doAudit(id){
        let url = this.toUrl("/commissionBill/doAuditCommissionBillByManager.action");
        $.ajax({
            type: "POST",
            data:{billId:id},
            url: url,
            processData:true,
            dataType: "json",
            async : true,
            success: function (ret) {
                if(ret.success){
                    //TLOG.component.operateLog('登记单管理',"审核","【编号】:"+selected.code);
                    // layer.alert('操作成功',{title:'操作',time : 3000});
                    //@ts-ignore
                    bs4pop.alert("操作成功", {type: 'success'}, function () {
                        try {
                            window['commissionBillGrid'].removeAllAndLoadData();
                        }
                    });

                }else{
                    //@ts-ignore
                    bs4pop.alert("操作失败", {type: 'warning'});
                }
            },
            error: function(){
                //@ts-ignore
                bs4pop.alert("操作失败", {type: 'error'});
            }
        });
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
        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '新增委托单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '70%',
            height: '70%',
            btns: [],
            onShowEnd:function(){
                //dia.$el.find('iframe')[0].contentWindow['RegisterBillGridObj']=cthis;
            }
        });
    }

    private doDetail(){
        let  row= this.grid.bootstrapTable("getSelections");
        if (row.length == 0) {
            //@ts-ignore
            bs4pop.alert("请选择一条数据", {type: 'warning'});
            return;
        }
        console.log(row);
        let selected_id=row[0].id;
        let url = this.toUrl('/commissionBill/view/' + selected_id +'/true');
        //@ts-ignore
        var detail_dia = bs4pop.dialog({
            title: '查看委托单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '70%',
            height: '75%',
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
        return this.grid.bootstrapTable("getSelections");
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
            bs4pop.alert("请选择一条数据", {type: 'warning'});
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

}
