<script type="text/javascript">
var currentUser={"depId":"${user.depId!}"
	,"id":"${user.id!}"
	,"realName":"${user.realName!}"
	,"userName":"${user.userName!}"
	,"depName":"${user.depName!}"};
	
 function onBeforeLoad(param) {
    var firstLoad = $(this).attr("firstLoad");
    if (firstLoad == "false" || typeof (firstLoad) == "undefined")
    {
        $(this).attr("firstLoad","true");
        return false;
    }
    return true;
}
    //表格查询
	function loadRegisterBillGridData(param,success,error){
		$.extend(_registerBillGrid.datagrid("options").queryParams,buildGridQueryData());

		
    	var data={};
    	 findFirstWaitAuditRegisterBillCreateByCurrentUser();
		 $.ajax({
             type: "POST",
             url: "${contextPath}/registerBill/listPage.action",
             processData:true,
             dataType: "json",
             data:buildGridQueryData(),
             async : false,
             success: function (ret) {
            	 if(ret&&ret.rows){
            		 data=ret;
            	 }
             },
             error: function(){
            	 data={}
             }
         });
		 success(data);
    }
    function queryRegisterBillGrid() {
        var opts = _registerBillGrid.datagrid("options");
        if (null == opts.url || "" == opts.url) {
            opts.url = "${contextPath}/registerBill/listPage.action";
        }
    	//console.info('queryRegisterBillGrid')
        if(!$('#queryForm').form("validate")){
            return false;
        }
        _registerBillGrid.datagrid("reload", buildGridQueryData());
        initBtnStatus();

    }
    function buildGridQueryData(){
    	var formdata=bindGridMeta2Form("registerBillGrid", "queryForm");
        delete  formdata['productCombobox'];
        var rows=_registerBillGrid.datagrid("getRows");
        var options=_registerBillGrid.datagrid("options");
        //debugger
        
        formdata['rows']=options.pageSize;
        formdata['page']=options.pageNumber;
        
        formdata['sort']=options.sortName;
        formdata['order']=options.sortOrder;
        
    	return formdata;
    }
    var findFirstWaitAuditRegisterBill={};
    function findFirstWaitAuditRegisterBillCreateByCurrentUser(){
    	findFirstWaitAuditRegisterBill={};
		 $.ajax({
             type: "POST",
             url: "${contextPath}/registerBill/findFirstWaitAuditRegisterBillCreateByCurrentUser.action",
             processData:true,
             dataType: "json",
             data:{},
             async : false,
             success: function (ret) {
            	 if(ret&&ret.code=='200'){
            		 findFirstWaitAuditRegisterBill=ret.data;
            	 }
             },
             error: function(){
            	 findFirstWaitAuditRegisterBill={}
             }
         });
    }
    function coloredRowRender(index,row){
    	if(findFirstWaitAuditRegisterBill&&findFirstWaitAuditRegisterBill.id&&row.id==findFirstWaitAuditRegisterBill.id){
    		return {style:'background:#7dc57d'};	
    	}
    }

	function onPlateChange(v){
		if(v&&v!='全部'){
			queryRegisterBillGrid();
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
            createColumnMenu("registerBillGrid");
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
     window._registerBillGrid = $('#registerBillGrid');
    $(function () {
 	   
        bindFormEvent("queryForm", "state", queryRegisterBillGrid);
        initRegisterBillGrid();
        //queryRegisterBillGrid();

    })


    /**
     * 初始化用户列表组件
     */
    function initRegisterBillGrid() {
        var pager = _registerBillGrid.datagrid('getPager');
        var toolbar=[
            <#resource method="post" url="registerBill/index.html#add">
                {
                    iconCls:'icon-add',
                    text:'新增',
                    handler:function(){
                        openInsert();
                    }
                },
            </#resource>
                <#resource method="post" url="registerBill/index.html#edit">
                {
                    iconCls:'icon-edit',
                    text:'修改',
                    id:'edit-btn',
                    disabled :true,
                    handler:function(){
                        openEdit();
                    }
                },
            </#resource>
            <#resource method="post" url="registerBill/index.html#audit">
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
        
            <#resource method="post" url="registerBill/index.html#undo">
                {
                    iconCls:'icon-undo',
                    text:'撤销',
                    id:'undo-btn',
                    disabled :true,
                    handler:undo,
                    handler:function(){
                        undo();
                    }
                },
        </#resource>
              
        
            <#resource method="post" url="registerBill/index.html#checkin">
                {
                    iconCls:'icon-man',
                    text:'进场审核',
                    id:'checkin-btn',
                    disabled :true,
                    handler:function(){
                        openCheckin();
                    }
                },
            </#resource>
            <#resource method="post" url="registerBill/index.html#checkManully">
                {
                    iconCls:'icon-man',
                    text:'人工检测',
                    id:'check-manully-btn',
                    disabled :true,
                    handler:function(){
                        openCheckManully();
                    }
                },
            </#resource>

            <#resource method="post" url="registerBill/index.html#detail">
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
            <#resource method="post" url="registerBill/index.html#export">
                {
                    iconCls:'icon-export',
                    text:'导出',
                    handler:function(){
                   	 layer.confirm('确认导出数据?', {
                         type: 0,
                         title: '提示',
                         btn: ['确定','取消'],
                         yes:function(){
                        	    layer.closeAll();
                        	   var opts = _registerBillGrid.datagrid("options");
                               if (null == opts.url || "" == opts.url) {
                                   opts.url = "${contextPath}/registerBill/listPage.action";
                               }
                       		$.extend(_registerBillGrid.datagrid("options").queryParams,buildGridQueryData());
                        	 doExport('registerBillGrid');
                         }
                     });
                       
                    }
                }
            </#resource>
            ]
        _registerBillGrid.datagrid({
            toolbar:toolbar
        });
        pager.pagination({
            <#controls_paginationOpts/>,
            //buttons:toolbar

        });
    }
	function initBtnStatus(){
        /*$('#undo-btn').linkbutton('disable');
        $('#audit-btn').linkbutton('disable');
        $('#auto-btn').linkbutton('disable');
        $('#sampling-btn').linkbutton('disable');
        $('#review-btn').linkbutton('disable');
        $('#handle-btn').linkbutton('disable');*/
        
        var btnArray=['check-manully-btn','checkin-btn','upload-detectreport-btn','upload-origincertifiy-btn','copy-btn','edit-btn','detail-btn','undo-btn','audit-btn','audit-withoutDetect-btn','auto-btn','sampling-btn','review-btn','handle-btn'
        	,'batch-audit-btn','batch-sampling-btn','batch-auto-btn','remove-reportAndcertifiy-btn','createsheet-btn']
	    for (var i = 0; i < btnArray.length; i++) {
	        var btnId = btnArray[i];
	        $('#'+btnId).linkbutton('enable');
	        $('#'+btnId).hide();
	      
	    }
	}
	function isOnlyBatchAudit(){
		//var batched=true;
		var rows=_registerBillGrid.datagrid("getSelections");
        if(rows.length>1){
        	for(var i=0;i<rows.length;i++){
        		 var state = rows[i].$_state;
        		 if (state == ${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_AUDIT.getCode()} ){
        			  return true;
                 }
        	}
        }
        return false;
	}
	function isOnlyBatchSimpling(){
		//var batched=true;
		var rows=_registerBillGrid.datagrid("getSelections");
        if(rows.length>1){
        	for(var i=0;i<rows.length;i++){
        		 var state = rows[i].$_state;
        		 if (state == ${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_SAMPLE.getCode()} ){
        			  return true;
                 }
        	}
        }
        return false;
	}
	function isOnlyBatchAuto(){
		//var batched=true;
		var rows=_registerBillGrid.datagrid("getSelections");
        if(rows.length>1){
        	for(var i=0;i<rows.length;i++){
        		 var state = rows[i].$_state;
        		 if (state == ${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_SAMPLE.getCode()} ){
        			  return true;
                 }
        	}
        }
        return false;
	}
	function onUnselectAll (){
		onClickRow();
	}
	function onSelectAll(){
		onClickRow();
		
	}
	function onUnselect(index,row){
		onClickRow(index,row);
	}
    /**
     * datagrid行点击事件
     * 目前用于来判断 启禁用是否可点
     */
    function onClickRow(index,row) {

        initBtnStatus();
        var rows=_registerBillGrid.datagrid("getSelections");
        if(rows.length==0){
        	return;
        }
        
	    var rows=_registerBillGrid.datagrid("getSelections");
	    
		var arr=$.makeArray(rows).filter(function(v,i){
			var detectState=v.$_detectState;
			   if(detectState==${@com.dili.trace.glossary.BillDetectStateEnum.PASS.getCode()} || detectState==${@com.dili.trace.glossary.BillDetectStateEnum.REVIEW_PASS.getCode()}){
				   return true;
			   }
			   return false;
		}).filter(function(v,i){
			if(v.$_checkSheetId&&v.$_checkSheetId!=null&&v.$_checkSheetId!=''){
        		return false;
        	}else{
        		return true;
        	}
		});
		if(arr.length>0){
			 $('#createsheet-btn').show();
		}else{
			 $('#createsheet-btn').hide();
		}
		
        if(rows.length>1){
        	//batch
            if(isOnlyBatchAudit()){
            	$('#batch-audit-btn').show();
            }
            if(isOnlyBatchSimpling()){
            	$('#batch-sampling-btn').show();
            }
            if(isOnlyBatchAuto()){
            	$('#batch-auto-btn').show();
            }
            return;
        }

      
    	$('#copy-btn').show();
    	$('#detail-btn').show();

    	
    	
    	
        var selected = rows[0];
        var state = selected.$_state;
        var detectState= selected.$_detectState;
        var handleResult= selected.handleResult;
    	$('#upload-origincertifiy-btn').show();
        if (state == ${@com.dili.trace.glossary.RegisterBillStateEnum.NEW.getCode()} ){
            //接车状态是“已打回”,启用“撤销打回”操作
            $('#undo-btn').show();
            $('#edit-btn').show();
            $('#upload-detectreport-btn').show();
        	
            if(selected.originCertifiyUrl=='有'||selected.detectReportUrl=='有'){
           	 $('#remove-reportAndcertifiy-btn').show();
    	   }
        	
            //$('#batch-audit-btn').show();

        }else if (state == ${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_AUDIT.getCode()} ){
            //接车状态是“已打回”,启用“撤销打回”操作
            $('#undo-btn').show();
            $('#audit-btn').show();
            $('#edit-btn').show();
            $('#upload-detectreport-btn').show();
            $('#checkin-btn').show();
        	
            if(selected.originCertifiyUrl=='有'||selected.detectReportUrl=='有'){
           	 $('#remove-reportAndcertifiy-btn').show();
    	   }
        	
            //$('#batch-audit-btn').show();

        }else if(state == ${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_SAMPLE.getCode()} ){
        	 $('#undo-btn').show();
            $('#auto-btn').show();
            $('#sampling-btn').show();
            //按钮不可用
        }else if(state == ${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_CHECK.getCode()} ){
            $('#check-manully-btn').show();
            //按钮不可用
        }else if(state == ${@com.dili.trace.glossary.RegisterBillStateEnum.CHECKING.getCode()} ){
            //按钮不可用
        }else if(state == ${@com.dili.trace.glossary.RegisterBillStateEnum.ALREADY_CHECK.getCode()}){
            //按钮不可用
        	if(detectState==${@com.dili.trace.glossary.BillDetectStateEnum.NO_PASS.getCode()}){
           	 	$('#review-btn').show();
           }else if(detectState==${@com.dili.trace.glossary.BillDetectStateEnum.REVIEW_NO_PASS.getCode()}){
        	  if(handleResult==null||handleResult==''){
              	 $('#review-btn').show();
              }
           }
        }

			
		

        
        
       
      //debugger
        //if(selected.handleResultUrl&&selected.handleResult&&selected.handleResultUrl!=null&&selected.handleResult!=null&&selected.handleResultUrl!=''&&selected.handleResult!=''){
        	 //$('#handle-btn').linkbutton('disable');
        //}else if(detectState==${@com.dili.trace.glossary.BillDetectStateEnum.REVIEW_NO_PASS.getCode()}&&selected.handleResultUrl==null&&selected.handleResult==null){
        //if(detectState==${@com.dili.trace.glossary.BillDetectStateEnum.REVIEW_NO_PASS.getCode()}){
        	 $('#handle-btn').show();
        //}

       
    }

    function blankFormatter(val,row){
        if(!val){
            return "-";
        }
        return val;
    }

    function closeLastWin(id){
        $('#'+id).last().remove();
    }
    function closeWin(id){
        $('#'+id).remove();
        $('#registerBillGrid').datagrid('reload');
    }
    function openWin(url){
        $('body').append('<iframe id="view_win" name="view_win" src="'+url+'" style="border:0px;width:100%;height:100%;position:fixed;left:0;top:0"></iframe>');
    }

    async function openCheckin(){
        var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }

        let promise = new Promise((resolve, reject) => {
            layer.confirm('是否允许进场？', {btn: ['允许', '拒绝','取消'], title: "进场审核"
                ,yes:function () {
                    resolve(${@com.dili.trace.glossary.CheckinStatusEnum.ALLOWED.getCode()});
                },btn2:function(){
                    resolve(${@com.dili.trace.glossary.CheckinStatusEnum.NOTALLOWED.getCode()});
                }
            });
		});

	  let checkinStatus = await promise; // wait until the promise resolves (*)
       $.ajax({
                 type: "POST",
                 url: "${contextPath}/registerBillHZ/doCheckIn.action",
               data: JSON.stringify({billId:selected.id,checkinStatus:checkinStatus}),
            processData:true,
            dataType:  "json",
            async : true,
            contentType: "application/json; charset=utf-8",
                 success: function (ret) {
                     if(ret.success){
                         _registerBillGrid.datagrid("reload");
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
    async function openCheckManully(){
        var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }

          let promise = new Promise((resolve, reject) => {
            layer.confirm('是否检测合格？', {btn: ['合格', '不合格','取消'], title: "进门审核"
                ,yes:function () {
                    resolve(true);
                },btn2:function(){
                    resolve(false);
                }
            });
		});

	  let pass = await promise; // wait until the promise resolves (*)
       $.ajax({
                 type: "POST",
                 url: "${contextPath}/registerBillHZ/doManullyCheck.action",
               data: JSON.stringify({billId:selected.id,pass:pass}),
            processData:true,
            dataType:  "json",
            async : true,
            contentType: "application/json; charset=utf-8",
                 success: function (ret) {
                     if(ret.success){
                         _registerBillGrid.datagrid("reload");
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

    function openInsert(){
        openWin('/registerBill/create.html');
    }
    function openEdit(){
        var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
    	openWin('/registerBill/edit.html?id='+ selected.id);
    }
    function doDetail(){
        var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
        openWin('/registerBill/view/' + selected.id+'/true');
    }
    function audit(){
        var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
        openIframe('/registerBill/audit/' + selected.id,selected)

    }
	function    auditWithoutDetect(){
	    var selected = _registerBillGrid.datagrid("getSelected");
	    if (null == selected) {
	        swal({
	            title: '警告',
	            text: '请选中一条数据',
	            type: 'warning',
	            width: 300
	        });
	        return;
	    }
	    layer.confirm('确认审核不检测?', {btn: ['确定', '取消'], title: "提示"}
	    	, function () {
	    	 $.ajax({
                 type: "POST",
                 url: "${contextPath}/registerBill/doAuditWithoutDetect.action",
                 processData:true,
                 dataType: "json",
                 data:{id:selected.id},
                 async : true,
                 success: function (ret) {
                     if(ret.success){
                         _registerBillGrid.datagrid("reload");
                         TLOG.component.operateLog('登记单管理',"审核不检测","【编号】:"+selected.code);
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
	    	
	    })
	}
	async function batchAudit(){
	   	var rows=_registerBillGrid.datagrid("getSelections");
	   	if (rows.length==0) {
	           swal({
	               title: '警告',
	               text: '请选中一条数据',
	               type: 'warning',
	               width: 300
	           });
	           return;
	       }
		var codeList=[];
		var batchIdList=[];
		var onlyWithOriginCertifiyUrlIdList=[];
		for(var i=0;i<rows.length;i++){
				 if (rows[i].$_state == ${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_AUDIT.getCode()} ){
					  batchIdList.push(rows[i].id);
					  codeList.push(rows[i].code)
					  if(rows[i].originCertifiyUrl=='有'&&rows[i].detectReportUrl=='无'){
						onlyWithOriginCertifiyUrlIdList.push(rows[i].code)
					  };
		         }
		}
		if(codeList.length==0){
			layer.alert('所选登记单不能进行审核')
			return;
		}
		let promise = new Promise((resolve, reject) => {
		  layer.confirm(codeList.join("<br\>"), {btn: ['确定', '取消'], title: "批量审核"},function () {
				resolve("yes");
		  },function(){
				resolve("cancel");
		  }
		  );
		});

	  let result = await promise; // wait until the promise resolves (*)
	  if(result=='yes'){
		  var passWithOriginCertifiyUrl=null;
		  if(onlyWithOriginCertifiyUrlIdList.length>0){
				let reconfirmPromise = new Promise((resolve, reject) => {
					  layer.confirm('只有产地证明登记单:<br/>'+onlyWithOriginCertifiyUrlIdList.join("<br\>"), {btn: ['不检测', '检测'], title: "是否不再进行检测"},function () {
							resolve(true);
					  },function(){
							resolve(false);
					  });
					});
				passWithOriginCertifiyUrl=await reconfirmPromise;
		  }
		  layer.closeAll();
		  $.ajax({
      		type: "POST",
              url: "${contextPath}/registerBill/doBatchAudit",
              processData:true,
              contentType:'application/json;charset=utf-8',
              data:JSON.stringify({registerBillIdList:batchIdList,pass:true,passWithOriginCertifiyUrl:passWithOriginCertifiyUrl}),
              dataType: "json",
              async : true,
              success: function (ret) {
                  if(ret.success){
                 	 var failureList=ret.data.failureList;
	              	 if(failureList.length==0){
	                       _registerBillGrid.datagrid("reload");
	                       TLOG.component.operateLog('登记单管理',"批量审核","【编号】:"+codeList.join(','));
	                       layer.alert('操作成功：</br>'+ret.data.successList.join('</br>'),{title:'操作',time : 3000});  
	
	              	 }else{
	              		 swal(
	                               '操作',
	                               '成功:'+ret.data.successList.join('</br>')+'失败:'+ret.data.failureList.join('</br>'),
	                               'info'
	                       );
	              	 }
                  }else{
                      swal(
                              '错误',
                              ret.result,
                              'error'
                      );
                  }
              },
              error: function(){
                  swal(
                          '错误',
                          '远程访问失败',
                          'error'
                  );
              }
          });
		  
	  }
	  

    }


    function autoCheck() {
        var selected = _registerBillGrid.datagrid("getSelected");
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
            title: "请确认是否主动送检？",
            type: 'question',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if(result.value){
            $.ajax({
                type: "GET",
                url: "${contextPath}/registerBill/autoCheck/"+ selected.id,
                processData:true,
                dataType: "json",
                async : true,
                success: function (ret) {
                    if(ret.success){
                        _registerBillGrid.datagrid("reload");
                        TLOG.component.operateLog('登记单管理',"主动送检","【编号】:"+selected.code);
                        layer.alert('操作成功',{title:'操作',time : 600});  
                    }else{
                        swal(
                                '错误',
                                ret.result,
                                'error'
                        );
                    }
                },
                error: function(){
                    swal(
                            '错误',
                            '远程访问失败',
                            'error'
                    );
                }
            });
        }
    });
    }
    
    function samplingCheck() {
        var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
        swal({
            title: "请确认是否采样检测？",
            type: 'question',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if(result.value){
            $.ajax({
                type: "GET",
                url: "${contextPath}/registerBill/samplingCheck/"+ selected.id,
                processData:true,
                dataType: "json",
                async : true,
                success: function (ret) {
                    if(ret.success){
                        _registerBillGrid.datagrid("reload");
                        TLOG.component.operateLog('登记单管理',"采样检测","【编号】:"+selected.code);
                        layer.alert('操作成功',{title:'操作',time : 600});  
                    }else{
                        swal(
                                '错误',
                                ret.result,
                                'error'
                        );
                    }
                },
                error: function(){
                    swal(
                            '错误',
                            '远程访问失败',
                            'error'
                    );
                }
            });
        }
    });
    }
    
    
    function batchAutoCheck(){
    	var rows=_registerBillGrid.datagrid("getSelections");
    	if (rows.length==0) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
        
    	var codeList=[];
        var batchIdList=[];
       	for(var i=0;i<rows.length;i++){
       		 if (rows[i].$_state == ${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_SAMPLE.getCode()} ){
       			  batchIdList.push(rows[i].id);
       			  codeList.push(rows[i].code)
                }
       	}
		if(codeList.length==0){
			layer.alert('所选登记单子不能主动送检')
			return;
		}
        layer.confirm(codeList.join("<br\>"), {btn: ['确定', '取消'], title: "批量主动送检"}, function () {
        	 $.ajax({
                 type: "POST",
                 url: "${contextPath}/registerBill/doBatchAutoCheck",
                 processData:true,
                 dataType: "json",
                 contentType:'application/json;charset=utf-8',
                 data:JSON.stringify(batchIdList),
                 async : true,
                 success: function (ret) {
                     if(ret.success){
                    	 var failureList=ret.data.failureList;
                    	 if(failureList.length==0){
                             _registerBillGrid.datagrid("reload");
                             TLOG.component.operateLog('登记单管理',"批量主动送检","【编号】:"+codeList.join(','));
                           layer.alert('操作成功',{title:'操作',time : 600});   
                               
                    	 }else{
                    		 swal(
                                     '操作',
                                     '成功:'+ret.data.successList.join('</br>')+'失败:'+ret.data.failureList.join('</br>'),
                                     'info'
                             );
                    	 }
                    	 
                    	 

                     }else{
                         swal(
                                 '错误',
                                 ret.result,
                                 'error'
                         );
                     }
                 },
                 error: function(){
                     swal(
                             '错误',
                             '远程访问失败',
                             'error'
                     );
                 }
             });
        	
        	
        	
        	
        })
    	
    }
    
    function batchSamplingCheck(){
    	var rows=_registerBillGrid.datagrid("getSelections");
    	if (rows.length==0) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
    	var codeList=[];
        var batchIdList=[];
       	for(var i=0;i<rows.length;i++){
       		 if (rows[i].$_state == ${@com.dili.trace.glossary.RegisterBillStateEnum.WAIT_SAMPLE.getCode()} ){
       			  batchIdList.push(rows[i].id);
       			  codeList.push(rows[i].code)
                }
       	}
		if(codeList.length==0){
			layer.alert('所选登记单子不能采样检测')
			return;
		}
        layer.confirm(codeList.join("<br\>"), {btn: ['确定', '取消'], title: "批量采样检测"}, function () {
        	$.ajax({
        		type: "POST",
                url: "${contextPath}/registerBill/doBatchSamplingCheck",
                processData:true,
                contentType:'application/json;charset=utf-8',
                data:JSON.stringify(batchIdList),
                dataType: "json",
                async : true,
                success: function (ret) {
                    if(ret.success){
                   	 var failureList=ret.data.failureList;
                	 if(failureList.length==0){
                         _registerBillGrid.datagrid("reload");
                         TLOG.component.operateLog('登记单管理',"批量采样检测","【编号】:"+codeList.join(','));
                         layer.alert('操作成功',{title:'操作',time : 600});  

                	 }else{
                		 swal(
                                 '操作',
                                 '成功:'+ret.data.successList.join('</br>')+'失败:'+ret.data.failureList.join('</br>'),
                                 'info'
                         );
                	 }
                    }else{
                        swal(
                                '错误',
                                ret.result,
                                'error'
                        );
                    }
                },
                error: function(){
                    swal(
                            '错误',
                            '远程访问失败',
                            'error'
                    );
                }
            });
        })
    	
    }
    function reviewCheck() {
        var selected = _registerBillGrid.datagrid("getSelected");
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
            title: "请确认是否复检？",
            type: 'question',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if(result.value){
            $.ajax({
                type: "GET",
                url: "${contextPath}/registerBill/reviewCheck/"+ selected.id,
                processData:true,
                dataType: "json",
                async : true,
                success: function (ret) {
                    if(ret.success){
                        _registerBillGrid.datagrid("reload");
                        TLOG.component.operateLog('登记单管理',"复检","【编号】:"+selected.code);
                        layer.alert('操作成功',{title:'操作',time : 600});  
                    }else{
                        swal(
                                '错误',
                                ret.result,
                                'error'
                        );
                    }
                },
                error: function(){
                    swal(
                            '错误',
                            '远程访问失败',
                            'error'
                    );
                }
            });
        }
    });
    }
    function undo() {
        var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
        swal({
            title: "请确认是否撤销？",
            type: 'question',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33'
        }).then((result) => {
            if(result.value){
            $.ajax({
                type: "GET",
                url: "${contextPath}/registerBill/undo/"+ selected.id,
                processData:true,
                dataType: "json",
                async : true,
                success: function (ret) {
                    if(ret.success){
                        _registerBillGrid.datagrid("reload");
                        TLOG.component.operateLog('登记单管理',"撤销","【编号】:"+selected.code
                        		+"<br/>【采样编号】:"+(typeof(selected.sampleCode)=='undefined'?'':selected.sampleCode)
                        		+"<br/>【理货区号】:"+(typeof(selected.tallyAreaNo)=='undefined'?'':selected.tallyAreaNo) 
                        		+"<br/>【业户姓名】:"+(typeof(selected.name)=='undefined'?'':selected.name)
                        		+"<br/>【身份证号】:"+(typeof(selected.idCardNo)=='undefined'?'':selected.idCardNo) 
                        		+"<br/>【业户手机号】:"+(typeof(selected.phone)=='undefined'?'':selected.phone) 
                        		+"<br/>【交易账号】:"+(typeof(selected.tradeAccount)=='undefined'?'':selected.tradeAccount) 
                        		+"<br/>【印刷卡号】:"+(typeof(selected.tradePrintingCard)=='undefined'?'':selected.tradePrintingCard)
                        		+"<br/>【车牌】:"+(typeof(selected.plate)=='undefined'?'':selected.plate) 
                        
                        );
                        layer.alert('操作成功',{title:'操作',time : 600});  
                    }else{
                        swal(
                                '错误',
                                ret.result,
                                'error'
                        );
                    }
                },
                error: function(){
                    swal(
                            '错误',
                            '远程访问失败',
                            'error'
                    );
                }
            });
        }
    });
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
    function doUploadOrigincertifiy(){
    	
    	var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
        openWin('/registerBill/uploadOrigincertifiy/' + selected.id);
    }
    function doUploadDetectReport(){
    	
    	var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
        openWin('/registerBill/uploadDetectReport/' + selected.id);
    }
    function doCreateCheckSheet(){
        var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
        
        var rows=_registerBillGrid.datagrid("getSelections");
        var registerBillIdList=rows.filter(function(v,i){return true;}).map(function(v,i){return v.id});
        openWin('/checkSheet/edit.html?' +$.param({registerBillIdList:registerBillIdList},true));
    }
   
    
    function openIframe(content,selected){
        layer.open({
            type: 2,
            title: "审核",
            shadeClose: true,
            shade: 0.3,
            offset: "20%",
            shadeClose : false,
            area: ['1100px', "350px"],
            content: content,//传入一个链接地址 比如：http://www.baidu.com
            btn: ['进场审核','取消'],
            yes: function(index, layero){
                $.ajax({
                    type: "GET",
                    url: "${contextPath}/registerBill/audit/"+ selected.id+"/true",
                    processData:true,
                    dataType: "json",
                    async : true,
                    success: function (ret) {
                        if(ret.success){
                            _registerBillGrid.datagrid("reload");
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
    }
    function openLayer(url){
        layer.open({
            area: ['600px', "400px"],
            title: '审核',
            content: [url, 'no'], //iframe的url
            btn: ['确定','取消'],
            yes: function(index, layero){
                //按钮【确认】的回调
                var body = layer.getChildFrame('body', index); //iframe的body获取方式
                var password = $(body).find("#password").val();
            }
        });
    }
    
    
  //打开处理结果窗口
    function doHandler(){
        var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
        $.each($(' .fileimg-box'),function(i,v){
 		   if(i>0){
 			   $(this).remove();
 		   }
 	   })
    	
        $('#dlg').dialog("setTitle","上传处理结果");
        $('#dlg').dialog('open');
        $('#dlg').dialog('center');
        $(".magnifying").hide();
        $(".fileimg-cover,.fileimg-edit").hide();
        $(":file").attr('disabled',false);
        $('#_form').form('clear');
        var formData = $.extend({},selected);
        formData = addKeyStartWith(getOriginalData(formData),"_");
        $('#_form').form('load', formData);
        if(selected.handleResultUrl){
            $.makeArray(selected.handleResultUrl.split(",")).filter(function(v,i){
              	return v!='';
              }).forEach (function(url,i){
            	 var fileInputObj=$('.fileimg-box .fileimg-edit:not(:visible):first').parents('.fileimg-box:first').find('input[type="file"]');
            	  afterUpload(url,fileInputObj);
              });
        }
        initFileUpload();
    }
    async function doRemoveReportAndCertifiy(){
    	var selected = _registerBillGrid.datagrid("getSelected");
        if (null == selected) {
            swal({
                title: '警告',
                text: '请选中一条数据',
                type: 'warning',
                width: 300
            });
            return;
        }
		let promise = new Promise((resolve, reject) => {
			  layer.confirm('请确认是否删除产地证明和报告？', {btn:['全部删除','删除产地证明','删除检测报告','取消'], title: "警告！！！",
					btn1:function(){
					  resolve("all");
					  return false;
					},
					btn2:function(){
						resolve("originCertifiy");
						return false;
					},
					btn3:function(){
						resolve("detectReport");
						 return false;
					},
					btn4:function(){
						resolve("cancel");
						 return false;
					}
				  });
			  $('.layui-layer').width('460px');
			});
		  let result = await promise; // wait until the promise resolves (*) 
		
		  if(result!='cancel'){
			  var _url = "${contextPath}/registerBill/doRemoveReportAndCertifiy.action";
		        $.ajax({
		            type: "POST",
		            url: _url,
		            data: {id:selected.id,deleteType:result},
		            processData:true,
		            dataType: "json",
		            async : true,
		            success: function (data) {
		                if(data.code=="200"){
		                	TLOG.component.operateLog('登记单管理',"删除产地证明和报告",'【ID】:'+selected.id);
		               	
	                         layer.alert('操作成功',{
	 								title:'操作',
		                           	time : 600,
		                           	end :function(){
		                           		 layer.closeAll();
		                           		queryRegisterBillGrid();
		                           	}
		                          },
	                         	 function () {
	                         	  layer.closeAll();
	                         	 queryRegisterBillGrid();
	                                 }
	                         );

		                }else{
		                	  layer.closeAll();
		                    swal('错误',data.result, 'error');
		                }
		            },
		            error: function(){
		            	  layer.closeAll();
		                swal('错误', '远程访问失败', 'error');
		            }
		        });
		  }else{
			  layer.closeAll();
		  }
		 
		 
    }

    function saveOrUpdateHandleResult(){
        if(!$('#_form').form("validate")){
            return;
        }
       var _formData = removeKeyStartWith($("#_form").serializeObject(),"_");
       var  handleResultUrl=$.makeArray(_formData.resultUrl).filter(function(v,i){
        	return v!='';
        }).join(',');
       
       delete _formData.resultUrl
       _formData.handleResultUrl=handleResultUrl;
       if(handleResultUrl==''){
    	   layer.alert('请上传处理结果图片');
    	   return;
       }
    
        var _url = "${contextPath}/registerBill/saveHandleResult.action";
        $.ajax({
            type: "POST",
            url: _url,
            data: _formData,
            processData:true,
            dataType: "json",
            async : true,
            success: function (data) {
                if(data.code=="200"){
                	TLOG.component.operateLog('登记单管理',"上传处理结果",'【图片ID】:'+_formData.handleResultUrl);
               	 $('#dlg').dialog('close');	 
                    layer.alert('处理成功',function(){
                   	 layer.closeAll();
                   	 $('#handle-btn').hide();
                   	 queryRegisterBillGrid();
                    })
                    
                }else{
                    swal('错误',data.result, 'error');
                }
            },
            error: function(){
                swal('错误', '远程访问失败', 'error');
            }
        });
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
        	$('<input type="file" name="file"  data-url="${contextPath!}/action/imageApi/upload" multiple="multipart/form-data" />').insertAfter(newImgBox.find('.fileimg-des'))
        	newImgBox.appendTo(td);
        	initFileInput(newImgBox.find('input[type="file"]'))
        }
    }
    
    function initFileInput(jqueryObj){
    	jqueryObj.fileupload({
            dataType : 'json',
            formData: {type:5,compress:true},
            done : function(e, res) {
                if (res.result.code == 200) {
                	var url = res.result.data;
                	var fileInputObj=$(e.target);
                	afterUpload(url,fileInputObj);
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
