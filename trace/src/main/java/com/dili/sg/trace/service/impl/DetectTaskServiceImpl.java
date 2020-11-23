package com.dili.trace.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.DetectRecordParam;
import com.dili.trace.dto.DetectTaskApiOutputDto;
import com.dili.trace.dto.TaskGetParam;
import com.dili.trace.exception.TraceBizException;
import com.dili.trace.glossary.RegisterBilCreationSourceEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.service.BillService;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.DetectTaskService;

@Service
public class DetectTaskServiceImpl implements DetectTaskService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DetectTaskServiceImpl.class);

	@Autowired
	BillService billService;
	@Autowired
	RegisterBillMapper registerBillMapper;

	@Autowired
	private DetectRecordService detectRecordService;

	private List<RegisterBill> findByExeMachineNo(String exeMachineNo, int taskCount) {
		LOGGER.info(">>>获得检测数据-参数:findByExeMachineNo(exeMachineNo={},taskCount={})", exeMachineNo, taskCount);
		this.registerBillMapper.taskByExeMachineNo2(exeMachineNo, taskCount);
		RegisterBill domain = new RegisterBill();
		domain.setExeMachineNo(exeMachineNo);
		domain.setState(RegisterBillStateEnum.CHECKING.getCode());
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
//    	
//        List<RegisterBill> exist = this.registerBillMapper.findByExeMachineNo(exeMachineNo);
//        if (!exist.isEmpty()) {
//            LOGGER.info("获取的任务已经有相应的数量了" + taskCount);
//            if (exist.size() >= taskCount) {
//                return exist.subList(0, taskCount);
//            }
//        }
//
//        int fetchSize = taskCount - exist.size();
//        LOGGER.info("还需要再拿多少个：" + fetchSize);
//
//        List<Long> ids = this.registerBillMapper.findIdsByExeMachineNo(fetchSize);
//        StringBuilder sb = new StringBuilder();
//        sb.append(0);
//        for (Long id : ids) {
//            sb.append(",").append(id);
//        }
//        this.registerBillMapper.taskByExeMachineNo(exeMachineNo, sb.toString());
//        return this.registerBillMapper.findByExeMachineNo(exeMachineNo);
	}

	@Transactional
	@Override
	public List<DetectTaskApiOutputDto> findByExeMachineNo(TaskGetParam taskGetParam) {
		String exeMachineNo = taskGetParam.getExeMachineNo();
		int taskCount = taskGetParam.getPageSize();
		List<RegisterBill> billList = this.findByExeMachineNo(exeMachineNo, taskCount);
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
		RegisterBill registerBill = billService.findBySampleCode(samplecode);

		if (registerBill == null) {
			LOGGER.error("上传检测任务结果失败该采样单号无登记单");
			throw new TraceBizException("没有对应的登记单");
		}
		if (RegisterBillStateEnum.ALREADY_CHECK.getCode().equals(registerBill.getState())) {
			LOGGER.error("上传检测任务结果失败,该单号已完成检测");
			throw new TraceBizException("已完成检测");
		}
		if (!registerBill.getExeMachineNo().equals(detectRecord.getExeMachineNo())) {
			LOGGER.error("上传检测任务结果失败，该仪器没有获取该登记单");
			throw new TraceBizException("该仪器无权操作该单据");
		}

		if (registerBill.getLatestDetectRecordId() != null) {
			// 复检
			/// 1.第一次送检 2：复检 状态 1.合格 2.不合格
			detectRecord.setDetectType(2);
			// '默认null,1.合格 2.不合格 3.复检合格 4.复检不合格',
			registerBill.setDetectState(detectRecord.getDetectState() + 2);
		} else {
			// 第一次检测
			detectRecord.setDetectType(1);
			registerBill.setDetectState(detectRecord.getDetectState());
		}
		detectRecord.setRegisterBillCode(registerBill.getCode());
		detectRecordService.saveDetectRecord(detectRecord);

		registerBill.setState(RegisterBillStateEnum.ALREADY_CHECK.getCode());
		registerBill.setLatestDetectRecordId(detectRecord.getId());
		registerBill.setLatestDetectTime(detectRecord.getDetectTime());
		registerBill.setLatestPdResult(detectRecord.getPdResult());
		registerBill.setLatestDetectOperator(detectRecord.getDetectOperator());
		registerBill.setCreationSource(RegisterBilCreationSourceEnum.WX.getCode());

		billService.updateSelective(registerBill);

	}

}
