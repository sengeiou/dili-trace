<script type="text/javascript">
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
        window._checkBillGrid = $('#checkBillGrid');
        bindFormEvent("queryForm", "checkTimeStart", queryCheckBillGrid);
        initCheckBillGrid();
        queryCheckBillGrid();
    })

    function checkFileType(){
        var filePath = $('#file').textbox('getValue');
        var acceptFileTypes = /^xls|xlsx$/i;
        if (filePath != '') {
            var fileType = filePath.substring(filePath.lastIndexOf("."));
            if(!acceptFileTypes.test(fileType)){
                swal('错误', '请您上传Excel类文件xls/xlsx!', 'error');
                return ;
            }
        }
    }


    //打开新增窗口
    function openInsert(){

        var index = layer.open({
            type : 2,
            title : '新增检测登记单',
            content : '${contextPath}/checkBill/edit.html',
            area : ['800px', '650px'],
            shadeClose : false,
            shade : 0.5,
            btn: ['保存', '取消']
            ,yes: function(index, layero){
                //按钮【按钮一】的回调
                var body = layer.getChildFrame('body', index);
                body.find("#submitFormBtn").trigger('click')
            }
            ,btn2: function(index, layero){
                //按钮【按钮二】的回调

                //return false 开启该代码可禁止点击该按钮关闭
            },
            cancel : function() {

            }
        });

    }

    //打开修改窗口
    var formOldData={};
    function openUpdate(){
        var selected = $("#checkBillGrid").datagrid("getSelected");
        if (null == selected) {
            swal('警告','请选中一条数据', 'warning');
            return;
        }
        console.info(selected.reportFlag)
        if ("已上报"== selected.reportFlag) {
            swal('警告','已上报数据不可编辑', 'warning');
            return;
        }
        var index = layer.open({
            type : 2,
            title : '修改检测登记单',
            content : '${contextPath}/checkBill/edit.html?id='+selected.id,
            area : ['800px', '650px'],
            shadeClose : false,
            shade : 0.5,
            btn: ['保存', '取消']
            ,yes: function(index, layero){
                //按钮【按钮一】的回调
                var body = layer.getChildFrame('body', index);
                body.find("#submitFormBtn").trigger('click')
            }
            ,btn2: function(index, layero){
                //按钮【按钮二】的回调

                //return false 开启该代码可禁止点击该按钮关闭
            },
            cancel : function() {

            }
        });


    }

    function batchImport(){
        $('#importDialog').dialog({
            title: '导入文件',
            width: 400,
            height: 113,
            closed: false,
            cache: false,
            content :'<form action="" id="importForm" enctype="multipart/form-data">'
                +'<div style=" text-align: center">'
                +'请导入Excel文件： <input class="easyui-filebox" id="file" name="file" prompt="选择文件..." buttonText="选择文件" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel" style="width:260px;height:30px" data-options="required:true" >'
                +'</div></form>',
            modal: true,
            buttons:[{
                text:'导入',
                handler:function(){
                    checkFileType();
                    importFormSubmit();
                }
            },{
                text:'关闭',
                handler:function(){
                    $('#importDialog').dialog('close');
                }
            }]
        });
    }

    function importFormSubmit(){
        if(!$('#importForm').form("validate")){
            return false;
        }
        var form = new FormData($('#importForm')[0]);
        console.info(form)
        $.ajax({
            url:"/checkBill/upload.action",
            type:"post",
            data:form,
            processData:false,
            contentType:false,
            beforeSend:function(){
                $.messager.progress({
                    title: '提示',
                    msg: '文件上传中，请稍候……',
                    text: ''
                });
            },
            complete:function () {
                $.messager.progress('close');
            },
            success:function(json){
                console.info(json);
                if(json.success){
                    $.messager.alert('温馨提示',json.message);
                    $('#importDialog').dialog('close');
                    queryCheckBillGrid()
                }else{
                    $.messager.alert('温馨提示',json.message);
                }
            },
            error: function(XmlHttpRequest, textStatus, errorThrown){
                $.messager.alert('导入失败',"导入失败，请检查Excel文件格式");
                $('#importDialog').dialog('close');
            }
        });
    }
    //表格查询
    function queryCheckBillGrid() {
        var opts = _checkBillGrid.datagrid("options");
        if (null == opts.url || "" == opts.url) {
            opts.url = "${contextPath}/checkBill/listPage.action";
        }

        if(!$('#queryForm').form("validate")){
            return false;
        }
        bindGridMeta2Form("checkBillGrid", "queryForm");
        _checkBillGrid.datagrid("load", bindGridMeta2Form("checkBillGrid", "queryForm"));

    }


    //清空表单
    function clearQueryForm() {
        $('#queryForm').form('clear');
    }

    //表格表头右键菜单
    function headerContextMenu(e, field){
        e.preventDefault();
        if (!cmenu){
            createColumnMenu("checkBillGrid");
        }
        cmenu.menu('show', {
            left:e.pageX,
            top:e.pageY
        });
    }






    /**
     * 初始化用户列表组件
     */
    function initCheckBillGrid() {
        var pager = _checkBillGrid.datagrid('getPager');
        var toolbar=[
            <#resource method="post" code="checkBill/index.html#add">
            {
                iconCls:'icon-add',
                text:'新增',
                handler:function(){
                    openInsert();
                }
            },
    </#resource>
        <#resource method="post" code="checkBill/index.html#update">
            {
                iconCls:'icon-edit',
                text:'修改',
                handler:function(){
                    openUpdate();
                }
            },
    </#resource>
        <#resource method="post" code="checkBill/index.html#import">
            {
                iconCls:'icon-save',
                text:'批量导入',
                class:'easyui-filebox',
                handler:function(){
                    batchImport();
                }
            },
    </#resource>
    <#resource method="post" code="checkBill/index.html#export">
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
                            doExport('checkBillGrid');
                        }
                    });
                }
            },
    </#resource>
    ];
        _checkBillGrid.datagrid({
            toolbar:toolbar
        });
        pager.pagination({
            <#controls_paginationOpts/>,
        //buttons:toolbar

    });
        pager.pagination({
            <#controls_paginationOpts/>,

    });
    }


    function blankFormatter(val,row){
        if(!val){
            return "-";
        }
        return val;
    }

    function phoneFormatter(val,row){
        if(val){
            return val.replace(val.substring(3,7), "****")
        }
        return val;
    }

    function cardNoFormatter(val,row){
        if(val){
            var val = val.substr(0,4)+'**************';
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
</script>
