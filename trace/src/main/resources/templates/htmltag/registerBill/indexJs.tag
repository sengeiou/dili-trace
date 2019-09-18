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
        bindFormEvent("queryForm", "registerSource", queryRegisterBillGrid);
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
        <#resource method="post" url="registerBill/index.html#add">
            {
                iconCls:'icon-add',
                text:'新增',
                handler:function(){
                    openInsert();
                }
            },
    </#resource>
        <#resource method="post" url="registerBill/index.html#audit">
            {
                iconCls:'icon-man',
                text:'进场审核',
                id:'audit-btn',
                handler:function(){
                    audit();
                }
            },
    </#resource>
        <#resource method="post" url="registerBill/index.html#review">
            {
                iconCls:'icon-man',
                text:'复检',
                id:'review-btn',
                handler:function(){
                    reviewCheck();
                }
            },
    </#resource>
        <#resource method="post" url="registerBill/index.html#auto">
            {
                iconCls:'icon-man',
                text:'主动送检',
                id:'auto-btn',
                handler:function(){
                    autoCheck();
                }
            },
    </#resource>
        <#resource method="post" url="registerBill/index.html#sampling">
            {
                iconCls:'icon-man',
                text:'采样检测',
                id:'sampling-btn',
                handler:function(){
                    samplingCheck();
                }
            },
    </#resource>
        <#resource method="post" url="registerBill/index.html#undo">
            {
                iconCls:'icon-undo',
                text:'撤销',
                id:'undo-btn',
                handler:undo,
                handler:function(){
                    undo();
                }
            },
    </#resource>
     <#resource method="post" url="registerBill/index.html#handle">
            {
                iconCls:'icon-redo',
                text:'上传处理结果',
                id:'handle-btn',
                disabled :true,
                handler:doHandler,
                handler:function(){
                    doHandler();
                }
            },
        </#resource>
        <#resource method="post" url="registerBill/index.html#modify">
            {
                iconCls:'icon-edit',
                text:'上传检测报告',
                id:'edit-btn',
                handler:doModify,
                handler:function(){
                    doModify();
                }
            },
        </#resource>
            <#resource method="post" url="registerBill/index.html#copy">
            {
                iconCls:'icon-copy',
                text:'复制登记单',
                id:'copy-btn',
                disabled :true,
                handler:doCopy,
                handler:function(){
                	doCopy();
                }
            },
        </#resource>
        <#resource method="post" url="registerBill/index.html#detail">
            {
                iconCls:'icon-detail',
                text:'查看',
                handler:function(){
                    doDetail();
                }
            },
    </#resource>
        <#resource method="post" url="registerBill/index.html#export">
            {
                iconCls:'icon-export',
                text:'导出',
                handler:function(){
               	 layer.confirm('确认导出数据?', {
                     type: 0,
                     title: '提示',
                     btn: ['确定','取消'],
                     yes:function(){
                    	 layer.closeAll();
                    	 doExport('registerBillGrid');
                     }
                 });
                   
                }
            }
        </#resource>
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
        $('#copy-btn').linkbutton('enable');
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
        }else if(state == ${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_CHECK.getCode()} ){
            //按钮不可用
            $('#auto-btn').linkbutton('disable');
            $('#sampling-btn').linkbutton('disable');
            $('#undo-btn').linkbutton('disable');
            $('#audit-btn').linkbutton('disable');
            $('#review-btn').linkbutton('disable');
        }else if(state == ${@com.dili.trace.glossary.RegisterBillStateEnum.CHECKING.getCode()} ){
            //按钮不可用
            $('#auto-btn').linkbutton('disable');
            $('#sampling-btn').linkbutton('disable');
            $('#undo-btn').linkbutton('disable');
            $('#audit-btn').linkbutton('disable');
            $('#review-btn').linkbutton('disable');
        }else if(state == ${@com.dili.trace.glossary.RegisterBillStateEnum.ALREADY_CHECK.getCode()}){
            //按钮不可用
            $('#auto-btn').linkbutton('disable');
            $('#sampling-btn').linkbutton('disable');
            $('#undo-btn').linkbutton('disable');
            $('#audit-btn').linkbutton('disable');
            if(detectState==${@com.dili.trace.glossary.BillDetectStateEnum.NO_PASS.getCode()}){
                $('#review-btn').linkbutton('enable');
            }else{
                $('#review-btn').linkbutton('disable');
            }
        }
        $('#handle-btn').linkbutton('disable');
        if(row.handleResultUrl&&row.handleResult&&row.handleResultUrl!=null&&row.handleResult!=null&&row.handleResultUrl!=''&&row.handleResult!=''){
        	 $('#handle-btn').linkbutton('disable');
        }else if(detectState==${@com.dili.trace.glossary.BillDetectStateEnum.REVIEW_NO_PASS.getCode()}&&row.handleResultUrl==null&&row.handleResult==null){
        	 $('#handle-btn').linkbutton('enable');
        }
       
        //handle-btn
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
        $('#registerBillGrid').datagrid('reload');
    }
    function openWin(url){
        $('body').append('<iframe id="view_win" name="view_win" src="'+url+'" style="border:0px;width:100%;height:100%;position:fixed;left:0;top:0"></iframe>');
    }
    function openInsert(){
        openWin('/registerBill/create.html');
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
        openWin('/registerBill/view/' + selected.id);
    }
    function audit(){
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
        openIframe('/registerBill/audit/' + selected.id,selected.id)

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
                        _registerBillGrid.datagrid("reload");
                        swal(
                                '操作',
                                '操作成功',
                                'info'
                        );
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
                width: 300
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
                        _registerBillGrid.datagrid("reload");
                        swal(
                                '操作',
                                '操作成功',
                                'info'
                        );
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
                        _registerBillGrid.datagrid("reload");
                        swal(
                                '操作',
                                '操作成功',
                                'info'
                        );
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
                width: 300
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
                        _registerBillGrid.datagrid("reload");
                        swal(
                                '操作',
                                '操作成功',
                                'info'
                        );
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

    function cityLoader(param,success,error) {
        var q = param.q || '';
        if (q.length < 1){return false}
        $.ajax({
            type: "POST",
            url: '${contextPath}/provider/getLookupList.action',
            dataType: 'json',
            data: {
                provider: 'cityProvider',
                queryParams: '{required:true}',
                value: q
            },
            success: function(data){
                success(data);
            },
            error: function(){
                error.apply(this, arguments);
            }
        });
    }
    function doModify(){
    	
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
        openWin('/registerBill/modify/' + selected.id);
    }
    
    function doCopy(){
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
        openWin('/registerBill/copy.html?id=' + selected.id+"#copy");
    }
    
    function openIframe(content,id){
        layer.open({
            type: 2,
            title: "审核",
            shadeClose: true,
            shade: 0.3,
            offset: "20%",
            shadeClose : false,
            area: ['1100px', "350px"],
            content: content,//传入一个链接地址 比如：http://www.baidu.com
            btn: ['进场审核','取消'],
            yes: function(index, layero){
                $.ajax({
                    type: "GET",
                    url: "${contextPath}/registerBill/audit/"+ id+"/true",
                    processData:true,
                    dataType: "json",
                    async : true,
                    success: function (ret) {
                        if(ret.success){
                            _registerBillGrid.datagrid("reload");

                            swal(
                                    '操作',
                                    '操作成功',
                                    'info'
                            );
                        }else{
                            swal(
                                    '操作',
                                    ret.result,
                                    'info'
                            );

                        }
                        layer.closeAll();
                    },
                    error: function(){

                        swal(
                                '错误',
                                '远程访问失败',
                                'error'
                        );
                        layer.closeAll();
                    }
                });
            }
        });
    }
    function openLayer(url){
        layer.open({
            area: ['600px', "400px"],
            title: '审核',
            content: [url, 'no'], //iframe的url
            btn: ['确定','取消'],
            yes: function(index, layero){
                //按钮【确认】的回调
                var body = layer.getChildFrame('body', index); //iframe的body获取方式
                var password = $(body).find("#password").val();
            }
        });
    }
    
    
  //打开处理结果窗口
    function doHandler(){
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
          
        $('#dlg').dialog("setTitle","上传处理结果");
        $('#dlg').dialog('open');
        $('#dlg').dialog('center');
        $(".magnifying").hide();
        $(".fileimg-cover,.fileimg-edit").hide();
        $(":file").attr('disabled',false);
        $('#_form').form('clear');
        var formData = $.extend({},selected);
        formData = addKeyStartWith(getOriginalData(formData),"_");
        $('#_form').form('load', formData);
        
        initFileUpload();
    }


    function saveOrUpdateHandleResult(){
        if(!$('#_form').form("validate")){
            return;
        }
        var _formData = removeKeyStartWith($("#_form").serializeObject(),"_");
       if(!_formData.handleResultUrl||_formData.handleResultUrl==''){
    	   layer.alert('请上传处理结果图片');
    	   return;
    	   
       }
        var _url = "${contextPath}/registerBill/saveHandleResult.action";
        $.ajax({
            type: "POST",
            url: _url,
            data: _formData,
            processData:true,
            dataType: "json",
            async : true,
            success: function (data) {
                if(data.code=="200"){
               	 $('#dlg').dialog('close');	 
                    layer.alert('处理成功',function(){
                   	 layer.closeAll();
                   	 $('#handle-btn').linkbutton('disable');
                   	 queryRegisterBillGrid();
                    })
                    
                }else{
                    swal('错误',data.result, 'error');
                }
            },
            error: function(){
                swal('错误', '远程访问失败', 'error');
            }
        });
    }
    
    $('.fileimg-view').on('click', function () {
        var url = $(this).parent().siblings(".magnifying").attr('src');
        if(url){
            layer.open({
                title:'图片',
                type: 1,
                skin: 'layui-layer-rim',
                closeBtn: 2,
                area: ['90%', '90%'], //宽高
                content: '<p style="text-align:center"><img src="' + url + '" alt="" class="show-image-zoom"></p>'
            });
        }
    });

    function initFileUpload(){
        $(":file").fileupload({
            dataType : 'json',
            formData: {type:5,compress:true},
            done : function(e, res) {
                if (res.result.code == 200) {
                    var url = res.result.data;
                    $(this).siblings(".magnifying").attr('src', url).show();
                    $(this).siblings("input:hidden").val(url);
                    $(this).siblings('.fileimg-cover,.fileimg-edit').show();
                }
            },
            add:function (e, data){//判断文件类型 var acceptFileTypes = /\/(pdf|xml)$/i;
                var acceptFileTypes = /^gif|bmp|jpe?g|png$/i;
                var name = data.originalFiles[0]["name"];
                var index = name.lastIndexOf(".")+1;
                var fileType = name.substring(index,name.length);
                if(!acceptFileTypes.test(fileType)){
                    swal('错误', '请您上传图片类文件jpe/jpg/png/bmp!', 'error');
                    return ;
                }
                var size = data.originalFiles[0]["size"];
                // 10M
                if(size > (1024*10*1024)){
                    swal('错误', '上传文件超过最大限制!', 'error');
                    return ;
                }
                data.submit();
            }
        });
    }
</script>
