package com.dili.trace.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.IDTO;
import com.dili.trace.dao.RUserUpStreamMapper;
import com.dili.trace.dao.UpStreamMapper;
import com.dili.trace.domain.RUserUpstream;
import com.dili.trace.domain.UpStream;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.UpStreamDto;
import com.dili.trace.glossary.UpStreamTypeEnum;

import tk.mybatis.mapper.entity.Example;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpStreamService extends BaseServiceImpl<UpStream, Long> {
	public UpStreamMapper getActualDao() {
		return (UpStreamMapper) getDao();
	}

	@Autowired
	RUserUpStreamService rUserUpStreamService;
	@Autowired
	UserQrItemService userQrItemService;

	/**
	 * 分页查询上游信息
	 */
	public BasePage<UpStream> listPageUpStream(Long userId, UpStream query) {
		if (userId == null) {
			BasePage<UpStream> result = new BasePage<>();

			result.setPage(1);
			result.setRows(0);
			result.setTotalItem(0);
			result.setTotalPage(1);
			result.setStartIndex(1);
			return result;
		}

		query.setMetadata(IDTO.AND_CONDITION_EXPR,
				"id in(select upstream_id from r_user_upstream where user_id=" + userId + ")");
		BasePage<UpStream> page = this.listPageByExample(query);
		return page;
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
			throw new TraceBusinessException("参数错误");
		}
		RUserUpstream rUserUpstream = new RUserUpstream();
		rUserUpstream.setUserId(userId);
		rUserUpstream.setUpstreamId(upstreamId);
		RUserUpstream item = this.rUserUpStreamService.listByExample(rUserUpstream).stream().findFirst().orElse(null);
		if (item != null) {
			int value= this.rUserUpStreamService.delete(item.getId());
			this.userQrItemService.updateUserQrStatus(userId);
			return value;
		}
		return 0;
	}

	/**
	 * 新增上游
	 * 
	 * @param upStreamDto
	 * @return
	 */
	@Transactional
	public BaseOutput addUpstream(UpStreamDto upStreamDto, OperatorUser operatorUser) {
		insertSelective(upStreamDto);
		addUpstreamUsers(upStreamDto, operatorUser);
		return BaseOutput.success();
	}

	/**
	 * 添加上游的所有业户信息（绑定）
	 * 
	 * @param upStreamDto
	 */
	public void addUpstreamUsers(UpStreamDto upStreamDto, OperatorUser operatorUser) {
		if (CollectionUtils.isNotEmpty(upStreamDto.getUserIds())) {
			List<RUserUpstream> rUserUpstreams = new ArrayList<>();
			upStreamDto.getUserIds().forEach(o -> {
				RUserUpstream rUserUpstream = new RUserUpstream();
				rUserUpstream.setUpstreamId(upStreamDto.getId());
				rUserUpstream.setUserId(o);
				rUserUpstream.setOperatorId(operatorUser.getId());
				rUserUpstream.setOperatorName(operatorUser.getName());
				rUserUpstream.setCreated(new Date());
				rUserUpstream.setModified(new Date());
				rUserUpstreams.add(rUserUpstream);
			});
			rUserUpStreamService.batchInsert(rUserUpstreams);
		}
		if (CollectionUtils.isNotEmpty(upStreamDto.getUserIds())) {
			
			upStreamDto.getUserIds().forEach(userId -> {
				this.userQrItemService.updateUserQrStatus(userId);
			});
		}
		
		
		
	}

	/**
	 * 修改上游
	 * 
	 * @param upStreamDto
	 * @return
	 */
	@Transactional
	public BaseOutput updateUpstream(UpStreamDto upStreamDto, OperatorUser operatorUser) {
		updateSelective(upStreamDto);

		RUserUpstream rUserUpstreamDelCondition = new RUserUpstream();
		rUserUpstreamDelCondition.setUpstreamId(upStreamDto.getId());
		
		
		List<Long>changedUserId=this.rUserUpStreamService.list(rUserUpstreamDelCondition).stream().map(RUserUpstream::getUserId).collect(Collectors.toList());
		

		rUserUpStreamService.deleteByExample(rUserUpstreamDelCondition);
		addUpstreamUsers(upStreamDto, operatorUser);
		
		changedUserId.stream().forEach(userId->{
			this.userQrItemService.updateUserQrStatus(userId);
		});	
		
		return BaseOutput.success();

	}

	/**
	 * 批量查询上游对应的业户集合
	 * 
	 * @param upstreamIds
	 * @return Map｛upstreamId，userNames｝
	 */
	public List<Map<String, Object>> queryUsersByUpstreamIds(List<Long> upstreamIds) {
		return getActualDao().queryUsersByUpstreamIds(upstreamIds);
	}

	public List<UpStream> queryUpStreamByUserId(Long userId) {
		if (userId == null) {
			return new ArrayList<>();
		}
		RUserUpstream rUpstreamQuery = new RUserUpstream();
		rUpstreamQuery.setUserId(userId);
		List<Long> upStreamIdList = this.rUserUpStreamService.listByExample(rUpstreamQuery).stream()
				.map(RUserUpstream::getUpstreamId).collect(Collectors.toList());
		if (upStreamIdList.isEmpty()) {
			return new ArrayList<>();
		}
		Example example = new Example(UpStream.class);
		example.and().andIn("id", upStreamIdList);
		return this.getActualDao().selectByExample(example);

	}
	
	public UpStream queryUpStreamBySourceUserId(Long sourceUserId) {
		if (sourceUserId == null) {
			return null;
		}
		UpStream query=new UpStream();
		query.setSourceUserId(sourceUserId);
		UpStream upStream=this.listByExample(query).stream().findFirst().orElse(null);
		 return upStream;

	}
	
	
	


}