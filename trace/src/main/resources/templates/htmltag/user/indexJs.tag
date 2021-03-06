<script type="text/javascript">

    //打开新增窗口
    function openInsert(){
        
         var index = layer.open({
            type : 2,
            title : '用户新增',
            content : '${contextPath}/user/edit.html',
            area : ['670px', '550px'],
            shadeClose : false,
            shade : 0.5,
            btn: ['确认', '取消']
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
        var selected = $("#userGrid").datagrid("getSelected");
        if (null == selected) {
            swal('警告','请选中一条数据', 'warning');
            return;
        }
       
        var index = layer.open({
            type : 2,
            title : '用户修改',
            content : '${contextPath}/user/edit.html?id='+selected.id,
              area : ['670px', '550px'],
            shadeClose : false,
            shade : 0.5,
            btn: ['确认', '取消']
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

    function downloadQrCode(){
        var selected = _userGrid.datagrid("getSelected");
        if (null == selected) {
            swal('警告','请选中一条数据', 'warning');
            return;
        }
        $.ajax({
            type: "POST",
            url: "${contextPath}/user/getUserQrCode.action",
            data: {id: selected.id},
            processData:true,
            dataType: "json",
            async : true,
            success : function(ret) {
                if(ret.success){
                    TLOG.component.operateLog('用户管理',"下载二维码","【ID】:"+selected.id);
                    var a = document.createElement('a'); // 创建a标签
                    a.setAttribute('download', ret.data.userName);// download属性
                    a.setAttribute('href', ret.data.base64QRImg);// href链接
                    a.click();//
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
    }

    function activeUser(){
        var selected = _userGrid.datagrid("getSelected");
        if (null == selected) {
            swal('警告','请选中一条数据', 'warning');
            return;
        }
        $.ajax({
            type: "POST",
            url: "${contextPath}/user/activeUser.action",
            data: {id: selected.id,is_active: 1},
            processData:true,
            dataType: "json",
            async : true,
            success : function(ret) {
                if(ret.success){
                    _userGrid.datagrid("reload");
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

    }

    function unActiveUser(){
        var selected = _userGrid.datagrid("getSelected");
        if (null == selected) {
            swal('警告','请选中一条数据', 'warning');
            return;
        }
        $.ajax({
            type: "POST",
            url: "${contextPath}/user/activeUser.action",
            data: {id: selected.id,is_active: -1},
            processData:true,
            dataType: "json",
            async : true,
            success : function(ret) {
                if(ret.success){
                    _userGrid.datagrid("reload");
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

    }
    //根据主键删除
    function del() {
        var selected = _userGrid.datagrid("getSelected");
        if (null == selected) {
            swal('警告','请选中一条数据', 'warning');
            return;
        }

        swal({
            title : '确定要删除该用户吗？',
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
                url: "${contextPath}/user/delete.action",
                data: {id: selected.id},
                processData:true,
                dataType: "json",
                async : true,
                success : function(ret) {
                    if(ret.success){
                    	TLOG.component.operateLog('用户管理',"用户删除","【ID】:"+selected.id);
                        _userGrid.datagrid("reload");
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

    /**
     * 城市自动联想加载
     * ***/
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

    //表格查询
    function queryUserGrid() {
        var opts = _userGrid.datagrid("options");
        if (null == opts.url || "" == opts.url) {
            opts.url = "${contextPath}/user/listPage.action";
        }

        if(!$('#queryForm').form("validate")){
            return false;
        }
        _userGrid.datagrid("load", bindGridMeta2Form("userGrid", "queryForm"));

    }


    //清空表单
    function clearQueryForm() {
        $('#queryForm').form('clear');
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
        window._userGrid = $('#userGrid');
        bindFormEvent("queryForm", "createdStart", queryUserGrid);
        initUserGrid();
        queryUserGrid();
    })




    /**
     * 初始化用户列表组件
     */
    function initUserGrid() {
        var pager = _userGrid.datagrid('getPager');
        var toolbar=[
            <#resource method="post" code="user/index.html#add">
            {
                iconCls:'icon-add',
                text:'新增',
                handler:function(){
                    openInsert();
                }
            },
            </#resource>
            <#resource method="post" code="user/index.html#update">
            {
                iconCls:'icon-edit',
                text:'修改',
                handler:function(){
                    openUpdate();
                }
            },
            </#resource>
            <#resource method="post" code="user/index.html#reset">
            {
                iconCls:'icon-reset',
                text:'重置密码',
                handler:function(){
                    doResetPassword();
                }
            },
            </#resource>
            <#resource method="post" code="user/index.html#enabled">
            {
                iconCls:'icon-play',
                text:'启用',
                id:'play_btn',
                handler:function(){
                    doEnable(true);
                }
            },
            </#resource>
            <#resource method="post" code="user/index.html#disabled">
            {
                iconCls:'icon-stop',
                text:'禁用',
                id:'stop_btn',
                handler:function(){
                    doEnable(false);
                }
            },
            </#resource>
            <#resource method="post" code="user/index.html#delete">
            {
                  iconCls:'icon-undo',
                  text:'删除',
                  id:'undo-btn',
                handler:function(){
                	del();
                }
            },
            </#resource>

            {
                iconCls:'icon-detail',
                id:'detail-btn',
                text:'查看',
                handler:function(){
                    doDetail();
                }
            },
            <#resource method="post" code="user/index.html#export">
            {
                iconCls:'icon-export',
                text:'导出',
                id:'stop_btn',
                handler:function(){
                    layer.confirm('确认导出数据?', {
                        type: 0,
                        title: '提示',
                        btn: ['确定','取消'],
                        yes:function(){
                            layer.closeAll();
                            doExport('userGrid');
                        }
                    });
                }
            },
            </#resource>
            <#resource method="post" code="user/index.html#donwload">
            {
                iconCls:'icon-down',
                text:'下载二维码',
                id:'download-btn',
                handler:function(){
                    downloadQrCode();
                }
            },
            </#resource>
        ];
        	_userGrid.datagrid({
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
     * 禁启用操作
     * @param enable 是否启用:true-启用
     */
    function doResetPassword() {
        var selected = _userGrid.datagrid("getSelected");
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
            title : '确定要重置密码吗？',
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
                url: "${contextPath}/user/resetPassword.action",
                data: {id: selected.id},
                processData:true,
                dataType: "json",
                async : true,
                success : function(ret) {
                    if(ret.success){
                        _userGrid.datagrid("reload");
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

    /**
     * 禁启用操作
     * @param enable 是否启用:true-启用
     */
    function doEnable(enable) {
        var selected = _userGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300,
            });
            return;
        }
        var msg = (enable || 'true' == enable) ? '确定要启用该用户吗？' : '确定要禁用该用户吗？';

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
                url: "${contextPath}/user/doEnable.action",
                data: {id: selected.id, enable: enable},
                processData:true,
                dataType: "json",
                async : true,
                success : function(ret) {
                    if(ret.success){
                        _userGrid.datagrid("reload");
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

    /**
     * datagrid行点击事件
     * 目前用于来判断 启禁用是否可点
     */
    function onClickRow(index,row) {
        var state = row.$_state;
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

    function doDetail(){
        var selected = _userGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300,
            });
            return;
        }
        openWin('${contextPath}/user/view/' + selected.id)
    }
    

</script>
