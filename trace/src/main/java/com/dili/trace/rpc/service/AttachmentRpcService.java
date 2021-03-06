package com.dili.trace.rpc.service;

import com.dili.customer.sdk.domain.Attachment;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.sdk.rpc.AttachmentRpc;
import com.dili.ss.domain.BaseOutput;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 查询附件接口
 */
@Service
public class AttachmentRpcService {
    private static final Logger logger = LoggerFactory.getLogger(AttachmentRpcService.class);
    @Autowired
    AttachmentRpc attachmentRpc;

    /**
     * 根据市场id和customerid查询
     *
     * @param marketId
     * @param customerId
     * @return
     */
    public List<Attachment> findAttachmentByMarketIdAndCustomerId(Long marketId, Long customerId) {
        if (marketId == null || customerId == null) {
            return Lists.newArrayList();
        }

        try {
            BaseOutput<List<Attachment>> out = this.attachmentRpc.listAttachment(customerId, marketId);
            if (out == null) {
                logger.error("查询返回BaseOutput为Null");
                return Lists.newArrayList();
            }
            if (!out.isSuccess()) {
                logger.error("查询返回Message为:{}", out.getMessage());
                return Lists.newArrayList();
            }
            return out.getData();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Lists.newArrayList();
        }
    }

    /**
     * 根据市场id和多个customerid查询
     *
     * @param marketId
     * @param customerIdList
     * @return
     */
    public Map<Long, List<Attachment>> findAttachmentByMarketIdAndCustomerIdList(Long marketId, List<Long> customerIdList) {
        if (marketId == null || customerIdList == null || customerIdList.isEmpty()) {
            return Maps.newHashMap();
        }
        Attachment q = new Attachment();
        q.setMarketId(marketId);
        q.setCustomerIdSet(Sets.newHashSet(customerIdList));
        try {
            BaseOutput<List<Attachment>> out = this.attachmentRpc.listByExample(q);
            if (out == null) {
                logger.error("查询返回BaseOutput为Null");
                return Maps.newHashMap();
            }
            if (!out.isSuccess()) {
                logger.error("查询返回Message为:{}", out.getMessage());
                return Maps.newHashMap();
            }
            Map<Long, List<Attachment>> map = StreamEx.of(out.getData()).groupingBy(Attachment::getCustomerId);
            return StreamEx.of(customerIdList).toMap(cid -> {
                return cid;
            }, cid -> {
                return map.getOrDefault(cid, Lists.newArrayList());
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Maps.newHashMap();
        }


    }

    /**
     * 根据市场id和多个customerid查询
     *
     * @param marketId
     * @param customerIdList
     * @return
     */
    public Map<Long, Attachment> findAttachmentByAttachmentTypeAndCustomerIdList(Long marketId, List<Long> customerIdList, CustomerEnum.AttachmentType attachmentType) {
        if (marketId == null || customerIdList == null || customerIdList.isEmpty()) {
            return Maps.newHashMap();
        }

        return EntryStream.of(this.findAttachmentByMarketIdAndCustomerIdList(marketId, customerIdList))
                .mapValues(list -> {
                    return StreamEx.of(list).filter(attachment -> {
                        return (attachmentType == null) ? true : attachmentType.getCode().equals(attachment.getFileType());
                    }).toList();
                }).filterValues(list -> list.size() > 0).mapValues(list -> list.get(0)).toMap();
    }

}
