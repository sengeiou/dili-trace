package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.*;
import com.dili.trace.domain.hangguo.HangGuoTrade;
import com.dili.trace.domain.hangguo.HangGuoUser;

import java.util.List;
import java.util.Map;

/**
 * @author asa.lee
 */
public interface HangGuoDataService extends BaseService<HangGuoUser, Long> {

    /**
     * 杭果商品信息存储到溯源系统
     *
     * @param commodityList
     */
    void bachInsertCommodityList(List<Category> commodityList);

    /**
     * 杭果商品插入后更新parentId
     *
     * @param category
     */
    void updateHangGuoCommodityParent(Category category);

    /**
     * 根据第三方编码获取用户更新列表
     *
     * @param list
     * @return
     */
    List<User> getUserListByThirdPartyCode(List<String> list);

    /**
     * 根据第三方编码更新用户
     *
     * @param updateUserList
     */
    void batchUpdateUserByThirdCode(List<User> updateUserList);

    /**
     * 根据商品编码获取商品列表
     *
     * @param codeList
     * @return
     */
    List<Category> getCategoryListByThirdCode(List<String> codeList);

    /**
     * 根据第三方编码删除商品
     *
     * @param categoryList
     */
    void deleteHangGuoCommodityByThirdCode(List<Category> categoryList);

    /**
     * 插入交易缓存数据
     *
     * @param tradeList
     */
    void batchInsertCacheTradeList(List<HangGuoTrade> tradeList);

    /**
     * 根据报备单id获取报备详情
     *
     * @param billIds
     * @return
     */
    List<TradeDetail> getBillTradeDetailListByBillIds(List<String> billIds);

    /**
     * 根据ids获取stock详情
     *
     * @param stockIdList
     * @return
     */
    List<ProductStock> getProductStockListByIds(List<Long> stockIdList);

    /**
     * 更新超过金额标志位
     *
     * @return
     */
    void updateTradeReportListByBeyondAmount();

    /**
     * 查询的交易数据
     *
     * @param trade
     * @return
     */
    List<HangGuoTrade> selectTradeReportListByHandleFlag(HangGuoTrade trade);

    /**
     * 根据报备单id获取报备单号
     *
     * @param ids
     * @return
     */
    List<RegisterBill> getRegisterBillByIds(List<String> ids);

    /**
     * 批量插入tradeList（由于Column同名导致batchinsert无法调用）
     *
     * @param addDetailList
     */
    void batchInsertTradeDetail(List<TradeDetail> addDetailList);

    /**
     * 更新交易数据处理标志位
     *
     * @param map
     */
    void batchUpdateCacheTradeHandleFlag(Map<String, Object> map);
}
