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
        if(!$('#_form').form("validate")){
            return;
        }
        var formData = $("#_form").serializeObject();
        $.ajax({
            type: "POST",
            url: "/upStream/save.action",
            data: JSON.stringify(formData),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success:function (result) {
                if(result.success){
                    _grid.datagrid("reload");
                    $('#dlg').dialog('close');
                }else{
                    swal('错误',result.message, 'error');
                }
            },
            error: function(){
                swal('错误', '远程访问失败', 'error');
            }
        });
    }

    //打开新增窗口
    function openInsert(){
        $('#dlg').dialog('open');
        $('#dlg').dialog('center');

        $(".magnifying,.fileimg-cover,.fileimg-edit").hide();//隐藏图片img
        $('#_form').form('clear');
        $('#upstreamType').combobox('setValue',10);
        selectedTags = {};
        initFileUpload();
    }

    //打开修改窗口
    function openUpdate(){
        var selected = _grid.datagrid("getSelected");
        if (null == selected) {
            swal('警告','请选中一条数据', 'warning');
            return;
        }

        $('#dlg').dialog('open');
        $('#dlg').dialog('center');
        $('#_form').form('clear');

        var formData = $.extend({},selected);
        if(formData.cardNoFrontUrl){
            $('#cardNoFrontUrl').siblings('.fileimg-cover,.fileimg-edit').show();
            $('#cardNoFrontUrl').siblings(".magnifying").attr('src',formData.cardNoFrontUrl).show();
        }
        if(formData.cardNoBackUrl){
            $('#cardNoBackUrl').siblings('.fileimg-cover,.fileimg-edit').show();
            $('#cardNoBackUrl').siblings(".magnifying").attr('src',formData.cardNoBackUrl).show();
        }
        if(formData.businessLicenseUrl){
            $('#businessLicenseUrl').siblings('.fileimg-cover,.fileimg-edit').show();
            $('#businessLicenseUrl').siblings(".magnifying").attr('src',formData.businessLicenseUrl).show();
        }
        if(formData.operationLicenseUrl){
            $('#operationLicenseUrl').siblings('.fileimg-cover,.fileimg-edit').show();
            $('#operationLicenseUrl').siblings(".magnifying").attr('src',formData.operationLicenseUrl).show();
        }
        if(formData.manufacturingLicenseUrl){
            $('#manufacturingLicenseUrl').siblings('.fileimg-cover,.fileimg-edit').show();
            $('#manufacturingLicenseUrl').siblings(".magnifying").attr('src',formData.manufacturingLicenseUrl).show();
        }
        $('#_form').form('load', formData);
        $('#upstreamType').combobox('setValue',formData.$_upstreamType);
        selectedTags = {};
        $.ajax({
            url:'/upStream/listUserByUpstreamId.action',
            data : {upstreamId : formData.id},
            success:function (result) {
                if(result.success){
                    $.each(result.data, function(index,item){
                        selectedTags[item.id] = item.name+" "+item.cardNo;
                    });
                    $('#userIds').tagbox('setValues',Object.keys(selectedTags));
                }else{
                    swal('错误',result.message,'error');
                }
            }
        });
        initFileUpload();
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
            onChange : function (newValue,oldValue) {
                if(newValue == 10){
                    $('#corporateInfo').hide();
                }else{
                    $('#corporateInfo').show();
                }
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