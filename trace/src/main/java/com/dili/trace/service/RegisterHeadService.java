package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.api.input.CreateRegisterHeadInputDto;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.RegisterHead;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterHeadDto;

import java.util.List;
import java.util.Optional;

/**
 * 进门主台账单接口
 *
 * @author Lily
 */
public interface RegisterHeadService extends BaseService<RegisterHead, Long> {

	public String listPage(RegisterHeadDto dto) throws Exception;

	/**
	 * 创建多个进门主台账单
	 *
	 * @param registerHeads
	 * @param user
	 * @param operatorUser
	 * @return
	 */
	public List<Long> createRegisterHeadList(List<CreateRegisterHeadInputDto> registerHeads, User user,
									 Optional<OperatorUser> operatorUser);

	/**
	 * 创建单个进门主台账单
	 *
	 * @param registerHead
	 * @param imageCertList
	 * @param operatorUser
	 * @return
	 */
	Long createRegisterHead(RegisterHead registerHead, List<ImageCert> imageCertList, Optional<OperatorUser> operatorUser);

	/**
	 * 修改单个进门主台账单
	 *
	 * @param registerHead
	 * @param imageCertList
	 * @param operatorUser
	 * @return
	 */
	public Long doEdit(RegisterHead registerHead, List<ImageCert> imageCertList, Optional<OperatorUser> operatorUser);

	/**
	 * 查询并检查报备单子是否删除
	 *
	 * @param id
	 */
	public Optional<RegisterHead> getAndCheckById(Long id);

	/**
	 * 作废进门主台账单
	 * @param id
	 * @param userId
	 * @param operatorUser
	 * @return
	 */
	public Long doDelete(Long id, Long userId, Optional<OperatorUser> operatorUser);

	/**
	 * 启用/关闭进门主台账单
	 * @param dto
	 * @param userId
	 * @param operatorUser
	 * @return
	 */
	public Long doUpdateActive(CreateRegisterHeadInputDto dto, Long userId, Optional<OperatorUser> operatorUser);
}