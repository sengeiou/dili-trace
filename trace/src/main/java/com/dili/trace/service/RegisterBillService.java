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
	 * 通过登记单查找
	 * 
	 * @param code
	 * @return
	 */
	RegisterBill findByCode(String code);


	/**
	 * 创建登记单
	 * 
	 * @param registerBill
	 * @return
	 */
	Long createRegisterBill(RegisterBill registerBill, List<ImageCert> imageCertList, OperatorUser operatorUser);


	/**
	 * 通过登记单，获取详情
	 * 
	 * @param registerBill
	 * @return
	 */
	public RegisterBillOutputDto conversionDetailOutput(RegisterBill registerBill);



	/**
	 * 修改登记单
	 * 
	 * @param input
	 * @return
	 */
	public Long doEdit(RegisterBill input);

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