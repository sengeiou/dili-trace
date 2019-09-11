<script type="text/javascript">

    //打开新增窗口
    function openInsert(){
        $('#dlg').dialog("setTitle","用户新增");
        $('#dlg').dialog('open');
        $('#dlg').dialog('center');
        $('#_name').textbox({readonly:false});
        $('#_phone').textbox({readonly:false});
        $('#_cardNo').textbox({readonly:false});
        $('#_addr').textbox({readonly:false});
        $('#_taillyAreaNo').textbox({readonly:false});
        $(".magnifying").hide();
        $(".fileimg-cover,.fileimg-edit").hide();
        $(":file").attr('disabled',false);
        $('#_form').form('clear');
        initFileUpload();
        formFocus("_form", "_name");
    }

    //打开修改窗口
    function openUpdate(){
        var selected = $("#userGrid").datagrid("getSelected");
        if (null == selected) {
            swal('警告','请选中一条数据', 'warning');
            return;
        }
        $('#dlg').dialog("setTitle","用户修改");
        $('#dlg').dialog('open');
        $('#dlg').dialog('center');
        $('#_name').textbox({readonly:true});
        $('#_cardNo').textbox({readonly:true});
        $('#_addr').textbox({readonly:true});
        $('#_form').form('clear');
        initFileUpload();
        formFocus("_form", "_name");
        var formData = $.extend({},selected);
        formData = addKeyStartWith(getOriginalData(formData),"_");
        $(".magnifying").hide();
        $(".fileimg-cover,.fileimg-edit").hide();
        if(formData._cardNoFrontUrl){
            $('#_cardNoFrontUrl').siblings('.fileimg-cover,.fileimg-edit').show();
            $('#_cardNoFrontUrl').siblings(".magnifying").attr('src',formData._cardNoFrontUrl).show();
        }
        if(formData._cardNoBackUrl){
            $('#_cardNoBackUrl').siblings('.fileimg-cover,.fileimg-edit').show();
            $('#_cardNoBackUrl').siblings(".magnifying").attr('src',formData._cardNoBackUrl).show();
        }
        if(formData._businessLicenseUrl){
            $('#_businessLicenseUrl').siblings('.fileimg-cover,.fileimg-edit').show();
            $('#_businessLicenseUrl').siblings(".magnifying").attr('src',formData._businessLicenseUrl).show();
        }
        $('#_form').form('load', formData);
        $('#_salesCityId').combobox('setText',formData._salesCityName);
    }

    function saveOrUpdate(){
        if(!$('#_form').form("validate")){
            return;
        }
        var _formData = removeKeyStartWith($("#_form").serializeObject(),"_");
        var _url = null;
        //没有id就新增
        if(_formData.id == null || _formData.id==""){
            _url = "${contextPath}/user/insert.action";
        }else{//有id就修改
            _url = "${contextPath}/user/update.action";
        }
        $.ajax({
            type: "POST",
            url: _url,
            data: _formData,
            processData:true,
            dataType: "json",
            async : true,
            success: function (data) {
                if(data.code=="200"){
                    _userGrid.datagrid("reload");
                    $('#dlg').dialog('close');
                }else{
                    swal('错误',data.result, 'error');
                }
            },
            error: function(){
                swal('错误', '远程访问失败', 'error');
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
            formData: {type:4,compress:true},
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

    /**
     * 初始化用户列表组件
     */
    function initUserGrid() {
        var pager = _userGrid.datagrid('getPager');
        pager.pagination({
            <#controls_paginationOpts/>,
            buttons:[
            <#resource method="post" url="user/index.html#add">
            {
                iconCls:'icon-add',
                text:'新增',
                handler:function(){
                    openInsert();
                }
            },
            </#resource>
            <#resource method="post" url="user/index.html#update">
            {
                iconCls:'icon-edit',
                text:'修改',
                handler:function(){
                    openUpdate();
                }
            },
            </#resource>
            <#resource method="post" url="user/index.html#reset">
            {
                iconCls:'icon-reset',
                text:'重置密码',
                handler:function(){
                    doResetPassword();
                }
            },
            </#resource>
            <#resource method="post" url="user/index.html#enabled">
            {
                iconCls:'icon-play',
                text:'启用',
                id:'play_btn',
                handler:function(){
                    doEnable(true);
                }
            },
            </#resource>
            <#resource method="post" url="user/index.html#disabled">
            {
                iconCls:'icon-stop',
                text:'禁用',
                id:'stop_btn',
                handler:function(){
                    doEnable(false);
                }
            }
            </#resource>
        ]
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
        location.href ='/user/view/' + selected.id;
    }

</script>
