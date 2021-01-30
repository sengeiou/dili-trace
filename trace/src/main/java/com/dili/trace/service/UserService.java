package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.api.input.UserQueryDto;
import com.dili.trace.domain.UserInfo;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
public interface UserService extends BaseService<UserInfo, Long> {


    /**
     * 登录
     *
     * @param phone             账号
     * @param encryptedPassword MD5加密密码
     * @return
     */
    UserInfo login(String phone, String encryptedPassword);


    /**
     * 根据手机号查询用户
     *
     * @param phone
     * @return
     */
    List<UserInfo> getUserByExistsAccount(String phone);


    /**
     * @param dto
     * @return
     * @throws Exception
     */
    public EasyuiPageOutput listEasyuiPageByExample(UserQueryDto dto) throws Exception;


    /**
     * @param keyword
     * @return
     */
    public List<UserInfo> findUserByNameOrPhoneOrTallyNo(String keyword);

    /**
     * @param user
     * @return
     */
    public Integer countUser(UserInfo user);

    /**
     * @param openid
     * @return
     */
    UserInfo wxLogin(String openid);

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
     * 查询用户
     *
     * @param tallyAreaNo
     * @param marketId
     * @return
     */
    UserInfo findByTallyAreaNo(String tallyAreaNo, Long marketId);

    /**
     * 获取无照片的经营户
     *
     * @param user
     * @return
     */
    List<UserInfo> getUserByCredentialUrl(UserInfo user);

    /**
     * 根据用户ids获取用户list
     *
     * @param userIdList
     * @return
     */
    List<UserInfo> getUserListByUserIds(List<Long> userIdList);
}