<script type="text/javascript">
var resubmit =0;
    function create(){
        if(resubmit==0){
            resubmit=1;
        }else{
            swal(
                    '错误',
                    '重复提交',
                    'error'
            );
            resubmit=0;
            return;
        }
        if($('createRecordForm').validate().form() != true){
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
