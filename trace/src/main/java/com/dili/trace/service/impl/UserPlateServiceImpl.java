package com.dili.trace.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.UserPlate;
import com.dili.trace.service.UserPlateService;

import tk.mybatis.mapper.entity.Example;

@Service
public class UserPlateServiceImpl extends BaseServiceImpl<UserPlate, Long> implements UserPlateService {

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

	@Override
	public List<UserPlate> findUserPlateByPlates(List<String> plateList) {
		if(CollectionUtils.isEmpty(plateList)) {
			return new ArrayList<>(0);
		}
		Example example = new Example(UserPlate.class);
		example.and().andIn("plate", plateList);
		return this.getDao().selectByExampleExpand(example);
	}

	@Override
	public int deleteAndInsertUserPlate(Long userId, List<String> plateList) {
		UserPlate example = DTOUtils.newDTO(UserPlate.class);
		example.setUserId(userId);
		this.deleteByExample(example);
		
		List<UserPlate> list =plateList.stream().map(p->{
			
			UserPlate item = DTOUtils.newDTO(UserPlate.class);
			item.setPlate(p);
			item.setUserId(userId);
			item.setModified(new Date());
			item.setCreated(new Date());
			return item;
		}).collect(Collectors.toList());
		return this.batchInsert(list);
	}

}
