package com.dili.trace.service;

import java.util.*;

import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.IDTO;
import com.dili.trace.dao.UpStreamMapper;
import com.dili.trace.domain.Market;
import com.dili.trace.domain.RUserUpstream;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.UpStreamDto;
import com.dili.trace.enums.UserFlagEnum;
import com.dili.trace.enums.ValidateStateEnum;
import com.dili.trace.glossary.UpStreamTypeEnum;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import tk.mybatis.mapper.entity.Example;

@Service
public class UpStreamService extends BaseServiceImpl<UpStream, Long> {
	/**
	 * 下游企业标志 20
	 */

	public UpStreamMapper getActualDao() {
		return (UpStreamMapper) getDao();
	}

	@Autowired
	RUserUpStreamService rUserUpStreamService;

	@Autowired
	UserService userService;

	@Autowired
	MarketService marketService;

	/**
	 * 分页查询上游信息
	 */
	public BasePage<UpStream> listPageUpStream(Long userId, UpStream query) {
		if (userId == null) {
			BasePage<UpStream> result = new BasePage<>();

			result.setPage(1);
			result.setRows(0);
			result.setTotalItem(0L);
			result.setTotalPage(1);
			result.setStartIndex(1L);
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
			int value = this.rUserUpStreamService.delete(item.getId());
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
	public BaseOutput addUpstream(UpStreamDto upStreamDto, OperatorUser operatorUser,Boolean ... noticeException) {
		// 增加下游企业时创建用户
		this.addUserForDownStream(upStreamDto).ifPresent(sourceuserId -> {
			upStreamDto.setSourceUserId(sourceuserId);
		});
		try {
			UpStreamDto query = new UpStreamDto();
			query.setSourceUserId(upStreamDto.getSourceUserId());
			query.setTelphone(upStreamDto.getTelphone());
			query.setUpORdown(upStreamDto.getUpORdown());
			query.setMarketId(upStreamDto.getMarketId());

			List<UpStream> upStreamList = listByExample(query);
			if (CollUtil.isEmpty(upStreamList)) {
				insertSelective(upStreamDto);
			}
			if (upStreamDto.getId() == null){
				upStreamDto.setId(upStreamList.get(0).getId());
			}
			addUpstreamUsers(upStreamDto, operatorUser);
		} catch (DuplicateKeyException e) {
			if (noticeException != null && noticeException[0]){
				throw new TraceBusinessException("已存在手机号:" + upStreamDto.getTelphone() + "的企业/个人");
			}
		}

		return BaseOutput.success();
	}

	// 增加下游企业时创建用户
	public Optional<Long> addUserForDownStream(UpStreamDto upStreamDto) {

		if (UserFlagEnum.DOWN.equalsToCode(upStreamDto.getUpORdown())) {
			Long sourceuserId = doAddUser(upStreamDto);
			return Optional.ofNullable(sourceuserId);
		}

		return Optional.empty();
	}

	private Long doAddUser(UpStreamDto upStreamDto) {
		List<User> users = userService.getUserByExistsAccount(upStreamDto.getTelphone());
		boolean existsFlag = !CollUtil.isEmpty(users);
		if (!existsFlag) {
			JSONObject object = new JSONObject();
			object.put("phone", upStreamDto.getTelphone());
			object.put("name", upStreamDto.getName());
			if (Objects.nonNull(upStreamDto.getMarketId())) {
				object.put("marketId", upStreamDto.getMarketId());
				Market market = marketService.get(upStreamDto.getMarketId());
				if (Objects.nonNull(market)) {
					object.put("marketName", market.getName());
				}
			}
			if (upStreamDto.getUpstreamType() == UpStreamTypeEnum.CORPORATE.getCode()) {// 企业
				object.put("legal_person", upStreamDto.getLegalPerson());
				object.put("license", upStreamDto.getLicense());
			}

			User user = JSONObject.parseObject(object.toJSONString(), User.class);
			userService.register(user, false);
			return user.getId();
		}
		return users.get(0).getId();

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
				if (upStreamDto.getId() == null){
					return;
				}
				RUserUpstream rUserUpstream = new RUserUpstream();
				rUserUpstream.setUpstreamId(upStreamDto.getId());
				rUserUpstream.setUserId(o);
				if (CollUtil.isEmpty(rUserUpStreamService.listByExample(rUserUpstream))){
					rUserUpstream.setOperatorId(operatorUser.getId());
					rUserUpstream.setOperatorName(operatorUser.getName());
					rUserUpstreams.add(rUserUpstream);
				}
//				rUserUpstream.setCreated(new Date());
//				rUserUpstream.setModified(new Date());


			});
			if (rUserUpstreams.size() > 0){
				rUserUpStreamService.batchInsert(rUserUpstreams);
			}

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

		List<Long> changedUserId = this.rUserUpStreamService.list(rUserUpstreamDelCondition).stream()
				.map(RUserUpstream::getUserId).collect(Collectors.toList());

		rUserUpStreamService.deleteByExample(rUserUpstreamDelCondition);
		addUpstreamUsers(upStreamDto, operatorUser);

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

	
	public List<UpStream> queryUpStreamByUserIdAndFlag(Long userId,UserFlagEnum userFlagEnum,Long sourceUserId) {
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
		example.and().andIn("id", upStreamIdList).andEqualTo("upORdown", userFlagEnum.getCode()).andEqualTo("sourceUserId", sourceUserId);
		return this.getActualDao().selectByExample(example);

	}

	public UpStream queryUpStreamBySourceUserId(Long sourceUserId) {
		if (sourceUserId == null) {
			return null;
		}
		UpStream query = new UpStream();
		query.setSourceUserId(sourceUserId);
		UpStream upStream = this.listByExample(query).stream().findFirst().orElse(null);
		return upStream;

	}

	public List<UpStream> queryUpStreamByKeyword(Long userId, String keyword) {
		if (userId == null || StringUtils.isBlank(keyword)) {
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
		example.and().andIn("id", upStreamIdList).andLike("name", "%" + keyword + "%");
		return this.getActualDao().selectByExample(example);

	}

}