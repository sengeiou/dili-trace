<script type="text/javascript">
    function returnBack(){
        history.go(-1);
    }
    /*   登记来源     */
    $('[name="registerSource"]').on('change', function () {
        if ($(this).val() === '1') {
            $('[name="tallyAreaNo"], [name="plate"]').closest('.form-group').show();
            $('[name="userId"], [name="b2"]').closest('.form-group').hide();
        } else {
            $('[name="tallyAreaNo"], [name="plate"]').closest('.form-group').hide();
            $('[name="userId"], [name="b2"]').closest('.form-group').show();
        }
    })

    /* 货品表格  */
    let goodsItemCount = 0;
    $('.main-container').on('click', '#addGoodsItem', function () {
        $('#goodsTable tbody').append(template('goodsItem', {index: goodsItemCount++}));

        $('input[name="originName"]').autocomplete({
            noCache: 1,
            serviceUrl: '/api/toll/city',  //数据地址
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
                    //console.log("name:"+$(this).val()+",id:"+$(originIdNote).val())
                }, 50);
            }
        });

        $('input[name="productName"]').autocomplete({
            noCache: 1,
            serviceUrl: '/api/toll/category',  //数据地址
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
                    //console.log("name:"+$(this).val()+",id:"+$(categoryIdNote).val())
                }, 50);
            }
        });

    })
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
    function customerId() {
        var customerId = $("#userId").val();
        if(customerId == ""){
            return;
        }
        if (customerId.length > 0) {
            $.ajax({
                type: 'post',
                url: '/api/trade/customer/id/'+customerId,
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

                    }
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
                url: '/api/trade/customer/card/'+cardNo,
                dataType: 'json',
                async: false,
                success: function (ret) {
                    if (ret.code == "200") {
                        var customer = ret.data;
                        $("#idCardNo").val(customer.idNo);
                        $("#name").val(customer.name);
                        $("#addr").val(customer.address);
                        $("#userId").val(customer.customerId);
                    } else {

                    }
                }
            });
        }
    }
    //产地联系输入
    $('input[name="originName"]').autocomplete({
        noCache: 1,
        serviceUrl: '/api/toll/city',  //数据地址
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
                //console.log("name:"+$(this).val()+",id:"+$(originIdNote).val())
            }, 50);
        }
    });
    $('input[name="productName"]').autocomplete({
        noCache: 1,
        serviceUrl: '/api/toll/category',  //数据地址
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
                //console.log("name:"+$(this).val()+",id:"+$(categoryIdNote).val())
            }, 50);
        }
    });

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
            return;
        }
        //console.log("参数:"+$('#createRecordForm').serialize());
        var registerBills = new Array();
        $("#goodsTable").find("tbody").find("tr").each(function(){
            var registerBill = new Object();
            registerBill.registerSource=$("#registerSource").val();
            registerBill.tallyAreaNo=$("#tallyAreaNo").val();
            registerBill.plate=$("#plate").val();
            registerBill.userId=$("#userId").val();
            registerBill.name=$("#name").val();
            registerBill.idCardNo=$("#idCardNo").val();
            registerBill.addr=$("#addr").val();

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
                    location.href = '/registerBill/index.html';
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

</script>
