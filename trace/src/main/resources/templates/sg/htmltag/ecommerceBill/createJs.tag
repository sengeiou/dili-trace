<script type="text/javascript">

		
    let seperateItemCount = 0;
    $(function () {
       $('.upload-div input[type="file"]').each(function(){
    	   initFileUpload($(this));   
       })
        initAutoComplete('#originName','${contextPath}/toll/city');
       initAutoComplete('#productName','${contextPath}/toll/category');
       // initAutoComplete('#originName','/toll/city');
    });

    function returnBack(){
        history.go(-1);
    }


    /* 货品表格 */
    $('.main-container').on('click', '#addSeperateItemBtn', function () {
    	var index=seperateItemCount;
        $('#seperateTable tbody').append(template('seperateItem', {index: index}));
        tallyAreaNoAutoComplete('#tallyAreaNo_'+index,'${contextPath}/user/queryByTallyAreaNo.action');
        seperateItemCount++;
    });


    $('.main-container').on('click', '.split-minus-btn', function () {
        if ($('#seperateTable tr').length > 2) {
            $(this).closest('tr').remove();
        }
    })


    /* 选择图片 */
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
                area: ['90%', '90%'], // 宽高
                content: '<p style="text-align:center"><img src="' + url + '" alt="" class="show-image-zoom"></p>'
            });
        }
    })


    function appendCachedPlate(userplateList,editableSelector){
    	
    	   var cachedPlateArray=getCachedPlateArray()

           var notInUserPlateList=$.grep(cachedPlateArray, function(v,i){
           	return $.inArray(v,userplateList)==-1;
           });
           if(notInUserPlateList.length>0){
           	$(editableSelector).editableSelect('add', function () {
                   $(this).val('&#x2500;&#x2500;&#x2500;&#x2500;&#x2500;&#x2500;&#x2500;&#x2500;&#x2500;&#x2500;&#x2500;&#x2500;&#x2500;&#x2500;');
                   $(this).attr('disabled','disabled')
                   $(this).text('---------------');
               });// 调用add方法 通过函数的方式绑定上val和txt
               
               $.each(notInUserPlateList, function (i, v) {
               	$(editableSelector).editableSelect('add', function () {
                       $(this).val(v);
                       $(this).text(v);
                   });// 调用add方法 通过函数的方式绑定上val和txt
              });
           	
           }
    	
    }
    function findUserPlateByUserId(userId,editableSelector){
        $.ajax({
            type: 'post',
            url: '${contextPath}/trade/customer/findUserPlateByUserId',
            data:{userId:userId},
            dataType: 'json',
            async: false,
            success: function (ret) {
                if (ret.code == "200") {
                	// $('#plate').empty();
                	 var userplateList = $.map(ret.data,function(v,i){
                		 return v.plate;
                	 });
                	$(editableSelector).editableSelect('clear');// 清空现有数据
                    $.each(userplateList, function (i, v) {
                    	$(editableSelector).editableSelect('add', function () {
                            $(this).val(v);
                            $(this).text(v);
                        });// 调用add方法 通过函数的方式绑定上val和txt
                   });
                   appendCachedPlate(userplateList,editableSelector);
                    
                  
                } else {
                	$(editableSelector).editableSelect('clear');
                }
            },
            error:function(){
            	$(editableSelector).editableSelect('clear');
            }
        });
    }

    function initAutoComplete(selector,url){
    	$(selector).keydown(function (e){
    	    if(e.keyCode == 13){
    	    	// $(selector).data('keycode',e.keyCode);
    	    	// console.info('keydown')
    	    }
    	});
    	$(selector).data('oldvalue','');
    	// $(selector).data('keycode','');
        $(selector).on('change',function () {
        	var oldvalue=$(selector).data('oldvalue');
        	var val=$(this).val();
        	if(oldvalue!=val){
        		$(this).siblings('input').val('');
        	}
        });
        // 产地联系输入
        $(selector).devbridgeAutocomplete({
            noCache: 1,
            serviceUrl: url,  // 数据地址
            // lookup: countries, 本地测试模拟数据使用结合上面的var countries
            dataType: 'json',
            onSearchComplete: function (query, suggestions) {
            	// console.info(2)
            },
            showNoSuggestionNotice: true,
            noSuggestionNotice: "不存在，请重输！",
            autoSelectFirst:true,
            autoFocus: true,
            onSelect: function (suggestion) {
            	console.info('onSelect')
                var self = this;
                var idField = $(self).siblings('input');
                idField.val(suggestion.id);
                $(self).val(suggestion.value.trim());
                $(selector).data('oldvalue',suggestion.value);
                var v=$(self).valid();
            }
        });
    }
    
    /**
	 * 初始化自动完成框
	 */
    function tallyAreaNoAutoComplete(selector,url){
    	$(selector).keyup(function (e){
    		var oldvalue=$(selector).data('oldvalue');
        	var val=$(this).val();
        	if(oldvalue!=val){
        		$(this).parents('tr').find('input[name*=salesUserName]').val('')
            	$(this).parents('tr').find('input[name*=salesPlate]').val('')
        	}
    	});
    	$(selector).data('oldvalue','');
    	// $(selector).data('keycode','');
        $(selector).on('change',function () {
        	var oldvalue=$(selector).data('oldvalue');
        	var val=$(this).val();

        	if(oldvalue!=val){

        		$(this).parents('tr').find('input[name*=salesUserName]').val('')
            	$(this).parents('tr').find('input[name*=salesPlate]').val('')
            
        	}
        });
        // 产地联系输入
        $(selector).devbridgeAutocomplete({
            noCache: 1,
            serviceUrl: url,  // 数据地址
            // lookup: countries, 本地测试模拟数据使用结合上面的var countries
            dataType: 'json',
            onSearchComplete: function (query, suggestions) {
            	
            	// console.info(2)
            },
            formatResult: function (suggestion, currentValue) {
            	return suggestion.tallyAreaNo+"|"+suggestion.userName+"|"+suggestion.plate
            },
            transformResult: function(response, originalQuery){
            	var suggestions=[]
            	if(response.code=='200'){
            		$.each(response.data,function(){
            			suggestions.push({"id":this.tallyAreaNo,"value":this.tallyAreaNo,"plate":this.plate,"userName":this.userName,"tallyAreaNo":this.tallyAreaNo})
            		})
            	}
            	return {suggestions:suggestions}
            },
            showNoSuggestionNotice: true,
            noSuggestionNotice: "不存在，请重输！",
            autoSelectFirst:true,
            autoFocus: true,
            onSelect: function (suggestion) {
                var self = this;
            	var plate=suggestion.plate;
            	var userName=suggestion.userName;
            	var tallyAreaNo=suggestion.tallyAreaNo;
      
            	$(self).parents('tr').find('input[name*=salesUserName]').val(userName)
            	$(self).parents('tr').find('input[name*=salesPlate]').val(plate)
            
            	var idField = $(self).siblings('input');
            	idField.val(suggestion.id);
                $(self).val(suggestion.value.trim());
                debugger
                $(selector).data('oldvalue',suggestion.value);
                //var v=$(self).valid();
            }
        });
    }

    
    jQuery.validator.addMethod("isPlate", function(value, element) { 
        return this.optional(element) || isLicensePlate(value.toUpperCase());
    }, "请输入正确格式的车牌");  
    
    
 // 正则验证车牌,验证通过返回true,不通过返回false
    function isLicensePlate(str) {
    	return /^([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4,5}[A-HJ-NP-Z0-9挂学警港澳使领])$/.test(str);
    }
   

  	function buildTableData(){
  		var formData=$('#createRecordForm').serializeForm();
  		return formData;
  	}
  	function cachePlate(plate){
  		if(jQuery.type(plate) === "string"&&plate.trim().length>0){
  	  		var plateArray=getCachedPlateArray();
  	  		if(jQuery.inArray(plate.trim(), plateArray )==-1){
  	  	  		plateArray.push(plate.trim());
  	  	  		localStorage.setItem('plateArray',JSON.stringify(plateArray));
  	  		}
  		}

  	}
  	function getCachedPlateArray(){
  		var plateArray=localStorage.getItem('plateArray');
  		if(typeof(plateArray)!='undefined'&&plateArray!=null){
  			return $.makeArray(JSON.parse(plateArray));
  		}else{
  			localStorage.setItem('plateArray',JSON.stringify([]));
  		}
  		return []
  	}
  	function cacheInputtedPlate(editableSelector){
  		var inList=false;
  		var plate=$(editableSelector).val();
  		
  		$(editableSelector).siblings(".es-list" ).find('li').each(function(){
  			if(plate==$(this).text()){
  				inList=true;
  				return false;
  			}
  		})
  		
  		if(inList==false){
  			cachePlate(plate);
  		}
  		
  	}
    var resubmit = 0;
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
        
        var data = buildTableData();
        $.ajax({
            type: "POST",
            url: "${contextPath}/ecommerceBill/insert.action",
            data :  JSON.stringify(data),
            dataType: "json",
            async : true,
            contentType: "application/json; charset=utf-8",
            success: function (ret) {
                if(ret.success){
                    var paramStr = JSON.stringify(ret.data);
                          cacheInputtedPlate("#plate");
                          layer.alert('登记成功',{
                           	type:0,
                           	time : 600,
                           	end :function(){
                           		parent.closeWin('view_win');
                           		
                           	}
                          },function () {
                                    parent.closeWin('view_win');
                                 }
                          );
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



    $( document ).on( "click", ".img-view-a",function () {
        var url =  $(this).parent().siblings('.upload-div').find('input[type="hidden"]').val()
        debugger
        if(url){
            layer.open({
                title:'图片',
                type: 1,
                skin: 'layui-layer-rim',
                closeBtn: 2,
                area: ['90%', '90%'], // 宽高
                content: '<p style="text-align:center"><img src="' + url + '" alt="" class="show-image-zoom"></p>'
            });
        }
    });
    $('.img-del-btn').click(function(){
    	
	   	 $(this).parent().siblings('.upload-div').show()
	   	 $(this).parent().hide();
	   	 $(this).parent().siblings('.upload-div').find("input:hidden").val('');
	   	 $(this).parent().siblings('.upload-div').find('.magnifying').attr('src', '').hide();
	   	 $(this).parent().siblings('.upload-div').find('.fileimg-cover,.fileimg-edit').hide();
   	
   })

    // 文件上传组件初始化
    function initFileUpload(selecter) {
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
            add: function (e, data) {// 判断文件类型 var acceptFileTypes =
										// /\/(pdf|xml)$/i;
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
    function selectCity(cthis,id,mergeName){
    	$('#originName').each(function(k,v){
    		// if($(this).val()==''){
    			$(this).val(mergeName);
    			$(this).siblings('input:hidden').val(id)
    		// }
    	});
    }
</script>
