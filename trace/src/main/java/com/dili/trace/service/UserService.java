package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.User;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:35.
 */
public interface UserService extends BaseService<User, Long> {

    /**
     * 注册
     * @param user
     */
    void register(User user);

    /**
     * 登录
     * @param phone 账号
     * @param encryptedPassword MD5加密密码
     * @return
     */
    User login(String phone, String encryptedPassword);

    /**
     * 重置密码
     * @param user
     */
    void resetPassword(User user);

    /**
     * 修改密码
     * @param user
     */
    void changePassword(User user);

    /**
     * 判断手机号是否存在
     * @param phone
     * @return
     */
    boolean existsAccount(String phone);

    /**
     * 根据用户ID，操作启禁用
     * @param id
     * @param enable 是否启用(true-启用，false-禁用)
     * @return
     */
    BaseOutput updateEnable(Long id, Boolean enable);
    User findByTaillyAreaNo(String taillyAreaNo);
}