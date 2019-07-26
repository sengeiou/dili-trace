//说明：时间格式化输出
//调用：var d = new Date();d.format('yyyy-MM-dd hh:mm:ss')
//返回值：fmt格式字符串
Date.prototype.format = function(fmt) {
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt)) {
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
}


//说明：计算时间差
//调用：Date.dateDiff('2019-04-23 23:59:59','2018-04-23 23:59:59','year')
//返回值：type 数量
Date.dateDiff = function(dstr1, dstr2, type) {
    var value
    switch (type) {
        case 'year':
            value = 365 * 24 * 60 * 60 * 1000 // 计算差多少年
            break;
        case 'month':
            value = 30*24 * 60 * 60 * 1000 // 计算差多少月
            break;
        case 'day':
            value = 24 * 60 * 60 * 1000 // 计算差多少天
            break;
        case 'hours':
            value = 24 * 60 * 60 * 1000 // 计算差多少小时
            break;
        default:
            return false
    }
    try {
        var d1 = new Date(dstr1);
        var d2 = new Date(dstr2);
        return Math.abs((d1.getTime() - d2.getTime())/value);
    } catch (e) {
        return false
    }
}