package com.dili.trace.controller;


import com.dili.assets.sdk.dto.CarTypeDTO;
import com.dili.assets.sdk.dto.CarTypePublicDTO;
import com.dili.assets.sdk.rpc.CarTypeRpc;
import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.customer.sdk.domain.VehicleInfo;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.domain.dto.IndividualCustomerInput;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.customer.sdk.rpc.VehicleRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.trace.domain.PurchaseIntentionRecord;
import com.dili.trace.domain.TruckEnterRecord;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.UpStreamDto;
import com.dili.trace.dto.query.TruckEnterRecordQueryDto;
import com.dili.trace.enums.TruckTypeEnum;
import com.dili.trace.rpc.service.CarTypeRpcService;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.service.AssetsRpcService;
import com.dili.trace.service.TruckEnterRecordService;
import com.dili.trace.service.UapRpcService;
import com.dili.trace.util.MarketUtil;
import com.dili.trace.util.TraceUtil;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.FirmDto;
import com.dili.uap.sdk.rpc.FirmRpc;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * ??????????????????
 */
@Controller
@RequestMapping("/truckEnterRecord")
public class TruckEnterRecordController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TruckEnterRecordController.class);
    @Autowired
    TruckEnterRecordService truckEnterRecordService;
    @Autowired
    CarTypeRpcService carTypeRpcService;
    @Autowired
    UapRpcService uapRpcService;
    @Autowired
    FirmRpc firmRpc;
    @Autowired
    CustomerRpc customerRpc;
    @Autowired
    VehicleRpc vehicleRpc;
    @Autowired
    CarTypeRpc carTypeRpc;

    /**
     * ?????????TruckEnterRecord??????
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("?????????TruckEnterRecord??????")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap, HttpServletRequest req) {
        List<CarTypeDTO> carTypeList = this.carTypeRpcService.listCarType();
        modelMap.put("carTypeList", carTypeList);
        return "truckEnterRecord/index";
    }

    /**
     * ????????????TruckEnterRecord
     *
     * @param queryInput
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "????????????TruckEnterRecord", notes = "????????????TruckEnterRecord?????????easyui????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "TruckEnterRecord", paramType = "form", value = "TruckEnterRecord???form??????", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    EasyuiPageOutput listPage(@RequestBody TruckEnterRecordQueryDto queryInput) throws Exception {
        queryInput.setMarketId(this.uapRpcService.getCurrentFirm().get().getId());
        return this.truckEnterRecordService.listEasyuiPageByExample(queryInput, true);

    }

    /**
     * ????????????????????????
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/view.html", method = RequestMethod.GET)
    public String view(ModelMap modelMap, @RequestParam(required = true, name = "id") Long id) {
        TruckEnterRecord truckEnterRecord = truckEnterRecordService.get(id);
        modelMap.put("truckEnterRecord",truckEnterRecord);
        return "truckEnterRecord/view";
    }


    /**
     * ?????????????????? OR ????????????
     * @param modelMap
     * @param id
     * @return
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(ModelMap modelMap, Long id) {
        if (null != id) {
            TruckEnterRecord truckEnterRecord = truckEnterRecordService.get(id);
            modelMap.put("truckEnterRecord",truckEnterRecord);
        } else {
            modelMap.put("truckEnterRecord",null);
        }
        return "truckEnterRecord/edit";
    }

    /**
     * ????????????
     *
     * @param truckEnterRecord
     * @return
     */
    @RequestMapping(value = "/save.action", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput save(@RequestBody TruckEnterRecord truckEnterRecord) {
        try {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            if (null == userTicket) {
                return BaseOutput.failure("????????????????????????");
            }
            if (null == truckEnterRecord.getId()) {
                truckEnterRecord.setMarketId(MarketUtil.returnMarket());
               return truckEnterRecordService.addTruckEnterRecord(truckEnterRecord);
            } else {
               return truckEnterRecordService.updateTruckEnterRecord(truckEnterRecord);
            }
        } catch (BusinessException e) {
            LOGGER.info("???????????????????????????????????????", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("???????????????????????????????????????", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * ??????
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput delete(Long id) {
        this.truckEnterRecordService.delete(id);
        return BaseOutput.success("????????????");
    }

    /**
     * ????????????????????????
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/add_driver.html", method = RequestMethod.GET)
    public String addBuyer(ModelMap modelMap) throws Exception {
        FirmDto firmDto = TraceUtil.newDTO(FirmDto.class);
        firmDto.setDeleted(false);
        firmDto.setFirmState(EnabledStateEnum.ENABLED.getCode());
        BaseOutput<List<Firm>> baseOutput = firmRpc.listByExample(firmDto);
        if (null != baseOutput) {
            List<Firm> firmList = baseOutput.getData();
            modelMap.put("firmList", firmList);
        }
        //BaseOutput<List<VehicleInfo>> baseOutput1 = vehicleRpc.listVehicle(null, MarketUtil.returnMarket());
        /*if(null!=baseOutput1){
            List<VehicleInfo> data = baseOutput1.getData();
            if(CollectionUtils.isNotEmpty(data)){
                modelMap.put("vehicleList", data);
            }
        }*/
        return "truckEnterRecord/add_driver";
    }

    /**
     * ??????????????????
     *
     * @param customer
     * @throws Exception
     */
    @RequestMapping(value = "/doAddDriver.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput doAddDriver(@RequestBody IndividualCustomerInput customer,HttpServletRequest request) {
        try {
            BaseOutput<CustomerExtendDto> baseOutput = customerRpc.registerIndividual(customer,request.getHeader("UAP_SessionId"));
            return baseOutput;
        } catch (TraceBizException e) {
            return BaseOutput.failure().setErrorData(e.getMessage());
        } catch (Exception e){
            return BaseOutput.failure().setErrorData(e.getMessage());
        }
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    @RequestMapping("/carTypeList.action")
    @ResponseBody
    public BaseOutput carTypeList() {
        try {
            return  carTypeRpc.listCarType();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return BaseOutput.failure().setErrorData(e.getMessage());
        }
    }
}
