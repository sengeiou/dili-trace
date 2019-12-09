/**
 * 日志内容构建器
 * Created by mengxiaofei on 2017/4/11.
 */
TLOG = window.TLOG || {};

(function (tl) {
    tl.operates = {
        add: "新增",
        del: "删除",
        edit: "修改",
        editData: "数据修改",
        download: "下载",
        copy: "复制",
        enable: "启用",
        disable: "禁用",
        submit: "提交"
    };


    tl.component = {
        model: "",              //模块
        contentArr: [],         //日志内容数组
        content: "",           //日志内容
        array_old_title: [],    //修改之前对应的title
        array_old_value: [],    //修改之前的值
        array_new_title: [],    //修改之后对应的title
        array_new_value: [],    //修改之后的值
        NEWTYPE: 1,             //修改之前
        OLDTYPE: 2,             //修改之后
        operate: "",            //操作类型
        targetId: "",           //目标ID
        clear : function(){
            window.localStorage.clear();
            tl.component.array_old_title = [];
            tl.component.array_old_value = [];
            tl.component.array_new_title = [];
            tl.component.array_new_value = [];
            tl.component.contentArr = [];
        },
        buildArray: function (type, _this) {
            var checkboxValue = [];
            tl.component.targetId = $(_this).attr("targetId");
            $(_this).find("*[tlog]").each(function () {
                var _tagName = this.tagName;
                if (_tagName == "LABEL") {
                    if (type == tl.component.OLDTYPE)
                        tl.component.array_old_title.push(this.innerHTML);
                    else
                        tl.component.array_new_title.push(this.innerHTML);
                } else if (_tagName == "INPUT" || _tagName == "SELECT" || _tagName == "TEXTAREA") {
                    if (_tagName == "INPUT") {
                        if($(this).attr("type") == "checkbox") {
                            if(checkboxValue.length == 0) {
                                $(this).parents("div[tlogParent]").find("*[tlogValue]:checked").each(function () {

                                    checkboxValue.push($(this).attr("tlogValue"));
                                });
                                if (type == tl.component.OLDTYPE)
                                    tl.component.array_old_value.push(checkboxValue.join(","));
                                else
                                    tl.component.array_new_value.push(checkboxValue.join(","));
                            }
                        } else {
                            if (type == tl.component.OLDTYPE)
                                tl.component.array_old_value.push(this.value);
                            else
                                tl.component.array_new_value.push(this.value);
                        }
                    } else if (_tagName == "SELECT") {
                        if (type == tl.component.OLDTYPE) {
                            if(this.options[this.selectedIndex].value == "")
                                if($(this).attr("selectVal") != undefined)
                                    tl.component.array_old_value.push($(this).find('option[value='+$(this).attr("selectVal")+']')[0].text);
                                else
                                    tl.component.array_old_value.push($(this).find('option')[0].text);
                            else
                                tl.component.array_old_value.push(this.options[this.selectedIndex].text);
                        }else {
                            tl.component.array_new_value.push(this.options[this.selectedIndex].text);
                        }
                    } else {
                        if (type == tl.component.OLDTYPE)
                            tl.component.array_old_value.push(this.value);
                        else
                            tl.component.array_new_value.push(this.value);
                    }
                }
            });
        },
        editNewData: function (_this) {
            try {
                tl.component.buildArray(tl.component.NEWTYPE, _this);
                tl.component.buildLogContent();
                tl.component.sentLog();
            } catch (e){
                console.log("获取修改新数据异常！");
            }
        },
        editOldData: function (_this, model) {
            tl.component.clear();
            try {
                tl.component.buildArray(tl.component.OLDTYPE, _this);
                tl.component.operate = tl.operates.editData;
                tl.component.model = model;
                window.localStorage.setItem("log_old_tl",JSON.stringify(tl));
            } catch (e) {
                console.log("获取修改老数据异常！");
            }
        },
        //只针对进门收费[更正]操作
        correctOldData: function (_this, model) {
            tl.component.clear();
            try {
                tl.component.buildArray(tl.component.OLDTYPE, _this);
                tl.component.operate = tl.operates.correct;
                tl.component.model = model;
                window.localStorage.setItem("log_old_tl",JSON.stringify(tl));
            } catch (e) {
                console.log("获更正改老数据异常！");
            }
        },

        buildLogContent: function () {
            var old_tl = JSON.parse(window.localStorage.getItem("log_old_tl"));
            var eachObj = [];
            if(old_tl.component.array_old_title.length > tl.component.array_new_title.length) {
                eachObj = old_tl.component.array_old_title;
                $.each(eachObj, function(i){
                    if(i > tl.component.array_new_title.length){
                        tl.component.array_new_title[i] = old_tl.component.array_old_title[i];
                        tl.component.array_new_value[i] = "";
                    }
                });
            } else if(old_tl.component.array_old_title.length < tl.component.array_new_title.length){
                eachObj = tl.component.array_new_title;
                $.each(eachObj, function(i){
                    if(i > old_tl.component.array_old_title.length){
                        old_tl.component.array_old_title[i] = tl.component.array_new_title[i];
                        old_tl.component.array_old_value[i] = "";
                    }
                });
            } else {
                eachObj = old_tl.component.array_old_title;
            }
            $.each(eachObj, function (i, v) {
                if(old_tl.component.array_old_value[i]!= tl.component.array_new_value[i]){
                    tl.component.contentArr.push("【");
                    tl.component.contentArr.push(v);
                    tl.component.contentArr.push("】：从‘");
                    tl.component.contentArr.push(old_tl.component.array_old_value[i]);
                    tl.component.contentArr.push("’修改为‘");
                    tl.component.contentArr.push(tl.component.array_new_value[i]);
                    tl.component.contentArr.push("’\n");
                }
            });
            tl.component.contentArr.push("【目标ID】：");
            tl.component.contentArr.push(old_tl.component.targetId);
            tl.component.content = tl.component.contentArr.join("");
            tl.component.model = old_tl.component.model;
            tl.component.operate = old_tl.component.operate;
        },

        operateLog: function (operate, model, targetId) {
            try {
                tl.component.operate = operate;
                tl.component.model = model;
                tl.component.content = targetId;
                tl.component.sentLog();
            } catch (e) {
                console.log(tl.component.operate + "操作日志异常！");
            }
        },

        sentLog: function () {
            var url = 'http://toll.nong12.com/log/save';
            try {
                url = logUrl;
            } catch (e){
                console.warn("日志路径可能有误, 请调整!");
            }
            $.ajax({
                url : url,
                xhrFields:{withCredentials: true},
                dataType : 'json',
                data : {model : tl.component.model, operate : tl.component.operate, content : tl.component.content},
                type : 'post',
                async : false,
                success : function(data){

                }
            });
            if(tl.operates.editData == tl.component.operate){
                window.localStorage.clear();
            }
        }
    };
})(TLOG);