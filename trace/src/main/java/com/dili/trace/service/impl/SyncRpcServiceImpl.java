package com.dili.trace.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.common.config.DefaultConfiguration;
import com.dili.common.util.MD5Util;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.DateUtils;
import com.dili.trace.enums.MarketEnum;
import com.dili.trace.enums.ValidateStateEnum;
import com.dili.trace.provider.YNEnumStateProvider;
import com.dili.trace.service.MarketService;
import com.dili.trace.service.SyncRpcService;
import com.dili.trace.service.UserService;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.rpc.UserRpc;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author asa.lee
 */
@Service
public class SyncRpcServiceImpl implements SyncRpcService {

    @Autowired
    private UserRpc userRpc;
    @Autowired
    private UserService userService;
    @Autowired
    private MarketService marketService;
    @Autowired
    DefaultConfiguration defaultConfiguration;

    @Override
    public List<User> syncRpcUserByMarketId(String marketCode) {
        Firm market = marketService.getMarketByCode(MarketEnum.valueOf(marketCode));
        User user = DTOUtils.newDTO(User.class);
        user.setFirmCode(marketCode);
        Integer version = 0;
        BaseOutput<List<User>> resultData = userRpc.list(user);
        if (null != resultData) {
            List<User> userList = resultData.getData();
            //查询当前市场的所有用户
            com.dili.trace.domain.User queUser = getNewUserPojo();
            queUser.setMarketId(market.getId());
            queUser.setYn(YesOrNoEnum.YES.getCode());
            List<com.dili.trace.domain.User> list = userService.list(queUser);

            Map<Long, com.dili.trace.domain.User> userMap = StreamEx.of(list).nonNull().collect(Collectors.toMap(com.dili.trace.domain.User::getId, Function.identity(), (a, b) -> a));

            //取需要更新的用户信息updateList
            List<com.dili.trace.domain.User> updateList = new ArrayList<>();
            List<com.dili.trace.domain.User> addList = new ArrayList<>();
            StreamEx.of(userList).nonNull().forEach(rpcUser -> {
                if (userMap.containsKey(rpcUser.getId())) {
                    com.dili.trace.domain.User upUser = getNewUserPojo();
                    upUser.setId(rpcUser.getId());
                    upUser.setName(rpcUser.getUserName());
                    upUser.setCardNo(rpcUser.getCardNumber());
                    updateList.add(upUser);
                } else {
                    com.dili.trace.domain.User addUser = getNewUserPojo();
                    addUser.setId(rpcUser.getId());
                    addUser.setName(rpcUser.getUserName());
                    addUser.setCardNo(null==rpcUser.getCardNumber()?"''":rpcUser.getCardNumber());
                    addUser.setPhone(null==rpcUser.getCellphone()?"''":rpcUser.getCellphone());
                    addUser.setState(EnabledStateEnum.ENABLED.getCode());
                    addUser.setPassword(MD5Util.md5(this.defaultConfiguration.getPassword()));
                    addUser.setYn(YesOrNoEnum.YES.getCode());
                    addUser.setVersion(version);
                    addUser.setCreated(DateUtils.getCurrentDate());
                    addUser.setModified(DateUtils.getCurrentDate());
                    addUser.setValidateState(ValidateStateEnum.PASSED.getCode());
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
        return null;
    }

    private static com.dili.trace.domain.User getNewUserPojo() {
        return DTOUtils.newDTO(com.dili.trace.domain.User.class);
    }

}
