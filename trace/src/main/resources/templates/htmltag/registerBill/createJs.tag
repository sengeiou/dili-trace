<script type="text/javascript">
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
    })

    /* 货品表格  */
    let goodsItemCount = 0;
    $('.main-container').on('click', '#addGoodsItem', function () {
        $('#goodsTable tbody').append(template('goodsItem', {index: ++goodsItemCount}));
        initFileUpload('#originCertifiyUrl'+goodsItemCount);
        $('input[name="originName"]').autocomplete({
            noCache: 1,
            serviceUrl: '/toll/city',  //数据地址
            //lookup: countries,    本地测试模拟数据使用结合上面的var countries
            dataType: 'json',
            onSearchComplete: function (query, suggestions) {
                var originIdNote = $(this).next();
                //console.log("1:"+$(this).data('selectVal')+"2:"+$(this).val());
                if ($(this).data('selectVal') != $(this).val()) {
                    $(originIdNote).val("");
                }
            },
            showNoSuggestionNotice: true,
            noSuggestionNotice: "不存在，请重输！",
            onSelect: function (suggestion) {
                var originIdNote = $(this).next();
                $(this).data('selectVal', suggestion.value);
                setTimeout(function () {
                    $(this).val(suggestion.value);
                    $(originIdNote).val(suggestion.id);
                    console.log("originIdNote,id:"+$(originIdNote).val())
                }, 50);
            }
        });

        $('input[name="productName"]').autocomplete({
            noCache: 1,
            serviceUrl: '/toll/category',  //数据地址
            //lookup: countries,    本地测试模拟数据使用结合上面的var countries
            dataType: 'json',
            onSearchComplete: function (query, suggestions) {
                var categoryIdNote = $(this).next();
                //console.log("1:"+$(this).data('selectVal')+"2:"+$(this).val());
                if ($(this).data('selectVal') != $(this).val()) {
                    $(categoryIdNote).val("");
                }
            },
            showNoSuggestionNotice: true,
            noSuggestionNotice: "不存在，请重输！",
            onSelect: function (suggestion) {
                var categoryIdNote = $(this).next();
                $(this).data('selectVal', suggestion.value);
                setTimeout(function () {
                    $(this).val(suggestion.value);
                    $(categoryIdNote).val(suggestion.id);
                    console.log("categoryIdNote,id:"+$(categoryIdNote).val())
                }, 50);
            }
        });

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
        layer.open({
            type: 1,
            skin: 'layui-layer-rim',
            closeBtn: 2,
            area: ['90%', '90%'], //宽高
            content: '<p style="text-align:center"><img src="' + $(this).siblings('.show-image').attr('src') + '" alt="" class="show-image-zoom"></p>'
        });
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
                        $("#idCardNo").val(customer.cardNo);
                        $("#name").val(customer.name);
                        $("#addr").val(customer.addr);
                        $("#userId").val(customer.id);
                    } else {
                        $("#idCardNo").val("");
                        $("#name").val("");
                        $("#addr").val("");
                        $("#userId").val("");
                    }
                },
                error:function(){
                    $("#idCardNo").val("");
                    $("#name").val("");
                    $("#addr").val("");
                    $("#userId").val("");
                }
            });
        }
    }
    function customerId() {
        var customerId = $("#tradeAccount").val();
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
                        $("#idCardNo").val(customer.idNo);
                        $("#name").val(customer.name);
                        $("#addr").val(customer.address);
                        $("#printingCard").val(customer.printingCard);
                    } else {
                        $("#idCardNo").val("");
                        $("#name").val("");
                        $("#addr").val("");
                        $("#printingCard").val("");
                    }
                },
                error:function(){
                    $("#idCardNo").val("");
                    $("#name").val("");
                    $("#addr").val("");
                    $("#printingCard").val("");
                }
            });
        }
    }
    function cardNo() {
        var cardNo = $("#printingCard").val();
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
                        $("#idCardNo").val(customer.idNo);
                        $("#name").val(customer.name);
                        $("#addr").val(customer.address);
                        $("#tradeAccount").val(customer.customerId);
                    } else {
                        $("#idCardNo").val("");
                        $("#name").val("");
                        $("#addr").val("");
                        $("#tradeAccount").val("");
                    }
                },
                error:function(){
                    $("#idCardNo").val("");
                    $("#name").val("");
                    $("#addr").val("");
                    $("#tradeAccount").val("");
                }
            });
        }
    }
    //产地联系输入
    $('input[name="originName"]').autocomplete({
        noCache: 1,
        serviceUrl: '/toll/city',  //数据地址
        //lookup: countries,    本地测试模拟数据使用结合上面的var countries
        dataType: 'json',
        onSearchComplete: function (query, suggestions) {
            var originIdNote = $(this).next();
            //console.log("1:"+$(this).data('selectVal')+"2:"+$(this).val());
            if ($(this).data('selectVal') != $(this).val()) {
                $(originIdNote).val("");
            }
        },
        showNoSuggestionNotice: true,
        noSuggestionNotice: "不存在，请重输！",
        onSelect: function (suggestion) {
            var originIdNote = $(this).next();
            $(this).data('selectVal', suggestion.value);
            setTimeout(function () {
                $(this).val(suggestion.value);
                $(originIdNote).val(suggestion.id);
                console.log("originIdNote,id:"+$(originIdNote).val())
            }, 50);
        }
    });
    $('input[name="productName"]').autocomplete({
        noCache: 1,
        serviceUrl: '/toll/category',  //数据地址
        //lookup: countries,    本地测试模拟数据使用结合上面的var countries
        dataType: 'json',
        onSearchComplete: function (query, suggestions) {
            var categoryIdNote = $(this).next();
            //console.log("1:"+$(this).data('selectVal')+"2:"+$(this).val());
            if ($(this).data('selectVal') != $(this).val()) {
                $(categoryIdNote).val("");
            }
        },
        showNoSuggestionNotice: true,
        noSuggestionNotice: "不存在，请重输！",
        onSelect: function (suggestion) {
            var categoryIdNote = $(this).next();
            $(this).data('selectVal', suggestion.value);
            setTimeout(function () {
                $(this).val(suggestion.value);
                $(categoryIdNote).val(suggestion.id);
                console.log("categoryIdNote id:"+$(categoryIdNote).val());
            }, 50);
        }
    });

    var resubmit =0;
    function create(){

        if($('#createRecordForm').validate().form() != true){
            return;
        }
        //console.log("参数:"+$('#createRecordForm').serialize());
        var registerBills = new Array();
        var registerSource = $("#registerSource").val();
        $("#goodsTable").find("tbody").find("tr").each(function(){
            var registerBill = new Object();
            registerBill.registerSource=registerSource;
            if(registerBill.registerSource==1){
                registerBill.tallyAreaNo=$("#tallyAreaNo").val();
                registerBill.userId = $("#userId").val();
            }else{
                registerBill.tradeAccount=$("#tradeAccount").val();
               // registerBill.tradeTypeName=$("#tradeTypeName").val();
                registerBill.tradeTypeId=$("#tradeTypeId").val();
            }
            registerBill.plate=$("#plate").val();
            registerBill.name=$("#name").val();
            registerBill.idCardNo=$("#idCardNo").val();
            registerBill.addr=$("#addr").val();
            registerBill.detectReportUrl = $("#detectReportUrl").val();

            $(this).find("input").each(function(){
                console.log($(this).attr("name")+":参数:"+$(this).val());
                if($(this).attr("name")=="productName"){
                    registerBill.productName=$(this).val();
                }
                if($(this).attr("name")=="productId"){
                    registerBill.productId=$(this).val();
                }
                if($(this).attr("name")=="originName"){
                    registerBill.originName=$(this).val();
                }
                if($(this).attr("name")=="originId"){
                    registerBill.originId=$(this).val();
                }
                if($(this).attr("name")=="weight"){
                    registerBill.weight=$(this).val();
                }
                if($(this).attr("name")=="originCertifiyUrl"){
                    registerBill.originCertifiyUrl=$(this).val();
                }
            });

            registerBills.push(registerBill);
        });

        
        for(var i in registerBills){
        	var bill=registerBills[i];
        	if(bill.productId==''){
            	swal(
                        '错误',
                        '请选择商品',
                        'error'
                );
            	return;
            }
        	
        	if(bill.originId==''){
            	swal(
                        '错误',
                        '请选择产地',
                        'error'
                );
            	return;
            }
        	
        }
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
                    console.log("打印信息:--:"+paramStr);
                    printDirect(paramStr)
                }else{
                    resubmit=0;
                    swal(
                            '错误',
                            ret.result,
                            'error'
                    );
                }
                saveFlag=false;
            },
            error: function(){
                resubmit=0;
                swal(
                        '错误',
                        '远程访问失败',
                        'error'
                );
                saveFlag=false;
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

    $(function () {
        initFileUpload(':file');
    });

    $( document ).on( "click", ".fileimg-view",function () {
        var url = $(this).parent().siblings(".magnifying").attr('src');
        layer.open({
            title:'图片',
            type: 1,
            skin: 'layui-layer-rim',
            closeBtn: 2,
            area: ['90%', '90%'], //宽高
            content: '<p style="text-align:center"><img src="' + url + '" alt="" class="show-image-zoom"></p>'
        });
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
</script>
