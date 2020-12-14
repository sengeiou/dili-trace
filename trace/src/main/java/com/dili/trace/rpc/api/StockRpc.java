package com.dili.trace.rpc.api;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import org.springframework.web.bind.annotation.PathVariable;

@Restful("${message.contextPath}")
public interface StockRpc {


    /**
     * 发送注册验证码短信
     * @param obj
     * @return
     */
    @POST("/messageApi/receiveMessage.api")
    public BaseOutput sendVerificationCodeMsg(@VOBody JSONObject obj);
    /**
     * 发送注册验证码短信
     * @param obj
     * @return
     */
    @POST("/message/subscribe/send?access_token={accessToken}")
    public Object uniformSend(@PathVariable(name="accessToken") String accessToken,
                              @VOBody JSONObject obj);
}
