class CommissionBillGrid extends WebConfig{
    grid: any;
    queryform: any;
    toolbar: any;
    btns:any[];
    highLightBill: any;

    constructor(grid: any, queryform: any, toolbar: any) {
        super();
        this.grid = grid;
        this.queryform = queryform;
        this.toolbar=toolbar;
        //this.btns=this.toolbar.find('button');
        window['commissionBillGrid']=this;
        $('#add-btn').on('click',async ()=>await this.openCreatePage());
        $('#detail-btn').on('click',async ()=>await this.doDetail());
        $('#audit-btn').on('click',async ()=>await this.audit());
        $('#createsheet-btn').on('click',async ()=>await this.doCreateCheckSheet());
        $('#batch-reviewCheck-btn').on('click',async ()=>await this.doReviewCheck());

        this.initAutoComplete($("[name='productName']"),'/toll/category');
        this.initAutoComplete($("[name='originName']"),'/toll/city');
        this.grid.on('check.bs.table uncheck.bs.table', async () => await this.checkAndShowHideBtns());
        this.grid.bootstrapTable('refreshOptions', {url: '/commissionBill/listPage.action'
            ,'queryParams':(params)=>this.buildQueryData(params)
            ,'ajaxOptions':{}

        });
        // this.grid.bootstrapTable({'query-params':(params)=>this.buildQueryData(params)});
        this.queryform.find('#query').click(async () => await this.queryGridData());


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
                    // @ts-ignore
                    bs4pop.alert("操作成功", {type: 'success'}, function () {
                            window['commissionBillGrid'].removeAllAndLoadData();
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

    private resetButtons(){
        var btnArray=['detail-btn','createsheet-btn','audit-btn','batch-reviewCheck-btn'];
        $.each(btnArray,function(i,btnId){
            $('#'+btnId).hide();
        })

    }


    private async checkAndShowHideBtns() {

        this.resetButtons();
        var rows=this.rows;
        if(rows.length==0){
            return;
        }
        if(this.isReviewCheck()){
            $('#batch-reviewCheck-btn').show();
        }
        var exists_detectState_pass= _.chain(rows).filter(item=>{
            return DetectStatusEnum.FINISH_DETECT==item.detectStatus;
        }).filter(item=>{
            return !_.isUndefined(item.checkSheetId)
        }).filter(item=>{
            return !_.isNull(item.checkSheetId)
        }).filter(item=>{
            return !_.isEmpty(item.checkSheetId)
        }).value().length>0;


        var hasName=false;
        var hasCorporateName=false;
        var rowsArray=$.makeArray(rows);

        var nameArray= _.chain(rows).map(item=>item.name).filter(item=>!_.isEmpty(item)).value();

        if(exists_detectState_pass){
            var distinctNameArray=nameArray.reduce(function(accumulator, currentValue, index, array ){
                if($.inArray(currentValue,array,index+1)==-1){
                    accumulator.push(currentValue);
                }
                return accumulator;
            },[] );

            var corporateNameArray= _.chain(rows).map(item=>item.corporateName).filter(item=>!_.isEmpty(item)).value();

            var distinctCorporateNameArray=corporateNameArray.reduce(function(accumulator, currentValue, index, array ){
                if($.inArray(currentValue,array,index+1)==-1){
                    accumulator.push(currentValue);
                }
                return accumulator;
            },[] );

            $('#createsheet-btn').hide();

            if(rowsArray.length==corporateNameArray.length&&distinctCorporateNameArray.length==1){ //全部都有企业名称，且企业名称相同
                $('#createsheet-btn').show();
            }else if(rowsArray.length==nameArray.length&&distinctCorporateNameArray.length==0&&distinctNameArray.length==1){ //全部没有企业名称，且业户名称相同
                $('#createsheet-btn').show();
            }else{
                $('#createsheet-btn').hide();
            }
        }else{
            $('#createsheet-btn').hide();
        }

        if(rows.length>1){
            //batch
            return;
        }
        var selected = rows[0];
        if(BillVerifyStatusEnum.WAIT_AUDIT==selected.verifyStatus){
            $('#audit-btn').show();
        }else{
            $('#audit-btn').hide();
        }

        $('#detail-btn').show();

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
            width: '98%',
            height: '98%',
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
        let selected_id=row[0].id;
        let url = this.toUrl('/commissionBill/view/' + selected_id +'/true');
        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '查看委托单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '78%',
            height: '98%',
            btns: [],
            onShowEnd:function(){
                //dia.$el.find('iframe')[0].contentWindow['RegisterBillGridObj']=cthis;
            }
        });
    }
    private buildQueryData(params){
        let temp = {
            rows: params.limit,   //页面大小
            page: ((params.offset / params.limit) + 1) || 1, //页码
            sort: params.sort,
            order: params.order
        }
        let data=$.extend(temp, this.queryform.serializeJSON());
        let jsonData=jq.removeEmptyProperty(data);
        return JSON.stringify(jsonData);
    }

    private async queryGridData(){
        if (!this.queryform.validate().form()) {
            //@ts-ignore
            bs4pop.notice("请完善必填项", {type: 'warning', position: 'topleft'});
            return;
        }
        this.grid.bootstrapTable('refresh');
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

        return _.chain(this.rows).filter(item=>{
            return DetectStatusEnum.FINISH_DETECT==item.detectStatus;
        }).filter(item=>!_.isUndefined(item.detectRequest)).filter(item=>{
            return DetectResultEnum.FAILED==item.detectRequest.detectResult
        }).value();
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

}
