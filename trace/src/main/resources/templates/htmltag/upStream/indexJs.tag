<script type="text/javascript">

    //打开新增窗口
    function openInsert(){
        layer.open({
            type : 2,
            title : '上游新增',
            content : '${contextPath}/upStream/edit.html',
            area : ['30%', '580px'],
            shadeClose : false,
            shade : 0.5,
            scrollbar: true,
            move: true,
            btn: ['确认', '取消']
            ,yes: function(index, layero){
                //按钮【按钮一】的回调
                var body = layer.getChildFrame('body', index);
                let childWindow = window[layero.find('iframe')[0]['name']];
                debounce(function(){
                    childWindow.saveOrUpdate(function () {
                        layer.close(index);
                        _grid.datagrid("reload");
                    });
                },1000,true)();
                return false;
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
    function openUpdate(){
        var selected = _grid.datagrid("getSelected");
        if (null == selected) {
            swal('警告','请选中一条数据', 'warning');
            return;
        }

        layer.open({
            type : 2,
            title : '上游修改',
            content : '${contextPath}/upStream/edit.html?id='+selected.id,
            area : ['30%', '580px'],
            shadeClose : false,
            shade : 0.5,
            scrollbar: true,
            move: true,
            btn: ['确认', '取消']
            ,yes: function(index, layero){
                //按钮【按钮一】的回调
                var body = layer.getChildFrame('body', index);
                let childWindow = window[layero.find('iframe')[0]['name']];
                debounce(function(){
                    childWindow.saveOrUpdate(function () {
                        layer.close(index);
                        _grid.datagrid("reload");
                    });
                },1000,true)();
                return false;
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
        if (document.addEventListener) {
            document.addEventListener("keyup",getKey,false);
        } else if (document.attachEvent) {
            document.attachEvent("onkeyup",getKey);
        } else {
            document.onkeyup = getKey;
        }
        initUpStreamGrid();
        queryUpStreamGrid();
    })

    /**
     * 初始化用户列表组件
     */
    function initUpStreamGrid() {
        var pager = _grid.datagrid('getPager');
            pager.pagination({
                <#controls_paginationOpts/>,
                buttons:[
                <#resource method="post" url="upStream/index.html#add">
                {
                    iconCls:'icon-add',
                    text:'新增',
                    handler:function(){
                        openInsert();
                    }
                },
                </#resource>
                <#resource method="post" url="upStream/index.html#update">
                 {
                     iconCls:'icon-edit',
                     text:'修改',
                     handler:function(){
                         openUpdate();
                     }
                 }
                </#resource>
        ]
        });
        //表格仅显示下边框
        _grid.datagrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom");
    }

    /**
     * fn [function] 需要防抖的函数
     * wait [number] 毫秒，防抖期限值
     * immediate 是否立即执行
     */
    const debounce = (fn, wait, immediate = false) => {
        let timer;

        return function() {
            if(timer) clearTimeout(timer);
            if(immediate) {
                let trigger = !timer;
                timer = setTimeout(() => {
                    timer = null;
                }, wait);

                if(trigger) {
                    fn.apply(this, arguments);
                }
                return;
            }

            timer = setTimeout(() => {
                fn.apply(this, arguments);
            }, wait);
            return;
        }
    };

</script>