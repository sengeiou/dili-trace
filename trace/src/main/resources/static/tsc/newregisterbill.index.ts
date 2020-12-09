 // import $ from 'jquery';
 // import _ from 'underscore';


class NewRegisterBillGrid extends WebConfig {
    grid: any;
    uid:string;
    queryform: any;
    highLightBill: any;
    btns:any[];
    toolbar:any;

    constructor(grid: any, queryform: any, toolbar: any) {
        super();
        this.grid = grid;
        this.queryform = queryform;
        this.toolbar=toolbar;
        this.btns=this.toolbar.find('button');
        this.uid=_.uniqueId("trace_id_");

        this.initAutoComplete($("[name='productName']"),'/toll/category');
        this.initAutoComplete($("[name='originName']"),'/toll/city');

        $(window).on('resize',()=> this.grid.bootstrapTable('resetView') );

        // $(window.document.body).attr("trace_id",this.uid);
        var cthis=this;
        window['RegisterBillGridObj']=this;

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
        $('#createsheet-btn').on('click',async ()=>await this.openCreateSheetPage())
        $('#sampling-btn').on('click',async ()=>await this.doSamplingCheck())
        $('#batch-auto-btn').on('click',async ()=>await this.doBatchAutoCheck())
        $('#auto-btn').on('click',async ()=>await this.doAutoCheck())
        $('#remove-reportAndcertifiy-btn').on('click',async ()=>await this.doRemoveReportAndCertifiy())
        $('#batch-sampling-btn').on('click',async ()=>await this.doBatchSamplingCheck())

        $('#audit-withoutDetect-btn').on('click',async ()=>await this.doAuditWithoutDetect());
        $('#review-btn').on('click',async ()=>await this.doReviewCheck());

        this.grid.on('check.bs.table uncheck.bs.table', async () => await this.checkAndShowHideBtns());
       // this.grid.bootstrapTable('refreshOptions', {url: '/chargeRule/listPage.action', pageSize: parseInt(size)});

        // this.grid.bootstrapTable({'query-params':(params)=>this.buildQueryData(params)});
        this.queryform.find('#query').click(async () => await this.queryGridData());

        window.addEventListener('message', function(e) {
            var data=JSON.parse(e.data);
            if(data.obj&&data.fun){
                if(data.obj=='RegisterBillGridObj'){
                    cthis[data.fun].call(cthis,data.args)
                }
           }

        }, false);
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
    public removeAllAndLoadData(){
        //@ts-ignore
        bs4pop.removeAll();
        (async ()=>{
            await this.queryGridData();
        })();
    }
    public async doAudit(billId:string,code:string){
        //@ts-ignore
        bs4pop.removeAll();
        let promise = new Promise((resolve, reject) => {
            //@ts-ignore
            bs4pop.confirm('是否审核通过当前登记单？<br/>'+code,
                {type: 'warning',btns: [
                        {label: '通过', className: 'btn-primary',onClick(cb){  resolve("true");}},
                        {label: '不通过', className: 'btn-primary',onClick(cb){resolve("false");}},
                        {label: '取消', className: 'btn-cancel',onClick(cb){resolve("cancel");}},
                    ]});

        });
        let result = await promise; // wait until the promise resolves (*)
        if(result=='cancel'){
            return;
        }
        let url= this.toUrl("/newRegisterBill/doAudit.action?id="+billId+"&pass="+result);

        try{
            var resp=await jq.ajaxWithProcessing({type: "GET",url: url,processData:true,dataType: "json"});
            if(!resp.success){
                //@ts-ignore
                bs4pop.alert(resp.message, {type: 'error'});
                return;
            }
            await this.queryGridData();
            //@ts-ignore
            //TLOG.component.operateLog('登记单管理',"审核","【编号】:"+selected.code);
            //@ts-ignore
            bs4pop.removeAll()
            //@ts-ignore
            bs4pop.alert('操作成功', {type: 'info',autoClose: 600});
        }catch (e){
            debugger
            //@ts-ignore
            bs4pop.alert('远程访问失败', {type: 'error'});
        }
    }
    private async  doReviewCheck() {
        var selected = this.rows[0];
        let url= this.toUrl("/newRegisterBill/doReviewCheck.action?id="+ selected.id);
        let sure=await popwrapper.confirm('请确认是否复检？',undefined);
        if(!sure){
            return;
        }

        try{
            var resp=await jq.ajaxWithProcessing({type: "GET",url: url,processData:true,dataType: "json"});
            if(!resp.success){
                //@ts-ignore
                bs4pop.alert(resp.message, {type: 'error'});
                return;
            }
            await this.queryGridData();
            //@ts-ignore
            //TLOG.component.operateLog('登记单管理',"复检","【编号】:"+selected.code);
            //@ts-ignore
            bs4pop.removeAll()
            //@ts-ignore
            bs4pop.alert('操作成功', {type: 'info',autoClose: 600});
        }catch (e){
            //@ts-ignore
            bs4pop.alert('远程访问失败', {type: 'error'});
        }


    }
    private async   doAuditWithoutDetect(){
        var selected = this.rows[0];
        let url= this.toUrl("/newRegisterBill/doAuditWithoutDetect.action");
        let sure=await popwrapper.confirm('确认审核不检测？',undefined);
        if(!sure){
            return;
        }

        try{
            var resp=await jq.ajaxWithProcessing({type: "POST",data:{id:selected.id},url: url,processData:true,dataType: "json"});
            if(!resp.success){
                //@ts-ignore
                bs4pop.alert(resp.message, {type: 'error'});
                return;
            }
            await this.queryGridData();
            //@ts-ignore
            //TLOG.component.operateLog('登记单管理',"审核不检测","【编号】:"+selected.code);
            //@ts-ignore
            bs4pop.removeAll()
            //@ts-ignore
            bs4pop.alert('操作成功', {type: 'info',autoClose: 600});
        }catch (e){
            //@ts-ignore
            bs4pop.alert('远程访问失败', {type: 'error'});
        }
    }
    private async doBatchSamplingCheck(){
        var rows=this.rows;

        if(this.batchSamplingRows.length==0){
            //@ts-ignore
            layer.alert('所选登记单子不能采样检测')
            return;
        }
        var codeList=[];
        var batchIdList=[];
        for(var i=0;i<this.batchSamplingRows.length;i++){
                batchIdList.push(this.batchSamplingRows[i].id);
                codeList.push(this.batchSamplingRows[i].code)
        }
        let sure=await popwrapper.confirm('批量采样检测'+codeList.join("<br\>"),undefined);
        if(!sure){
            return;
        }
        let url= this.toUrl("/newRegisterBill/doBatchSamplingCheck.action");
        try{
            var resp=await jq.postJsonWithProcessing(url,batchIdList);
            if(!resp.success){
                //@ts-ignore
                bs4pop.alert(resp.message, {type: 'error'});
                return;
            }
            if(!resp.success){
                //@ts-ignore
                bs4pop.alert(resp.message, {type: 'error'});
                return;
            }
            var failureList=resp.data.failureList;
            if(failureList.length>0){
                //@ts-ignore
                bs4pop.alert('成功:'+resp.data.successList.join('</br>')+'失败:'+resp.data.failureList.join('</br>'), {type: 'warning'});
                return;
            }
            await this.queryGridData();
            //@ts-ignore
            //TLOG.component.operateLog('登记单管理',"审核不检测","【编号】:"+selected.code);
            //@ts-ignore
            bs4pop.removeAll()
            //@ts-ignore
            bs4pop.alert('操作成功', {type: 'info',autoClose: 600});
        }catch (e){
            //@ts-ignore
            bs4pop.alert('远程访问失败', {type: 'error'});
        }

    }

    private async  doRemoveReportAndCertifiy(){
        var selected = this.rows[0];
        let promise = new Promise((resolve, reject) => {
            //@ts-ignore
            bs4pop.confirm('所选登记单子不能主动送检',
                {type: 'warning',btns: [
                    {label: '全部删除', className: 'btn-primary',onClick(cb){  resolve("all");}},
                    {label: '删除产地证明', className: 'btn-primary',onClick(cb){resolve("originCertifiy");}},
                    {label: '删除检测报告', className: 'btn-primary',onClick(cb){resolve("detectReport");}},
                    {label: '取消', className: 'btn-cancel',onClick(cb){resolve("cancel");}},
                ]});

        });
        let result = await promise; // wait until the promise resolves (*)
        var _url = this.toUrl("/newRegisterBill/doRemoveReportAndCertifiy.action");
        if(result!='cancel'){
            try{
                var resp=await jq.postJsonWithProcessing(_url,{id:selected.id,deleteType:result});
                if(!resp.success){
                    //@ts-ignore
                    bs4pop.alert(resp.message, {type: 'error'});
                    return;
                }

                await this.queryGridData();
                //@ts-ignore
                //TLOG.component.operateLog('登记单管理',"删除产地证明和报告",'【ID】:'+selected.id);
                //@ts-ignore
                bs4pop.removeAll()
                //@ts-ignore
                bs4pop.alert('操作成功', {type: 'info',autoClose: 600});
            }catch (e){
                //@ts-ignore
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        }else{
            //@ts-ignore
            layer.closeAll();
        }


    }
    private async doAutoCheck() {

        var selected = this.rows[0];

        let cthis=this;
        var url= this.toUrl( "/newRegisterBill/doAutoCheck.action?id="+ selected.id);
        let sure=await popwrapper.confirm('请确认是否主动送检？',undefined);
        if(!sure){
            return;
        }

        try{
            var resp=await jq.ajaxWithProcessing({type: "GET",url: url,processData:true,dataType: "json"});
            if(!resp.success){
                //@ts-ignore
                bs4pop.alert(resp.message, {type: 'error'});
                return;
            }
            await cthis.queryGridData();
            //@ts-ignore
            //TLOG.component.operateLog('登记单管理',"主动送检","【编号】:"+selected.code);
            //@ts-ignore
            bs4pop.removeAll()
            //@ts-ignore
            bs4pop.alert('操作成功', {type: 'info',autoClose: 600});
        }catch (e){
            //@ts-ignore
            bs4pop.alert('远程访问失败', {type: 'error'});
        }
    }
    private async doBatchAutoCheck(){
        if(this.batchSamplingRows.length==0){
            //@ts-ignore
            bs4pop.alert('所选登记单子不能主动送检', {type: 'warning'});
            return;
        }

        var codeList=[];
        var batchIdList=[];
        for(var i=0;i<this.batchSamplingRows.length;i++){
                batchIdList.push(this.batchSamplingRows[i].id);
                codeList.push(this.batchSamplingRows[i].code)
        }


        var url= this.toUrl( "/newRegisterBill/doBatchAutoCheck.action");
        let sure=await popwrapper.confirm('请确认是否主动送检？',undefined);
        if(!sure){
            return;
        }

        try{
            var resp=await jq.postJsonWithProcessing(url,batchIdList);
            if(!resp.success){
                //@ts-ignore
                bs4pop.alert(resp.message, {type: 'error'});
                return;
            }
            var failureList=resp.data.failureList;
            if(failureList.length>0){
                //@ts-ignore
                bs4pop.alert('成功:'+resp.data.successList.join('</br>')+'失败:'+resp.data.failureList.join('</br>'), {type: 'warning'});
                return;
            }
            await this.queryGridData();
            //@ts-ignore
            //TLOG.component.operateLog('登记单管理',"批量主动送检","【编号】:"+codeList.join(','));
            //@ts-ignore
            bs4pop.removeAll()
            //@ts-ignore
            bs4pop.alert('操作成功', {type: 'info',autoClose: 600});
        }catch (e){
            //@ts-ignore
            bs4pop.alert('远程访问失败', {type: 'error'});
        }
    }
    private async  doSamplingCheck() {
        var selected = this.rows[0];
        var url= this.toUrl( "/newRegisterBill/doSamplingCheck.action?id="+ selected.id);
        let sure=await popwrapper.confirm('请确认是否采样检测？',undefined);
        if(!sure){
            return;
        }
        try{
            var resp=await jq.ajaxWithProcessing({type: "GET",url: url,processData:true,dataType: "json"});
            if(!resp.success){
                //@ts-ignore
                bs4pop.alert(resp.message, {type: 'error'});
                return;
            }
            await this.queryGridData();
            //@ts-ignore
            //TLOG.component.operateLog('登记单管理',"采样检测","【编号】:"+selected.code);
            //@ts-ignore
            bs4pop.removeAll()
            //@ts-ignore
            bs4pop.alert('操作成功', {type: 'info',autoClose: 600});
        }catch (e){
            //@ts-ignore
            bs4pop.alert('远程访问失败', {type: 'error'});
        }


    }
    private async openCreateSheetPage(){

        var rows=this.rows;
        var idList=rows.map(function(v,i){return v.id});
        let url=this.toUrl('/checkSheet/edit.html?'+$.param({idList:idList},true));
        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '打印检测报告',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });

    }
    private async doUndo(){

        let selected = this.rows[0];

        let cthis=this;
        let url= this.toUrl( "/newRegisterBill/doUndo.action?id="+ selected.id);
        //@ts-ignore
        bs4pop.confirm('请确认是否撤销？', undefined, async function (sure) {
            if(!sure){
                return;
            }
            try{
                var resp=await jq.ajaxWithProcessing({type: "GET",url: url,processData:true,dataType: "json"});

                if(!resp.success){
                    //@ts-ignore
                    bs4pop.alert(resp.message, {type: 'error'});
                    return;
                }
                await cthis.queryGridData();
                //@ts-ignore
                //TLOG.component.operateLog('登记单管理',"撤销","【编号】:"+selected.code);
                //@ts-ignore
                bs4pop.removeAll()
                //@ts-ignore
                bs4pop.alert('操作成功', {type: 'info',autoClose: 600});
            }catch (e){
                //@ts-ignore
                bs4pop.alert('远程访问失败', {type: 'error'});
            }

        });
    }


