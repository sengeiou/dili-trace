package com.dili.trace.controller;

import com.dili.sg.trace.glossary.SalesTypeEnum;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.*;
import com.dili.trace.domain.sg.QualityTraceTradeBill;
import com.dili.trace.dto.DetectRequestWithBillDto;
import com.dili.trace.enums.BillDeleteStatusEnum;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.service.*;
import com.dili.trace.util.MarketUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 场外委托检测管理
 */
@Api("/commissionDetectRequest")
@Controller
@RequestMapping("/commissionDetectRequest")
public class CommissionDetectRequestController {
    private static final Logger logger = LoggerFactory.getLogger(CommissionDetectRequestController.class);

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
    @ApiOperation("跳转到commissionDetectRequest页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap, HttpServletRequest req) {
        return "commissionDetectRequest/index";
    }

    /**
     * 分页查询DetectRequest
     *
     * @param detectRequestDto 查询条件
     * @return DetectRequest 列表
     * @throws Exception
     */
    @ApiOperation(value = "分页查询commissionDetectRequest", notes = "分页查询commissionDetectRequest，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commissionDetectRequest", paramType = "form", value = "commissionDetectRequest的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@RequestBody DetectRequestWithBillDto detectRequestDto) throws Exception {
        detectRequestDto.setMarketId(MarketUtil.returnMarket());
        detectRequestDto.setBillType(BillTypeEnum.COMMISSION_BILL.getCode());
        detectRequestDto.setIsDeleted(BillDeleteStatusEnum.NORMAL.getCode());
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
        DetectRequest detectRequest = this.detectRequestService.get(id);
        RegisterBill registerBill = this.billService.get(detectRequest.getBillId());
        modelMap.put("detectRequest", detectRequest);
        modelMap.put("registerBill", registerBill);

        return "commissionDetectRequest/confirm";
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