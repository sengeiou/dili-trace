package com.dili.trace.service.impl;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.util.MD5Util;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.domain.TallyingArea;
import com.dili.customer.sdk.domain.dto.CustomerQueryInput;
import com.dili.customer.sdk.domain.dto.CustomerSimpleExtendDto;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.customer.sdk.rpc.TallyingAreaRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.User;
import com.dili.trace.enums.ClientTypeEnum;
import com.dili.trace.enums.ValidateStateEnum;
import com.dili.trace.service.SyncRpcService;
import com.dili.trace.service.UserService;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
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

    @Resource
    private CustomerRpc customerRpc;
    @Resource
    private TallyingAreaRpc tallyingAreaRpc;
    @Autowired
    private UserService userService;
    @Autowired
    private DefaultConfiguration defaultConfiguration;

    private Integer userYNy = 1;
    private Integer userYNn = -1;

    @Override
    public void syncRpcUserByUserIds(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        CustomerQueryInput customerQueryInput = new CustomerQueryInput();
        Set<Long> userSet = new HashSet<>(userIds);
        customerQueryInput.setIdSet(userSet);
        BaseOutput<List<CustomerSimpleExtendDto>> rpcUserByIds = customerRpc.listSimple(customerQueryInput);
        List<User> userList = userService.getUserListByUserIds(userIds);
        if (null == rpcUserByIds) {
            return;
        }
        updateUserByRpcUserList(rpcUserByIds.getData(), userList);
    }

    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void syncRpcUserByUserId(Long userId) {
        //rpc用户列表
        BaseOutput<Customer> resultData = getRpcUserById(userId);
        //未获取到rpc用户
        if (null == resultData) {
            return;
        }
        Customer rpcUser = resultData.getData();
        if (null != rpcUser) {
            User user = userService.get(userId);
            //取需要更新的用户信息updateList
            if (null != user) {
                User upUser = buildUpdateUser(rpcUser);
                updateUserInfo(upUser);
            } else {
                User addUser = buildAddUser(rpcUser);
                addUserInfoByRpc(addUser);
            }
        }
    }


    /**
     * 同步rpc用户信息到溯源用户信息
     *
     * @param userList
     * @param list
     */
    private void updateUserByRpcUserList(List<CustomerSimpleExtendDto> userList, List<User> list) {
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
    private BaseOutput<Customer> getRpcUserById(Long userId) {
        return customerRpc.getById(userId);
    }


    /**
     * 构建新增用户实体
     *
     * @param rpcUser
     * @return
     */
    private User buildAddUser(Customer rpcUser) {
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
    private User buildUpdateUser(Customer rpcUser) {
        User upUser = getNewUserPojo();
        upUser.setId(rpcUser.getId());
        upUser.setName(rpcUser.getName());
        upUser.setPhone(null == rpcUser.getContactsPhone() ? "''" : rpcUser.getContactsPhone());
        upUser.setCardNo(null == rpcUser.getCertificateNumber() ? "''" : rpcUser.getCertificateNumber());
        if (null != rpcUser.getIsDelete() && rpcUser.getIsDelete().equals(YesOrNoEnum.YES.getCode())) {
            upUser.setYn(userYNn);
        }
        //YesOrNoEnum.YES
        upUser.setIsDelete(rpcUser.getIsDelete().longValue());
        //未激活
        if (CustomerEnum.State.NORMAL.getCode().equals(rpcUser.getState())) {
            upUser.setState(EnabledStateEnum.ENABLED.getCode());
        } else {
            upUser.setState(EnabledStateEnum.DISABLED.getCode());
        }
        return upUser;
    }


    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void testAsync() throws InterruptedException {
        System.out.print("===>>>service 异步 执行 开始");
        TimeUnit.SECONDS.sleep(3);
        System.out.print("===>>>service 异步 执行 结束");
    }

    /**
     * @return
     */
    private static User getNewUserPojo() {
        return DTOUtils.newDTO(User.class);
    }

    /**
     * 同步用户摊位号
     *
     * @param userList
     */
    /*private void syncUserArea(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }
        Set<Long> userIdSet = StreamEx.of(userList).nonNull().map(User::getId).collect(Collectors.toSet());
        TallyingArea tallyingArea = new TallyingArea();
        tallyingArea.setCustomerIdSet(userIdSet);
        BaseOutput<List<TallyingArea>> listByExample = tallyingAreaRpc.listByExample(tallyingArea);
        if (null != listByExample && CollectionUtils.isNotEmpty(listByExample.getData())) {
            List<TallyingArea> rpcData = listByExample.getData();
            Date currentDate = DateUtils.getCurrentDate();
            StreamEx.of(rpcData).nonNull().map(rpcArea -> {
                TallyAreaNo area = DTOUtils.newDTO(TallyAreaNo.class);
                area.setArea(rpcArea.getAssetsName());
                area.setCreated(currentDate);
                area.setModified(currentDate);
                return area;
            }).collect(Collectors.toList());
        }

    }*/
}
