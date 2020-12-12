package com.dili.trace.api.manager;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.AppException;
import com.dili.ss.util.DateUtils;
import com.dili.trace.api.input.CreateDetectRequestInputDto;
import com.dili.trace.api.input.DetectRequestQueryDto;
import com.dili.trace.api.output.SampleSourceCountOutputDto;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
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
import com.dili.trace.service.SgRegisterBillService;
import io.swagger.annotations.Api;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    SgRegisterBillService registerBillService;
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

    /**
     * 查询采样检测列表
     *
     * @param input
     * @return
     */
    @RequestMapping("/listPagedSampleSourceDetect.api")
    public BaseOutput listPagedSampleSourceDetect(@RequestBody Object input) {

        return BaseOutput.success();
    }

    /**
     * 采样检测-采样来源数量统计
     */
    @RequestMapping(value = "/countBySampleSource.api", method = { RequestMethod.POST })
    public BaseOutput<List<VerifyStatusCountOutputDto>> countBySampleSource(@RequestBody DetectRequestQueryDto query) {
        try {
            List<SampleSourceCountOutputDto> list = this.detectRequestService.countBySampleSource(query);
            return BaseOutput.success().setData(list);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("操作失败：服务端出错");
        }
    }

    /**
     * 采样检测-查看详情
     */
    @RequestMapping(value = "/getSampleSourceDetectDetail.api", method = { RequestMethod.GET })
    public BaseOutput<RegisterBill> getSampleSourceDetectDetail(@RequestParam(name = "id", required = true) Long id) {
        try {
            DetectRequest detectRequest = this.detectRequestService.get(id);
            RegisterBill registerBill = this.billService.getById(detectRequest.getBillId());
            return BaseOutput.success().setData(registerBill);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("操作失败：服务端出错");
        }
    }

    /**
     * 采样检测-手动填写检测结果
     */
    @RequestMapping(value = "/createDetectRecord.api", method = { RequestMethod.POST })
    public BaseOutput<Long> createDetectRecord(@RequestBody DetectRecord detectRecord) {
        try {
            if (StringUtils.isBlank(detectRecord.getRegisterBillCode())) {
                logger.error("上传检测任务结果失败无编号");
                return BaseOutput.failure("没有对应的登记单");
            }
            if (StringUtils.isBlank(detectRecord.getDetectOperator())) {
                logger.error("上传检测任务结果失败无检测人员");
                return BaseOutput.failure("没有对应的检测人员");
            }
            if (detectRecord.getDetectState() == null) {
                logger.error("上传检测任务结果失败无检测状态");
                return BaseOutput.failure("没有对应的检测状态");
            } else if (detectRecord.getDetectState() > 2 || detectRecord.getDetectState() < 1) {
                logger.error("上传检测任务结果失败无,检测状态异常" + detectRecord.getDetectState());
                return BaseOutput.failure("没有对应的检测状态");
            }
            if (detectRecord.getDetectTime() == null) {
                logger.error("上传检测任务结果失败无检测时间");
                return BaseOutput.failure("没有对应的检测时间");
            }
            if (StringUtils.isBlank(detectRecord.getPdResult())) {
                logger.error("上传检测任务结果失败无检测值");
                return BaseOutput.failure("没有对应的检测值");
            }
            if (detectRecord.getDetectRequestId() == null) {
                logger.error("上传检测任务结果失败无检测任务");
                return BaseOutput.failure("没有对应的检测检测任务");
            }
            detectRecord.setCreated(new Date());
            detectRecord.setModified(new Date());
            int i = this.detectRecordService.saveDetectRecord(detectRecord);
            return BaseOutput.success().setData(i);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("操作失败：服务端出错");
        }
    }

    /**
     * 采样检测-主动送检
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/doAutoCheck.api", method = RequestMethod.GET)
    public BaseOutput doAutoCheck(@RequestParam(name = "id", required = true) Long id) {
        try {
            registerBillService.autoCheckRegisterBill(id);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.success("操作成功");
    }

    /**
     * 采样检测-采样检测
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/doSamplingCheck.api", method = RequestMethod.GET)
    public BaseOutput doSamplingCheck(@RequestParam(name = "id", required = true) Long id) {
        try {
            registerBillService.samplingCheckRegisterBill(id);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.success("操作成功");
    }

    /**
     * 采样检测-抽检
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/doSpotCheck.api", method = RequestMethod.GET)
    public BaseOutput doSpotCheck(@RequestParam(name = "id", required = true) Long id) {
        try {
            registerBillService.samplingCheckRegisterBill(id);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.success("操作成功");
    }

}
