package com.dili.trace.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.DetectRequest;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.DetectRecordParam;
import com.dili.trace.dto.DetectTaskApiOutputDto;
import com.dili.trace.dto.TaskGetParam;
import com.dili.trace.enums.DetectResultEnum;
import com.dili.trace.enums.DetectStatusEnum;
import com.dili.trace.enums.DetectTypeEnum;
import com.dili.trace.glossary.RegisterBilCreationSourceEnum;
import com.dili.trace.service.BillService;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.DetectRequestService;
import com.dili.trace.service.DetectTaskService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DetectTaskServiceImpl implements DetectTaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DetectTaskServiceImpl.class);

    @Autowired
    BillService billService;
    @Autowired
    RegisterBillMapper registerBillMapper;

    @Autowired
    private DetectRecordService detectRecordService;
    @Autowired
    DetectRequestService detectRequestService;

    private List<RegisterBill> findByExeMachineNo(String exeMachineNo, int taskCount,Long marketId) {
        LOGGER.info(">>>获得检测数据-参数:findByExeMachineNo(exeMachineNo={},taskCount={})", exeMachineNo, taskCount);
        this.registerBillMapper.taskByExeMachineNo(exeMachineNo, taskCount,marketId);
        this.registerBillMapper.taskByExeMachineNoForRequest(exeMachineNo, taskCount,marketId);
        RegisterBill domain = new RegisterBill();
        domain.setExeMachineNo(exeMachineNo);
        domain.setDetectStatus(DetectStatusEnum.DETECTING.getCode());
        domain.setMarketId(marketId);
        domain.setPage(1);
        domain.setRows(taskCount);
        domain.setSort("bill_type,id");
        domain.setOrder("DESC,ASC");
        List<RegisterBill> list = this.billService.listPageByExample(domain).getDatas();
        try {
            LOGGER.info(">>>获得检测数据-返回值:findByExeMachineNo({})", JSON.toJSONString(list));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return list;

    }

    @Transactional
    @Override
    public List<DetectTaskApiOutputDto> findByExeMachineNo(TaskGetParam taskGetParam) {
        String exeMachineNo = taskGetParam.getExeMachineNo();
        int taskCount = taskGetParam.getPageSize();
        List<RegisterBill> billList = this.findByExeMachineNo(exeMachineNo, taskCount,taskGetParam.getMarketId());
        return DetectTaskApiOutputDto.build(billList);
    }

    @Transactional
    @Override
    public BaseOutput<Boolean> updateDetectTask(DetectRecordParam detectRecord) {
        try {
            LOGGER.info(">>>提交检测数据-参数:updateDetectTask(detectRecord={})", JSON.toJSONString(detectRecord));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        // 对code,samplecode进行互换(检测客户端程序那边他们懒得改,不要动这行代码.具体看findByExeMachineNo里面的代码及注释)
        String samplecode = detectRecord.getRegisterBillCode();
        RegisterBill detectTask = this.billService.findBySampleCode(samplecode);
        if (detectTask == null) {
            return BaseOutput.failure("找不到检测任务");
        }
        try {
            this.updateRegisterBill(detectRecord);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            return BaseOutput.failure("服务端出错");
        }

        LOGGER.info(">>>提交检测数据-返回值:updateDetectTask({})", true);
        return BaseOutput.success().setData(true);

    }

    private void updateRegisterBill(DetectRecordParam detectRecord) {
        String samplecode = detectRecord.getRegisterBillCode();
        RegisterBill registerBillItem = billService.findBySampleCode(samplecode);

        if (registerBillItem == null) {
            LOGGER.error("上传检测任务结果失败该采样单号无登记单");
            throw new TraceBizException("没有对应的登记单");
        }

        if (DetectStatusEnum.FINISH_DETECT.equalsToCode(registerBillItem.getDetectStatus())) {
            LOGGER.error("上传检测任务结果失败,该单号已完成检测");
            throw new TraceBizException("已完成检测");
        }
        if (StringUtils.isNoneBlank(registerBillItem.getExeMachineNo())
                && !registerBillItem.getExeMachineNo().equals(detectRecord.getExeMachineNo())) {
            LOGGER.error("上传检测任务结果失败，该仪器没有获取该登记单");
            throw new TraceBizException("该仪器无权操作该单据");
        }
        DetectRequest detectRequestItem = this.detectRequestService.findDetectRequestByBillId(registerBillItem.getBillId()).orElseThrow(() -> {
            LOGGER.error("上传检测任务结果失败，检测请求不存在");
            return new TraceBizException("检测请求不存在");
        });
        DetectRequest detectRequest=new DetectRequest();
        detectRequest.setId(detectRequestItem.getId());
        if (registerBillItem.getLatestDetectRecordId() != null) {
            // 复检
            /// 1.第一次送检 2：复检 状态 1.合格 2.不合格
            detectRecord.setDetectType(DetectTypeEnum.RECHECK.getCode());
        } else {
            // 第一次检测
            detectRecord.setDetectType(DetectTypeEnum.INITIAL_CHECK.getCode());
        }
        if(DetectTypeEnum.NEW.equalsToCode(detectRequestItem.getDetectType())&&DetectResultEnum.NONE.equalsToCode(detectRequestItem.getDetectResult())){
            detectRequest.setDetectType(DetectTypeEnum.INITIAL_CHECK.getCode());
        }else if(DetectResultEnum.FAILED.equalsToCode(detectRequestItem.getDetectResult())){
            detectRequest.setDetectType(DetectTypeEnum.RECHECK.getCode());
        }

        detectRecord.setRegisterBillCode(registerBillItem.getCode());
        detectRecord.setDetectRequestId(detectRequestItem.getId());
        detectRecord.setCreated(new Date());
        detectRecord.setModified(new Date());
        detectRecordService.saveDetectRecord(detectRecord);

        RegisterBill registerBill=new RegisterBill();
        registerBill.setId(registerBillItem.getBillId());
        registerBill.setDetectStatus(DetectStatusEnum.FINISH_DETECT.getCode());
//		registerBill.setState(RegisterBillStateEnum.ALREADY_CHECK.getCode());
        registerBill.setLatestDetectRecordId(detectRecord.getId());
        registerBill.setLatestDetectTime(detectRecord.getDetectTime());
        registerBill.setLatestPdResult(detectRecord.getPdResult());
        registerBill.setLatestDetectOperator(detectRecord.getDetectOperator());
        registerBill.setCreationSource(RegisterBilCreationSourceEnum.WX.getCode());

        DetectResultEnum detectResultEnum=  DetectResultEnum.fromCode(detectRecord.getDetectState()).orElse(null);
        detectRequest.setDetectResult(detectResultEnum.getCode());
        detectRequest.setDetectorName(detectRecord.getDetectOperator());
        detectRequest.setDetectTime(detectRecord.getDetectTime());

        this.detectRequestService.updateSelective(detectRequest);

        this.billService.updateSelective(registerBill);

    }

}
