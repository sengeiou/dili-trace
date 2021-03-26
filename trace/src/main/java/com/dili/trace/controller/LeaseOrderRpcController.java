package com.dili.trace.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.rpc.dto.AssetsParamsDto;
import com.dili.trace.rpc.service.LeaseOrderRpcService;
import com.dili.trace.service.UapRpcService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 调用rpc，查询可用摊位号
 */
@Controller
@RequestMapping("/leaseOrderRpc")
public class LeaseOrderRpcController {
    @Autowired
    UapRpcService uapRpcService;
    @Autowired
    LeaseOrderRpcService leaseOrderRpcService;

    /**
     * 查询可用摊位号
     *
     * @param assetsParamsDto
     * @return
     */
    @RequestMapping("/findLease.action")
    @ResponseBody
    public BaseOutput<List<String>> findLease(@RequestBody AssetsParamsDto assetsParamsDto) {
        if (assetsParamsDto.getCustomerId() == null) {
            return BaseOutput.failure("参数错误");
        }
        assetsParamsDto.setBizType(1);
        assetsParamsDto.setMarketId(this.uapRpcService.getCurrentFirm().get().getId());
        try {
            this.leaseOrderRpcService.findLease(assetsParamsDto);
            return BaseOutput.successData(Lists.newArrayList("11111", "22222"));
        } catch (Exception e) {
            return BaseOutput.failure("后端出错");
        }

    }

}
