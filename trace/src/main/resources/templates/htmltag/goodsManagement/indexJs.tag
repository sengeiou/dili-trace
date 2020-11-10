<script type="text/javascript">

    /**
     * 页面加载完毕后默认选中菜单树的第一个根节点节点
     *
     * @param node
     * @param data
     */
    var currentNodeId=null;
    var gridId="goodsGrid";

    $(function () {
        window._goodsGrid = $('#goodsGrid');
        bindMetadata("goodsGrid")
        initGoodsGrid();
    })
    function onTreeLoadSuccess(node, data) {
        $(this).tree('collapseAll');
       /* var roots = $('#goodsTree').tree('getRoots');
        $('#goodsTree').tree("select", roots[0].target);*/
    }

    /**
     * 点击菜单树事件
     */
    function onSelectTree(node) {
        renderMenuGrid(node, "goodsGrid");
        currentNodeId=node.id;
    }


    /**
     * 渲染菜单列表
     *
     * @param node
     * @param gridId
     */
    function renderMenuGrid(node, gridId) {
        var leaf=$('#goodsTree').tree('isLeaf', node.target);
        var opts = _goodsGrid.datagrid("options");
        opts.url = "${contextPath}/goodsManagement/list.action?id="+node.id+"&leaf="+leaf;
        //刷新表格
        _goodsGrid.datagrid("load",bindMetadata("goodsGrid") );
    }


    /**
     * 拖动完菜单时触发
     *
     * @param target
     *            the target node element to be dropped.
     * @param source
     *            the source node being dragged.
     */
    function dragMenu(target, source, point) {

    }

    //清空表单
    function clearQueryForm() {
        $('#queryForm').form('clear');
    }

    //商品列表查询
    function queryGoodsGrid() {
        var opts = _goodsGrid.datagrid("options");
        opts.url = "${contextPath}/goodsManagement/listByQueryParams.action";
        if(!$('#queryForm').form("validate")){
            return false;
        }
        //刷新表格
        _goodsGrid.datagrid("load", bindGridMeta2Form("goodsGrid", "queryForm"));
    }

    /**
     * 初始化用户列表组件
     */
    function initGoodsGrid() {
        var pager = _goodsGrid.datagrid('getPager');
        var toolbar=[
            <#resource method="post" code="goodsManagement/index.html#add">
            {
                iconCls:'icon-add',
                text:'新增',
                handler:function(){
                    openInsert();
                }
            },
    </#resource>
        <#resource method="post" code="goodsManagement/index.html#update">
            {
                iconCls:'icon-edit',
                text:'修改',
                handler:function(){
                    openUpdate();
                }
            },
    </#resource>
        <#resource method="post" code="goodsManagement/index.html#enabled">
            {
                iconCls:'icon-play',
                text:'启用',
                id:'play_btn',
                handler:function(){
                    doEnable(true);
                }
            },
    </#resource>
        <#resource method="post" code="goodsManagement/index.html#disabled">
            {
                iconCls:'icon-stop',
                text:'禁用',
                id:'stop_btn',
                handler:function(){
                    doEnable(false);
                }
            },
    </#resource>
    ];
        _goodsGrid.datagrid({
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

    /**
     * datagrid行点击事件
     * 目前用于来判断 启禁用是否可点
     */
    function onClickRow(index,row) {
        var state = row.$_state;
        console.info(state)
        if (state == ${@com.dili.trace.glossary.EnabledStateEnum.DISABLED.getCode()}){
            //当用户状态为 禁用，可操作 启用
            $('#play_btn').linkbutton('enable');
            $('#stop_btn').linkbutton('disable');
        }else if(state == ${@com.dili.trace.glossary.EnabledStateEnum.ENABLED.getCode()}){
            //当用户状态为正常时，则只能操作 禁用
            $('#stop_btn').linkbutton('enable');
            $('#play_btn').linkbutton('disable');
        } else{
            //其它情况，按钮不可用
            $('#stop_btn').linkbutton('disable');
            $('#play_btn').linkbutton('disable');
        }
    }

    //表格表头右键菜单
    function headerContextMenu(e, field){
        e.preventDefault();
        if (!cmenu){
            createColumnMenu("userGrid");
        }
        cmenu.menu('show', {
            left:e.pageX,
            top:e.pageY
        });
    }

    //根据节点id获取节点信息
    function getNode(id){
        return node = $('#goodsTree').tree('find',id);
    }

    //打开新增窗口
    function openInsert(){
        if(currentNodeId == null){
            return ;
        }
        var index = layer.open({
            type : 2,
            title : '商品新增',
            content : '${contextPath}/goodsManagement/edit.html?currentNodeId='+currentNodeId,
            area : ['670px', '550px'],
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
        var selected = $("#goodsGrid").datagrid("getSelected");
        if (null == selected) {
            swal('警告','请选中一条数据', 'warning');
            return;
        }

        var index = layer.open({
            type : 2,
            title : '商品修改',
            content : '${contextPath}/goodsManagement/edit.html?id='+selected.id+'&currentNodeId='+currentNodeId,
            area : ['670px', '550px'],
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

    /**
     * 禁启用操作
     * @param enable 是否启用:true-启用
     */
    function doEnable(enable) {
        var selected = $("#goodsGrid").datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300,
            });
            return;
        }
        if(selected.state == enable) {
            var isEnableMsg =(enable || 'true' == enable) ? '该商品已经启用' : '该商品已经禁用';
            swal({
                title: '警告',
                text: isEnableMsg,
                type: 'warning',
                width: 300,
            });
            return;
        }
        var msg = (enable || 'true' == enable) ? '确定要启用该商品吗？' : '确定要禁用该商品吗？';
        swal({
            title : msg,
            type : 'question',
            showCancelButton : true,
            confirmButtonColor : '#3085d6',
            cancelButtonColor : '#d33',
            confirmButtonText : '确定',
            cancelButtonText : '取消',
            confirmButtonClass : 'btn btn-success',
            cancelButtonClass : 'btn btn-danger'
        }).then(function(flag) {
            if (flag.dismiss == 'cancel' || flag.dismiss == 'overlay' || flag.dismiss == "esc" || flag.dismiss == "close"){
                return;
            }
            $.ajax({
                type: "POST",
                url: "${contextPath}/goodsManagement/doEnable.action",
                data: {id: selected.id, enable: enable},
                processData:true,
                dataType: "json",
                async : true,
                success : function(ret) {
                    if(ret.success){
                        $("#goodsGrid").datagrid("reload");
                        $('#stop_btn').linkbutton('disable');
                        $('#play_btn').linkbutton('disable');
                    }else{
                        swal(
                            '错误',
                            ret.result,
                            'error'
                        );
                    }
                },
                error : function() {
                    swal(
                        '错误',
                        '远程访问失败',
                        'error'
                    );
                }
            });
        });
    }
</script>