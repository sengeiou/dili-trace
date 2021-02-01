package com.dili.trace.async;

import com.dili.assets.sdk.dto.CusCategoryDTO;
import com.dili.assets.sdk.dto.CusCategoryQuery;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.trace.domain.UserInfo;
import com.dili.trace.domain.hangguo.HangGuoCategory;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.service.AssetsRpcService;
import com.dili.trace.service.CategoryService;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * 异步服务接口
 */
@Service
public class AsyncService {
    @Autowired
    AssetsRpcService assetsRpcService;
    @Autowired
    CustomerRpcService customerRpcService;

    /**
     * 异步查询客户信息
     *
     * @param userInfo
     * @param consumer
     */
    @Async
    public void syncUserInfo(UserInfo userInfo, Consumer<CustomerExtendDto> consumer) {
        this.customerRpcService.findCustomerById(userInfo.getUserId(), userInfo.getMarketId()).ifPresent(cust -> {
            consumer.accept(cust);
        });
    }

    /**
     * 异步查询品类信息
     *
     * @param category
     * @param consumer
     */
    @Async
    public void syncCategoryInfo(HangGuoCategory category, Consumer<CusCategoryDTO> consumer) {
        if (category == null || category.getMarketId() == null) {
            return;
        }
        CusCategoryQuery cusQuery = new CusCategoryQuery();
        cusQuery.setIds(Lists.newArrayList(String.valueOf(category.getId())));
        StreamEx.of(this.assetsRpcService.listCusCategory(cusQuery, category.getMarketId())).nonNull().findFirst().ifPresent(cate -> {
            consumer.accept(cate);
        });
    }
}
