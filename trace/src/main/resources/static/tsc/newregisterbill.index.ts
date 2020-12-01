import Table = WebAssembly.Table;

class NewRegisterBillGrid extends WebConfig {
    grid: any;
    queryform: any;
    highLightBill: any;

    constructor(grid: any, queryform: any) {
        super();
        this.grid = grid;
        this.queryform = queryform;

        this.grid.on('check.bs.table', () => this.onClickRow());
        this.grid.on('uncheck.bs.table', () => this.onClickRow());

        this.queryform.find('#query').click(async () => await this.queryGridData())
        $('#edit-btn').on('click',async ()=>await this.openEditPage())
        $('#btn_add').on('click',async ()=>await this.openCreatePage())
        $('#copy-btn').on('click',async ()=>await this.openCopyPage())
        $('#upload-origincertifiy-btn').on('click',async ()=>await this.openUploadOriginCertifyPage())
        $('#upload-detectreport-btn').on('click',async ()=>await this.openUploadDetectReportPage())
        $('#upload-handleresult-btn').on('click',async ()=>await this.openUploadHandleResultPage())
        $('#detail-btn').on('click',async ()=>await this.openDetailPage())
        $('#audit-btn').on('click',async ()=>await this.openAuditPage())
        $('#undo-btn').on('click',async ()=>await this.doUndo())
        $('#batch-undo-btn').on('click',async ()=>await this.doBatchUndo())
        $('#batch-audit-btn').on('click',async ()=>await this.doBatchAudit())
        //$('#createsheet-btn').on('click',async ()=>await this.doBatchAudit())
        //$('#sampling-btn').on('click',async ()=>await this.doBatchAudit())
        //$('#batch-auto-btn').on('click',async ()=>await this.doBatchAudit())
        //$('#auto-btn').on('click',async ()=>await this.doBatchAudit())
        //$('#remove-reportAndcertifiy-btn').on('click',async ()=>await this.doBatchAudit())
        //$('#batch-sampling-btn').on('click',async ()=>await this.doBatchAudit())

        //$('#audit-withoutDetect-btn').on('click',async ()=>await this.doBatchAudit())
        //$('#review-btn').on('click',async ()=>await this.doBatchAudit())


        $(window).on('resize',()=> this.grid.bootstrapTable('resetView') );
        (async ()=>{
            await this.queryGridData();
        })();

    }
    private async doUndo(){
        var row=this.rows[0]
        var url=this.toUrl('/newRegisterBill/doUndo.action');
        var resp=await  jq.postJson(url,{id:row.billId})
        //var url=this.toUrl('/newRegisterBill/doUndo.action?id='+row.billId);
        alert('undo')
    }


