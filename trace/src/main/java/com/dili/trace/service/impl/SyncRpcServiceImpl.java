package com.dili.trace.service.impl;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.util.MD5Util;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.sdk.domain.TallyingArea;
import com.dili.customer.sdk.rpc.TallyingAreaRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.TallyAreaNo;
import com.dili.trace.enums.MarketEnum;
import com.dili.trace.enums.ValidateStateEnum;
import com.dili.trace.service.MarketService;
import com.dili.trace.service.SyncRpcService;
import com.dili.trace.service.UserService;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.dto.UserQuery;
import com.dili.uap.sdk.rpc.UserRpc;
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
    private UserRpc userRpc;
    @Resource
    private TallyingAreaRpc tallyingAreaRpc;
    @Autowired
    private UserService userService;
    @Autowired
    private MarketService marketService;
    @Autowired
    private DefaultConfiguration defaultConfiguration;

    @Override
    public void syncRpcUserByMarketId(String marketCode) {
        //市场
        Firm market = getMarketByCode(marketCode);
        //rpc用户列表
        BaseOutput<List<User>> resultData = getPpcUserList(marketCode);
        if (null != resultData) {
            List<User> userList = resultData.getData();
            //查询当前市场的所有用户
            List<com.dili.trace.domain.User> list = getUserByMarketId(market.getId());
            updateUserByRpcUserList(userList, list);
        }
    }

    @Override
    public void syncRpcUserByUserIds(List<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        BaseOutput<List<User>> rpcUserByIds = userRpc.listUserByIds(userIds);
        List<Long> idList = StreamEx.of(userIds).nonNull().map(u -> Long.valueOf(u)).collect(Collectors.toList());
        List<com.dili.trace.domain.User> userList = userService.getUserListByUserIds(idList);
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
        BaseOutput<User> resultData = getRpcUserById(userId);
        //未获取到rpc用户
        if (null == resultData) {
            return;
        }
        User rpcUser = resultData.getData();
        if (null != rpcUser) {
            com.dili.trace.domain.User user = userService.get(userId);
            //取需要更新的用户信息updateList
            if (null != user) {
                com.dili.trace.domain.User upUser = buildUpdateUser(rpcUser);
                updateUserInfo(upUser);
            } else {
                com.dili.trace.domain.User addUser = buildAddUser(rpcUser);
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
    private void updateUserByRpcUserList(List<User> userList, List<com.dili.trace.domain.User> list) {
        Map<Long, com.dili.trace.domain.User> userMap = StreamEx.of(list).nonNull().collect(Collectors.toMap(com.dili.trace.domain.User::getId, Function.identity(), (a, b) -> a));
        //取需要更新的用户信息updateList
        List<com.dili.trace.domain.User> updateList = new ArrayList<>();
        List<com.dili.trace.domain.User> addList = new ArrayList<>();
        StreamEx.of(userList).nonNull().forEach(rpcUser -> {
            if (userMap.containsKey(rpcUser.getId())) {
                com.dili.trace.domain.User upUser = buildUpdateUser(rpcUser);
                updateList.add(upUser);
            } else {
                com.dili.trace.domain.User addUser = buildAddUser(rpcUser);
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
     * 远程用户列表
     *
     * @param marketCode
     * @return
     */
    private BaseOutput<List<User>> getPpcUserList(String marketCode) {
        User user = DTOUtils.newDTO(User.class);
        user.setFirmCode(marketCode);
        return userRpc.list(user);
    }

    /**
     * 获取市场信息
     *
     * @param marketCode
     * @return
     */
    private Firm getMarketByCode(String marketCode) {
        return marketService.getMarketByCode(MarketEnum.valueOf(marketCode));
    }

    /**
     * 按市场id获取用户列表
     *
     * @param marketId
     * @return
     */
    private List<com.dili.trace.domain.User> getUserByMarketId(Long marketId) {
        com.dili.trace.domain.User queUser = getNewUserPojo();
        queUser.setMarketId(marketId);
        queUser.setYn(YesOrNoEnum.YES.getCode());
        return userService.list(queUser);
    }


    /**
     * 根据rpc用户信息新增用户
     *
     * @param addUser
     */
    private void addUserInfoByRpc(com.dili.trace.domain.User addUser) {
        userService.insertSelective(addUser);
    }

    /**
     * 根据rpc用户信息更新用户
     *
     * @param upUser
     */
    private void updateUserInfo(com.dili.trace.domain.User upUser) {
        userService.updateSelective(upUser);
    }

    /**
     * 返回prc用户信息
     *
     * @param userId
     * @return
     */
    private BaseOutput<User> getRpcUserById(Long userId) {
        return userRpc.findUserById(userId);
    }


    /**
     * 构建新增用户实体
     *
     * @param rpcUser
     * @return
     */
    private com.dili.trace.domain.User buildAddUser(User rpcUser) {
        Integer version = 0;
        com.dili.trace.domain.User addUser = getNewUserPojo();
        addUser.setId(rpcUser.getId());
        addUser.setName(rpcUser.getUserName());
        addUser.setPhone(null == rpcUser.getCellphone() ? "''" : rpcUser.getCellphone());
        addUser.setCardNo(null == rpcUser.getCardNumber() ? "''" : rpcUser.getCardNumber());
        addUser.setState(EnabledStateEnum.ENABLED.getCode());
        addUser.setPassword(MD5Util.md5(this.defaultConfiguration.getPassword()));
        addUser.setYn(YesOrNoEnum.YES.getCode());
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
    private com.dili.trace.domain.User buildUpdateUser(User rpcUser) {
        com.dili.trace.domain.User upUser = getNewUserPojo();
        upUser.setId(rpcUser.getId());
        upUser.setName(rpcUser.getUserName());
        upUser.setPhone(null == rpcUser.getCellphone() ? "''" : rpcUser.getCellphone());
        upUser.setCardNo(null == rpcUser.getCardNumber() ? "''" : rpcUser.getCardNumber());
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
    private static com.dili.trace.domain.User getNewUserPojo() {
        return DTOUtils.newDTO(com.dili.trace.domain.User.class);
    }

    /**
     * 同步用户摊位号
     *
     * @param userList
     */
    private void syncUserArea(List<com.dili.trace.domain.User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }
        Set<Long> userIdSet = StreamEx.of(userList).nonNull().map(com.dili.trace.domain.User::getId).collect(Collectors.toSet());
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

    }
}
