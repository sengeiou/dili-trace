package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;
import com.dili.trace.api.input.UserInput;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.api.output.UserQrOutput;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.UserListDto;
import com.dili.trace.glossary.UserTypeEnum;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
public interface UserService extends BaseService<User, Long> {


    /**
     * 登录
     *
     * @param phone             账号
     * @param encryptedPassword MD5加密密码
     * @return
     */
    User login(String phone, String encryptedPassword);


    /**
     * 根据手机号查询用户
     *
     * @param phone
     * @return
     */
    List<User> getUserByExistsAccount(String phone);


    /**
     * @param dto
     * @return
     * @throws Exception
     */
    public EasyuiPageOutput listEasyuiPageByExample(UserListDto dto) throws Exception;


    /**
     * @param keyword
     * @return
     */
    public List<User> findUserByNameOrPhoneOrTallyNo(String keyword);

    /**
     * @param user
     * @return
     */
    public Integer countUser(User user);

    /**
     * @param openid
     * @return
     */
    User wxLogin(String openid);

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
    User findByTallyAreaNo(String tallyAreaNo, Long marketId);

    /**
     * 获取无照片的经营户
     *
     * @param user
     * @return
     */
    List<User> getUserByCredentialUrl(User user);

    /**
     * 根据用户ids获取用户list
     *
     * @param userIdList
     * @return
     */
    List<User> getUserListByUserIds(List<Long> userIdList);
}