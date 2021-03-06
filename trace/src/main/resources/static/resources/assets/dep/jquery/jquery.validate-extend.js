$.extend($.validator.messages, {
    required: "必填",
    remote: "请修正此字段",
    email: "请输入有效的电子邮件地址",
    url: "请输入有效的网址",
    date: "请输入有效的日期",
    dateISO: "请输入有效的日期 (YYYY-MM-DD)",
    number: "请输入有效的数字",
    digits: "只能输入数字",
    creditcard: "请输入有效的信用卡号码",
    equalTo: "你的输入不相同",
    extension: "请输入有效的后缀",
    maxlength: $.validator.format("最多输入{0}个字符"),
    minlength: $.validator.format("最少输入{0}个字符"),
    rangelength: $.validator.format("长度{0}到{1}之间"),
    range: $.validator.format("请输{0}到{1}之间数"),
    max: $.validator.format("不大于{0}"),
    min: $.validator.format("不小于{0}"),
    firstname: "firstname"
}), $.validator.setDefaults({
    focusInvalid: !0,
    focusCleanup: !1,
    noSpace: !0
}), jQuery.validator.addMethod("isMobile", function (t, a) {
    var d = t.length;
    return this.optional(a) || 11 == d && /^1[3-9]\d{9}$/.test(t)
}, "请填写正确的手机号码"), jQuery.validator.addMethod("isRealName", function (t, a) {
    return this.optional(a) || /^[\u4E00-\u9FA5+·]{2,10}$/.test(t) && !/^[\u4E00-\u9FA5+·]{2,10}[·]$/.test(t) && !/^[·][\u4E00-\u9FA5+.]{2,10}$/.test(t)
}, "请填写正确的名字"), jQuery.validator.addMethod("isLetter", function (t, a) {
    return this.optional(a) || /^[a-z]+$/.test(t)
}, "请填写字母"), jQuery.validator.addMethod("isRMB", function (t, a) {
    return this.optional(a) || /^(\d{1,3}(\,\d{3})*|(\d+))(\.\d{1,2})?$/.test(t)
}, "请输入正确金额"), jQuery.validator.addMethod("isRMB1", function (t, a) {
    return this.optional(a) || /^[1-9]{1,3}(,\d{3})*\.\d{2}$/.test(t)
}, "金额格式: 12,234.70"), jQuery.validator.addMethod("isRMB2", function (t, a) {
    return this.optional(a) || /^[1-9]{1,3}(,\d{3})*\.\d{1,2}?$/.test(t)
}, "逗号分隔最多两位小数"), jQuery.validator.addMethod("isRMB3", function (t, a) {
    return this.optional(a) || /^(\d{1,9})(\.\d{1,2})?$/.test(t)
}, "请输入正确金额不带逗号"), jQuery.validator.addMethod("isIntRMB", function (t, a) {
    return this.optional(a) || /^(\d{1,3}(\,\d{3})*|(\d+))(\.00)?$/.test(t)
}, "金额需为整数"), jQuery.validator.addMethod("isInt", function (t, a) {
    return this.optional(a) || /^\d+$/.test(t)
}, "请输入整数"), jQuery.validator.addMethod("isNaturalNum", function (t, a) {
    return this.optional(a) || /^(0|[1-9][0-9]*)$/.test(t)
}, "不小于0的整数"), jQuery.validator.addMethod("maxCurrency", function (t, a, d) {
    var e = d, o = t.replace(/,/g, "");
    return this.optional(a) || !(o > e)
}, "不大于{0}"), jQuery.validator.addMethod("minCurrency", function (t, a, d) {
    var e = d, o = t.replace(/,/g, "");
    return this.optional(a) || !(o < e)
}, "不小于{0}"), jQuery.validator.addMethod("floatADecimal", function (t, a) {
    var d = /^(([1-9]\d+)|\d)(\.\d)?$/.test(t);
    return this.optional(a) || d
}, "最多一位小数"), jQuery.validator.addMethod("floatReserve", function (t, a) {
    var d = /^(([1-9]\d+)|\d)(\.\d{1,2})?$/.test(t);
    return this.optional(a) || d
}, "最多两位小数"), jQuery.validator.addMethod("floaFour", function (t, a) {
    var d = /^(([1-9]\d+)|\d)(\.\d{1,4})?$/.test(t);
    return this.optional(a) || d
}, "最多四位小数"), jQuery.validator.addMethod("floatFive", function (t, a) {
    var d = /^(([1-9]\d+)|\d)(\.\d{1,5})?$/.test(t);
    return this.optional(a) || d
}, "最多五位小数"), jQuery.validator.addMethod("isIdCard0", function (t, a) {
    return this.optional(a) || /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/.test(t)
}, "请填写正确身份证号"), jQuery.validator.addMethod("isIdCard", function (t, a) {
    return this.optional(a) || /^[0-9a-zA-Z()]{1,18}$/.test(t)
}, "请填写正确身份证号"), jQuery.validator.addMethod("isWord", function (t, a) {
    return this.optional(a) || /^[a-zA-Z0-9-\u4e00-\u9fa5]+$/.test(t)
}, "仅限数字字母汉字"), jQuery.validator.addMethod("isChinese", function (t, a) {
    return this.optional(a) || /^[\u4e00-\u9fa5]+$/.test(t)
}, "仅限汉字"), jQuery.validator.addMethod("standardTime", function (t, a) {
    return this.optional(a) || /^((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) ([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$/.test(t)
}, "格式(YYYY-MM-DD hh:ii:ss)"), jQuery.validator.addMethod("standardDate", function (t, a) {
    return this.optional(a) || /^((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$/.test(t)
}, "格式(YYYY-MM-DD)"), jQuery.validator.addMethod("isIP", function (t, a) {
    return this.optional(a) || /^\d{1,3}\.\d{1,3}\.\d{1,3}$/.test(t)
}, "请输入正确IP地址"), jQuery.validator.addMethod("isSelected", function (t, a) {
    return this.optional(a) || $(a).siblings('input').val()?true:false
}, "请选择下拉框选项");
//# sourceMappingURL=../../../maps/static/dep/jquery/jquery.validate-extend.js.map
