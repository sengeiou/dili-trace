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
        window._checkOrderDisposeGrid = $('#checkOrderDisposeGrid');
        bindFormEvent("queryForm", "disposeDateStart", queryCheckOrderDisposeGrid);
        initCheckOrderDisposeGrid();
        queryCheckOrderDisposeGrid();
    })


    //打开新增窗口
    function openInsert(){

        var index = layer.open({
            type : 2,
            title : '新增检测登记单',
            content : '${contextPath}/checkOrderDispose/edit.html',
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
        var selected = $("#checkOrderDisposeGrid").datagrid("getSelected");
        if (null == selected) {
            swal('警告','请选中一条数据', 'warning');
            return;
        }

        var index = layer.open({
            type : 2,
            title : '修改检测登记单',
            content : '${contextPath}/checkOrderDispose/edit.html?id='+selected.id,
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

    //表格查询
    function queryCheckOrderDisposeGrid() {
        var opts = _checkOrderDisposeGrid.datagrid("options");
        if (null == opts.url || "" == opts.url) {
            opts.url = "${contextPath}/checkOrderDispose/listPage.action";
        }

        if(!$('#queryForm').form("validate")){
            return false;
        }
        bindGridMeta2Form("checkOrderDisposeGrid", "queryForm");
        _checkOrderDisposeGrid.datagrid("load", bindGridMeta2Form("checkOrderDisposeGrid", "queryForm"));
    }


    //清空表单
    function clearQueryForm() {
        $('#queryForm').form('clear');
    }

    //表格表头右键菜单
    function headerContextMenu(e, field){
        e.preventDefault();
        if (!cmenu){
            createColumnMenu("checkOrderDisposeGrid");
        }
        cmenu.menu('show', {
            left:e.pageX,
            top:e.pageY
        });
    }






    /**
     * 初始化用户列表组件
     */
    function initCheckOrderDisposeGrid() {
        var pager = _checkOrderDisposeGrid.datagrid('getPager');
        var toolbar=[
            <#resource method="post" url="checkOrderDispose/index.html#add">
            {
                iconCls:'icon-add',
                text:'新增',
                handler:function(){
                    openInsert();
                }
            },
    </#resource>
        <#resource method="post" url="checkOrderDispose/index.html#update">
            {
                iconCls:'icon-edit',
                text:'修改',
                handler:function(){
                    openUpdate();
                }
            },
    </#resource>
    <#resource method="post" url="checkOrderDispose/index.html#export">
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
                            doExport('checkOrderDisposeGrid');
                        }
                    });
                }
            },
    </#resource>
    ];
        _checkOrderDisposeGrid.datagrid({
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
