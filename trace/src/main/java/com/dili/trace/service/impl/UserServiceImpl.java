package com.dili.trace.service.impl;

import cn.hutool.core.collection.CollUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.common.entity.ExecutionConstants;
import com.dili.common.exception.BusinessException;
import com.dili.common.service.RedisService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dao.UserMapper;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserTallyArea;
import com.dili.trace.dto.UserListDto;
import com.dili.trace.glossary.EnabledStateEnum;
import com.dili.trace.glossary.YnEnum;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserService;
import com.dili.trace.service.UserTallyAreaService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private UserTallyAreaService userTallyAreaService;
    @Resource
    UserPlateService userPlateService;;

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
        if(StringUtils.isNotBlank(user.getTallyAreaNos()) ){
            existsTallyAreaNo(null, Arrays.asList(user.getTallyAreaNos().split(",")));
        }

        //验证身份证号是否已注册
        if(existsCardNo(user.getCardNo())){
            throw new BusinessException("身份证号已注册");
        }
      
        insertSelective(user);
        //更新用户理货区
        updateUserTallyArea(user.getId(),Arrays.asList(user.getTallyAreaNos().split(",")));
        //增加车牌信息
        List<String>plateList=this.parsePlate(user.getPlates());
        if(!plateList.isEmpty()) {
        	boolean isEmpty=this.userPlateService.findUserPlateByPlates(plateList).isEmpty();
        	if(!isEmpty) {
        		throw new BusinessException("车牌已被其他用户使用");
        	}
        	this.userPlateService.deleteAndInsertUserPlate(user.getId(), plateList);
        }
    }

    /**
     * 更新用户理货区
     * @param userId
     * @param tallyAreaNos
     */
    private void updateUserTallyArea(Long userId,List<String> tallyAreaNos){
        if(null != userId){
            UserTallyArea query = DTOUtils.newDTO(UserTallyArea.class);
            query.setUserId(userId);
            userTallyAreaService.deleteByExample(query);
        }

        List<UserTallyArea> userTallyAreas = new ArrayList<>();
        tallyAreaNos.forEach(tallyAreaNo->{
            UserTallyArea userTallyArea = DTOUtils.newDTO(UserTallyArea.class);
            userTallyArea.setUserId(userId);
            userTallyArea.setTallyAreaNo(tallyAreaNo);
            userTallyAreas.add(userTallyArea);
        });

        userTallyAreaService.batchInsert(userTallyAreas);
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

        User userPO = get(user.getId());
        if(EnabledStateEnum.ENABLED.getCode().equals(userPO.getState())){
            existsTallyAreaNo(user.getId(),Arrays.asList(user.getTallyAreaNos().split(",")));
            //更新用户理货区
            updateUserTallyArea(user.getId(),Arrays.asList(user.getTallyAreaNos().split(",")));
        }
        List<String>plateList=this.parsePlate(user.getPlates());
        if(!plateList.isEmpty()) {
        	boolean otherHasPlate=this.userPlateService.findUserPlateByPlates(plateList).stream().anyMatch(p->{
        		return !p.getUserId().equals(userPO.getId());
        	});
        	if(otherHasPlate) {
        		throw new BusinessException("车牌已被其他用户使用");
        	}
        	this.userPlateService.deleteAndInsertUserPlate(userPO.getId(), plateList);
        }
        updateSelective(user);


    }
    private List<String>parsePlate(String plates){
    	List<String>plateList=new ArrayList<>();
    	if(JSON.isValid(plates)) {
    		if(JSON.isValidArray(plates)) {
    			JSON.parseArray(plates).stream().filter(Objects::nonNull).map(String::valueOf).collect(Collectors.toCollection(()->plateList));
    		}else {
    			
    			plateList.add(JSON.parseObject(plates).toString());
    		}
    		
    	}

    	
        	return plateList.stream()
        			.filter(StringUtils::isNotBlank)
        			.map(String::trim)
        			.collect(Collectors.toList());
    	
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
     * 检测理货区号是否已注册
     * @param userId
     * @param tallyAreaNos
     */
    public void existsTallyAreaNo(Long userId,List<String> tallyAreaNos){
        tallyAreaNos.forEach(tallyAreaNo->{
            UserTallyArea query = DTOUtils.newDTO(UserTallyArea.class);
            query.setTallyAreaNo(tallyAreaNo);
            UserTallyArea userTallyArea = userTallyAreaService.listByExample(query).stream().findFirst().orElse(null);
            if (null != userTallyArea && !userTallyArea.getUserId().equals(userId)) {
                throw new BusinessException("理货区号【"+tallyAreaNo+"】已被注册");
            }
        });
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
        List<String> tallyAreaNos = Arrays.asList(user.getTallyAreaNos().split(","));
        if (enable) {
            user.setState(EnabledStateEnum.ENABLED.getCode());
            this.updateSelective(user);

            //验证理货区号是否已注册，未注册则添加用户理货区关系
            existsTallyAreaNo(user.getId(),tallyAreaNos);
            List<UserTallyArea> userTallyAreas = new ArrayList<>();
            tallyAreaNos.forEach(tallyAreaNo->{
                UserTallyArea userTallyArea = DTOUtils.newDTO(UserTallyArea.class);
                userTallyArea.setUserId(user.getId());
                userTallyArea.setTallyAreaNo(tallyAreaNo);
                userTallyAreas.add(userTallyArea);
            });
            userTallyAreaService.batchInsert(userTallyAreas);
            redisService.setRemove(ExecutionConstants.WAITING_DISABLED_USER_PREFIX, id);
        } else {
            user.setState(EnabledStateEnum.DISABLED.getCode());
            this.updateSelective(user);

            //删除用户理货区关系
            UserTallyArea userTallyAreaQuery = DTOUtils.newDTO(UserTallyArea.class);
            userTallyAreaQuery.setUserId(id);
            userTallyAreaService.deleteByExample(userTallyAreaQuery);
            redisService.sSet(ExecutionConstants.WAITING_DISABLED_USER_PREFIX,id);
        }

        return BaseOutput.success("操作成功");
    }

    @Override
    public User findByTallyAreaNo(String tallyAreaNo) {
        UserTallyArea query = DTOUtils.newDTO(UserTallyArea.class);
        query.setTallyAreaNo(tallyAreaNo);
        UserTallyArea userTallyArea = userTallyAreaService.list(query).stream().findFirst().orElse(null);
        if (null == userTallyArea){
            return null;
        }

        User userQuery = DTOUtils.newDTO(User.class);
        userQuery.setId(userTallyArea.getUserId());
        userQuery.setState(EnabledStateEnum.ENABLED.getCode());
        return list(userQuery).stream().findFirst().orElse(null);
    }
}