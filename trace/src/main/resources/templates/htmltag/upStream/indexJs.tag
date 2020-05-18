<script type="text/javascript">

    let selectedTags = {};
    function userFormatter(value,row) {
        if(row){
            var opts = $(this).tagbox('options');
            selectedTags[value] = row[opts.textField];
            return row[opts.textField];
        }else{
            return selectedTags[value];
        }
    }

    /**
     * 客户自动联想加载
     * ***/
    function userLoader(param,success,error) {
        var q = param.q || '';
        if (q.length < 1){return false}
        $.ajax({
            type: "POST",
            url: '${contextPath}/user/listByCondition.action',
            contentType: "application/json; charset=utf-8",
            dataType: 'json',
            data: JSON.stringify({
                likeName: q
            }),
            success: function(result){
                if(result.success){
                    $.map(result.data, function(item,index){
                        return $.extend(item, {
                            text: item.name+" "+item.cardNo
                        });
                    });
                    success(result.data);
                }

            },
            error: function(){
                error.apply(this, arguments);
            }
        });
    }

    function saveOrUpdate() {

    }

    //打开新增窗口
    function openInsert(){
        $('#dlg').dialog('open');
        $('#dlg').dialog('center');
    }

    //打开修改窗口
    function openUpdate(){
        var selected = _grid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300,
            });
            return;
        }
        window.location.href="${contextPath}/upStream/edit.html?id="+selected["id"];
    }

    //表格查询
    function queryUpStreamGrid() {
        var opts = _grid.datagrid("options");
        if (null == opts.url || "" == opts.url) {
            opts.url = "${contextPath}/upStream/listPage.action";
        }
        if(!$('#queryForm').form("validate")){
            return;
        }
        _grid.datagrid("load", bindGridMeta2Form("grid", "queryForm"));
    }


    //清空表单
    function clearQueryForm() {
        $('#queryForm').form('clear');
    }

    //表格表头右键菜单
    function headerContextMenu(e, field){
        e.preventDefault();
        if (!cmenu){
            createColumnMenu("grid");
        }
        cmenu.menu('show', {
            left:e.pageX,
            top:e.pageY
        });
    }

    //全局按键事件
    function getKey(e){
        e = e || window.event;
        var keycode = e.which ? e.which : e.keyCode;
        if(keycode == 46){ //如果按下删除键
            var selected = $("#grid").datagrid("getSelected");
            if(selected && selected!= null){
                del();
            }
        }
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
        window._grid = $('#grid');
        bindFormEvent("queryForm", "likeUpstreamName", queryUpStreamGrid);
        if (document.addEventListener) {
            document.addEventListener("keyup",getKey,false);
        } else if (document.attachEvent) {
            document.attachEvent("onkeyup",getKey);
        } else {
            document.onkeyup = getKey;
        }
        initUpStreamGrid();
        queryUpStreamGrid();
        $('#upstreamType').combobox({
            onChange : function () {
                $('#corporateInfo').toggle();
            }
        });
    })




    /**
     * 初始化用户列表组件
     */
    function initUpStreamGrid() {
        var pager = _grid.datagrid('getPager');
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
                 }
        ]
        });
        //表格仅显示下边框
        _grid.datagrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom");
    }

    /**
     * datagrid行点击事件
     * 目前用于来判断 启禁用是否可点
     */
    function onClickRow(index,row) {
    }

    function iconFormatter(value, row, index) {
        return '<img height="80" src="'+value+'" />';
    }


</script>