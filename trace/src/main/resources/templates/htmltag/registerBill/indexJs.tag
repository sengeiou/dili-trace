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
                iconCls:'icon-edit',
                text:'修改',
                handler:function(){
                    openUpdate();
                }
            },
            {
                iconCls:'icon-remove',
                text:'删除',
                handler:function(){
                    del();
                }
            },
            {
                iconCls:'icon-export',
                text:'导出',
                handler:function(){
                    doExport('grid');
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

</script>
