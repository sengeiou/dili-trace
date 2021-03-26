package com.dili.trace.controller;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.*;
import com.dili.trace.dto.*;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.service.*;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;

/**
 * 进门主台账单实现类
 *
 * @author Lily
 */
@Api("/registerHead")
@Controller
@RequestMapping("/registerHead")
public class RegisterHeadController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterHeadController.class);

    @Autowired
    private CustomerRpcService customerRpcService;
    @Autowired
    RegisterHeadService registerHeadService;
    @Autowired
    RegisterHeadPlateService registerHeadPlateService;
    @Autowired
    RegisterTallyAreaNoService registerTallyAreaNoService;
    @Autowired
    UapRpcService uapRpcService;
    @Autowired
    UpStreamService upStreamService;

    /**
     * 跳转到RegisterHead页面
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到RegisterHead页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {

        RegisterHeadDto query = new RegisterHeadDto();
        Date now = new Date();
        query.setCreatedEnd(DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
        query.setCreatedStart(DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
        modelMap.put("query", query);

        return "registerHead/index";
    }

    /**
     * 查询RegisterHead
     *
     * @param registerHead
     * @return
     */
    @ApiOperation(value = "查询RegisterHead", notes = "查询RegisterHead，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "RegisterHead", paramType = "form", value = "RegisterHead的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    List<RegisterHead> list(RegisterHeadDto registerHead) {
        return registerHeadService.list(registerHead);
    }

    /**
     * 分页查询RegisterHead
     *
     * @param query
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询RegisterHead", notes = "分页查询RegisterHead，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "RegisterHead", paramType = "form", value = "RegisterHead的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})

    public @ResponseBody
    String listPage(RegisterHeadDto query) throws Exception {
        query.setMarketId(this.uapRpcService.getCurrentFirm().get().getId());
        return registerHeadService.listEasyuiPageByExample(query, true).toString();
    }

    /**
     * 新增RegisterHead
     *
     * @param registerHeads
     * @return
     */
    @ApiOperation("新增RegisterHead")
    @RequestMapping(value = "/insert.action", method = RequestMethod.POST)
    public @ResponseBody
    BaseOutput insert(@RequestBody List<RegisterHead> registerHeads) {
        logger.info("保存进门主台账单数据:" + registerHeads.size());
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        for (RegisterHead registerHead : registerHeads) {

            com.dili.customer.sdk.domain.Customer cust = this.customerRpcService.findCustByIdOrEx(registerHead.getUserId());
            registerHead.setName(cust.getName());
            registerHead.setIdCardNo(cust.getCertificateNumber());
            registerHead.setAddr(cust.getCertificateAddr());
            registerHead.setUserId(cust.getId());
            try {
                Long billId = registerHeadService.createRegisterHead(registerHead, new ArrayList<ImageCert>(),
                        Optional.ofNullable(new OperatorUser(userTicket.getId(), userTicket.getRealName())));

            } catch (TraceBizException e) {
                return BaseOutput.failure(e.getMessage());

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return BaseOutput.failure("服务端出错");

            }
        }
        return BaseOutput.success("新增成功").setData(registerHeads);
    }

    /**
     * 修改RegisterHead
     *
     * @param registerHead
     * @return
     */
    @ApiOperation("修改RegisterHead")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "RegisterHead", paramType = "form", value = "RegisterHead的form信息", required = true, dataType = "string")})
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput update(RegisterHead registerHead) {
        registerHeadService.updateSelective(registerHead);
        return BaseOutput.success("修改成功");
    }


    /**
     * 查询RegisterHead
     *
     * @param queryInput
     * @return
     */
    @RequestMapping(value = "/listRegisterHead.action", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput listRegisterHead(@RequestBody RegisterHeadDto queryInput) {
        if (queryInput.getUserId() == null) {
            return BaseOutput.successData(Lists.newArrayList());
        }
        queryInput.setMarketId(this.uapRpcService.getCurrentFirm().get().getId());
        List<RegisterHead> list = this.registerHeadService.listByExample(queryInput);

        List<Long> registerHeadIdList = StreamEx.of(list).map(RegisterHead::getId).toList();


        UpStreamDto upStreamQ=new UpStreamDto();
        upStreamQ.setIds(StreamEx.of(list).map(RegisterHead::getUpStreamId).nonNull().distinct().toList());
        Map<Long,String>upStreamIdNameMap=StreamEx.of(upStreamQ).filter(q->{
            return !q.getIds().isEmpty();
        }).flatCollection(q->{
            return this.upStreamService.listByExample(q);
        }).toMap(UpStream::getId, UpStream::getName);

        Map<Long, List<String>> plateListMap = this.registerHeadPlateService.findPlateByRegisterHeadIdList(registerHeadIdList);
        Map<Long, List<String>> tallyAreaNoListMap = this.registerTallyAreaNoService.findTallyAreaNoByRegisterHeadIdList(registerHeadIdList);
        List<RegisterHead> dataList = StreamEx.of(list).map(rh -> {
            List<String> plateList = plateListMap.getOrDefault(rh.getId(), Lists.newArrayList());
            rh.setPlateList(plateList);

            List<String> registerTallyAreaNoList = tallyAreaNoListMap.getOrDefault(rh.getId(), Lists.newArrayList());
            rh.setArrivalTallynos(registerTallyAreaNoList);

            rh.setUpStreamName(upStreamIdNameMap.getOrDefault(rh.getUpStreamId(),""));
            return rh;

        }).toList();


        return BaseOutput.successData(dataList);
    }
}