    private async  doBatchAudit(){
        var rows=this.rows;
        if (rows.length==0) {
            //@ts-ignore
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
        var codeList=[];
        var batchIdList=[];
        var onlyWithOriginCertifiyUrlIdList=[];
        for(var i=0;i<rows.length;i++){
            if (rows[i].$_state == RegisterBillStateEnum.WAIT_AUDIT ){
                batchIdList.push(rows[i].id);
                codeList.push(rows[i].code)
                if(rows[i].originCertifiyUrl=='有'&&rows[i].detectReportUrl=='无'){
                    onlyWithOriginCertifiyUrlIdList.push(rows[i].code)
                };
            }
        }
        if(codeList.length==0){
            //@ts-ignore
            layer.alert('所选登记单不能进行审核')
            return;
        }
        let promise = new Promise((resolve, reject) => {
            //@ts-ignore
            layer.confirm(codeList.join("<br\>"), {btn: ['确定', '取消'], title: "批量审核"},function () {
                    resolve("yes");
                },function(){
                    resolve("cancel");
                }
            );
        });

        let result = await promise; // wait until the promise resolves (*)
        if(result=='yes'){
            var passWithOriginCertifiyUrl=null;
            if(onlyWithOriginCertifiyUrlIdList.length>0){
                let reconfirmPromise = new Promise((resolve, reject) => {
                    //@ts-ignore
                    layer.confirm('只有产地证明登记单:<br/>'+onlyWithOriginCertifiyUrlIdList.join("<br\>"), {btn: ['不检测', '检测'], title: "是否不再进行检测"},function () {
                        resolve(true);
                    },function(){
                        resolve(false);
                    });
                });
                passWithOriginCertifiyUrl=await reconfirmPromise;
            }
            //@ts-ignore
            layer.closeAll();
            var cthis=this;
            $.ajax({
                type: "POST",
                url: "${contextPath}/registerBill/doBatchAudit",
                processData:true,
                contentType:'application/json;charset=utf-8',
                data:JSON.stringify({registerBillIdList:batchIdList,pass:true,passWithOriginCertifiyUrl:passWithOriginCertifiyUrl}),
                dataType: "json",
                async : true,
                success: function (ret) {
                    if(ret.success){
                        var failureList=ret.data.failureList;
                        if(failureList.length==0){
                            // _registerBillGrid.datagrid("reload");
                            cthis.queryGridData();
                            //@ts-ignore
                            TLOG.component.operateLog('登记单管理',"批量审核","【编号】:"+codeList.join(','));
                            //@ts-ignore
                            layer.alert('操作成功：</br>'+ret.data.successList.join('</br>'),{title:'操作',time : 3000});

                        }else{
                            //@ts-ignore
                            swal(
                                '操作',
                                '成功:'+ret.data.successList.join('</br>')+'失败:'+ret.data.failureList.join('</br>'),
                                'info'
                            );
                        }
                    }else{
                        //@ts-ignore
                        swal(
                            '错误',
                            ret.result,
                            'error'
                        );
                    }
                },
                error: function(){
                    //@ts-ignore
                    swal(
                        '错误',
                        '远程访问失败',
                        'error'
                    );
                }
            });

        }


    }
    private async  openAuditPage(){
        var row=this.rows[0]
        var url=this.toUrl('/newRegisterBill/audit.html?id='+row.billId);
        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '进场审核',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
    }
    private async  openDetailPage(){
        var row=this.rows[0]
        var url=this.toUrl('/newRegisterBill/view.html?id='+row.billId);
        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '报备单详情',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
    }
    private async openUploadHandleResultPage(){
        var row=this.rows[0]
        var url=this.toUrl('/newRegisterBill/uploadHandleResult.html?id='+row.billId);
        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '上传处理结果',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
    }
    private async openUploadDetectReportPage(){
        var row=this.rows[0]

        var url=this.toUrl('/newRegisterBill/uploadDetectReport.html?id='+row.billId);
        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '上传检测报告',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
    }
    private async openUploadOriginCertifyPage(){
        var row=this.rows[0]
        var url=this.toUrl('/newRegisterBill/uploadOrigincertifiy.html?id='+row.billId);
        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '上传产地证明',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });

    }

    private openCopyPage(){
        var row=this.rows[0]
        let url = this.toUrl("/newRegisterBill/copy.html?id="+row.billId);

        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '复制报备单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
    }
    private openCreatePage() {
        let url = this.toUrl("/newRegisterBill/create.html");

        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '新增报备单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
    }
    private openEditPage() {
        var row=this.rows[0]
        let url = this.toUrl("/newRegisterBill/edit.html?id="+row.billId);

        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '修改报备单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
    }
    private async queryGridData(){
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
        this.resetButtons();
        this.highLightBill = await this.findHighLightBill();

        try{
            let url = this.toUrl( "/newRegisterBill/listPage.action");
            let resp = await jq.postJson(url, this.queryform.serializeJSON(), {});
            this.grid.bootstrapTable('load',resp);
        }catch (e){
            console.error(e);
            this.grid.bootstrapTable('load',{rows:[],total:0});
        }
        this.grid.bootstrapTable('hideLoading');
        $('#toolbar button').removeAttr('disabled');
    }
    private resetButtons() {
        var btnArray = ['upload-detectreport-btn', 'upload-origincertifiy-btn', 'copy-btn', 'edit-btn', 'detail-btn', 'undo-btn', 'audit-btn', 'audit-withoutDetect-btn', 'auto-btn', 'sampling-btn', 'review-btn', 'upload-handleresult-btn'
            , 'batch-audit-btn', 'batch-sampling-btn', 'batch-auto-btn', 'batch-undo-btn', 'remove-reportAndcertifiy-btn', 'createsheet-btn']
        for (var i = 0; i < btnArray.length; i++) {
            var btnId = btnArray[i];
            $('#' + btnId).show();

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
            var url=this.toUrl("/newRegisterBill/findHighLightBill.action");
            return await jq.postJson(url, {}, {});
        } catch (e) {
            console.log(e);
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

        $('#upload-handleresult-btn').show();
    }

    get rows() {
        let rows = this.grid.bootstrapTable('getSelections');
        return rows;
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
        var url =  '/toll/category?name=' + productName;
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
        var cthis = this;
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
            var _url =  "/registerBill/doBatchUndo.action";
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
                                end: async function () {
                                    // @ts-ignore
                                    layer.closeAll();
                                    await cthis.queryGridData();
                                }
                            },
                            async function () {
                                // @ts-ignore
                                layer.closeAll();
                                await cthis.queryGridData();
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
