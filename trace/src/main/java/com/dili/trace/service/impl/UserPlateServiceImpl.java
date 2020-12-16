package com.dili.trace.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.dili.trace.domain.UserTallyArea;
import com.dili.trace.service.UserTallyAreaService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.UserPlate;
import com.dili.trace.service.UserPlateService;

import tk.mybatis.mapper.entity.Example;

@Service
public class UserPlateServiceImpl extends BaseServiceImpl<UserPlate, Long> implements UserPlateService {
	@Autowired
	UserTallyAreaService userTallyAreaService;
	@Override
	public List<UserPlate> findUserPlateByUserId(Long userId) {
		if (userId == null) {
			return new ArrayList<>(0);
		}
		UserPlate query = DTOUtils.newDTO(UserPlate.class);
		query.setUserId(userId);
		List<UserPlate> list = this.listByExample(query);
		return list;
	}

//	@Override
//	public List<UserPlate> findUserPlateByPlates(List<String> plateList) {
//		if (CollectionUtils.isEmpty(plateList)) {
//			return new ArrayList<>(0);
//		}
//		Example example = new Example(UserPlate.class);
//		example.and().andIn("plate", plateList);
//		return this.getDao().selectByExampleExpand(example);
//	}
//
//	@Override
//	public int deleteAndInsertUserPlate(Long userId, List<String> plateList) {
//		UserPlate example = DTOUtils.newDTO(UserPlate.class);
//		example.setUserId(userId);
//		this.deleteByExample(example);
//
//		List<UserPlate> list =plateList.stream().map(p->{
//
//			UserPlate item = DTOUtils.newDTO(UserPlate.class);
//			item.setPlate(p.toUpperCase());
//			item.setUserId(userId);
//			item.setModified(new Date());
//			item.setCreated(new Date());
//			return item;
//		}).collect(Collectors.toList());
//		if (!list.isEmpty()) {
//			return this.batchInsert(list);
//		}
//		return 0;
//
//	}
//
//	@Override
//	public Map<Long, List<UserPlate>> findUserPlateByUserIdList(List<Long> userIdList) {
//		if (CollectionUtils.isEmpty(userIdList)) {
//			return new HashMap<>(0);
//		}
//		Example example = new Example(UserPlate.class);
//		example.and().andIn("userId", userIdList);
//		List<UserPlate> userPlateList = this.getDao().selectByExampleExpand(example);
//		return userPlateList.stream().collect(Collectors.groupingBy(UserPlate::getUserId));
//	}

	@Override
	public int checkAndInsertUserPlate(Long userId, String plate) {
		if(userId!=null&&plate!=null){
			UserPlate item = DTOUtils.newDTO(UserPlate.class);
			item.setPlate(plate.toUpperCase());
			item.setUserId(userId);
			boolean exists=this.listByExample(item).size()>0;
			if(!exists){
				this.insertSelective(item);
			}
		}
		return 0;
	}

//	@Override
//	public List<UserPlate> findUserPlateByTallyAreaNo(String tallyAreaNo) {
//		UserTallyArea domain=DTOUtils.newDTO(UserTallyArea.class);
//		domain.setTallyAreaNo(tallyAreaNo);
//		Set<Long> userIdSet=userTallyAreaService.listByExample(domain).stream().map(UserTallyArea::getUserId).collect(Collectors.toSet());
//		if(!userIdSet.isEmpty()) {
//			Example example = new Example(UserPlate.class);
//			example.and().andIn("userId", userIdSet);
//			List<UserPlate>userPlateList= this.getDao().selectByExampleExpand(example);
//			return userPlateList;
//		}
//
//		return new ArrayList<UserPlate>();
//	}

}
