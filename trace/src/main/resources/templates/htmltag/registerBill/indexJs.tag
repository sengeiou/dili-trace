<script type="text/javascript">

    //表格查询
    function queryRegisterBillGrid() {
        var opts = _registerBillGrid.datagrid("options");
        if (null == opts.url || "" == opts.url) {
            opts.url = "${contextPath}/registerBill/listPage.action";
        }

        if(!$('#queryForm').form("validate")){
            return false;
        }
        _registerBillGrid.datagrid("load", bindGridMeta2Form("registerBillGrid", "queryForm"));

    }


    //清空表单
    function clearQueryForm() {
        $('#queryForm').form('clear');
    }

    //表格表头右键菜单
    function headerContextMenu(e, field){
        e.preventDefault();
        if (!cmenu){
            createColumnMenu("registerBillGrid");
        }
        cmenu.menu('show', {
            left:e.pageX,
            top:e.pageY
        });
    }

    /**
     * 绑定页面回车事件，以及初始化页面时的光标定位
     * @formId
     *          表单ID
     * @elementName
     *          光标定位在指点表单元素的name属性的值
     * @submitFun
     *          表单提交需执行的任务
     */
    $(function () {
        window._registerBillGrid = $('#registerBillGrid');
        bindFormEvent("queryForm", "code", queryRegisterBillGrid);
        initRegisterBillGrid();
        queryRegisterBillGrid();
    })


    /**
     * 初始化用户列表组件
     */
    function initRegisterBillGrid() {
        var pager = _registerBillGrid.datagrid('getPager');
        pager.pagination({
            <#controls_paginationOpts/>,
            buttons:[
            {
                iconCls:'icon-add',
                text:'新增',
                handler:function(){
                    openInsert();
                }
            },
            {
                iconCls:'icon-detail',
                text:'审核',
                id:'audit-btn',
                handler:function(){
                    audit();
                }
            },
            {
                iconCls:'icon-detail',
                text:'复检',
                id:'review-btn',
                handler:function(){
                    reviewCheck();
                }
            },
            {
                iconCls:'icon-detail',
                text:'主动送检',
                id:'auto-btn',
                handler:function(){
                    autoCheck();
                }
            },
            {
                iconCls:'icon-detail',
                text:'采样检测',
                id:'sampling-btn',
                handler:function(){
                    samplingCheck();
                }
            },
            {
                iconCls:'icon-remove',
                text:'撤销',
                id:'undo-btn',
                handler:undo,
                handler:function(){
                    undo();
                }
            },
            {
                iconCls:'icon-detail',
                text:'查看',
                handler:function(){
                    doDetail();
                }
            }
        ]
        });
    }

    /**
     * datagrid行点击事件
     * 目前用于来判断 启禁用是否可点
     */
    function onClickRow(index,row) {
        var state = row.$_state;
        var detectState= row.$_detectState;
        if (state == ${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_AUDIT.getCode()} ){
            //接车状态是“已打回”,启用“撤销打回”操作
            $('#undo-btn').linkbutton('enable');
            $('#audit-btn').linkbutton('enable');

            $('#auto-btn').linkbutton('disable');
            $('#sampling-btn').linkbutton('disable');
            $('#review-btn').linkbutton('disable');
        }else if(state == ${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_SAMPLE.getCode()} ){
            $('#auto-btn').linkbutton('enable');
            $('#sampling-btn').linkbutton('enable');
            //按钮不可用
            $('#undo-btn').linkbutton('disable');
            $('#audit-btn').linkbutton('disable');
            $('#review-btn').linkbutton('disable');
        }else if(state == ${@com.dili.trace.glossary.RegisterBillStateEnum.ALREADY_SAMPLE.getCode()} ){
            //按钮不可用
            $('#auto-btn').linkbutton('disable');
            $('#sampling-btn').linkbutton('disable');
            $('#undo-btn').linkbutton('disable');
            $('#audit-btn').linkbutton('disable');
            $('#review-btn').linkbutton('disable');
        }else if(state == ${@com.dili.trace.glossary.RegisterBillStateEnum.UNDO.getCode()} ){
            //按钮不可用
            $('#auto-btn').linkbutton('disable');
            $('#sampling-btn').linkbutton('disable');
            $('#undo-btn').linkbutton('disable');
            $('#audit-btn').linkbutton('disable');
            $('#review-btn').linkbutton('disable');
        }
        if(state == ${@com.dili.trace.glossary.RegisterBillStateEnum.ALREADY_CHECK.getCode()} && detectState==${@com.dili.trace.glossary.BillDetectStateEnum.NO_PASS.getCode()}){
            //按钮不可用
            $('#auto-btn').linkbutton('disable');
            $('#sampling-btn').linkbutton('disable');
            $('#undo-btn').linkbutton('disable');
            $('#audit-btn').linkbutton('disable');

            $('#review-btn').linkbutton('enable');
        }
    }

    function blankFormatter(val,row){
        if(!val){
            return "-";
        }
        return val;
    }

    function closeLastWin(id){
        $('#'+id).last().remove();
    }
    function closeWin(id){
        $('#'+id).remove();
        $('#grid').datagrid('reload');
    }
    function openWin(url){
        $('body').append('<iframe id="view_win" name="view_win" src="'+url+'" style="border:0px;width:100%;height:100%;position:fixed;left:0;top:0"></iframe>');
    }
    function openInsert(){
        location.href = '/registerBill/create.html';
    }
    function doDetail(){
        var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
        location.href ='/registerBill/view/' + selected.id;
    }
    /*function audit(){
        var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
        openWin('/registerBill/audit/' + selected.id)
    }*/
    function audit() {
        var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300,
            });
            return;
        }
        swal({
            title: "请确认是否审核通过？",
            type: 'question',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if(result.value){
            $.ajax({
                type: "GET",
                url: "${contextPath}/registerBill/audit/"+ selected.id+"/true",
                processData:true,
                dataType: "json",
                async : true,
                success: function (ret) {
                    if(ret.success){
                        //TLOG.component.operateLog(TLOG.operates.undo, "接车单管理", selected.number, selected.number);
                        _registerBillGrid.datagrid("reload");
                        $('#undo').linkbutton('disable');
                    }else{
                        swal(
                                '错误',
                                ret.result,
                                'error'
                        );
                    }
                },
                error: function(){
                    swal(
                            '错误',
                            '远程访问失败',
                            'error'
                    );
                }
            });
        }
    });
    }
    function autoCheck() {
        var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300,
            });
            return;
        }
        swal({
            title: "请确认是否主动送检？",
            type: 'question',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if(result.value){
            $.ajax({
                type: "GET",
                url: "${contextPath}/registerBill/autoCheck/"+ selected.id,
                processData:true,
                dataType: "json",
                async : true,
                success: function (ret) {
                    if(ret.success){
                        //TLOG.component.operateLog(TLOG.operates.undo, "接车单管理", selected.number, selected.number);
                        _registerBillGrid.datagrid("reload");
                        $('#undo').linkbutton('disable');
                    }else{
                        swal(
                                '错误',
                                ret.result,
                                'error'
                        );
                    }
                },
                error: function(){
                    swal(
                            '错误',
                            '远程访问失败',
                            'error'
                    );
                }
            });
        }
    });
    }

    function samplingCheck() {
        var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300,
            });
            return;
        }
        swal({
            title: "请确认是否采样检测？",
            type: 'question',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if(result.value){
            $.ajax({
                type: "GET",
                url: "${contextPath}/registerBill/samplingCheck/"+ selected.id,
                processData:true,
                dataType: "json",
                async : true,
                success: function (ret) {
                    if(ret.success){
                        //TLOG.component.operateLog(TLOG.operates.undo, "接车单管理", selected.number, selected.number);
                        _registerBillGrid.datagrid("reload");
                        $('#undo').linkbutton('disable');
                    }else{
                        swal(
                                '错误',
                                ret.result,
                                'error'
                        );
                    }
                },
                error: function(){
                    swal(
                            '错误',
                            '远程访问失败',
                            'error'
                    );
                }
            });
        }
    });
    }

    function reviewCheck() {
        var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300,
            });
            return;
        }
        swal({
            title: "请确认是否复检？",
            type: 'question',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if(result.value){
            $.ajax({
                type: "GET",
                url: "${contextPath}/registerBill/reviewCheck/"+ selected.id,
                processData:true,
                dataType: "json",
                async : true,
                success: function (ret) {
                    if(ret.success){
                        //TLOG.component.operateLog(TLOG.operates.undo, "接车单管理", selected.number, selected.number);
                        _registerBillGrid.datagrid("reload");
                        $('#undo').linkbutton('disable');
                    }else{
                        swal(
                                '错误',
                                ret.result,
                                'error'
                        );
                    }
                },
                error: function(){
                    swal(
                            '错误',
                            '远程访问失败',
                            'error'
                    );
                }
            });
        }
    });
    }
    function undo() {
        var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300,
            });
            return;
        }
        swal({
            title: "请确认是否撤销？",
            type: 'question',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if(result.value){
            $.ajax({
                type: "GET",
                url: "${contextPath}/registerBill/undo/"+ selected.id,
                processData:true,
                dataType: "json",
                async : true,
                success: function (ret) {
                    if(ret.success){
                        //TLOG.component.operateLog(TLOG.operates.undo, "接车单管理", selected.number, selected.number);
                        _registerBillGrid.datagrid("reload");
                        $('#undo').linkbutton('disable');
                    }else{
                        swal(
                                '错误',
                                ret.result,
                                'error'
                        );
                    }
                },
                error: function(){
                    swal(
                            '错误',
                            '远程访问失败',
                            'error'
                    );
                }
            });
        }
    });
    }

</script>
