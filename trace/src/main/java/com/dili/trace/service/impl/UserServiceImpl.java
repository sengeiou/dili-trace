package com.dili.trace.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.dili.common.exception.BusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dao.UserMapper;
import com.dili.trace.domain.User;
import com.dili.trace.glossary.EnabledStateEnum;
import com.dili.trace.glossary.YnEnum;
import com.dili.trace.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:35.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

    public UserMapper getActualDao() {
        return (UserMapper)getDao();
    }

    @Transactional
    @Override
    public void register(User user) {
        //@TODO验证验证码是否正确
//        if(!messageService.checkCode(user.getPhone(),user.getCheckCode())){
//            throw new BusinessException("验证码错误");
//        }
        //验证手机号是否已注册
        if(existsAccount(user.getPhone())){
            throw new BusinessException("账号已存在");
        }

        //验证理货区号是否已注册
        if(existsTaillyAreaNo(user.getTaillyAreaNo())){
            throw new BusinessException("理货区已存在");
        }

        insertSelective(user);
    }

    @Override
    public User login(String phone, String encryptedPassword) {
        User query= DTOUtils.newDTO(User.class);
        query.setPhone(phone);
        query.setState(EnabledStateEnum.ENABLED.getCode());
        query.setYn(YnEnum.YES.getCode());
        User po=listByExample(query).stream().findFirst().orElse(null);
        if(po==null){
            throw new BusinessException("手机号或密码错误");
        }
        if(!po.getPassword().equals(encryptedPassword)){
            throw new BusinessException("手机号或密码错误");
        }
        return po;
    }

    @Transactional
    @Override
    public void resetPassword(User user) {
        //@TODO        if(!messageService.checkCode(user.getPhone(),user.getCheckCode())){
//@TODO            throw new BusinessException("验证码错误");
// @TODO       }
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
}