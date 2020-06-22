package com.dili.trace.api;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.config.DefaultConfiguration;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dto.DetectRecordParam;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.dto.TaskGetParam;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.glossary.RegisterBilCreationSourceEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.RegisterBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by laikui on 2019/7/26.
 */
@RestController
@RequestMapping(value = "/api/detect")
@Api(value = "/api/detect", description = "检测任务相关接口")
@InterceptConfiguration(loginRequired = false)
public class DetectRecordApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(DetectRecordApi.class);
	@Autowired
	private RegisterBillService registerBillService;
	@Autowired
	private DetectRecordService detectRecordService;
	@Autowired
	private DefaultConfiguration defaultConfiguration;

	/**
	 * 保存检查单
	 * 
	 * @param detectRecord
	 * @return
	 */
	@ApiOperation("上传检测记录")
	@RequestMapping(value = "/saveRecord", method = RequestMethod.POST)
	public BaseOutput<Boolean> saveDetectRecord(DetectRecordParam detectRecord,HttpServletRequest req) {
//		try {
//			String content=IOUtils.readLines(req.getInputStream(), StandardCharsets.UTF_8).stream().collect(Collectors.joining());
//			detectRecord=DTOUtils.as(JSON.parseObject(content), DetectRecordParam.class);
//		} catch (IOException e) {
//			LOGGER.error(e.getMessage(),e);
//			return BaseOutput.failure("服务器出错");
//		}
		LOGGER.info(defaultConfiguration.getEnTag() + "=sys.en.tag]保存检查单:" + JSON.toJSONString(detectRecord));
		if (!StringUtils.trimToEmpty(defaultConfiguration.getEnTag()).equals(detectRecord.getTag())) {
			LOGGER.error("上传检测任务结果失败:签名出错");
			return BaseOutput.failure("签名出错");
		}

		if (StringUtils.isBlank(detectRecord.getRegisterBillCode())) {
			LOGGER.error("上传检测任务结果失败无编号");
			return BaseOutput.failure("没有对应的登记单");
		}
		if (StringUtils.isBlank(detectRecord.getDetectOperator())) {
			LOGGER.error("上传检测任务结果失败无检测人员");
			return BaseOutput.failure("没有对应的检测人员");
		}
		if (detectRecord.getDetectState() == null) {
			LOGGER.error("上传检测任务结果失败无检测状态");
			return BaseOutput.failure("没有对应的检测状态");
		} else if (detectRecord.getDetectState() > 2 || detectRecord.getDetectState() < 1) {
			LOGGER.error("上传检测任务结果失败无,检测状态异常" + detectRecord.getDetectState());
			return BaseOutput.failure("没有对应的检测状态");
		}
		if (detectRecord.getDetectTime() == null) {
			LOGGER.error("上传检测任务结果失败无检测时间");
			return BaseOutput.failure("没有对应的检测时间");
		}
		if (StringUtils.isBlank(detectRecord.getPdResult())) {
			LOGGER.error("上传检测任务结果失败无检测值");
			return BaseOutput.failure("没有对应的检测值");
		}
		
		//对code,samplecode进行互换(检测程序那边他们懒得改)
		RegisterBill registerBill = registerBillService.findBySampleCode(detectRecord.getRegisterBillCode());

		if (registerBill == null) {
			LOGGER.error("上传检测任务结果失败该采样单号无登记单");
			return BaseOutput.failure("没有对应的登记单");
		}
		if (RegisterBillStateEnum.ALREADY_CHECK.getCode().equals(registerBill.getState())) {
			LOGGER.error("上传检测任务结果失败,该单号已完成检测");
			return BaseOutput.failure("已完成检测");
		}
		if (!registerBill.getExeMachineNo().equals(detectRecord.getExeMachineNo())) {
			LOGGER.error("上传检测任务结果失败，该仪器没有获取该登记单");
			return BaseOutput.failure("该仪器无权操作该单据");
		}
//        //对不合格且没有提交处理结果的登记单,可以进行无限次提交检测结果!
//        if(RegisterBillStateEnum.ALREADY_CHECK.getCode().equals(registerBill.getState())) {
//        	if(registerBill.getDetectState()!=null&&StringUtils.isBlank(registerBill.getHandleResult())) {
//        		if(registerBill.getDetectState()==2||registerBill.getDetectState()==4) {
//        			 this.saveRecordAndUpdateBill(detectRecord,registerBill);
//        			 return BaseOutput.success().setData(true);
//        		}
//        	}
//        	LOGGER.error("上传检测任务结果失败,该单号已完成检测");
//            return BaseOutput.failure("已完成检测");
//        }
		saveRecordAndUpdateBill(detectRecord, registerBill);
		return BaseOutput.success().setData(true);
	}

	@Transactional(rollbackFor = Exception.class)
	private void saveRecordAndUpdateBill(DetectRecordParam detectRecord, RegisterBill registerBill) {

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
		registerBillService.updateSelective(registerBill);
	}

	/**
	 * 获取检查任务
	 * 
	 * @param exeMachineNo
	 * @return
	 */
	@ApiOperation("获取检测任务")
	@RequestMapping(value = "/getDetectTask/{exeMachineNo}/{taskCount}/{tag}", method = RequestMethod.POST)
	public BaseOutput<List<RegisterBill>> getDetectTask(@PathVariable String tag, @PathVariable String exeMachineNo,
			@PathVariable Integer taskCount) {

		if (!StringUtils.trimToEmpty(defaultConfiguration.getEnTag()).equals(tag)) {
			LOGGER.error("上传检测任务结果失败:签名出错");
			return BaseOutput.failure("签名出错");
		}

		if (taskCount == null || taskCount <= 0) {
			return BaseOutput.failure("限制数量出错");
		}

		TaskGetParam taskGetParam = new TaskGetParam();
		taskGetParam.setExeMachineNo(exeMachineNo);
		if (taskCount > 95) {
			taskCount = 95;
		}
		taskGetParam.setPageSize(taskCount);
		LOGGER.info(defaultConfiguration.getEnTag() + "=sys.en.tag]获取检查任务:" + JSON.toJSONString(taskGetParam) + tag);
		List<RegisterBill> registerBills = registerBillService.findByExeMachineNo(taskGetParam.getExeMachineNo(),
				taskGetParam.getPageSize()).stream().map(rb->{
					//对code,samplecode进行互换(检测程序那边他们懒得改)
					rb.setCode(rb.getSampleCode());
					rb.setSampleCode(null);
					return rb;
				}).collect(Collectors.toList());
		return BaseOutput.success().setData(registerBills);
	}

	




	
}
