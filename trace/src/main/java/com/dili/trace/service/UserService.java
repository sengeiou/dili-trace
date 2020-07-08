package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.trace.api.input.UserInput;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.UserListDto;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
public interface UserService extends BaseService<User, Long> {

	/**
	 * 注册 flag 是否有验证码
	 * 
	 * @param user
	 */
	void register(User user, Boolean flag);

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
	 * 根据手机号查询用户
	 *
	 * @param phone
	 * @return
	 */
	List<User> getUserByExistsAccount(String phone);

	/**
	 * 根据用户ID，操作启禁用
	 * 
	 * @param id
	 * @param enable 是否启用(true-启用，false-禁用)
	 * @return
	 */
	BaseOutput updateEnable(Long id, Boolean enable);


	public EasyuiPageOutput listEasyuiPageByExample(UserListDto dto) throws Exception;
	
	/**
	 * 删除用户信息
	 * 
	 * @param user
	 */
	BaseOutput deleteUser(Long id);

	BaseOutput<List<UserOutput>> countGroupByValidateState(User user);

	BasePage<UserOutput> pageUserByQuery(UserInput user);

	BaseOutput verifyUserCert(UserInput input, OperatorUser operatorUser);

	public List<User> findUserByNameOrPhoneOrTallyNo(String keyword);
}