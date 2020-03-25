<script type="text/javascript">
//查询常用地址列表并填充到常用地址中
	function listUsualAddress(jqEle){
		
		var result=[];
        $.ajax({
            type: "POST",
            url: '/usualAddress/listUsualAddress.action',
            data: JSON.stringify({type:'user'}),
            processData:true,
            dataType: "json",
            async : false,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                if(data.code=="200"){
                	result=data.data;
                }
            },
            error: function(){
               
            }
        });
        jqEle.empty();
       return result.map(function(city,i){
        	var link='<a href="javascript:void(0)" style="padding:2px;margin:2px;" onclick="selectCity(this,'+city.addressId+',\''+city.mergedAddress+'\')"  title="'+city.mergedAddress+'">'+city.address+'</a>&nbsp;&nbsp;';
        	jqEle.append($(link));
        	return $(link);
        	
        });
	}
    //打开新增窗口
    function openInsert(){
        $('#dlg').dialog("setTitle","签名新增");
        $('#dlg').dialog('open');
        $('#dlg').dialog('center');
        $('#_userName').textbox({readonly:false});
        $('#_phone').textbox({readonly:false});
        $(".magnifying").hide();
        $(".fileimg-cover,.fileimg-edit").hide();
        $(":file").attr('disabled',false);


        initFileUpload();
        formFocus("_form", "_userName");
    }

    //打开修改窗口
    var formOldData={};
    function openUpdate(){
        var selected = $("#approverSignatureGrid").datagrid("getSelected");
        if (null == selected) {
            swal('警告','请选中一条数据', 'warning');
            return;
        }
        $('#dlg').dialog("setTitle","签名修改");
        $('#dlg').dialog('open');
        $('#dlg').dialog('center');
        $('#_userName').textbox({readonly:true});
        $('#_cardNo').textbox({readonly:true});
        $('#_addr').textbox({readonly:true});
        $('#_tallyAreaNos').tagbox('enable');
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

        $('#_form').form('load', formData);

        formOldData=$('#_form').serializeObject();
      
        
    }

    function saveOrUpdate(){

        var _formData = removeKeyStartWith($("#_form").serializeObject(),"_");

        var _url = null;
        var isNewData=false;
        //没有id就新增
        if(_formData.id == null || _formData.id==""){
            _url = "${contextPath}/approverSignature/insert.action";
        }else{//有id就修改
            _url = "${contextPath}/approverSignature/update.action";
            isNewData=true;
        }
        $.ajax({
            type: "POST",
            url: _url,
            data: JSON.stringify(_formData),
            processData:true,
            dataType: "json",
            async : true,
            contentType: "application/json; charset=utf-8",
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


    //表格查询
    function queryUserGrid() {
        var opts = _userGrid.datagrid("options");
        if (null == opts.url || "" == opts.url) {
            opts.url = "${contextPath}/approverSignature/listPage.action";
        }

        if(!$('#queryForm').form("validate")){
            return false;
        }
        _userGrid.datagrid("load", bindGridMeta2Form("approverSignatureGrid", "queryForm"));

    }


    //清空表单
    function clearQueryForm() {
        $('#queryForm').form('clear');
    }

    //表格表头右键菜单
    function headerContextMenu(e, field){
        e.preventDefault();
        if (!cmenu){
            createColumnMenu("approverSignatureGrid");
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
        window._userGrid = $('#approverSignatureGrid');
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
        var toolbar=[
            <#resource method="post" url="approverSignature/index.html#add">
            {
                iconCls:'icon-add',
                text:'新增',
                handler:function(){
                    openInsert();
                }
            },
            </#resource>
            <#resource method="post" url="approverSignature/index.html#update">
            {
                iconCls:'icon-edit',
                text:'修改',
                handler:function(){
                    openUpdate();
                }
            },
            </#resource>
         
            
            <#resource method="post" url="approverSignature/index.html#delete">
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
            <#resource method="post" url="approverSignature/index.html#export">
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
                            doExport('approverSignatureGrid');
                        }
                    });
                }
            }
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
     * datagrid行点击事件
     * 目前用于来判断 启禁用是否可点
     */
    function onClickRow(index,row) {
        var state = row.$_state;

    }



    function phoneFormatter(val,row){
        if(val){
            return val.replace(val.substring(3,7), "****")
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
