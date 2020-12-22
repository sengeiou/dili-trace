package com.dili.trace.rpc.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.ReqParam;
import com.dili.ss.retrofitful.annotation.Restful;

@Restful("${uid.contextPath}")
public interface UidRestfulRpc {

    /**
     * 根据业务类型获取业务号
     * @param type
     * @return
     */
    @POST("/api/bizNumber")
    BaseOutput<String> bizNumber(@ReqParam(value = "type") String type);

}