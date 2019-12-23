var UAP_TOOLS = {
    getNowStr: function () {
        var now = new Date();
        return UAP_TOOLS.formatDateToString(now);
    },
    formatDateToString: function (dateObj) {
        var year = dateObj.getFullYear();
        var month = dateObj.getMonth()+1 ;
        var date = dateObj.getDate();

        var hour= dateObj.getHours();
        var minute = dateObj.getMinutes();
        var second = dateObj.getSeconds();

        var yearStr=year;
        var monthStr=month>= 10? month : '0' +month;
        var dateStr = date >= 10 ? date : '0' + date;

        var hourStr =hour >= 10 ? hour : '0' + hour;
        var minuteStr = minute >= 10 ? minute : '0' + minute;
        var secondStr = second >= 10 ? second : '0' +second;
        return yearStr + "-" + monthStr + "-" + dateStr + " " + hourStr + ":" + minuteStr + ":" + secondStr;
    }
};
$.fn.tagbox.defaults.missingMessage = '该输入项为必输项';
// 正则验证车牌,验证通过返回true,不通过返回false
function isLicensePlate(str) {
	return /^([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4,5}[A-HJ-NP-Z0-9挂学警港澳使领])$/.test(str);
}

$(function () {
    //去掉输入框中的前后空格
    $(document).on('change', 'input[type=text], textarea', function () {
        $(this).val($.trim($(this).val()));
    });
    //过滤某些特殊字符，不能被输入
    $(document).on('keydown', 'input[type=text], textarea', function (e) {
        var isContainsSpecialChar = /[(\`)(\~)(\^)(\<)(\>)(\$)(\—)]+/g;
        if (isContainsSpecialChar.test(e.key)) {
            e.preventDefault();
        }
    });


    /**
     * 扩展easyui的验证框架，可加入一些自定义的验证
     */
    $.extend($.fn.validatebox.defaults.rules, {
        //验证手机号规则
        phoneNum: {
            validator: function(value, param){
                return /^1[3-9]\d{9}$/.test(value);
            },
            message: '请输入正确的手机号码。'
        },
        //验证只能是中文汉字
        isChinese:{
            validator:function (value,param) {
                return  /^[\u4e00-\u9fa5]+$/.test(value);
            },
            message:'只能输入中文汉字'
        },
        //字符验证，只能包含中文、英文、数字、下划线等字符。
        isWord:{
            validator:function (value,param) {
                return /^[a-zA-Z0-9\u4e00-\u9fa5_]+$/.test(value);
            },
            message:'只能包含中文、英文、数字、下划线'
        },
        //验证银行卡号规则：目前只要求是数字，且长度在12-22位之间即可
        bankCard: {
            validator: function(value, param){
                return /^[1-9]\d{11,21}$/.test(value);
            },
            message: '请输入正确的卡号。'
        },
        //混合模式的联系电话验证，只验证位数，手机号或带区号的固定电话，多个电话请用;隔开
        mixtureTel: {
            validator: function(value, param){
                return /^\d{11}([\;]+\d{11})*$/.test(value);
            },
            message: '请输入正确的联系电话。'
        },
        //正整数
        integer:{
            validator:function (value, param) {
                return /^[+]?[1-9]\d*$/.test(value);
            },
            message: '请输入最小为1的整数'
        },
        //数字
        number:{
            validator:function (value, param) {
                return /^[0-9]*$/.test(value);
            },
            message: '请输入数字'
        },
        //身份证号
        cardNo:{
            validator:function (value, param) {
                return /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(value);
            },
            message: '请输入正确的身份证号'
        },
        //验证下拉框是否使用下拉选项值
        comboBoxEditvalid: {
            validator: function (value, param) {
                var $combobox = $("#" + param[0]);
                if (value) {
                    if ($combobox.combobox('getValue').trim() == $combobox.combobox('getText').trim())
                        return false;
                    return true;
                }
                return false;

            },
            message: '请选择下拉框选项'
        },
        onlyLength: {
            validator: function(value, param){

                return value.length == param[0];
            },
            message: '请输入{0}位'
        },
        uniquetag: {
            validator: function(value, param){
                var newValues = value.split(',');
                var valid = {};
                for (let val of newValues){
                    if(!valid[val]){
                        valid[val] = val;
                    }else{
                        return false;
                    }
                }
                return true;
            },
            message: '存在重复的值。'
        },
        tagLength: {
            validator: function(value, param){
                var newValues = value.split(',');
                var valid = {};
                for (let val of newValues){
                    if(val.length >0 && val.length != param[0]){
                        return false;
                    }
                }
                return true;
            },
            message: '请输入{0}位'
        },
        tagLengthRange: {
            validator: function(value, param){
                var newValues = value.split(',');
                var valid = {};
                var min=param[0];
                var max=param[1];
                for (let val of newValues){
                	var len=val.length;
                    if(len >0 ){
                    	if(len<min||len>max){
                    		return false;	
                    	}
                    }
                }
                return true;
            },
            message: '请输入{0}-{1}位'
        },
        isLicensePlate: {
            validator: function(value, param){
                var newValues = value.split(',');
                var valid = {};
                for (let val of newValues){
                	var len=val.length;
                	if(len >0 ){
                    	var checkValue=isLicensePlate(val.toUpperCase());
                    	if(checkValue==false){
                    		return checkValue;
                    	}
                	}

                }
                return true;
            },
            message: '请输入正确格式的车牌'
        },
        tagNum: {
            validator: function(value, param){
                var newValues = value.split(',');
                var valid = {};
                for (let val of newValues){
                    if(val.length >0 && !/^[0-9]*$/.test(val)){
                        return false;
                    }
                }
                return true;
            },
            message: '请输入数字'
        }
    });
});