package com.dili.trace.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.assets.sdk.dto.CategoryDTO;
import com.dili.assets.sdk.dto.CusCategoryQuery;
import com.dili.assets.sdk.dto.SubjectQuery;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.common.config.DefaultConfiguration;
import com.dili.common.util.MD5Util;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.domain.dto.CustomerQueryInput;
import com.dili.customer.sdk.domain.dto.CustomerSimpleExtendDto;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.customer.sdk.rpc.TallyingAreaRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.User;
import com.dili.trace.domain.hangguo.HangGuoCategory;
import com.dili.trace.enums.ValidateStateEnum;
import com.dili.trace.service.CategoryService;
import com.dili.trace.service.SyncRpcService;
import com.dili.trace.service.UserService;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author asa.lee
 */
@Service
public class SyncRpcServiceImpl implements SyncRpcService {

    private static final Logger logger = LoggerFactory.getLogger(SyncRpcServiceImpl.class);

    @Resource
    private CustomerRpc customerRpc;
    @Resource
    private AssetsRpc assetsRpc;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DefaultConfiguration defaultConfiguration;

    private Integer pushFlag = 1;
    private Integer userYNn = -1;

    @Override
    public void syncRpcUserByUserIds(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        CustomerQueryInput customerQueryInput = new CustomerQueryInput();
        Set<Long> userSet = new HashSet<>(userIds);
        customerQueryInput.setIdSet(userSet);
        BaseOutput<List<CustomerExtendDto>> rpcUserByIds = customerRpc.list(customerQueryInput);
        List<User> userList = userService.getUserListByUserIds(userIds);
        if (null == rpcUserByIds) {
            return;
        }
        updateUserByRpcUserList(rpcUserByIds.getData(), userList);
    }

    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void syncGoodsToRpcCategory(Long categoryId) {
        BaseOutput<CategoryDTO> baseOutput = assetsRpc.get(categoryId);

        if(Objects.nonNull(baseOutput)){
            CategoryDTO dto = baseOutput.getData();
            HangGuoCategory category = categoryService.get(categoryId);
            if(Objects.isNull(category)){
                HangGuoCategory syCategory=buildCreatCategory(dto);
                categoryService.insertSelective(syCategory);
            }else{
                HangGuoCategory syCategory=buildUpdateCategory(dto);
                categoryService.updateSelective(syCategory);
            }
        }
    }

    /**
     * 通过rpc构建新增商品
     * @param dto
     * @return
     */
    private HangGuoCategory buildCreatCategory(CategoryDTO dto) {
        HangGuoCategory category = buildUpdateCategory(dto);
        category.setCreated(DateUtils.getCurrentDate());
        return category;
    }

    /**
     * 通过rpc更新商品
     * @param dto
     * @return
     */
    private HangGuoCategory buildUpdateCategory(CategoryDTO dto) {
        HangGuoCategory category = new HangGuoCategory();
        category.setId(dto.getId());
        category.setMarketId(dto.getMarketId());
        category.setName(dto.getName());
        category.setParentId(dto.getParent());
        category.setFullName(dto.getCusName());
        category.setModified(DateUtils.getCurrentDate());
        return category;
    }
    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void syncRpcUserByUserId(Long userId) {
        //rpc用户列表
        BaseOutput<List<CustomerExtendDto>> resultData = getRpcUserById(userId);
        //未获取到rpc用户
        if (null == resultData) {
            return;
        }
        List<CustomerExtendDto> rpcUserList =   resultData.getData();
        if (CollectionUtils.isNotEmpty(rpcUserList)) {
            StreamEx.of(rpcUserList).nonNull().forEach(rpcUser->{
                //用户没有id或者没有市场时不同步
                if(Objects.isNull(rpcUser.getId())||Objects.isNull(rpcUser.getCustomerMarket())){
                    return;
                }
                User user = getNewUserPojo();
                user.setId(rpcUser.getId());
                user.setMarketId(rpcUser.getCustomerMarket().getMarketId());
                List<User> userList = userService.list(user);
                if(CollectionUtils.isNotEmpty(userList)){
                    User upUser = buildUpdateUser(rpcUser);
                    updateUserInfo(upUser);
                }else{
                    User addUser = buildAddUser(rpcUser);
                    addUserInfoByRpc(addUser);
                }
            });

        }
    }