    private async  doBatchAudit(){
        var waitAudit=this.filterByProp('verifyStatus', [BillVerifyStatusEnum.WAIT_AUDIT]);
        if(waitAudit.length==0){
            //@ts-ignore
            bs4pop.alert('所选登记单不能进行审核', {type: 'error'});
            return;
        }
        var codeList=[];
        var batchIdList=[];
        var onlyWithOriginCertifiyUrlIdList=[];
        for(var i=0;i<waitAudit.length;i++){
                batchIdList.push(waitAudit[i].id);
                codeList.push(waitAudit[i].code)
                if(waitAudit[i].hasOriginCertifiy!=0&&waitAudit[i].hasDetectReport==0){
                    onlyWithOriginCertifiyUrlIdList.push(waitAudit[i].code)
                };
        }

        let promise = new Promise((resolve, reject) => {
            //@ts-ignore
            bs4pop.confirm('批量审核<br\>'+codeList.join("<br\>"), undefined, async function (sure) {
                resolve(sure);
            });
        });

        let result = await promise; // wait until the promise resolves (*)
        if(result==true){
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
                url: "${contextPath}/newRegisterBill/doBatchAudit.action",
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
                            //TLOG.component.operateLog('登记单管理',"批量审核","【编号】:"+codeList.join(','));
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
            width: '80%',
            height: '60%',
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
        var cthis=this;
        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '新增报备单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: [],
            onShowEnd:function(){
                dia.$el.find('iframe')[0].contentWindow['RegisterBillGridObj']=cthis;
            }
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
        this.grid.bootstrapTable('refresh');
    }

    private resetButtons() {
        var btnArray=this.btns;
        _.chain(btnArray).each((btn)=> {
            $(btn).hide();
        });
    }
    private async checkAndShowHideBtns(){
        this.resetButtons();
        var rows=this.rows;
            try{
                var billIdList=_.chain(rows).map(v=>v.id).value();
                var resp=await jq.postJson(this.toUrl('/newRegisterBill/queryEvents.action'),billIdList);
                // console.info(resp)
                resp.forEach(btnid=>{ $('#'+btnid).show();})
            }catch (e){
                console.error(e);
            }

    }
    get waitAuditRows(){
        return  this.filterByProp('verifyStatus', [BillVerifyStatusEnum.WAIT_AUDIT]);
    }
    get batchSamplingRows(){
         return this.filterByProp('detectStatus', [DetectStatusEnum.WAIT_SAMPLE]);
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



    get rows() {
        let rows = this.grid.bootstrapTable('getSelections');
        return rows;
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





    /**
     *
     * @param applyStates
     */
    public async doBatchUndo() {

        var cthis = this;
        var waitAudit = this.waitAuditRows;

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
            var idlist = waitAudit.map(e => e.id);
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
                        //TLOG.component.operateLog('登记单管理', "批量撤销", '【IDS】:' + JSON.stringify(idlist));
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
