package com.dili.trace.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.api.input.CreateRegisterHeadInputDto;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.RegisterHead;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.dto.RegisterHeadDto;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
public interface RegisterBillService extends BaseService<RegisterBill, Long> {
    /**
     * 通过ID悲观锁定数据
     */
	public Optional<RegisterBill> selectByIdForUpdate(Long id);

	/**
	 * 查询并检查报备单子是否删除
	 */
	public Optional<RegisterBill> getAndCheckById(Long billId);

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
	Long createRegisterBill(RegisterBill registerBill, List<ImageCert> imageCertList, Optional<OperatorUser> operatorUser);

	/**
	 * 修改单个报备单
	 * 
	 * @param input
	 * @return
	 */
	public Long doEdit(RegisterBill registerBill,List<ImageCert> imageCertList,Optional<OperatorUser> operatorUser);

	/**
	 * 进门前审核
	 * 
	 * @param input
	 * @param operatorUser
	 * @return
	 */
	public Long doVerifyBeforeCheckIn(RegisterBill input, Optional<OperatorUser> operatorUser);

	/**
	 * 进门后审核
	 * 
	 * @param input
	 * @param operatorUser
	 * @return
	 */
	public Long doVerifyAfterCheckIn(Long billId,Integer verifyStatus,String reason,Optional<OperatorUser> operatorUser);

	/**
	 * 创建多个报备单
	 * 
	 * @param registerBills
	 * @param user
	 * @param operatorUser
	 * @return
	 */
	public List<Long> createBillList(List<CreateRegisterBillInputDto> registerBills, User user,
	Optional<OperatorUser> operatorUser);

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
	public void updateUserQrStatusByUserId(Long billId,Long userId);

	/**
	 * 根据报备单数量更新用户状态到黑码
	 * 
	 * @param createdStart
	 * @param createdEnd
	 */
	public void updateAllUserQrStatusByRegisterBillNum(Date createdStart, Date createdEnd);


	public RegisterBillOutputDto viewTradeDetailBill(Long billId,Long tradeDetailId);

	/**
	 * 删除报备单
	 * @param userId
	 * @param operatorUser
	 * @return
	 */
	public Long doDelete(Long billId,Long userId,Optional<OperatorUser> operatorUser) ;



	public Map<Integer, Map<String,List<RegisterBill>>> listPageCheckInData(RegisterBillDto query);

	/**
	 * 创建多个进门登记单
	 *
	 * @param registerBills
	 * @param user
	 * @param operatorUser
	 * @param marketId
	 * @return
	 */
	public List<Long> createRegisterFormBillList(List<CreateRegisterBillInputDto> registerBills, User user,
											 Optional<OperatorUser> operatorUser, Long marketId);

	/**
	 * 创建单个报备单
	 *
	 * @param registerBill
	 * @param imageCertList
	 * @param operatorUser
	 * @return
	 */
	Long createRegisterFormBill(RegisterBill registerBill, List<ImageCert> imageCertList, Optional<OperatorUser> operatorUser);

	/**
	 * 修改单个进门登记单
	 *
	 * @param registerBill
	 * @param imageCertList
	 * @param operatorUser
	 * @return
	 */
	public Long doEditFormBill(RegisterBill registerBill,List<ImageCert> imageCertList,Optional<OperatorUser> operatorUser);

	/**
	 * 进门登记单审核(通过/进门/不通过/退回/进门待检)
	 *
	 * @param input
	 * @param operatorUser
	 * @return
	 */
	public Long doVerifyFormCheckIn(RegisterBill input, Optional<OperatorUser> operatorUser);

	/**
	 *
	 * @Description
	 * 给小程序提供查询接口
	 * * @Date 2020/10/21 16:04
	 * @return
	 */
	public BasePage<RegisterBill> listPageApi(RegisterBillDto input);

	/**
	 * 删除进门登记单
	 * @param dto
	 * @param userId
	 * @param operatorUser
	 * @return
	 */
	public Long doDelete(CreateRegisterBillInputDto dto,Long userId,Optional<OperatorUser> operatorUser);
}