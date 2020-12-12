package com.dili.trace.api.manager;

import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.api.input.CreateDetectRequestInputDto;
import com.dili.trace.api.input.DetectRequestQueryDto;
import com.dili.trace.api.output.SampleSourceCountOutputDto;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.DetectRequest;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.enums.DetectResultEnum;
import com.dili.trace.enums.DetectTypeEnum;
import com.dili.trace.glossary.SampleSourceEnum;
import com.dili.trace.service.BillService;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.DetectRequestService;
import com.dili.trace.service.SgRegisterBillService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 检测请求接口
 */
@RestController
@RequestMapping(value = "/api/manager/managerDetectRquestApi")
@Api(value = "/api/manager/managerDetectRquestApi", description = "登记单相关接口")
//@AppAccess(role = Role.Manager)
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
     * @param input
     * @return
     */
    @RequestMapping("/listPagedDetectRequest.api")
    public BaseOutput listPagedDetectRequest(@RequestBody Object input) {
        return BaseOutput.success();

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
            RegisterBill billItem = this.billService.getBill(detectRequest.getBillId()).orElseThrow(() -> {
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
