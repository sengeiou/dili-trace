package com.dili.trace.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.common.config.DefaultConfiguration;
import com.dili.common.entity.ExecutionConstants;
import com.dili.common.entity.PatternConstants;
import com.dili.common.exception.BusinessException;
import com.dili.common.service.RedisService;
import com.dili.common.util.MD5Util;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.trace.dao.UserMapper;
import com.dili.trace.domain.Customer;
import com.dili.trace.domain.User;
import com.dili.trace.glossary.EnabledStateEnum;
import com.dili.trace.glossary.YnEnum;
import com.dili.trace.rpc.MessageRpc;
import com.dili.trace.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:35.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

    public UserMapper getActualDao() {
        return (UserMapper)getDao();
    }

    @Resource
    private RedisService redisService;
    @Resource
    private DefaultConfiguration defaultConfiguration;

    @Transactional(rollbackFor=Exception.class)
    @Override
    public void register(User user,Boolean flag) {
        //验证验证码是否正确
        if(flag){
            checkVerificationCode(user.getPhone(),user.getCheckCode());
        }

        //验证手机号是否已注册
        if(existsAccount(user.getPhone())){
            throw new BusinessException("手机号已注册");
        }

        //验证理货区号是否已注册
        if(StringUtils.isNotBlank(user.getTaillyAreaNo()) && existsTaillyAreaNo(user.getTaillyAreaNo())){
            throw new BusinessException("理货区号已注册");
        }

        //验证身份证号是否已注册
        if(existsCardNo(user.getCardNo())){
            throw new BusinessException("身份证号已注册");
        }

        insertSelective(user);
    }

    @Override
    public void updateUser(User user) {

        //手机号验重
        if(StringUtils.isNotBlank(user.getPhone())){
            User condition = DTOUtils.newDTO(User.class);
            condition.setPhone(user.getPhone());
            List<User> users = listByExample(condition);
            if(CollectionUtils.isNotEmpty(users)){
                users.forEach(o->{
                    if(!o.getId().equals(user.getId()) && o.getPhone().equals(user.getPhone())){
                        throw new BusinessException("手机号已注册");
                    }
                });
            }
        }
        //理货区号验重
        if(StringUtils.isNotBlank(user.getTaillyAreaNo())){
            User condition = DTOUtils.newDTO(User.class);
            condition.setTaillyAreaNo(user.getTaillyAreaNo());
            List<User> users = listByExample(condition);
            if(CollectionUtils.isNotEmpty(users)){
                users.forEach(o->{
                    if(!o.getId().equals(user.getId()) && o.getTaillyAreaNo().equals(user.getTaillyAreaNo())){
                        throw new BusinessException("理货区号已注册");
                    }
                });
            }
        }
        updateSelective(user);
    }

    private Boolean checkVerificationCode(String phone, String verCode){
        String verificationCodeTemp = String.valueOf(redisService.get(ExecutionConstants.REDIS_SYSTEM_VERCODE_PREIX + phone));
        if (StringUtils.isBlank(verificationCodeTemp)) {
            throw new BusinessException("验证码已过期");
        }
        if (verificationCodeTemp.equals(verCode)) {
            redisService.del(ExecutionConstants.REDIS_SYSTEM_VERCODE_PREIX + phone);
            return true;
        }else{
            throw new BusinessException("验证码不正确");
        }
    }

    @Override
    public User login(String phone, String encryptedPassword) {
        User query= DTOUtils.newDTO(User.class);
        query.setPhone(phone);
        query.setYn(YnEnum.YES.getCode());
        User po=listByExample(query).stream().findFirst().orElse(null);
        if(po == null){
            throw new BusinessException("手机号未注册");
        }
        if(EnabledStateEnum.DISABLED.getCode().equals(po.getState())){
            throw new BusinessException("手机号已禁用");
        }
        if(!po.getPassword().equals(encryptedPassword)){
            throw new BusinessException("密码错误");
        }
        return po;
    }

    @Transactional
    @Override
    public void resetPassword(User user) {
        User query = DTOUtils.newDTO(User.class);
        query.setPhone(user.getPhone());
        query.setYn(YnEnum.YES.getCode());
        User po=listByExample(query).stream().findFirst().orElse(null);
        if(po==null){
            throw new BusinessException("用户不存在");
        }
        User condition = DTOUtils.newDTO(User.class);
        condition.setId(po.getId());
        condition.setVersion(po.getVersion());
        po.setPassword(user.getPassword());
        po.setVersion(po.getVersion()+1);
        int i=updateSelectiveByExample(po,condition);
        if(i != 1){
            throw new BusinessException("数据已变更,请稍后重试");
        }
    }

    @Transactional
    @Override
    public void changePassword(User user) {
        User po=get(user.getId());
        if(po==null){
            throw new BusinessException("用户不存在");
        }
        if(!po.getPassword().equals(user.getOldPassword())){
            throw new BusinessException("原密码错误");
        }
        User condition = DTOUtils.newDTO(User.class);
        condition.setId(po.getId());
        condition.setVersion(po.getVersion());
        po.setPassword(user.getPassword());
        po.setVersion(po.getVersion()+1);
        int i=updateSelectiveByExample(po,condition);
        if(i != 1){
            throw new BusinessException("数据已变更,请稍后重试");
        }
    }

    /**
     * 检测手机号是否存在
     * @param phone
     * @return true 存在 false 不存在
     */
    @Override
    public boolean existsAccount(String phone){
        User query = DTOUtils.newDTO(User.class);
        query.setPhone(phone);
        query.setYn(YnEnum.YES.getCode());
        return !CollUtil.isEmpty(listByExample(query));
    }

    /**
     * 检测手机号是否存在
     * @param taillyAreaNo
     * @return true 存在 false 不存在
     */
    public boolean existsTaillyAreaNo(String taillyAreaNo){
        User query = DTOUtils.newDTO(User.class);
        query.setTaillyAreaNo(taillyAreaNo);
        query.setYn(YnEnum.YES.getCode());
        return !CollUtil.isEmpty(listByExample(query));
    }

    /**
     * 检测身份证号是否存在
     * @param cardNo
     * @return true 存在 false 不存在
     */
    public boolean existsCardNo(String cardNo){
        User query = DTOUtils.newDTO(User.class);
        query.setCardNo(cardNo);
        query.setYn(YnEnum.YES.getCode());
        return !CollUtil.isEmpty(listByExample(query));
    }

    @Override
    @Transactional
    public BaseOutput updateEnable(Long id, Boolean enable) {
        long tt = redisService.getExpire(ExecutionConstants.WAITING_DISABLED_USER_PREFIX);
        User user = get(id);
        if (enable) {
            user.setState(EnabledStateEnum.ENABLED.getCode());
            this.updateSelective(user);
            redisService.setRemove(ExecutionConstants.WAITING_DISABLED_USER_PREFIX, id);
        } else {
            user.setState(EnabledStateEnum.DISABLED.getCode());
            this.updateSelective(user);
            redisService.sSet(ExecutionConstants.WAITING_DISABLED_USER_PREFIX,id);
        }

        return BaseOutput.success("操作成功");
    }

    @Override
    public User findByTaillyAreaNo(String taillyAreaNo) {
        User user = DTOUtils.newDTO(User.class);
        user.setTaillyAreaNo(taillyAreaNo);
        List<User> userList = list(user);
        if(userList.size()>0){
            return userList.get(0);
        }
        return null;
    }
}