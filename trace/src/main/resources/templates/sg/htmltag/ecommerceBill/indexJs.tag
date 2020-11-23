<script type="text/javascript">


    function onBeforeLoad(param) {

        return true;
    }
    //表格查询
	function loadCommissionBillGridData(param,success,error){
        //debugger
        $.extend(_ecommerceBillGrid.datagrid("options").queryParams, buildGridQueryData());
    	var data={};
    	findHighLightBill();
		$.ajax({
        type: "POST",
            async : false,
            url: "${contextPath}/ecommerceBill/listPage.action",
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
        var opts = _ecommerceBillGrid.datagrid("options");
        if (null == opts.url || "" == opts.url) {
        opts.url = "${contextPath}/ecommerceBill/listPage.action";
        }
    	//console.info('queryCommissionBillGrid')
        if(!$('#queryForm').form("validate")){
            return false;
        }
        _ecommerceBillGrid.datagrid("reload", buildGridQueryData());
        initBtnStatus();

    }
    function buildGridQueryData(){
    	var formdata=bindGridMeta2Form("ecommerceBillGrid", "queryForm");
        delete  formdata['productCombobox'];
        var rows=_ecommerceBillGrid.datagrid("getRows");
        var options=_ecommerceBillGrid.datagrid("options");
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
             url: "${contextPath}/ecommerceBill/findHighLightBill.action",
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
        createColumnMenu("ecommerceBillGrid");
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
     window._ecommerceBillGrid = $('#ecommerceBillGrid');
    $(function () {

        bindFormEvent("queryForm", "state", queryCommissionBillGrid);
        initCommissionBillGrid();

    })


    /**
     * 初始化用户列表组件
     */
    function initCommissionBillGrid() {
        var pager = _ecommerceBillGrid.datagrid('getPager');
        var toolbar=[
            <#resource method="post" url="ecommerceBill/index.html#add">
        {
            iconCls:'icon-add',
                    text:'新增',
                    handler:function(){
            openInsert();
                    }
                },
            </#resource>
                
            <#resource method="post" url="ecommerceBill/index.html#audit">
                {
                    iconCls:'icon-man',
                    text:'审核',
                    id:'audit-btn',
                    disabled :true,
                    handler:function(){
                    	openAudit();
                    }
                },
                
	        </#resource>
                <#resource method="post" url="ecommerceBill/index.html#delete">
                {
                    iconCls:'icon-undo',
                    text:'撤销',
                    id:'delete-btn',
                    disabled :true,
                    handler:function(){
                        openDelete();
                    }
                },
        </#resource>
        <#resource method="post" url="ecommerceBill/index.html#print">
                {
                    iconCls:'icon-print',
                    text:'打印不干胶',
                    id:'print-btn',
                    disabled :true,
                    handler:function(){
                    	openPrint();
                    }
                },
                
	     </#resource>
                <#resource method="post" url="ecommerceBill/index.html#printSeperatePrintReport">
                {
                    iconCls:'icon-print',
                    text:'打印分销报告',
                    id:'printSeperatePrintReport-btn',
                    disabled :true,
                    handler:function(){
                    	openPrintSeperatePrintReport();
                    }
                },
                
	     </#resource>
		 <#resource method="post" url="ecommerceBill/index.html#detail">
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
		 <#resource method="post" url="ecommerceBill/index.html#export">
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
		                    var opts = _ecommerceBillGrid.datagrid("options");
		                    if (null == opts.url || "" == opts.url) {
		                        opts.url = "${contextPath}/ecommerceBill/listPage.action";
		                    }
		                    $.extend(_ecommerceBillGrid.datagrid("options").queryParams, buildGridQueryData());
		                    doExport('ecommerceBillGrid');
		                }
		            });
		
		                    }
		                }
         </#resource>
            ]
        _ecommerceBillGrid.datagrid({
        toolbar:toolbar
        });
        pager.pagination({
        <#controls_paginationOpts />,
            //buttons:toolbar

        });
    }
	function initBtnStatus(){
        var btnArray=['detail-btn','audit-btn','delete-btn','print-btn','printSeperatePrintReport-btn'];
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
        var rows=_ecommerceBillGrid.datagrid("getSelections");
        if(rows.length==0){
        	return;
        }
	    var rows=_ecommerceBillGrid.datagrid("getSelections");
	    if(rows.length==1){
	    	$('#print-btn').show();
	    }else{
	    	$('#print-btn').hide();
	    }
        var rowsArray=$.makeArray(rows);

        var waitAuditRowArray=rowsArray.filter(function(v,i){
            return v.$_state== ${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_AUDIT.getCode()};
        });

        if (waitAuditRowArray.length==1){
        	$('#audit-btn').show();
        	$('#delete-btn').show();
        }else{
            $('#audit-btn').hide();
            $('#delete-btn').hide();
        }
       
        if(rows.length>1){
        	//batch
            return;
        }

    	$('#detail-btn').show();
    	$('#printSeperatePrintReport-btn').show();
    	
        var selected = rows[0];
        var state = selected.$_state;
        var detectState= selected.$_detectState;

    }
    async function openDelete(){
	   	 var selected = _ecommerceBillGrid.datagrid("getSelected");
	     if (null == selected) {
	         swal({
	             title: '警告',
	             text: '请选中一条数据',
	             type: 'warning',
	             width: 300
	         });
	         return;
	     }
	    var rows=_ecommerceBillGrid.datagrid("getSelections");
        var rowsArray=$.makeArray(rows);

        var waitAuditRowArray=rowsArray.filter(function(v,i){
            return v.$_state== ${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_AUDIT.getCode()};
        });
	     if(waitAuditRowArray.length!=1){
	    	 return;
	     } 
	     let confirmPromise = new Promise((resolve, reject) => {
			  layer.confirm('确定要撤销登记单:'+waitAuditRowArray[0].code+" ?", {btn: ['撤销', '取消'], title: "撤销登记单"},function () {
				  resolve({id:selected.id});
			  },function(){
				  resolve('');
				  return false;
			  });
			});
	 	var ret=await confirmPromise;
		if($.type(ret)=='object'){
			var result={};
			$.ajax({
			    type: "POST",
			    url: '${contextPath!}/ecommerceBill/doDelete.action',
			    data: JSON.stringify({id:waitAuditRowArray[0].id}),
			    processData:true,
			    dataType: "json",
			    async : false,
			    contentType: "application/json; charset=utf-8",
			    success: function (ret) {
			    	result=ret;
			    },
			    error: function(){
			    	result={"code":"5000",result:"远程访问失败"}
			    }
			});

			if(result.code=='200'){
                layer.alert('操作成功',{
						title:'操作',
                   	time : 600,
                   	end :function(){
                   		 layer.closeAll();
                   	     $('#ecommerceBillGrid').datagrid('reload');
                   	}
                  },
             	 function () {
             	  layer.closeAll();
                  $('#ecommerceBillGrid').datagrid('reload');
                     }
             );
                

			}else{
                swal(
                        '操作',
                        result.result,
                        'info'
                );
                layer.closeAll();
			}
			
		}else{
			 layer.closeAll();
		}
	     
    	
    }
    async function openPrint(){
    	 var selected = _ecommerceBillGrid.datagrid("getSelected");
         if (null == selected) {
	         swal({
	             title: '警告',
	             text: '请选中一条数据',
	             type: 'warning',
	             width: 300
	         });
             return;
         }
         
		var result={};
		$.ajax({
		    type: "POST",
		    url: '${contextPath!}/ecommerceBill/prePrint.action',
		    data: JSON.stringify({id:selected.id}),
		    processData:true,
		    dataType: "json",
		    async : false,
		    contentType: "application/json; charset=utf-8",
		    success: function (ret) {
		    	result=ret;
		    },
		    error: function(){
		    	result={"code":"5000",result:"远程访问失败"}
		    }
		});
		debugger
		if(result.code!='200'){
	         swal({
	             title: '警告',
	             text: result.result,
	             type: 'error',
	             width: 300
	         });
			return;
		}
		
    	if(typeof(callbackObj)!='undefined'&&callbackObj.printDirect){
    		callbackObj.printDirect(JSON.stringify(result),"StickerDocument");
    	}else{
    		 swal(
                     '错误',
                     '请升级客户端或者在客户端环境运行当前程序',
                     'error'
             );
    	}
    	
    	
    }
    
    
    async function openPrintSeperatePrintReport(){
   	 var selected = _ecommerceBillGrid.datagrid("getSelected");
        if (null == selected) {
	         swal({
	             title: '警告',
	             text: '请选中一条数据',
	             type: 'warning',
	             width: 300
	         });
            return;
        }
        openWin('/ecommerceBill/prePrintSeperatePrintReport.html?billId='+selected.id);
   }
    
    async function openAudit(){
	   	 var selected = _ecommerceBillGrid.datagrid("getSelected");
	     if (null == selected) {
	         swal({
	             title: '警告',
	             text: '请选中一条数据',
	             type: 'warning',
	             width: 300
	         });
	         return;
	     }
     
	    var rows=_ecommerceBillGrid.datagrid("getSelections");
        var rowsArray=$.makeArray(rows);
        var waitAuditRowArray=rowsArray.filter(function(v,i){
            return v.$_state== ${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_AUDIT.getCode()};
        });
        
        if(waitAuditRowArray.length!=1){
        	return;
        }
        var hasOriginCertifiyUrlRowArray=waitAuditRowArray.filter(function(v,i){
            return $.trim(v.originCertifiyUrl)!='';
        });
        
        var hasDetectReportUrlRowArray=waitAuditRowArray.filter(function(v,i){
            return $.trim(v.detectReportUrl)!='';
        });
        var reconfirmPromise =  new Promise((resolve, reject) => {
        		resolve('');
		});
		if(hasDetectReportUrlRowArray.length==1){
			reconfirmPromise = new Promise((resolve, reject) => {
				  layer.confirm('登记单:<br/>'+waitAuditRowArray[0].code, {btn: ['合格', '取消'], title: "审核登记单"},function () {
						resolve({id:waitAuditRowArray[0].id,state:${@com.dili.trace.glossary.RegisterBillStateEnum.ALREADY_CHECK.getCode()},detectState:${@com.dili.trace.glossary.BillDetectStateEnum.PASS.getCode()}});
				  },function(){
						resolve('');
						return false;
				  });
				});
			
		}else if(hasOriginCertifiyUrlRowArray.length==1){
			reconfirmPromise = new Promise((resolve, reject) => {
				  layer.confirm('登记单:<br/>'+waitAuditRowArray[0].code, {btn: ['检测', '合格不检测', '取消'], title: "审核登记单"},function () {
					  resolve({id:waitAuditRowArray[0].id,state:${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_CHECK.getCode()}});
				  },function(){
					  resolve({id:waitAuditRowArray[0].id,state:${@com.dili.trace.glossary.RegisterBillStateEnum.ALREADY_CHECK.getCode()},detectState:${@com.dili.trace.glossary.BillDetectStateEnum.PASS.getCode()}});
					  return false;
				  },function(){
					  resolve('');
					  return false;
				  });
				});
		}else{
			reconfirmPromise = new Promise((resolve, reject) => {
				  layer.confirm('登记单:<br/>'+waitAuditRowArray[0].code, {btn: ['检测', '取消'], title: "审核登记单"},function () {
					  resolve({id:waitAuditRowArray[0].id,state:${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_CHECK.getCode()}});
				  },function(){
					  resolve('');
					  return false;
				  });
				});
		}
		var ret=await reconfirmPromise;
		if($.type(ret)=='object'){
			//console.info(ret)
			var result={};
			$.ajax({
			    type: "POST",
			    url: '${contextPath!}/ecommerceBill/doAudit.action',
			    data: JSON.stringify(ret),
			    processData:true,
			    dataType: "json",
			    async : false,
			    contentType: "application/json; charset=utf-8",
			    success: function (ret) {
			    	result=ret;
			    },
			    error: function(){
			    	result={"code":"5000",result:"远程访问失败"}

			    }
			});
			if(result.code=='200'){
                layer.alert('操作成功',{
						title:'操作',
                   	time : 600,
                   	end :function(){
                   		 layer.closeAll();
                   		 $('#ecommerceBillGrid').datagrid('reload');
                   	}
                  },
             	 function () {
             	  layer.closeAll();
             	 $('#ecommerceBillGrid').datagrid('reload');
                     }
             );
                

			}else{
                swal(
                        '操作',
                        result.result,
                        'info'
                );
                layer.closeAll();
			}
		
			
		}else{
			layer.closeAll();
		}

    	
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
        $('#ecommerceBillGrid').datagrid('reload');
    }
    function openWin(url){
        $('body').append('<iframe id="view_win" name="view_win" src="' + url + '" style="border:0px;width:100%;height:100%;position:fixed;left:0;top:0"></iframe>');
    }
    function openInsert(){
        openWin('/ecommerceBill/create.html');
    }


      //打开处理结果窗口
    function openIframe(content,selected){}
    function doDetail(){
        var selected = _ecommerceBillGrid.datagrid("getSelected");
        if (null == selected) {
        swal({
            title: '警告',
            text: '请选中一条数据',
            type: 'warning',
            width: 300
        });
            return;
        }
        openWin('/ecommerceBill/view/' + selected.id);
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
