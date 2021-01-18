package com.dili.trace.service;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceAdaptor;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserStore;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.service.UserStoreService;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserStoreService extends BaseServiceImpl<UserStore, Long> {
    private static final Logger logger = LoggerFactory.getLogger(UserStoreService.class);
    @Autowired
    CustomerRpcService customerRpcService;

    public int insertOrUpdateStore(UserStore input) {
        if (input.getUserId() == null || StringUtils.isBlank(input.getUserName())) {
            throw new TraceBizException("用户信息错误");
        }
        if (input.getMarketId() == null || StringUtils.isBlank(input.getMarketName())) {
            throw new TraceBizException("市场信息错误");
        }
        if (input == null || StringUtils.isBlank(input.getStoreName())) {
            throw new TraceBizException("用户店铺名称不能为空");
        }


        UserStore query = new UserStore();
        query.setMarketId(input.getMarketId());
        query.setStoreName(input.getStoreName().trim());
        List<UserStore> userStoreList = this.listByExample(query);
        boolean otherUserHasThisStoreName=StreamEx.of(userStoreList).map(UserStore::getUserId).anyMatch(uid->!uid.equals(input.getUserId()));
        if(otherUserHasThisStoreName){
            throw new TraceBizException("店铺已经存在");
        }

        if (userStoreList.isEmpty()) {
            input.setCreated(new Date());
            input.setModified(new Date());
            return this.insertSelective(input);
        } else {
            UserStore userStoreItem = userStoreList.get(0);
            input.setId(userStoreItem.getId());
            input.setModified(new Date());
            return this.updateSelective(input);
        }
    }
}
