package com.dili.trace.service;

import java.util.List;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;
import com.dili.trace.domain.User;
import com.dili.trace.dto.UserListDto;
import com.dili.trace.glossary.UserTypeEnum;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
public interface UserService extends BaseService<User, Long> {

/**注册（新增）用户
 * 
 * @param user 用户信息
 * @param originalPassword 原始密码
 */
	void register(User user,UserTypeEnum userType,String originalPassword);

	/**
	 * 修改用户信息
	 * 
	 * @param user
	 */
	void updateUser(User user);

	/**
	 * 登录
	 * 
	 * @param phone             账号
	 * @param encryptedPassword MD5加密密码
	 * @return
	 */
	User login(String phone, String encryptedPassword);

	/**
	 * 重置密码
	 * 
	 * @param user
	 */
	void resetPassword(User user);

	/**
	 * 修改密码
	 * 
	 * @param user
	 */
	void changePassword(User user);

	/**
	 * 判断手机号是否存在
	 * 
	 * @param phone
	 * @return
	 */
	boolean existsAccount(String phone);

	/**
	 * 根据用户ID，操作启禁用
	 * 
	 * @param id
	 * @param enable 是否启用(true-启用，false-禁用)
	 * @return
	 */
	BaseOutput updateEnable(Long id, Boolean enable);

	/**
	 * 查询用户
	 * @param tallyAreaNo
	 * @return
	 */
	User findByTallyAreaNo(String tallyAreaNo);

	/**
	 * 分页查询用户信息
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public EasyuiPageOutput listEasyuiPageByExample(UserListDto dto) throws Exception;
	
	/**
	 * 删除用户信息
	 * 
	 * @param id
	 */
	BaseOutput deleteUser(Long id);
	
	/**
	 * 通过理货区号查询用户及车牌信息
	 * @param likeTallyAreaNo
	 * @return
	 */
	public List<DTO> queryByTallyAreaNo(String likeTallyAreaNo) ;
}