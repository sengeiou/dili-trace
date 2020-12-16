package com.dili.trace.controller;

import com.dili.common.annotation.DetectRequestMessageEvent;
import com.dili.common.exception.TraceBizException;
import com.dili.sg.trace.glossary.SalesTypeEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.sg.QualityTraceTradeBill;
import com.dili.trace.dto.DetectRequestWithBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.service.*;
import com.dili.trace.util.MarketUtil;
import com.dili.uap.sdk.domain.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 检测请求业务类
 */
@Api("/customerDetectRequest")
@Controller
@RequestMapping("/customerDetectRequest")
public class CustomerDetectRequestController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerDetectRequestController.class);

    @Autowired
    DetectRequestService detectRequestService;

    @Autowired
    UserRpcService userRpcService;

    @Autowired
    BillService billService;
    @Autowired
    SeparateSalesRecordService separateSalesRecordService;
    @Autowired
    QualityTraceTradeBillService qualityTraceTradeBillService;
    @Autowired
    DetectRecordService detectRecordService;
    @Autowired
    SgRegisterBillService registerBillService;

    /**
     * 跳转到DetectRequest页面
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到DetectRequest页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap, HttpServletRequest req) {
        return "customerDetectRequest/index";
    }

    /**
     * 分页查询DetectRequest
     *
     * @param detectRequestDto 查询条件
     * @return DetectRequest 列表
     * @throws Exception
     */
    @ApiOperation(value = "分页查询DetectRequest", notes = "分页查询DetectRequest，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "DetectRequest", paramType = "form", value = "DetectRequest的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@RequestBody DetectRequestWithBillDto detectRequestDto) throws Exception {
        detectRequestDto.setMarketId(MarketUtil.returnMarket());
        List<Integer> billTypes = new ArrayList<>();
        billTypes.add(BillTypeEnum.REGISTER_BILL.getCode());
        billTypes.add(BillTypeEnum.CHECK_ORDER.getCode());
        billTypes.add(BillTypeEnum.CHECK_DISPOSE.getCode());
        billTypes.add(BillTypeEnum.E_COMMERCE_BILL.getCode());
        detectRequestDto.setBillTypes(billTypes);
        EasyuiPageOutput out = this.detectRequestService.listBasePageByExample(detectRequestDto);
        return out.toString();
    }

    /**
     * 接单页面
     *
     * @param modelMap
     * @param id
     * @return
     */
    @RequestMapping(value = "/confirm.html", method = RequestMethod.GET)
    public String assign(ModelMap modelMap, @RequestParam(name = "id", required = true) Long id) {
        modelMap.put("detectRequest", this.detectRequestService.get(id));
        return "customerDetectRequest/confirm";
    }

    /**
     * 接单
     * @param id 检测请求ID
     * @param designatedId 检测员ID
     * @param designatedName 检测员姓名
     * @return 指派结果
     */
    @RequestMapping(value = "/doConfirm.action", method = RequestMethod.GET)
    public @ResponseBody
    BaseOutput doAssign(@RequestParam(name = "id", required = true) Long id,
                        @RequestParam(name = "designatedId", required = false) Long designatedId,
                        @RequestParam(name = "designatedName", required = false) String designatedName,
                        @RequestParam(name = "detectTime", required = false) Date detectTime) {
        try {
            this.detectRequestService.confirm(id, designatedId, designatedName, detectTime);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.success("操作成功");
    }

    /**
     * 撤销
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/doUndo.action", method = RequestMethod.GET)
    public @ResponseBody
    BaseOutput doUndo(@RequestParam(name = "id", required = true) Long id) {
        try {
            this.detectRequestService.undoDetectRequest(id);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.success("操作成功");
    }

    /**
     * 根据名字查询检测员信息
     *
     * @param name
     * @return
     */
    @RequestMapping("/detector")
    @ResponseBody
    public Map<String, ?> listByName(String name) {
        Long marketId = MarketUtil.returnMarket();
        List<User> detectorUsers = userRpcService.findDetectDepartmentUsers(name, marketId).orElse(Lists.newArrayList());
        List<Map<String, Object>> list = Lists.newArrayList();
        for (User c : detectorUsers) {
            Map<String, Object> obj = Maps.newHashMap();
            obj.put("id", c.getId());
            obj.put("data", name);
            obj.put("value", c.getUserName());
            list.add(obj);
        }

        Map map = Maps.newHashMap();
        map.put("suggestions", list);
        return map;
    }

    /**
     * 查询当前controller可用事件
     *
     * @param billIdList
     * @return
     */
    @RequestMapping("/queryEvents.action")
    @ResponseBody
    public List<String> queryEvents(@RequestBody List<Long> billIdList) {
        List<DetectRequestMessageEvent> list= Lists.newArrayList();
        if (billIdList == null || billIdList.size()==0) {
            return StreamEx.of(list).map(msg -> {
                return msg.getCode();
            }).nonNull().toList();
        }
        // 只要有记录，可以导出和查看详情
        list.add(DetectRequestMessageEvent.export);
        list.add(DetectRequestMessageEvent.detail);
        if(billIdList.size()==1){
            return StreamEx.of(this.detectRequestService.queryEvents(billIdList.get(0))).append(list).map(msg -> {
                return msg.getCode();
            }).nonNull().toList();
        }else{
            return StreamEx.of(list).map(msg -> {
                return msg.getCode();
            }).nonNull().toList();
        }
    }

    /**
     * 登记单录查看页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/view.html", method = RequestMethod.GET)
    public String view(ModelMap modelMap, @RequestParam(required = true, name = "id") Long id
            , @RequestParam(required = false, name = "displayWeight") Boolean displayWeight) {
        RegisterBill item = billService.getAvaiableBill(id).orElse(null);
        if (item == null) {
            modelMap.put("registerBill", item);
            return "customerDetectRequest/view";
        }
        if (displayWeight == null) {
            displayWeight = false;
        }
        if (RegisterSourceEnum.TALLY_AREA.getCode().equals(item.getRegisterSource())) {
            // 分销信息
            if (item.getSalesType() != null
                    && item.getSalesType().intValue() == SalesTypeEnum.SEPARATE_SALES.getCode().intValue()) {
                // 分销
                List<SeparateSalesRecord> records = separateSalesRecordService
                        .findByRegisterBillCode(item.getCode());
                modelMap.put("separateSalesRecords", records);
            }
        } else {
            QualityTraceTradeBill condition = DTOUtils.newDTO(QualityTraceTradeBill.class);
            condition.setRegisterBillCode(item.getCode());
            modelMap.put("qualityTraceTradeBills", qualityTraceTradeBillService.listByExample(condition));
        }

        List<DetectRecord> detectRecordList = this.detectRecordService.findTop2AndLatest(item.getCode());
        modelMap.put("detectRecordList", detectRecordList);
        modelMap.put("displayWeight", displayWeight);

//        RegisterBillOutputDto registerBill = new RegisterBillOutputDto();
//        BeanUtils.copyProperties(this.maskRegisterBillOutputDto(item), registerBill);

        List<ImageCert> imageCerts = this.registerBillService.findImageCertListByBillId(item.getBillId());
        item.setImageCerts(imageCerts);

        modelMap.put("registerBill", item);

        return "customerDetectRequest/view";
    }
}