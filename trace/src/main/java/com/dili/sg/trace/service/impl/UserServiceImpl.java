package com.dili.sg.trace.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.dili.common.util.MD5Util;
import com.dili.sg.trace.dao.UserMapper;
import com.dili.sg.trace.domain.User;
import com.dili.sg.trace.domain.UserPlate;
import com.dili.sg.trace.domain.UserTallyArea;
import com.dili.sg.trace.dto.UserListDto;
import com.dili.common.exception.TraceBizException;
import com.dili.sg.trace.glossary.EnabledStateEnum;
import com.dili.sg.trace.glossary.UserTypeEnum;
import com.dili.sg.trace.glossary.UsualAddressTypeEnum;
import com.dili.sg.trace.glossary.YnEnum;
import com.dili.trace.api.components.SessionRedisService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.trace.service.UserHistoryService;
import com.dili.sg.trace.service.UserPlateService;
import com.dili.sg.trace.service.UserService;
import com.dili.sg.trace.service.UserTallyAreaService;
import com.dili.sg.trace.service.UsualAddressService;

import cn.hutool.core.collection.CollUtil;
import one.util.streamex.StreamEx;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

	public UserMapper getActualDao() {
		return (UserMapper) getDao();
	}

	@Autowired
	SessionRedisService sessionRedisService;
	@Resource
	private UserTallyAreaService userTallyAreaService;
	@Resource
    UserPlateService userPlateService;
	@Resource
    UserHistoryService userHistoryService;
	@Resource
    UsualAddressService usualAddressService;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void register(User user, UserTypeEnum userType, String originalPassword) {
		user.setState(EnabledStateEnum.ENABLED.getCode());
		user.setPassword(MD5Util.md5(originalPassword));
		user.setIsDelete(0L);
		user.setYn(YnEnum.YES.getCode());
		user.setUserType(userType.getCode());

//		// 验证验证码是否正确
//		if (flag) {
//			checkVerificationCode(user.getPhone(), user.getCheckCode());
//		}

		// 验证理货区号是否已注册
		if (StringUtils.isNotBlank(user.getTallyAreaNos())) {
			existsTallyAreaNo(null, Arrays.asList(user.getTallyAreaNos().split(",")));
		}
		// 验证手机号是否已注册
		if (existsAccount(user.getPhone())) {
			throw new TraceBizException("手机号已注册");
		}
		// 验证身份证号是否已注册
		if (StringUtils.isNotBlank(user.getCardNo()) && existsCardNo(user.getCardNo())) {
			throw new TraceBizException("身份证号已注册");
		}
		this.usualAddressService.increaseUsualAddressTodayCount(UsualAddressTypeEnum.USER, user.getSalesCityId());
		insertSelective(user);
		// 更新用户理货区
		updateUserTallyArea(user.getId(), Arrays.asList(StringUtils.trimToEmpty(user.getTallyAreaNos()).split(",")));
		// 增加车牌信息
//        LOGGER.info("输入车牌:{}",user.getPlates());
		List<String> plateList = this.parsePlate(user.getPlates());
//        LOGGER.info("解析车牌:{}",plateList.toString());
		if (!plateList.isEmpty()) {
			UserPlate up = this.userPlateService.findUserPlateByPlates(plateList).stream().findFirst().orElse(null);
			if (up != null) {
				throw new TraceBizException("车牌[" + up.getPlate() + "]已被其他用户使用");
			}
			this.userPlateService.deleteAndInsertUserPlate(user.getId(), plateList);
		}
		this.userHistoryService.insertUserHistoryForNewUser(user.getId());
	}

	/**
	 * 更新用户理货区
	 * 
	 * @param userId
	 * @param tallyAreaNos
	 */
	private void updateUserTallyArea(Long userId, List<String> tallyAreaNos) {
		if (null != userId) {
			UserTallyArea query = DTOUtils.newDTO(UserTallyArea.class);
			query.setUserId(userId);
			userTallyAreaService.deleteByExample(query);

			List<UserTallyArea> userTallyAreas = StreamEx.ofNullable(tallyAreaNos).nonNull()
					.flatCollection(Function.identity()).filter(StringUtils::isNotBlank).map(tallyAreaNo -> {
						UserTallyArea userTallyArea = DTOUtils.newDTO(UserTallyArea.class);
						userTallyArea.setUserId(userId);
						userTallyArea.setTallyAreaNo(tallyAreaNo);
						return userTallyArea;
					}).toList();

			if (!userTallyAreas.isEmpty()) {
				userTallyAreaService.batchInsert(userTallyAreas);
			}
		}

	}

	@Override
	public void updateUser(User user) {
		if (user == null || user.getId() == null) {
			throw new TraceBizException("参数错误");
		}
		User userItem = get(user.getId());
		if (userItem == null) {
			throw new TraceBizException("数据不存在");
		}

		// 手机号验重
		if (StringUtils.isNotBlank(user.getPhone())) {
			User condition = new User();
			condition.setPhone(user.getPhone());
			condition.setYn(YnEnum.YES.getCode());
			List<User> users = listByExample(condition);
			if (CollectionUtils.isNotEmpty(users)) {
				users.forEach(o -> {
					if (!o.getId().equals(user.getId()) && o.getPhone().equals(user.getPhone())) {
						throw new TraceBizException("手机号已注册");
					}
				});
			}
		}

		if (!YnEnum.YES.getCode().equals(userItem.getYn())) {
			throw new TraceBizException("数据已被删除");
		}

		if (EnabledStateEnum.ENABLED.getCode().equals(userItem.getState())) {
			List<String> tallyAreaNoList = StreamEx.ofNullable(user.getTallyAreaNos()).nonNull()
					.flatArray(str -> str.split(",")).filter(StringUtils::isNotBlank).toList();
			if (!tallyAreaNoList.isEmpty()) {
				existsTallyAreaNo(user.getId(), tallyAreaNoList);
				// 更新用户理货区
				updateUserTallyArea(user.getId(), tallyAreaNoList);
			}

		}
//        LOGGER.info("输入车牌:{}",user.getPlates());
		List<String> plateList = this.parsePlate(user.getPlates());
//        LOGGER.info("解析车牌:{}",plateList.toString());
		if (!plateList.isEmpty()) {
			UserPlate up = this.userPlateService.findUserPlateByPlates(plateList).stream().filter(p -> {
				return !p.getUserId().equals(userItem.getId());
			}).findFirst().orElse(null);
			if (up != null) {
				throw new TraceBizException("车牌[" + up.getPlate() + "]已被其他用户使用");
			}
		}
		this.userPlateService.deleteAndInsertUserPlate(userItem.getId(), plateList);
		updateSelective(user);
		this.usualAddressService.increaseUsualAddressTodayCount(UsualAddressTypeEnum.USER, userItem.getSalesCityId(),
				user.getSalesCityId());
		this.userHistoryService.insertUserHistoryForUpdateUser(user.getId());

	}

	private List<String> parsePlate(String plates) {
		List<String> plateList = new ArrayList<>();
		if (StringUtils.isBlank(plates)) {
			return plateList;
		}
		if (JSON.isValid(plates) && JSON.isValidArray(plates)) {
			JSON.parseArray(plates).stream().filter(Objects::nonNull).map(String::valueOf)
					.collect(Collectors.toCollection(() -> plateList));
		} else {
			plateList.add(plates);
		}

		return plateList.stream().filter(StringUtils::isNotBlank).map(String::trim).collect(Collectors.toList());

	}

