package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.api.input.UserInput;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.api.output.UserQrOutput;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.UserListDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

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

    public UserQrOutput getUserQrCode(Long userId) throws Exception;

    public List<User> findUserByQrStatusList(List<Integer> qrStatusList);

    public void renewPassword(User user, String smscode);

    public Integer countUser(User user);

    User wxLogin(String openid);

    /**
     * 微信一键注册
     * @return
     * @throws JsonProcessingException
     */
    String wxRegister(User user) throws JsonProcessingException;

    /**
     * 微信绑定用户
     *
     * @param openid
     * @param user_id
     */
    void userBindWeChat(String openid, Long user_id);

    /**
     * 确认今日不再弹出微信绑定提示
     *
     * @param user_id
     */
    void confirmBindWeChatTip(String user_id);

    /**
     * 根据店铺名查找user
     *
     * @param queryCondition
     * @return
     */
    List<UserOutput> listUserByStoreName(Long userId, String queryCondition, Long marketId);

    /**
     * 根据店铺名查找user
     *
     * @param userId
     * @return
     */
    UserOutput getUserByUserId(Long userId);

    /**
     * 生成带店铺名的二维码
     *
     * @param userId
     * @return
     * @throws Exception
     */
    UserQrOutput getUserQrCodeWithName(Long userId) throws Exception;

    /**
     * 根据用户id修改
     *
     * @param isPush
     * @param userIdList
     */
    void updateUserIsPushFlag(Integer isPush, List<Long> userIdList);

    /**
     * 根据时间更新用户活跃标志位
     */
    void updateUserActiveByTime();

    /**
     * 通过姓名/手机号/经营户卡号关键字查询用户信息
     *
     * @param input
     */
    public BasePage<User> findUserByKeyword(UserListDto input);
}