package com.dili.trace.api.manager;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.ss.util.DateUtils;
import com.dili.trace.api.input.CreateDetectRequestInputDto;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.DetectRequest;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.DetectRequestDto;
import com.dili.trace.enums.DetectResultEnum;
import com.dili.trace.enums.DetectStatusEnum;
import com.dili.trace.enums.DetectTypeEnum;
import com.dili.trace.glossary.SampleSourceEnum;
import com.dili.trace.service.BillService;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.DetectRequestService;
import io.swagger.annotations.Api;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 检测请求接口
 *
 * @author w
 */
@RestController
@RequestMapping(value = "/api/manager/managerDetectRquestApi")
@Api(value = "/api/manager/managerDetectRquestApi", description = "登记单相关接口")
@AppAccess(role = Role.Manager)
public class ManagerDetectRquestApi {
    private static final Logger logger = LoggerFactory.getLogger(ManagerDetectRquestApi.class);
    @Autowired
    DetectRequestService detectRequestService;
    @Autowired
    BillService billService;
    @Autowired
    DetectRecordService detectRecordService;
    @Autowired
    private LoginSessionContext sessionContext;

    /**
     * 查询检测请求列表
     *
     * @param detectRequestDto
     * @return
     */
    @RequestMapping("/listPagedDetectRequest.api")
    public BaseOutput listPagedDetectRequest(@RequestBody DetectRequestDto detectRequestDto) {
        if (null == detectRequestDto.getDetectStatus()) {
            return BaseOutput.failure("参数错误");
        }

        List<DetectRequestDto> dtoList = detectRequestService.listPageByUserCategory(detectRequestDto);
        return BaseOutput.success().setData(dtoList);

    }

    /**
     * 查询检测请求详情
     *
     * @param detectRequestDto
     * @return
     */
    @RequestMapping("/getDetectRequest.api")
    public BaseOutput getDetectRequest(@RequestBody DetectRequestDto detectRequestDto) {
        if (null == detectRequestDto.getId()) {
            return BaseOutput.failure("参数错误");
        }
        return BaseOutput.success().setData(detectRequestService.getDetectRequestDetail(detectRequestDto));
    }

    /**
     * 退回检测请求
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/returnDetectRequest.api", method = RequestMethod.GET)
    public BaseOutput returnDetectRequest(Long id) {
        if (null == id) {
            return BaseOutput.failure("参数错误");
        }
        try {
            RegisterBill bill = getBillByDetectRequestId(id);
            RegisterBill upBill = new RegisterBill();
            upBill.setDetectStatus(DetectStatusEnum.RETURN_DETECT.getCode());
            upBill.setId(bill.getId());
            billService.updateSelective(upBill);
        } catch (Exception e) {
            return BaseOutput.failure().setData(e.getMessage());
        }
        return BaseOutput.success();
    }

    /**
     * 检测请求接单
     *
     * @param detectRequestDto
     * @return
     */
    @RequestMapping("/receiveDetectRequest.api")
    public BaseOutput receiveDetectRequest(@RequestBody DetectRequest detectRequestDto) {
        if (null == detectRequestDto.getId()) {
            return BaseOutput.failure("参数错误");
        }
        try {
            RegisterBill bill = getBillByDetectRequestId(detectRequestDto.getId());
            //未填写时间时，设置为当前时间
            if (null == detectRequestDto.getDetectTime()) {
                detectRequestDto.setDetectTime(DateUtils.getCurrentDate());
            }
            //未填写检测人员时设置为当前登录人
            if (null == detectRequestDto.getDetectorId()) {
                detectRequestDto.setDetectorId(sessionContext.getAccountId());
                detectRequestDto.setDetectorName(sessionContext.getUserName());
            }
            detectRequestService.receiveDetectRequest(bill.getId(), detectRequestDto);
        } catch (Exception e) {
            return BaseOutput.failure().setData(e.getMessage());
        }
        return BaseOutput.success();
    }

    /**
     * 根据检测请求获取报备单信息，并校验状态是否为待接单
     *
     * @param id
     * @return
     */
    private RegisterBill getBillByDetectRequestId(Long id) {
        RegisterBill queBill = new RegisterBill();
        queBill.setDetectRequestId(id);
        List<RegisterBill> billList = billService.listByExample(queBill);
        if (CollectionUtils.isEmpty(billList)) {
            throw new AppException("操作失败，报备单据不存在");
        }
        RegisterBill bill = billList.get(0);
        if (!DetectStatusEnum.WAIT_DESIGNATED.equalsToCode(bill.getDetectStatus())) {
            throw new AppException("操作失败，单据非待接单状态");
        }
        return bill;
    }

    /**
     * 处理检测请求列表
     *
     * @param input
     * @return
     */
    @RequestMapping("/handleDetectRequest.api")
    public BaseOutput handleDetectRequest(@RequestBody CreateDetectRequestInputDto input) {
        if (input == null || input.getDetectRequest() == null) {
            return BaseOutput.failure("参数错误");
        }
        DetectRequest detectRequest = input.getDetectRequest();
        if (detectRequest.getDetectSource() == null || detectRequest.getBillId() == null) {
            return BaseOutput.failure("参数错误");
        }
        SampleSourceEnum sampleSourceEnum = SampleSourceEnum.fromCode(input.getDetectRequest().getDetectSource()).orElseThrow(() -> {
            return new TraceBizException("检测来源错误");
        });
        if (sampleSourceEnum == SampleSourceEnum.MANUALLY) {
            if (input.getDetectRecord() == null) {
                return BaseOutput.failure("参数错误");
            }
        }
        try {
            RegisterBill billItem = this.billService.getAvaiableBill(detectRequest.getBillId()).orElseThrow(() -> {
                throw new TraceBizException("数据不存在");
            });

            detectRequest.setDetectResult(DetectResultEnum.NONE.getCode());
            detectRequest.setDetectType(DetectTypeEnum.NEW.getCode());
            detectRequest.setCreated(new Date());
            detectRequest.setModified(new Date());
            this.detectRequestService.insertSelective(detectRequest);

            //处理人工检测结果
            if (sampleSourceEnum == SampleSourceEnum.MANUALLY) {
                DetectRecord detectRecord = input.getDetectRecord();
                detectRecord.setRegisterBillCode(billItem.getCode());
                this.detectRecordService.insertSelective(detectRecord);
                RegisterBill bill = new RegisterBill();
            }

            return BaseOutput.success();
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }


    }
}