    /**
     * 同步rpc用户信息到溯源用户信息
     *
     * @param userList
     * @param list
     */
    private void updateUserByRpcUserList(List<CustomerExtendDto> userList, List<User> list) {
        Map<Long, User> userMap = StreamEx.of(list).nonNull().collect(Collectors.toMap(User::getId, Function.identity(), (a, b) -> a));
        //取需要更新的用户信息updateList
        List<User> updateList = new ArrayList<>();
        List<User> addList = new ArrayList<>();
        StreamEx.of(userList).nonNull().forEach(rpcUser -> {
            if (userMap.containsKey(rpcUser.getId())) {
                User upUser = buildUpdateUser(rpcUser);
                updateList.add(upUser);
            } else {
                User addUser = buildAddUser(rpcUser);
                addList.add(addUser);
            }
        });
        if (CollectionUtils.isNotEmpty(updateList)) {
            userService.batchUpdateSelective(updateList);
        }
        if (CollectionUtils.isNotEmpty(addList)) {
            userService.batchInsert(addList);
        }
    }

    /**
     * 根据rpc用户信息新增用户
     *
     * @param addUser
     */
    private void addUserInfoByRpc(User addUser) {
        userService.insertSelective(addUser);
    }

    /**
     * 根据rpc用户信息更新用户
     *
     * @param upUser
     */
    private void updateUserInfo(User upUser) {
        userService.updateSelective(upUser);
    }

    /**
     * 返回prc用户信息
     *
     * @param userId
     * @return
     */
    private BaseOutput<List<CustomerExtendDto>> getRpcUserById(Long userId) {
        CustomerQueryInput customerQueryInput = new CustomerQueryInput();
        customerQueryInput.setId(userId);
        return customerRpc.list(customerQueryInput);
    }


    /**
     * 构建新增用户实体
     *
     * @param rpcUser
     * @return
     */
    private User buildAddUser(CustomerExtendDto rpcUser) {
        Integer version = 0;
        User addUser = buildUpdateUser(rpcUser);
        addUser.setPassword(MD5Util.md5(this.defaultConfiguration.getPassword()));
        addUser.setVersion(version);
        addUser.setCreated(DateUtils.getCurrentDate());
        addUser.setModified(DateUtils.getCurrentDate());
        addUser.setValidateState(ValidateStateEnum.PASSED.getCode());
        return addUser;
    }

    /**
     * 构建更新用户实体
     *
     * @param rpcUser
     * @return
     */
    private User buildUpdateUser(CustomerExtendDto rpcUser) {
        User upUser = getNewUserPojo();
        upUser.setId(rpcUser.getId());
        upUser.setName(rpcUser.getName());
        upUser.setPhone(null == rpcUser.getContactsPhone() ? "''" : rpcUser.getContactsPhone());
        upUser.setCardNo(null == rpcUser.getCertificateNumber() ? "''" : rpcUser.getCertificateNumber());
        if (null != rpcUser.getIsDelete() && rpcUser.getIsDelete().equals(YesOrNoEnum.YES.getCode())) {
            upUser.setYn(userYNn);
        }
        //YesOrNoEnum.YES
        if(Objects.nonNull(rpcUser.getIsDelete())){
            upUser.setIsDelete(rpcUser.getIsDelete().longValue());
        }
        //未激活
        if (CustomerEnum.State.NORMAL.getCode().equals(rpcUser.getState())) {
            upUser.setState(EnabledStateEnum.ENABLED.getCode());
        } else {
            upUser.setState(EnabledStateEnum.DISABLED.getCode());
        }
        upUser.setIsPush(pushFlag);
        if(Objects.nonNull(rpcUser.getCustomerMarket())){
            upUser.setMarketId(rpcUser.getCustomerMarket().getMarketId());
        }
        return upUser;
    }

    /**
     * @return
     */
    private static User getNewUserPojo() {
        return DTOUtils.newDTO(User.class);
    }
}
