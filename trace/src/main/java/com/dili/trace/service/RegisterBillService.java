package com.dili.trace.service;

import java.util.List;
import java.util.Map;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.BatchAuditDto;
import com.dili.trace.dto.QualityTraceTradeBillOutDto;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.dto.RegisterBillStaticsDto;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
public interface RegisterBillService extends BaseService<RegisterBill, Long> {
	/**
	 * 查找任务
	 * 
	 * @param exeMachineNo
	 * @param taskCount
	 * @return
	 */
	List<RegisterBill> findByExeMachineNo(String exeMachineNo, int taskCount);

	/**
	 * 通过商品查找
	 * 
	 * @param productName
	 * @return
	 */
	List<RegisterBill> findByProductName(String productName);

	/**
	 * 通过登记单查找
	 * 
	 * @param code
	 * @return
	 */
	RegisterBill findByCode(String code);
	
	/**
	 * 通过采样编号查找
	 * 
	 * @param sampleCode
	 * @return
	 */
	RegisterBill findBySampleCode(String sampleCode);

	/**
	 * 通过交易区交易单查询
	 * 
	 * @param tradeNo
	 * @return
	 */
	RegisterBillOutputDto findByTradeNo(String tradeNo);

	/**
	 * 创建登记单
	 * 
	 * @param registerBill
	 * @return
	 */
	BaseOutput createRegisterBill(RegisterBill registerBill);

	/**
	 * 审核登记单
	 * 
	 * @param id
	 * @param pass
	 * @return
	 */
	int auditRegisterBill(Long id, Boolean pass);

	/**
	 * 撤销交易单
	 * 
	 * @param id
	 * @return
	 */
	int undoRegisterBill(Long id);

	/**
	 * 自动送检标记
	 * 
	 * @param id
	 * @return
	 */
	int autoCheckRegisterBill(Long id);

	/**
	 * 采样检测标记
	 * 
	 * @param id
	 * @return
	 */
	int samplingCheckRegisterBill(Long id);

	/**
	 * 复检标记
	 * 
	 * @param id
	 * @return
	 */
	int reviewCheckRegisterBill(Long id);

	/**
	 * 通过交易单查询，未绑定就绑定
	 * 
	 * @param tradeNo
	 * @return
	 */
	public QualityTraceTradeBillOutDto findQualityTraceTradeBill(String tradeNo);

	/**
	 * 根据状态统计数据
	 * 
	 * @param dto
	 * @return
	 */
	public RegisterBillStaticsDto groupByState(RegisterBillDto dto);

	/**
	 * 通过登记单，获取详情
	 * 
	 * @param registerBill
	 * @return
	 */
	public RegisterBillOutputDto conversionDetailOutput(RegisterBill registerBill);

	/**
	 * 检测记录匹配
	 * 
	 * @param qualityTraceTradeBill
	 * @return
	 */
	int matchDetectBind(QualityTraceTradeBill qualityTraceTradeBill);

	/**
	 * 保存处理结果
	 * 
	 * @param input
	 * @return
	 */
	public Long saveHandleResult(RegisterBill input);

	/**
	 * 保存修改数据
	 * 
	 * @param input
	 * @return
	 */
	public Long doModifyRegisterBill(RegisterBill input);
	
	/**
	 * 直接审核通过不需要检测
	 * 
	 * @param input
	 * @return
	 */
	public Long doAuditWithoutDetect(RegisterBill input);

	/**
	 * 批量主动送检
	 * 
	 * @param idList
	 * @return
	 */
	public BaseOutput doBatchAutoCheck(List<Long> idList);

	/**
	 * 批量采样检测
	 * 
	 * @param idList
	 * @return
	 */
	public BaseOutput doBatchSamplingCheck(List<Long> idList);
	
	
	/**
	 * 批量审核
	 * @param batchAuditDto
	 * @return
	 */
	public  BaseOutput doBatchAudit(BatchAuditDto batchAuditDto) ;
}