//	private Boolean checkVerificationCode(String phone, String verCode) {
//		String verificationCodeTemp = String
//				.valueOf(redisService.get(ExecutionConstants.REDIS_SYSTEM_VERCODE_PREIX + phone));
//		if (StringUtils.isBlank(verificationCodeTemp)) {
//			throw new TraceBizException("验证码已过期");
//		}
//		if (verificationCodeTemp.equals(verCode)) {
//			redisService.del(ExecutionConstants.REDIS_SYSTEM_VERCODE_PREIX + phone);
//			return true;
//		} else {
//			throw new TraceBizException("验证码不正确");
//		}
//	}

	@Override
	public User login(String phone, String encryptedPassword) {
		User query = new User();
		query.setPhone(phone);
		query.setYn(YnEnum.YES.getCode());
		User po = listByExample(query).stream().findFirst().orElse(null);
		if (po == null) {
			throw new TraceBizException("手机号未注册");
		}
		if (EnabledStateEnum.DISABLED.getCode().equals(po.getState())) {
			throw new TraceBizException("手机号已禁用");
		}
		if (!po.getPassword().equals(encryptedPassword)) {
			throw new TraceBizException("密码错误");
		}
		return po;
	}

	@Transactional
	@Override
	public void resetPassword(User user) {
		User query = new User();
		query.setPhone(user.getPhone());
		query.setYn(YnEnum.YES.getCode());
		User po = listByExample(query).stream().findFirst().orElse(null);
		if (po == null) {
			throw new TraceBizException("用户不存在");
		}
		User condition = new User();
		condition.setId(po.getId());
		condition.setVersion(po.getVersion());
		po.setPassword(user.getPassword());
		po.setVersion(po.getVersion() + 1);
		int i = updateSelectiveByExample(po, condition);
		if (i != 1) {
			throw new TraceBizException("数据已变更,请稍后重试");
		}
	}

	@Transactional
	@Override
	public void changePassword(User user) {
		User po = get(user.getId());
		if (po == null) {
			throw new TraceBizException("用户不存在");
		}
		if (!po.getPassword().equals(user.getOldPassword())) {
			throw new TraceBizException("原密码错误");
		}
		User condition = new User();
		condition.setId(po.getId());
		condition.setVersion(po.getVersion());
		po.setPassword(user.getPassword());
		po.setVersion(po.getVersion() + 1);
		int i = updateSelectiveByExample(po, condition);
		if (i != 1) {
			throw new TraceBizException("数据已变更,请稍后重试");
		}
	}

	/**
	 * 检测手机号是否存在
	 * 
	 * @param phone
	 * @return true 存在 false 不存在
	 */
	@Override
	public boolean existsAccount(String phone) {
		User query = new User();
		query.setPhone(phone);
		query.setYn(YnEnum.YES.getCode());
		return !CollUtil.isEmpty(listByExample(query));
	}

	/**
	 * 检测理货区号是否已注册
	 * 
	 * @param userId
	 * @param tallyAreaNos
	 */
	public void existsTallyAreaNo(Long userId, List<String> tallyAreaNos) {
		tallyAreaNos.forEach(tallyAreaNo -> {
			UserTallyArea query = DTOUtils.newDTO(UserTallyArea.class);
			query.setTallyAreaNo(tallyAreaNo);
			UserTallyArea userTallyArea = userTallyAreaService.listByExample(query).stream().findFirst().orElse(null);
			if (null != userTallyArea && !userTallyArea.getUserId().equals(userId)) {
				throw new TraceBizException("理货区号【" + tallyAreaNo + "】已被注册");
			}
		});
	}

	/**
	 * 检测身份证号是否存在
	 * 
	 * @param cardNo
	 * @return true 存在 false 不存在
	 */
	public boolean existsCardNo(String cardNo) {
		User query = new User();
		query.setCardNo(cardNo);
		query.setYn(YnEnum.YES.getCode());
		return !CollUtil.isEmpty(listByExample(query));
	}

	@Override
	@Transactional
	public BaseOutput updateEnable(Long id, Boolean enable) {
		if (id == null) {
			throw new TraceBizException("参数错误");
		}
		User user = get(id);
		if (user == null) {
			throw new TraceBizException("数据不存在");
		}
		if (!YnEnum.YES.getCode().equals(user.getYn())) {
			throw new TraceBizException("数据已被删除");
		}
		
		List<String> tallyAreaNoList = StreamEx.ofNullable(user.getTallyAreaNos()).nonNull()
				.flatArray(str -> str.split(",")).filter(StringUtils::isNotBlank).toList();
		
		if (enable) {
			user.setState(EnabledStateEnum.ENABLED.getCode());
			this.updateSelective(user);

			// 验证理货区号是否已注册，未注册则添加用户理货区关系
			existsTallyAreaNo(user.getId(), tallyAreaNoList);
			List<UserTallyArea> userTallyAreas = new ArrayList<>();
			tallyAreaNoList.forEach(tallyAreaNo -> {
				UserTallyArea userTallyArea = DTOUtils.newDTO(UserTallyArea.class);
				userTallyArea.setUserId(user.getId());
				userTallyArea.setTallyAreaNo(tallyAreaNo);
				userTallyAreas.add(userTallyArea);
			});
			if(!userTallyAreas.isEmpty()) {
				userTallyAreaService.batchInsert(userTallyAreas);	
			}
			
			this.sessionRedisService.removeUserFromWaitDisabled(id);
		} else {
			user.setState(EnabledStateEnum.DISABLED.getCode());
			this.updateSelective(user);

			// 删除用户理货区关系
			UserTallyArea userTallyAreaQuery = DTOUtils.newDTO(UserTallyArea.class);
			userTallyAreaQuery.setUserId(id);
			userTallyAreaService.deleteByExample(userTallyAreaQuery);
			this.sessionRedisService.addWaitDisabledUser(id);
		}
		this.userHistoryService.insertUserHistoryForUpdateUser(user.getId());
		return BaseOutput.success("操作成功");
	}

	@Override
	public User findByTallyAreaNo(String tallyAreaNo) {
		UserTallyArea query = DTOUtils.newDTO(UserTallyArea.class);
		query.setTallyAreaNo(tallyAreaNo);
		UserTallyArea userTallyArea = userTallyAreaService.list(query).stream().findFirst().orElse(null);
		if (null == userTallyArea) {
			return null;
		}

		User userQuery = new User();
		userQuery.setId(userTallyArea.getUserId());
		userQuery.setState(EnabledStateEnum.ENABLED.getCode());
		return list(userQuery).stream().findFirst().orElse(null);
	}

	private Optional<String> andCondition(UserListDto dto) {
		List<String> strList = new ArrayList<String>();
		if (dto != null && dto.getHasBusinessLicense() != null) {
			if (dto.getHasBusinessLicense()) {
				strList.add(" (business_license_url is not null and business_license_url<>'') ");
			} else {
				strList.add(" (business_license_url is null or business_license_url='') ");
			}
		}

		if (dto != null && StringUtils.isNotBlank(dto.getPlates())) {
			strList.add(" id in (select user_id from user_plate where plate like '"
					+ dto.getPlates().trim().toUpperCase() + "%')");
		}
		if (!strList.isEmpty()) {
			return Optional.of(String.join(" and ", strList));
		}
		return Optional.empty();
	}

	@Override
	public EasyuiPageOutput listEasyuiPageByExample(UserListDto dto) throws Exception {
		this.andCondition(dto).ifPresent(str -> {
			dto.setMetadata(IDTO.AND_CONDITION_EXPR, str);
		});
		dto.setYn(1);
		EasyuiPageOutput out = this.listEasyuiPageByExample(dto, true);
		List<Map> users = out.getRows();
		List<Long> userIdList = users.stream().map(o -> {
			return (Long) o.get("id");
		}).collect(Collectors.toList());
		Map<Long, List<UserPlate>> userPlateMap = this.userPlateService.findUserPlateByUserIdList(userIdList);
		List<Map> userList = users.stream().map(u -> {
			Long userId = (Long) u.get("id");
			if (userPlateMap.containsKey(userId)) {
				String plates = userPlateMap.get(userId).stream().map(UserPlate::getPlate)
						.collect(Collectors.joining(","));
				u.put("plates", plates);
			} else {
				u.put("plates", "");
			}
			return u;
		}).collect(Collectors.toList());
		out.setRows(userList);

		return out;
	}

	@Override
	public BaseOutput deleteUser(Long id) {
		if (id == null) {
			return BaseOutput.failure("参数错误");
		}
		User user = this.get(id);
		if (user == null) {
			return BaseOutput.failure("数据不存在");
		}

//		List<String> tallyAreaNos = Arrays.asList(StringUtils.trimToEmpty(user.getTallyAreaNos()).split(","));

		user.setState(EnabledStateEnum.DISABLED.getCode());
		user.setYn(YnEnum.NO.getCode());
		user.setIsDelete(user.getId());
		this.updateSelective(user);

		this.userHistoryService.insertUserHistoryForDeleteUser(user.getId());

		// 删除用户理货区关系
		UserTallyArea userTallyAreaQuery = DTOUtils.newDTO(UserTallyArea.class);
		userTallyAreaQuery.setUserId(id);
		userTallyAreaService.deleteByExample(userTallyAreaQuery);
		this.sessionRedisService.addWaitDisabledUser(id);

		// 删除用户车牌信息
		UserPlate up = DTOUtils.newDTO(UserPlate.class);
		up.setUserId(user.getId());
		this.userPlateService.deleteByExample(up);

		return BaseOutput.success("操作成功");

//		UserTallyArea example=DTOUtils.newDTO(UserTallyArea.class);
//		example.setUserId(user.getId());
//		this.userTallyAreaService.deleteByExample(example);
//		user.setTallyAreaNos(null);
//		user.setState(EnabledStateEnum.DISABLED.getCode());
//		this.updateExact(user);

	}

	@Override
	public List<DTO> queryByTallyAreaNo(String likeTallyAreaNo) {

		UserListDto userListDto = new UserListDto();
		userListDto.setLikeTallyAreaNos(likeTallyAreaNo);

		List<User> userItemList = this.listByExample(userListDto);
		Map<Long, List<String>> userIdMap = StreamEx.of(userItemList).toMap(User::getId, u -> {
			return Arrays.asList(StringUtils.trimToEmpty(u.getTallyAreaNos()).split(","));
		});

		Map<Long, String> userIdNameMap = StreamEx.of(userItemList).toMap(User::getId, User::getName);

		List<Long> userIdList = StreamEx.ofKeys(userIdMap).toList();
		Map<Long, List<UserPlate>> userIdPlateMap = this.userPlateService.findUserPlateByUserIdList(userIdList);

		List<DTO> data = StreamEx.ofValues(userIdPlateMap).flatMap(List::stream).flatMap(up -> {

			return userIdMap.getOrDefault(up.getUserId(), Collections.emptyList()).stream().map(tno -> {

				DTO dto = new DTO();
				dto.put("userName", userIdNameMap.getOrDefault(up.getUserId(), ""));
				dto.put("plate", up.getPlate());
				dto.put("tallyAreaNo", tno);
				return dto;

			});
		}).toList();

		return data;

	}

	/**
	 * 创建场外委托用户
	 * 
	 * @param user
	 * @return
	 */
	@Transactional
	public User createCommissionUser(User user) {
		if (user == null || StringUtils.isAnyBlank(user.getPhone(), user.getPassword(), user.getAckPassword())) {
			throw new TraceBizException("参数错误");
		}
		if (!user.getPassword().equals(user.getAckPassword())) {
			throw new TraceBizException("两次输入密码不一致");
		}
//		user.setPlates(null);
//		user.setTallyAreaNos(null);
		this.register(user, UserTypeEnum.COMMISSION_USER, user.getPassword());
		return user;
	}
}