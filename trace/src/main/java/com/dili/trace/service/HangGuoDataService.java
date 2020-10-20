package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.Category;
import com.dili.trace.domain.ProductStock;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.User;
import com.dili.trace.domain.hangguo.HangGuoTrade;
import com.dili.trace.domain.hangguo.HangGuoUser;

import java.util.List;

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
     * 更新无需处理交易数据未待处理
     *
     * @param trades
     */
    void batchUpdateCacheTradeHandleFlagToTrue(List<HangGuoTrade> trades);

    /**
     * 更新待处理交易数据为无需处理
     *
     * @param trades
     */
    void batchUpdateCacheTradeHandleFlagToFalse(List<HangGuoTrade> trades);

    /**
     * 查询的交易数据
     *
     * @param trade
     * @return
     */
    List<HangGuoTrade> selectTradeReportListByHandleFlag(HangGuoTrade trade);
}
