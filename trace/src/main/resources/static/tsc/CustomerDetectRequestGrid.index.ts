class CustomerDetectRequestGrid extends ListPage {
    uid:string;
    btns:string[];
    toolbar:any;

    constructor(grid: any, queryform: any, toolbar: any) {
        super(grid,queryform,queryform.find('#query'),"/customerDetectRequest/listPage.action");
        this.toolbar=toolbar;
        this.btns=this.toolbar.find('button');
        this.uid=_.uniqueId("trace_id_");

        $(window).on('resize',()=> this.grid.bootstrapTable('resetView') );

        var cthis=this;
        window['CustomerDetectRequestGrid']=this;

        // 绑定按钮事件
        $('#booking-btn').on('click',async ()=>await this.doBookingRequest());
        $('#assign-btn').on('click',async ()=>await this.openAssignPage());
        $('#sampling-btn').on('click',async ()=>await this.doSamplingCheck());
        $('#auto-btn').on('click',async ()=>await this.doAutoCheck());
        $('#manual-btn').on('click',async ()=>await this.doManualCheck());
        $('#review-btn').on('click',async ()=>await this.doReviewCheck());
        $('#undo-btn').on('click',async ()=>await this.doUndo());
        $('#detail-btn').on('click',async ()=>await this.openDetailPage());
        $('#createSheet-btn').on('click',async ()=>await this.openCreateSheetPage());
        $('#upload-handleresult-btn').on('click',async ()=>await this.openUploadHandleResultPage());
        $('select[name="detectResultSelect"]').on('change',async (o,n)=>{
            var data=JSON.parse($('select[name="detectResultSelect"]').val().toString());
            $('input[name="detectType"]').val(data['detectType']);
            $('input[name="detectResult"]').val(data['detectResult']);
        });
        this.grid.on('check.bs.table uncheck.bs.table', async () => await this.resetButtons());
    }


    /**
     * 上传处理结果
     */
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

    private async openCreateSheetPage(){
        let rows=this.rows;
        let idList=rows.map(function(v,i){return v.billId});
        let url=this.toUrl('/checkSheet/edit.html?'+$.param({idList:idList},true));
        //@ts-ignore
        window.dia = bs4pop.dialog({
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

    /**
     * 预约申请
     */
    private async  doBookingRequest() {
        debugger
        var selected = this.rows[0];
        if(selected.verifyStatus==BillVerifyStatusEnum.NO_PASSED){
            //@ts-ignore
            bs4pop.alert('审核未通过登记单不能进行预约申请', {type: 'error'});
        }
        var url= this.toUrl( "/customerDetectRequest/doBookingRequest.action?billId="+ selected.billId);
        let sure=await popwrapper.confirm('请确认是否预约检测？',undefined);
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
            bs4pop.removeAll()
            //@ts-ignore
            bs4pop.alert('操作成功', {type: 'info',autoClose: 600});
        }catch (e){
            //@ts-ignore
            bs4pop.alert('远程访问失败', {type: 'error'});
        }
    }

    /**
     * 人工检测
     */
    public async doManualCheck(){
        let selected = this.rows[0];
        //@ts-ignore
        bs4pop.removeAll();
        var url=this.toUrl('/customerDetectRequest/manualCheck_confirm.html?billId='+selected.billId);
        //@ts-ignore
        var manual_dia = bs4pop.dialog({
            title: '人工检测',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '80%',
            height: '60%',
            btns: []
        });
        /*let selected = this.rows[0];
        //@ts-ignore
        bs4pop.removeAll();
        let promise = new Promise((resolve, reject) => {
            //@ts-ignore
            bs4pop.confirm('是否人工检测通过当前登记单？<br/>'+selected.code,
                {type: 'warning',btns: [
                        {label: '不通过', className: 'btn-primary',onClick(cb){resolve("false");}},
                        {label: '通过', className: 'btn-primary',onClick(cb){  resolve("true");}},
                        {label: '取消', className: 'btn-cancel',onClick(cb){resolve("cancel");}},
                    ]});
        });
        let result = await promise; // wait until the promise resolves (*)
        if(result=='cancel'){
            return;
        }
        let url= this.toUrl("/customerDetectRequest/doManualCheck.action?billId="+selected.billId+"&pass="+result);

        try {
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
            //@ts-ignore
            bs4pop.alert('远程访问失败', {type: 'error'});
        }*/
    }

    /**
     * 重置查询条件
     */
    public removeAllAndLoadData(){
        //@ts-ignore
        bs4pop.removeAll();
        //@ts-ignore
        $(this).closest("body").removeClass("modal-open");
        (async ()=>{
            await this.queryGridData();
        })();
    }

    /**
     * 进入分配检测员页面
     */
    private async  openAssignPage(){
        var row=this.rows[0]
        if(_.isUndefined(row.billId)||row.billId==null){
            //@ts-ignore
            bs4pop.alert('请行进行预约检测', {type: 'error'});
            return;
        }
        var url=this.toUrl('/customerDetectRequest/confirm.html?billId='+row.billId);
        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '接单',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '80%',
            height: '60%',
            btns: []
        });
    }

    /**
     * 检测接单
     * @param id
     * @param designatedId
     * @param designatedName
     */
    public async doAssign(billId:string, designatedId:string, designatedName:string, detectTime:string){
        //@ts-ignore
        bs4pop.removeAll();
        let promise = new Promise((resolve, reject) => {
            //@ts-ignore
            bs4pop.confirm('是否确认接单？<br/>',
                {type: 'warning',btns: [
                        {label: '是', className: 'btn-primary',onClick(cb){  resolve("true");}},
                        {label: '否', className: 'btn-cancel',onClick(cb){resolve("cancel");}},
                    ]});

        });
        let result = await promise;
        if(result=='cancel'){
            return;
        }

        let url= this.toUrl("/customerDetectRequest/doConfirm.action?billId="+billId+"&designatedId="+designatedId+"&designatedName="+designatedName+"&detectTime="+detectTime);
        try{
            var resp=await jq.ajaxWithProcessing({type: "GET",url: url,processData:true,dataType: "json"});
            if(!resp.success){
                //@ts-ignore
                bs4pop.alert(resp.message, {type: 'error'});
                return;
            }
            await this.queryGridData();
            //@ts-ignore
            bs4pop.removeAll()
            //@ts-ignore
            bs4pop.alert('操作成功', {type: 'info',autoClose: 600});
        }catch (e){
            //@ts-ignore
            bs4pop.alert('远程访问失败', {type: 'error'});
        }
    }

    /**
     * 退回
     * @param id
     * @param designatedId
     * @param designatedName
     */
    public async returnAssign(billId:string){
        //@ts-ignore
        bs4pop.removeAll();
        let promise = new Promise((resolve, reject) => {
            //@ts-ignore
            bs4pop.confirm('是否确认退回？<br/>',
                {type: 'warning',btns: [
                        {label: '是', className: 'btn-primary',onClick(cb){  resolve("true");}},
                        {label: '否', className: 'btn-cancel',onClick(cb){resolve("cancel");}},
                    ]});

        });
        let result = await promise;
        if(result=='cancel'){
            return;
        }

        let url= this.toUrl("/customerDetectRequest/doReturn.action?billId="+billId);
        try{
            var resp=await jq.ajaxWithProcessing({type: "GET",url: url,processData:true,dataType: "json"});
            if(!resp.success){
                //@ts-ignore
                bs4pop.alert(resp.message, {type: 'error'});
                return;
            }
            await this.queryGridData();
            //@ts-ignore
            bs4pop.removeAll()
            //@ts-ignore
            bs4pop.alert('操作成功', {type: 'info',autoClose: 600});
        }catch (e){
            //@ts-ignore
            bs4pop.alert('远程访问失败', {type: 'error'});
        }
    }

    /**
     * 采样检测
     */
    private async  doSamplingCheck() {
        var selected = this.rows[0];
        var url= this.toUrl( "/newRegisterBill/doSamplingCheck.action?id="+ selected.billId);
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
            bs4pop.removeAll()
            //@ts-ignore
            bs4pop.alert('操作成功', {type: 'info',autoClose: 600});
        }catch (e){
            //@ts-ignore
            bs4pop.alert('远程访问失败', {type: 'error'});
        }
    }

    /**
     * 主动送检
     */
    private async doAutoCheck() {
        var selected = this.rows[0];
        let cthis=this;
        var url= this.toUrl( "/newRegisterBill/doAutoCheck.action?id="+ selected.billId);
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

    /**
     * 复检
     */
    private async  doReviewCheck() {
        var selected = this.rows[0];
        let url= this.toUrl("/newRegisterBill/doReviewCheck.action?id="+ selected.billId);
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

    /**
     * 撤销检测请求
     */
    private async doUndo(){
        let selected = this.rows[0];
        let cthis=this;
        let url= this.toUrl( "/customerDetectRequest/doUndo.action?billId="+ selected.billId);
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

    /**
     * 查看详情
     */
    private async  openDetailPage(){
        var row=this.rows[0]
        var url=this.toUrl('/customerDetectRequest/view.html?billId='+row.billId);
        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '检测单详情',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '98%',
            height: '98%',
            btns: []
        });
    }

    get rows() {
        return  this.grid.bootstrapTable('getSelections');
    }

    public async resetButtons() {
        var btnArray=this.btns;
        _.chain(btnArray).each((btn)=> {
            $(btn).hide();
        });
        // _.chain(btnArray).each((btn)=> {
        //     $(btn).show();
        // });
        await this.queryEventAndSetBtn();
    }

    private async queryEventAndSetBtn(){
        var rows=this.rows;
        try{
            var billIdList=_.chain(rows).map(v=>v.billId).value();
            var resp=await jq.postJson(this.toUrl('/customerDetectRequest/queryEvents.action'),billIdList);
            // console.info(resp)
            resp.forEach(btnid=>{ $('#'+btnid).show();})
        }catch (e){
            console.error(e);
        }

    }

}
