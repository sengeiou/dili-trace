package com.dili.sg.trace.service;

import com.dili.assets.sdk.dto.TradeTypeDto;
import com.dili.assets.sdk.dto.TradeTypeQuery;
import com.dili.assets.sdk.rpc.TradeTypeRpc;
import com.dili.commons.bstable.TableResult;
import com.dili.sg.trace.domain.TradeType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-31 14:56:14.
 */
@Service
public class TradeTypeService {
    private static final Logger logger = LoggerFactory.getLogger(TradeTypeService.class);
    @Autowired(required = false)
    TradeTypeRpc tradeTypeRpc;

    /**
     * 查询 所有交易类型
     * @return
     */
    public List<TradeType> findAll() {
        TradeTypeQuery query = new TradeTypeQuery();
        query.setPageNum(1);
        query.setPageSize(Integer.MAX_VALUE);
        try {
            TableResult<TradeTypeDto> ret = this.tradeTypeRpc.query(query);
            return StreamEx.ofNullable(ret.getRows()).nonNull().flatCollection(Function.identity())
                    .nonNull().map(TradeType::convert).toList();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Lists.newArrayList();

    }

    /**
     * 所有交易类型数据
     *
     * @return
     */
    public Map<String, TradeType> queryTradeTypeMap() {
        try {

            Map<String, TradeType> tradeTypeMap =
                    StreamEx.of(this.findAll()).toMap(TradeType::getTypeId, Function.identity());
            return tradeTypeMap;
        } catch (Exception e) {
            return Maps.newHashMap();
        }
    }
}