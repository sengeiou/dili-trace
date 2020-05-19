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

    function saveOrUpdate(successfn) {
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
                    successfn();
                }else{
                    swal('错误',result.message, 'error');
                }
            },
            error: function(){
                swal('错误', '远程访问失败', 'error');
            }
        });
    }



    $(function () {
        initFileUpload();
        $('#upstreamType').combobox({
            onChange : function (newValue,oldValue) {
                if(newValue == 10){
                    $('#corporateInfo').hide();
                }else{
                    $('#corporateInfo').show();
                }
            }
        });
        let id = $('#id').val();
        if(id){
            $.ajax({
                url:'/upStream/listUserByUpstreamId.action',
                data : {upstreamId : $('#id').val()},
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
        }

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

</script>