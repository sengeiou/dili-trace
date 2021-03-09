package com.dili.trace.api;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.api.input.RegisterBillQueryInputDto;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.service.RegisterBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 进门称重接口
 */
@RestController
@RequestMapping(value = "/api/weightingBill")
@AppAccess(role = Role.NONE)
public class WeightingBillApi {
    @Autowired
    RegisterBillService registerBillService;


    /**
     * 进门称重应用远程调用此接口，创建登记单
     *
     * @param inputParam
     * @return
     */
    @RequestMapping("/createRegisterBill.api")
    public BaseOutput createRegisterBill(@RequestBody CreateListBillParam inputParam) {

        return BaseOutput.success();
    }

    /**
     * 进门称重应用远程调用此接口，查询登记单(审核通过)
     *
     * @param inputDto
     * @return
     */
    @RequestMapping("/queryRegisterBill.api")
    public BaseOutput queryRegisterBill(@RequestBody RegisterBillQueryInputDto inputDto) {

        return BaseOutput.success();
    }

}
