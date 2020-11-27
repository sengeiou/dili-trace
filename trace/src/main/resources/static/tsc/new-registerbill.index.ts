class NewRegisterBillGrid extends  PageTs{
    ctx:string;
    grid: any;
    billStateEnums: any;

    constructor(ctx:string,grid: any, billStateEnums: any) {
        super();
        this.ctx=ctx;
        this.grid = grid;
        this.billStateEnums = billStateEnums;

    }

    get rows() {
        return this.grid.datagrid("getSelections");
    }
    public static getInstance():RegisterBillGrid{
        return window['registerBillGrid'] as RegisterBillGrid;
    }
    public static queryProduct(param,success,error){
        // @ts-ignore
        var productName=$('#productCombobox').combotree('getText');
        var data=[];
        var url=RegisterBillGrid.getInstance().ctx+'/toll/category?name='+productName;
        $.ajax({
            url:url,
            success:function(resp){
                data=resp.suggestions.map(function(v,i){v['parentId']='';return v});
                //console.info(data)
                success(data);
                //data.unshift({id:'',value:'全部'});
                //$('#productName').combobox({data:data});
                //	$('#productName').combobox('showPanel');
                //$('#productName').combotree('setText',productName);
                //	$('#productIdList').focus();
                //$('#productIdList').next().find('input').focus()
            }
        })


        // return data;
    }

    public isBatchSimpling() {
        if (this.filterByProp('$_state', this.billStateEnums.WAIT_SAMPLE).length > 0) {
            return true;
        }
        return false;
    }

    public isBatchAudit() {
        if (this.filterByProp('$_state', this.billStateEnums.WAIT_AUDIT).length > 0) {
            return true;
        }
        return false;
    }

    /**
     * 是否可以进行批量采样
     * @param applyStates
     */
    public isBatchAuto() {
        if (this.filterByProp('$_state', this.billStateEnums.WAIT_SAMPLE).length > 0) {
            return true;
        }
        return false;

    }

    /**
     * 是否可以批量撤销
     * @param applyStates
     */
    public isBatchUndo() {
        if (this.filterByProp('$_state', this.billStateEnums.WAIT_AUDIT).length > 0) {
            return true;
        }
        return false;
    }


    /**
     *
     * @param applyStates
     */
    public async doBatchUndo() {
        if(!this.isBatchUndo()){
            // @ts-ignore
            swal({
                title: '警告',
                text: '没有数据可以进行批量撤销',
                type: 'warning',
                width: 300
            });
            return;
        }
        var arr=this.filterByProp("$_state",this.billStateEnums.WAIT_AUDIT);

        let promise = new Promise((resolve, reject) => {
            // @ts-ignore
            layer.confirm('请确认是否撤销选中数据？<br/>'+arr.map(e=>e.code).join("<br\>"), {btn:['确定','取消'], title: "警告！！！",
                btn1:function(){
                    resolve(true);
                    return false;
                },
                btn2:function(){
                    resolve(false);
                    return false;
                }
            });
            $('.layui-layer').width('460px');
        });
        let result = await promise; // wait until the promise resolves (*)
        if(result){
            var _url = RegisterBillGrid.getInstance().ctx+"/registerBill/batchUndo.action";
            var idlist=arr.map(e=>e.id);
            $.ajax({
                type: "POST",
                url: _url,
                data: JSON.stringify(idlist),
                processData:true,
                dataType: "json",
                contentType:'application/json;charset=utf-8',
                async : true,
                success: function (data) {
                    if(data.code=="200"){
                        // @ts-ignore
                        TLOG.component.operateLog('登记单管理',"批量撤销",'【IDS】:'+JSON.stringify(idlist));
                        // @ts-ignore
                        layer.alert('操作成功',{
                                title:'操作',
                                time : 600,
                                end :function(){
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

                    }else{
                        // @ts-ignore
                        layer.closeAll();
                        // @ts-ignore
                        swal('错误',data.result, 'error');
                    }
                },
                error: function(){
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
