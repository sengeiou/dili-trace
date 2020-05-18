package com.dili.trace.service;

import com.dili.common.exception.BusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.IDTO;
import com.dili.trace.domain.RUserUpstream;
import com.dili.trace.domain.UpStream;
import com.dili.trace.glossary.UpStreamTypeEnum;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpStreamService extends BaseServiceImpl<UpStream, Long> {
    @Autowired
    RUserUpStreamService rUserUpStreamService;

    /**
     * 分页查询上游信息
     */
    public BasePage<UpStream> listPageUpStream(Long userId) {
        if (userId == null) {
            BasePage<UpStream> result = new BasePage<>();

            result.setPage(1);
            result.setRows(0);
            result.setTotalItem(0);
            result.setTotalPage(1);
            result.setStartIndex(1);
            return result;
        }
        UpStream query = new UpStream();
        query.mset(IDTO.AND_CONDITION_EXPR,
                "id in(select upstream_id from r_user_upstream where user_id=" + userId + ")");
        BasePage<UpStream> page = this.listPageByExample(null);
        return page;
    }

    /**
     * 创建上游信息
     */
    public UpStream createUpstream(Long userId, UpStream input) {
        if (userId == null || input == null) {
            throw new BusinessException("参数错误");
        }
        if (StringUtils.isBlank(input.getName())) {
            throw new BusinessException("企业(个人)名称不能为空");
        }
        if (UpStreamTypeEnum.CORPORATE.equalsCode(input.getUpstreamType())) {

        } else if (UpStreamTypeEnum.USER.equalsCode(input.getUpstreamType())) {

        } else {
            throw new BusinessException("参数错误");
        }
        this.insertSelective(input);
        return input;
    }

    /**
     * 删除用户上游关系
     * 
     * @param userId
     * @param upstreamId
     * @return
     */
    public int deleteUpstream(Long userId, Long upstreamId) {

        if (userId == null || upstreamId == null) {
            throw new BusinessException("参数错误");
        }
        RUserUpstream rUserUpstream = new RUserUpstream();
        rUserUpstream.setUserId(userId);
        rUserUpstream.setUpstreamId(upstreamId);
        RUserUpstream item = this.rUserUpStreamService.listByExample(rUserUpstream).stream().findFirst().orElse(null);
        if (item != null) {
            return this.rUserUpStreamService.delete(item.getId());
        }
        return 0;
    }

    /***
     * 修改上游信息
     */
    public UpStream modifyUpstream(Long userId, UpStream input) {
        if (userId == null || input == null || input.getId() == null) {
            throw new BusinessException("参数错误");
        }
        if (StringUtils.isBlank(input.getName())) {
            throw new BusinessException("企业(个人)名称不能为空");
        }
        if (UpStreamTypeEnum.CORPORATE.equalsCode(input.getUpstreamType())) {

        } else if (UpStreamTypeEnum.USER.equalsCode(input.getUpstreamType())) {

        } else {
            throw new BusinessException("参数错误");
        }
        RUserUpstream rUserUpstream = new RUserUpstream();
        rUserUpstream.setUserId(userId);
        rUserUpstream.setUpstreamId(input.getId());
        RUserUpstream item = this.rUserUpStreamService.listByExample(rUserUpstream).stream().findFirst().orElse(null);
        if (item == null) {
            throw new BusinessException("数据错误");
        }
        this.updateSelective(input);
        return input;
    }

}