<script type="text/javascript">
        function onBeforeLoad(param) {

            return true;
        }
    //表格查询
	function loadCommissionBillGridData(param,success,error){
        //debugger
        $.extend(_commissionBillGrid.datagrid("options").queryParams, buildGridQueryData());
    	var data={};
    	findHighLightBill();
		$.ajax({
        type: "POST",
            async : false,
            url: "${contextPath}/commissionBill/listPage.action",
            processData:true,
            dataType: "json",
            data:buildGridQueryData(),
            success: function (ret){
                if(ret&&ret.rows){
        data = ret;
            	 }
             },
            error: function(){
        data = {}
    }
         });
		 success(data);
    }
    function queryCommissionBillGrid() {
        var opts = _commissionBillGrid.datagrid("options");
        if (null == opts.url || "" == opts.url) {
        opts.url = "${contextPath}/commissionBill/listPage.action";
        }
    	//console.info('queryCommissionBillGrid')
        if(!$('#queryForm').form("validate")){
            return false;
        }
        _commissionBillGrid.datagrid("reload", buildGridQueryData());
        initBtnStatus();

    }
    function buildGridQueryData(){
    	var formdata=bindGridMeta2Form("commissionBillGrid", "queryForm");
        delete  formdata['productCombobox'];
        var rows=_commissionBillGrid.datagrid("getRows");
        var options=_commissionBillGrid.datagrid("options");
        //debugger

        formdata['rows']=options.pageSize;
        formdata['page']=options.pageNumber;

        formdata['sort']=options.sortName;
        formdata['order']=options.sortOrder;

    	return formdata;
    }
    var highLightBill={};
    function findHighLightBill(){
        highLightBill = {};
		 $.ajax({
        type: "POST",
             url: "${contextPath}/commissionBill/findHighLightBill.action",
             processData:true,
             dataType: "json",
             data:{},
             async : false,
             success: function (ret) {
            	 if(ret&&ret.code=='200'){
        highLightBill = ret.data;
            	 }
             },
             error: function(){
        highLightBill = {}
    }
         });
    }
    function coloredRowRender(index,row){
    	if(highLightBill&&highLightBill.id&&row.id==highLightBill.id){
    		return {style:'background:#7dc57d'};
    	}
    }

	function queryPlatesByTallyAreaNo(tallyAreaNo){
        //console.info(tallyAreaNo)
        //console.info(tallyAreaNo)
        $('#likePlate').combobox('loadData', [])
		if(tallyAreaNo&&tallyAreaNo!=''){


        $.ajax({
            type: "POST",
            url: "${contextPath}/user/findPlatesByTallyAreaNo.action",
            processData: true,
            dataType: "json",
            data: { tallyAreaNo: tallyAreaNo },
            async: true,
            success: function (ret) {
                if (ret.success) {
                    $('#likePlate').combobox('loadData', ret.data)
                } else {
                    //$('#plate').combobox('loadData',[])
                }
            },
            error: function () {
                // $('#plate').combobox('loadData',[])
            }
        });

		}
	}

	function onPlateChange(v){
		if(v&&v!='全部'){
        queryCommissionBillGrid();
		}

	}
    //清空表单
    function clearQueryForm() {
        $('#queryForm').form('clear');
        $('#likePlate').combobox('loadData',[])
    }

    //表格表头右键菜单
    function headerContextMenu(e, field){
        e.preventDefault();
        if (!cmenu){
        createColumnMenu("commissionBillGrid");
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
     window._commissionBillGrid = $('#commissionBillGrid');
    $(function () {

        bindFormEvent("queryForm", "state", queryCommissionBillGrid);
        initCommissionBillGrid();

    })


    /**
     * 初始化用户列表组件
     */
    function initCommissionBillGrid() {
        var pager = _commissionBillGrid.datagrid('getPager');
        var toolbar=[
            <#resource method="post" url="commissionBill/index.html#add">
        {
            iconCls:'icon-add',
                    text:'新增',
                    handler:function(){
            openInsert();
                    }
                },
            </#resource>
            <#resource method="post" url="commissionBill/index.html#audit">
                {
                    iconCls:'icon-man',
                    text:'进场审核',
                    id:'audit-btn',
                    disabled :true,
                    handler:function(){
                        audit();
                    }
                },
			</#resource>
            <#resource method="post" url="commissionBill/index.html#batchReviewCheck">
            {
                iconCls:'icon-man',
                text:'批量复检',
                id:'batch-reviewCheck-btn',
                disabled :true,
                handler:function(){
                    commissionBillGrid.doReviewCheck();
                }
            },
            </#resource>

			<#resource method="post" url="commissionBill/index.html#createCheckSheet">
	        {
	            iconCls:'icon-add',
	                            text:'创建打印报告',
	                            id:'createsheet-btn',
	                            disabled :true,
	                            handler:doCreateCheckSheet,
	                            handler:function(){
	            doCreateCheckSheet();
                            }
                },
            </#resource>

		    <#resource method="post" url="commissionBill/index.html#detail">
		        {
		            iconCls:'icon-detail',
		                    id:'detail-btn',
		                    text:'查看',
		                    disabled :true,
		                    handler:function(){
		            doDetail();
		                    }
		                },
            </#resource>
    		<#resource method="post" url="commissionBill/index.html#export">
	        {
	            iconCls:'icon-export',
	                    text:'导出',
	                    handler:function(){
	            layer.confirm('确认导出数据?', {
	                type: 0,
	                title: '提示',
	                btn: ['确定', '取消'],
	                yes: function () {
	                    layer.closeAll();
	                    var opts = _commissionBillGrid.datagrid("options");
	                    if (null == opts.url || "" == opts.url) {
	                        opts.url = "${contextPath}/commissionBill/listPage.action";
	                    }
	                    $.extend(_commissionBillGrid.datagrid("options").queryParams, buildGridQueryData());
	                    doExport('commissionBillGrid');
	                }
	            });

                    }
                }
            </#resource>
            ]
        _commissionBillGrid.datagrid({
        toolbar:toolbar
        });
        pager.pagination({
        <#controls_paginationOpts />,
            //buttons:toolbar

        });
    }
	function initBtnStatus(){
        var btnArray=['detail-btn','createsheet-btn','audit-btn','batch-reviewCheck-btn'];
        $.each(btnArray,function(i,btnId){
            $('#' + btnId).linkbutton('enable');
	        $('#'+btnId).hide();
        })

	}

	function onUnselectAll (){
        onClickRow();
	}
	function onSelectAll(){
        onClickRow();

	}
	function onUnselect(index,row){
        onClickRow(index, row);
	}
    /**
     * datagrid行点击事件
     * 目前用于来判断 启禁用是否可点
     */
    function onClickRow(index,row) {

        initBtnStatus();
        var rows=_commissionBillGrid.datagrid("getSelections");
        if(rows.length==0){
        	return;
        }
        if(commissionBillGrid.isReviewCheck()){
            $('#batch-reviewCheck-btn').show();
        }
	    var rows=_commissionBillGrid.datagrid("getSelections");

		var exists_detectState_pass=$.makeArray(rows).filter(function(v,i){
			var detectState=v.$_detectState;
			   if(!_.isUndefined(detectState)){
				   return true;
			   }
			   return false;
		}).filter(function(v,i){
			if(v.$_checkSheetId&&v.$_checkSheetId!=null&&v.$_checkSheetId!=''){
        		return false;
        	}else{
        		return true;
        	}
		}).length>0;


        var hasName=false;
        var hasCorporateName=false;
        var rowsArray=$.makeArray(rows);


        var nameArray=rowsArray.map(function(v,i){
            return v.name;
        }).filter(function(v,i){
            return $.trim(v) != '';
        });


        if(exists_detectState_pass){
            var distinctNameArray=nameArray.reduce(function(accumulator, currentValue, index, array ){
                if($.inArray(currentValue,array,index+1)==-1){
            accumulator.push(currentValue);
                }
                return accumulator;
            },[] );
    
            var corporateNameArray=rowsArray.map(function(v,i){
                return v.corporateName;
            }).filter(function(v,i){
                return $.trim(v) != '';
            })
    
            var distinctCorporateNameArray=corporateNameArray.reduce(function(accumulator, currentValue, index, array ){
                if($.inArray(currentValue,array,index+1)==-1){
            accumulator.push(currentValue);
                }
                return accumulator;
            },[] );

            $('#createsheet-btn').hide();
           
            if(rowsArray.length==corporateNameArray.length&&distinctCorporateNameArray.length==1){ //全部都有企业名称，且企业名称相同
                $('#createsheet-btn').show();
            }else if(rowsArray.length==nameArray.length&&distinctCorporateNameArray.length==0&&distinctNameArray.length==1){ //全部没有企业名称，且业户名称相同
                $('#createsheet-btn').show();
            }else{
                $('#createsheet-btn').hide();
            }
        }else{
            $('#createsheet-btn').hide();
        }

        if(rows.length>1){
        	//batch
            return;
        }
        var selected = rows[0];
        //console.info(selected)
        if(selected.$_state=='${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_AUDIT.getCode()}'){
        	  $('#audit-btn').show();
        }else{
        	  $('#audit-btn').hide();
        }
      
    	$('#detail-btn').show();

     
        var state = selected.$_state;
        var detectState= selected.$_detectState;

    }
    async function audit(){
        var selected = _commissionBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
        var url='${contextPath}/commissionBill/audit.html?billId=' + selected.id;
        
        layer.open({
            type: 2,
            title: "审核",
            shadeClose: true,
            shade: 0.3,
            offset: "20%",
            shadeClose : false,
            area: ['800px', "350px"],
            content: url,//传入一个链接地址 比如：http://www.baidu.com
            btn: ['进场审核','取消'],
            yes: function(index, layero){
                $.ajax({
                    type: "POST",
                    data:{billId:selected.id},
                    url: "${contextPath}/commissionBill/doAuditCommissionBillByManager.action",
                    processData:true,
                    dataType: "json",

                    async : true,
                    success: function (ret) {
                        if(ret.success){
                        	_commissionBillGrid.datagrid("reload");
                            TLOG.component.operateLog('登记单管理',"审核","【编号】:"+selected.code);
                           // layer.alert('操作成功',{title:'操作',time : 3000}); 
                            
                            layer.alert('操作成功',{
                            	 title:'操作',
                              	time : 600,
                              	end :function(){
                              		 layer.closeAll();
                              		
                              	}
                             },
                             function () {
                            	 layer.closeAll();
                                    }
                                );
                            
                        }else{
                            swal(
                                    '操作',
                                    ret.result,
                                    'info'
                            );
                            layer.closeAll();

                        }
                        
                    },
                    error: function(){

                        swal(
                                '错误',
                                '远程访问失败',
                                'error'
                        );
                        layer.closeAll();
                    }
                });
            }
        });
        
        
        
		let promise = new Promise((resolve, reject) => {
			  layer.open(codeList.join("<br\>"), {btn: ['确定', '取消'], title: "批量审核"},function () {
					resolve("yes");
			  },function(){
					resolve("cancel");
			  }
			  );
			});

		  let result = await promise; // wait until the promise resolves (*)
		  
		  
		  

    }
    function blankFormatter(val,row){
        if(!val){
            return "-";
        }
        return val;
    }

    function closeLastWin(id){
        $('#' + id).last().remove();
    }
    function closeWin(id){
        $('#' + id).remove();
        $('#commissionBillGrid').datagrid('reload');
    }
    function openWin(url){
        $('body').append('<iframe id="view_win" name="view_win" src="' + url + '" style="border:0px;width:100%;height:100%;position:fixed;left:0;top:0"></iframe>');
    }
    function openInsert(){
        openWin('${contextPath}/commissionBill/create.html');
    }


      //打开处理结果窗口
    function openIframe(content,selected){}
    function doDetail(){
        var selected = _commissionBillGrid.datagrid("getSelected");
        if (null == selected) {
        swal({
            title: '警告',
            text: '请选中一条数据',
            type: 'warning',
            width: 300
        });
            return;
        }
        openWin('${contextPath}/commissionBill/view/' + selected.id+'/true');
    }
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
    function doCreateCheckSheet(){
        var selected = _commissionBillGrid.datagrid("getSelected");
        if (null == selected) {
        swal({
            title: '警告',
            text: '请选中一条数据',
            type: 'warning',
            width: 300
        });
            return;
        }

        var rows=_commissionBillGrid.datagrid("getSelections");
        var idList=rows.filter(function(v,i){return true;}).map(function(v,i){return v.id});
        openWin('/checkSheet/edit.html?' +$.param({idList:idList},true));
    }
    function openLayer(url){
        layer.open({
            area: ['600px', "400px"],
            title: '审核',
            content: [url, 'no'], //iframe的url
            btn: ['确定', '取消'],
            yes: function (index, layero) {
                //按钮【确认】的回调
                var body = layer.getChildFrame('body', index); //iframe的body获取方式
                var password = $(body).find("#password").val();
            }
        });
    }




    $('.fileimg-view').on('click', function () {
        var url = $(this).parent().siblings(".magnifying").attr('src');
        if(url){
        layer.open({
            title: '图片',
            type: 1,
            skin: 'layui-layer-rim',
            closeBtn: 2,
            area: ['90%', '90%'], //宽高
            content: '<p style="text-align:center"><img src="' + url + '" alt="" class="show-image-zoom"></p>'
        });
        }
    });
    $('.fileimg-delete').on('click', function () {

    	var imgBox=$(this).parents('.fileimg-box:first');
    	var td= imgBox.parent('td');
    	var imgBoxArr=td.find('.fileimg-box');
    	if(imgBoxArr.length==10){
    		var lastImgBox=$(imgBoxArr[imgBoxArr.length-1]);
    		if(lastImgBox.find("input:hidden").val()!=''){

        imgBox.find(".magnifying").attr('src', '').hide();
            	imgBox.find("input:hidden").val('');
            	imgBox.find('.fileimg-cover,.fileimg-edit').hide();

            	imgBox.appendTo(td);
    		}else{
        imgBox.remove();
    		}
    	}else if(imgBoxArr.length>1){
        imgBox.remove();
    	}else{
        imgBox.find(".magnifying").attr('src', '').hide();
        	imgBox.find("input:hidden").val('');
        	imgBox.find('.fileimg-cover,.fileimg-edit').hide();
    	}

    });
    function initFileUpload(){
        initFileInput($("input[type='file']"));
    }
    function afterUpload(url,fileInputObj){
    	var imgBox=fileInputObj.parent();
    	var td= imgBox.parent('td');
    	 var newImgBox=imgBox.clone(true);

        imgBox.find(".magnifying").attr('src', url).show();
        imgBox.find("input[type='hidden']").val(url);
        imgBox.find('.fileimg-cover,.fileimg-edit').show();
        //console.info(url)
        //debugger
        if(td.find('.fileimg-box').length<10){
        newImgBox.find('input[type="file"]').remove();
        	$('<input type="file" name="file" data-url="${contextPath!}/action/imageApi/upload" multiple="multipart/form-data" />').insertAfter(newImgBox.find('.fileimg-des'))
        	newImgBox.appendTo(td);
        	initFileInput(newImgBox.find('input[type="file"]'))
        }
    }

    function initFileInput(jqueryObj){
        jqueryObj.fileupload({
            dataType: 'json',
            formData: { type: 5, compress: true },
            done: function (e, res) {
                if (res.result.code == 200) {
                    var url = res.result.data;
                    var fileInputObj = $(e.target);
                    afterUpload(url, fileInputObj);
                	/*var imgBox=fileInputObj.parent();
                	var td= imgBox.parent('td');
                	 var newImgBox=imgBox.clone(true);
                	 
                    imgBox.find(".magnifying").attr('src', url).show();
                    imgBox.find("input:hidden").val(url);
                    imgBox.find('.fileimg-cover,.fileimg-edit').show();
                    
                    if(td.find('.fileimg-box').length<10){
                    	newImgBox.find('input[type="file"]').remove();
                    	$('<input type="file" name="file"  data-url="${contextPath!}/action/imageApi/upload" multiple="multipart/form-data" />').insertAfter(newImgBox.find('.fileimg-des'))
                    	newImgBox.appendTo(td);
                    	initFileInput(newImgBox.find('input[type="file"]'))
                    }*/
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
