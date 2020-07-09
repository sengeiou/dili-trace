package com.dili.trace.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
public interface RegisterBillService extends BaseService<RegisterBill, Long> {
    /**
     * 通过ID悲观锁定数据
     */
	public Optional<RegisterBill> selectByIdForUpdate(Long id);

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
	 * 创建单个报备单
	 * 
	 * @param registerBill
	 * @return
	 */
	Long createRegisterBill(RegisterBill registerBill, List<ImageCert> imageCertList, OperatorUser operatorUser);

	/**
	 * 修改单个报备单
	 * 
	 * @param input
	 * @return
	 */
	public Long doEdit(RegisterBill registerBill,List<ImageCert> imageCertList);

	/**
	 * 进门前审核
	 * 
	 * @param input
	 * @param operatorUser
	 * @return
	 */
	public Long doVerifyBeforeCheckIn(RegisterBill input, OperatorUser operatorUser);

	/**
	 * 进门后审核
	 * 
	 * @param input
	 * @param operatorUser
	 * @return
	 */
	public Long doVerifyAfterCheckIn(RegisterBill input, OperatorUser operatorUser);

	/**
	 * 创建多个报备单
	 * 
	 * @param registerBills
	 * @param user
	 * @param operatorUser
	 * @return
	 */
	public List<Long> createBillList(List<CreateRegisterBillInputDto> registerBills, User user,
			OperatorUser operatorUser);

	public BasePage<RegisterBill> listPageBeforeCheckinVerifyBill(RegisterBillDto query);

	public BasePage<RegisterBill> listPageAfterCheckinVerifyBill(RegisterBillDto query);

	/**
	 * 统计不同审核状态报备单数据
	 * 
	 * @param query
	 * @return
	 */
	public List<VerifyStatusCountOutputDto> countByVerifyStatuseBeforeCheckin(RegisterBillDto query);

	/**
	 * 统计不同审核状态报备单数据
	 * 
	 * @param query
	 * @return
	 */
	public List<VerifyStatusCountOutputDto> countByVerifyStatuseAfterCheckin(RegisterBillDto query);

	/**
	 * 根据用户最新报备单审核状态更新颜色码
	 * 
	 * @param userId
	 */
	public void updateUserQrStatusByUserId(Long userId);

	/**
	 * 根据报备单数量更新用户状态到黑码
	 * 
	 * @param createdStart
	 * @param createdEnd
	 */
	public void updateAllUserQrStatusByRegisterBillNum(Date createdStart, Date createdEnd);


	public RegisterBillOutputDto viewTradeDetailBill(Long billId,Long tradeDetailId);
}