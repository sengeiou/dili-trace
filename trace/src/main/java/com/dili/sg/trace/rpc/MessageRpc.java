package com.dili.sg.trace.rpc;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/11/13 11:36
 */
@Restful("${message.contextPath}")
public interface MessageRpc {

    /**
     * 发送注册验证码短信
     * @param obj
     * @return
     */
    @POST("/messageApi/receiveMessage.api")
    public BaseOutput sendVerificationCodeMsg(@VOBody JSONObject obj);
}
