<script type="text/javascript">
    let goodsItemCount = 0;
    $(function () {
        $('#registerSource').focus();
        $('#goodsTable tbody').append(template('goodsItem', {index: ++goodsItemCount}));
        initFileUpload('#detectReportUrlFile');
        initFileUpload1('#originCertifiyUrl_'+goodsItemCount);
        initAutoComplete('#productName_'+goodsItemCount,'/toll/category');
        initAutoComplete('#originName_'+goodsItemCount,'/toll/city');

        if(!location.hash){
            var registerSource = localStorage.getItem('registerSource');
            var tradeTypeId = localStorage.getItem('tradeTypeId');
            if(registerSource){
                $('#registerSource').val(registerSource);
                $('[name="registerSource"]').trigger('change');
            }
            if(tradeTypeId){
                $('#tradeTypeId').val(tradeTypeId);
            }
        }
    });

    function returnBack(){
        history.go(-1);
    }
    /*   登记来源     */
    $('[name="registerSource"]').on('change', function () {
        if ($(this).val() === '1') {
            $('[name="tallyAreaNo"], [name="plate"]').closest('.form-group').show();
            $('[name="tradeAccount"], [name="b2"], [name="tradeTypeId"]').closest('.form-group').hide();
        } else {
            $('[name="tallyAreaNo"], [name="plate"]').closest('.form-group').hide();
            $('[name="tradeAccount"], [name="b2"], [name="tradeTypeId"]').closest('.form-group').show();
        }
        $("#idCardNo").val("");
        $("#name").val("");
        $("#addr").val("");
        $("#userId").val("");
        $("#phone").val("");
    })

    /* 货品表格  */
    $('.main-container').on('click', '#addGoodsItem', function () {
        $('#goodsTable tbody').append(template('goodsItem', {index: ++goodsItemCount}));
        initFileUpload1('#originCertifiyUrl_'+goodsItemCount);
        initAutoComplete('#productName_'+goodsItemCount,'/toll/category');
        initAutoComplete('#originName_'+goodsItemCount,'/toll/city');
    });


    $('.main-container').on('click', '.split-minus-btn', function () {
        if ($('#goodsTable tr').length > 2) {
            $(this).closest('tr').remove();
        }
    })


    /*  选择图片  */
    $('.main-container').on('change', '.choose-image', function () {
        $('.choose-image').val()
        let filePath = $(this).val()
        if (!filePath || ! /\.(jpg|jpeg|gif|bmp|png)$/i.test(filePath)) {
            $(this).val('')
            $(this).siblings('.show-image').attr('src', '')
            return false;
        } else {
            let src = window.URL.createObjectURL(this.files[0])
            $(this).siblings('.show-image').attr('src', src)
        }
    })
    $('.main-container').on('click', '.imageUploadWrap .edit-zoom', function () {
        var url = $(this).siblings('.show-image').attr('src');
        if(url){
            layer.open({
                type: 1,
                skin: 'layui-layer-rim',
                closeBtn: 2,
                area: ['90%', '90%'], //宽高
                content: '<p style="text-align:center"><img src="' + url + '" alt="" class="show-image-zoom"></p>'
            });
        }
    })


    function onKeyUpEnter(e) {
        if (e.keyCode == 13) {
            customerId();
        }
    }
    function onKeyUpEnter2(e) {
        if (e.keyCode == 13) {
            cardNo();
        }
    }
    function onKeyUpEnter3(e) {
        if (e.keyCode == 13) {
            tallyAreaNo();
        }
    }

    function tallyAreaNo() {
        var tallyAreaNo = $("#tallyAreaNo").val();
        if(tallyAreaNo == ""){
            return;
        }
        if (tallyAreaNo.length > 0) {
            $.ajax({
                type: 'post',
                url: '/trade/customer/tallyAreaNo/'+tallyAreaNo,
                dataType: 'json',
                async: false,
                success: function (ret) {
                    if (ret.code == "200") {
                        var customer = ret.data;
                        $("#idCardNo").val(customer.cardNo).valid();
                        $("#name").val(customer.name).valid();
                        $("#addr").val(customer.addr).valid();
                        $("#userId").val(customer.id).valid();
                        $("#phone").val(customer.phone).valid();
                    } else {
                        $("#idCardNo").val("");
                        $("#name").val("");
                        $("#addr").val("");
                        $("#userId").val("");
                        $("#phone").val("");
                    }
                },
                error:function(){
                    $("#idCardNo").val("");
                    $("#name").val("");
                    $("#addr").val("");
                    $("#userId").val("");
                    $("#phone").val("");
                }
            });
        }
    }
    function customerId() {
        var customerId = $("#tradeAccountInput").val();
        if(customerId == ""){
            return;
        }
        if (customerId.length > 0) {
            $.ajax({
                type: 'post',
                url: '/trade/customer/id/'+customerId,
                dataType: 'json',
                async: false,
                success: function (ret) {
                    if (ret.code == "200") {
                        var customer = ret.data;
                        
                        $("#tradeAccount").val(customer.customerId).valid();
                        
                        
                        $("#printingCardInput").val(customer.printingCard).valid();
                        $("#tradePrintingCard").val(customer.printingCard).valid();
                        
                        $("#idCardNo").val(customer.idNo).valid();
                        $("#name").val(customer.name).valid();
                        $("#addr").val(customer.address).valid();
                        $("#phone").val(customer.phone).valid();
                    } else {
                        $("#printingCardInput").val('')
                        $("#tradePrintingCard").val('')
                    	$("#tradeAccount").val('')
                        $("#idCardNo").val("");
                        $("#name").val("");
                        $("#addr").val("");
                        $("#phone").val("");
                    }
                },
                error:function(){
                    $("#printingCardInput").val('')
                    $("#tradePrintingCard").val('')
                	$("#tradeAccount").val('')
                    $("#idCardNo").val("");
                    $("#name").val("");
                    $("#addr").val("");
                    $("#phone").val("");
                }
            });
        }

    }
    function cardNo() {
        var cardNo = $("#printingCardInput").val();
        if(cardNo == ""){
            return;
        }
        if (cardNo.length > 0) {
            $.ajax({
                type: 'post',
                url: '/trade/customer/card/'+cardNo,
                dataType: 'json',
                async: false,
                success: function (ret) {
                    if (ret.code == "200") {
                        var customer = ret.data;
                        $("#tradeAccountInput").val(customer.customerId).valid();
                        $("#tradeAccount").val(customer.customerId).valid();
                        $("#tradePrintingCard").val(customer.printingCard).valid();
                        $("#idCardNo").val(customer.idNo).valid();
                        $("#name").val(customer.name).valid();
                        $("#addr").val(customer.address).valid();
                        $("#phone").val(customer.phone).valid();
                    } else {
                    	$("#tradeAccountInput").val('')
                    	$("#tradeAccount").val('')
                    	$("#printingCard").val('')
                        $("#idCardNo").val("");
                        $("#name").val("");
                        $("#addr").val("");
                        $("#tradeAccount").val("");
                        $("#phone").val("");
                    }
                },
                error:function(){
                	$("#tradeAccountInput").val('')
                	$("#tradeAccount").val('')
                	$("#tradePrintingCard").val('')
                    $("#idCardNo").val("");
                    $("#name").val("");
                    $("#addr").val("");
                    $("#tradeAccount").val("");
                    $("#phone").val("");
                }
            });
        }
    }

    /**
     * 初始化自动完成框
     */
    function initAutoComplete(selector,url){
        $(selector).on('change',function () {
            $(this).siblings('input').val('');
        });
        //产地联系输入
        $(selector).autocomplete({
            noCache: 1,
            serviceUrl: url,  //数据地址
            //lookup: countries,    本地测试模拟数据使用结合上面的var countries
            dataType: 'json',
            onSearchComplete: function (query, suggestions) {
            },
            showNoSuggestionNotice: true,
            noSuggestionNotice: "不存在，请重输！",
            onSelect: function (suggestion) {
                var self = this;
                var idField = $(self).siblings('input');
                idField.val(suggestion.id);
                $(self).val(suggestion.value);
                $(self).valid();
            }
        });
    }

    var resubmit =0;
    function create(){
        if(resubmit==0){
            resubmit=1;
        }else{
            resubmit=0;
            swal(
                    '错误',
                    '重复提交',
                    'error'
            );
            return;
        }
        if($('#createRecordForm').validate().form() != true){
            resubmit = 0;
            return;
        }
        //console.log("参数:"+$('#createRecordForm').serialize());
        var registerBills = new Array();
        var registerSource = $("#registerSource").val();
        let index = 0;
        $("#goodsTable").find("tbody").find("tr").each(function(){
            var registerBill = new Object();
            registerBill.registerSource=registerSource;
            if(registerBill.registerSource==1){
                registerBill.tallyAreaNo=$("#tallyAreaNo").val();
                registerBill.userId = $("#userId").val();
            }else{
                registerBill.tradeAccount=$("#tradeAccount").val();
                registerBill.tradePrintingCard=$("#tradePrintingCard").val();
               // registerBill.tradeTypeName=$("#tradeTypeName").val();
                registerBill.tradeTypeId=$("#tradeTypeId").val();
            }
            registerBill.plate=$("#plate").val();
            registerBill.name=$("#name").val();
            registerBill.idCardNo=$("#idCardNo").val();
            registerBill.addr=$("#addr").val();
            registerBill.detectReportUrl = $("#detectReportUrl").val();
            registerBill.phone = $("#phone").val();

            $(this).find("input").each(function(t,el){
                let fieldName = $(this).attr("name").split('_')[0];
                registerBill[fieldName] = $(this).val();
            });
            registerBills.push(registerBill);
        });
        $.ajax({
            type: "POST",
            url: "${contextPath}/registerBill/insert.action",
            data :  JSON.stringify(registerBills),
            dataType: "json",
            async : true,
            contentType: "application/json; charset=utf-8",
            success: function (ret) {
                if(ret.success){
                    //TLOG.component.operateLog(TLOG.operates.add, "登记单管理", ret.data, ret.data);
                    //location.href = '/registerBill/index.html';
                    var paramStr = JSON.stringify(ret.data);
                    var registerSource = $("#registerSource").val();
                     // if(registerSource == 1){
                     //     console.log("打印信息:--:"+paramStr);
                     //     printDirect(paramStr);
                      //}else{
                          localStorage.setItem('registerSource',$("#registerSource").val());
                          localStorage.setItem('tradeTypeId',$("#tradeTypeId").val());
                          layer.alert("登记成功", {type: 0}, function () {
                          	parent.closeWin('view_win');
                          });
                      //}
                    

                }else{
                    resubmit=0;
                    swal(
                            '错误',
                            ret.result,
                            'error'
                    );
                }
            },
            error: function(){
                resubmit=0;
                swal(
                        '错误',
                        '远程访问失败',
                        'error'
                );
            }
        });
    }

    function printDirect(printDate){
        var registerSource = $("#registerSource").val();
        if(typeof callbackObj != 'undefined'){
            window.printFinish=function(){
                layer.alert("登记成功", {type: 0}, function () {
                    //history.go(-1);
                	parent.closeWin('view_win');
                });
            }

            if(registerSource == 1){
                callbackObj.printDirect(printDate,"TallySamplingDocument");
            }else{
                callbackObj.printDirect(printDate,"TransactionSamplingDocument");
            }
        }else{
            layer.confirm('请检查打印的设备是否已连接', {
                type: 0,
                title: '提示',
                btn: ['确定']
            });
        }
    }

    $( document ).on( "click", ".fileimg-view",function () {
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

    $( document ).on( "click", ".img-view-a",function () {
        var url = $('#originCertifiyUrl_'+$(this).attr('data-index')).siblings("input:hidden").val();
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

    //文件上传组件初始化
    function initFileUpload(selecter) {
        $(selecter).fileupload({
            dataType: 'json',
            formData: {type: 4, compress: true},
            done: function (e, res) {
                if (res.result.code == 200) {
                    var url = res.result.data;
                    $(this).siblings('.magnifying').attr('src', url).show();
                    $(this).siblings("input:hidden").val(url);
                    $(this).siblings('.fileimg-cover,.fileimg-edit').show();
                }
            },
            add: function (e, data) {//判断文件类型 var acceptFileTypes = /\/(pdf|xml)$/i;
                var acceptFileTypes = /^gif|bmp|jpe?g|png$/i;
                var name = data.originalFiles[0]["name"];
                var index = name.lastIndexOf(".") + 1;
                var fileType = name.substring(index, name.length);
                if (!acceptFileTypes.test(fileType)) {
                    swal('错误', '请您上传图片类文件jpe/jpg/png/bmp!', 'error');
                    return;
                }
                var size = data.originalFiles[0]["size"];
                // 10M
                if (size > (1024 * 10 * 1024)) {
                    swal('错误', '上传文件超过最大限制!', 'error');
                    return;
                }
                data.submit();
            }
        });
    }

    //文件上传组件初始化
    function initFileUpload1(selecter) {
        $(selecter).fileupload({
            dataType: 'json',
            formData: {type: 4, compress: true},
            done: function (e, res) {
                if (res.result.code == 200) {
                    var url = res.result.data;
                    $(this).siblings("input:hidden").val(url);
                    $(this).parent().parent().hide();
                    $(this).parent().parent().next().show();
                }
            },
            add: function (e, data) {//判断文件类型 var acceptFileTypes = /\/(pdf|xml)$/i;
                var acceptFileTypes = /^gif|bmp|jpe?g|png$/i;
                var name = data.originalFiles[0]["name"];
                var index = name.lastIndexOf(".") + 1;
                var fileType = name.substring(index, name.length);
                if (!acceptFileTypes.test(fileType)) {
                    swal('错误', '请您上传图片类文件jpe/jpg/png/bmp!', 'error');
                    return;
                }
                var size = data.originalFiles[0]["size"];
                // 10M
                if (size > (1024 * 10 * 1024)) {
                    swal('错误', '上传文件超过最大限制!', 'error');
                    return;
                }
                data.submit();
            }
        });
    }
</script>
