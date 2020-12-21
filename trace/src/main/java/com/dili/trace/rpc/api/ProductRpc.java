package com.dili.trace.rpc.api;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.trace.rpc.dto.StockReduceRequestDto;
import com.dili.trace.rpc.dto.StockReductResultDto;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 分布式文件系统rpc
 *
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/8/17 18:37
 */
@Restful("${product-service.contextPath}")
public interface ProductRpc {


    /**
     * 发送注册验证码短信
     *
     * @param obj
     * @return
     */
    @POST("/api/stock/reduceByStockIds")
    public BaseOutput<StockReductResultDto> sendVerificationCodeMsg(@VOBody StockReduceRequestDto obj);
}