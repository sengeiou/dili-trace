package com.dili.trace.rpc;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/11/13 11:36
 */
@Restful("https://api.weixin.qq.com/cgi-bin")
public interface WxRpc {

    /**
     * 发送注册验证码短信
     * @param obj
     * @return
     */
    @POST("/message/subscribe/send?access_token={accessToken}")
    public Object uniformSend(@PathVariable(name="accessToken") String accessToken,
                                              @VOBody JSONObject obj);
}
