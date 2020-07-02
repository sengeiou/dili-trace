package com.dili.trace.service;

import java.util.List;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.dto.BatchAuditDto;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
public interface RegisterBillService extends BaseService<RegisterBill, Long> {

	public RegisterBill findFirstWaitAuditRegisterBillCreateByCurrentUser(RegisterBillDto dto) throws Exception;

	public String listPage(RegisterBillDto dto) throws Exception;

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
	 * 创建登记单
	 * 
	 * @param registerBill
	 * @return
	 */
	Long createRegisterBill(RegisterBill registerBill, List<ImageCert> imageCertList, OperatorUser operatorUser);

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
	 * 通过登记单，获取详情
	 * 
	 * @param registerBill
	 * @return
	 */
	public RegisterBillOutputDto conversionDetailOutput(RegisterBill registerBill);

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
	public Long doUploadDetectReport(RegisterBill input);

	/**
	 * 保存修改数据
	 * 
	 * @param input
	 * @return
	 */
	public Long doUploadOrigincertifiy(RegisterBill input);

	/**
	 * 直接审核通过不需要检测
	 * 
	 * @param input
	 * @return
	 */
	public Long doAuditWithoutDetect(RegisterBill input);

	/**
	 * 修改登记单
	 * 
	 * @param input
	 * @return
	 */
	public Long doEdit(RegisterBill input);

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
	 * 
	 * @param batchAuditDto
	 * @return
	 */
	public BaseOutput doBatchAudit(BatchAuditDto batchAuditDto);

	/**
	 * 删除产地证明及检测报告
	 * 
	 * @param id
	 * @param deleteType
	 * @return
	 */

	public BaseOutput doRemoveReportAndCertifiy(Long id, String deleteType);

	public Long doVerifyBeforeCheckIn(RegisterBill input, OperatorUser operatorUser);

	public Long doVerifyAfterCheckIn(RegisterBill input, OperatorUser operatorUser);

	public List<Long> createBillList(List<CreateRegisterBillInputDto> registerBills, User user,
			OperatorUser operatorUser);

	public List<VerifyStatusCountOutputDto> countByVerifyStatus(RegisterBill query);
}