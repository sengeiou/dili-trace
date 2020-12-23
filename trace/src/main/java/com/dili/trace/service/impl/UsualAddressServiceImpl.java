package com.dili.trace.service.impl;

import java.util.Date;
import java.util.List;

import com.dili.assets.sdk.dto.CityDto;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.dao.UsualAddressMapper;
import com.dili.trace.domain.UsualAddress;
import com.dili.trace.glossary.UsualAddressTypeEnum;
import com.dili.trace.rpc.service.CityRpcService;
import com.dili.trace.service.UsualAddressService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsualAddressServiceImpl extends BaseServiceImpl<UsualAddress, Long> implements UsualAddressService {
    @Autowired
    CityRpcService cityService;
    @Autowired
    UsualAddressMapper usualAddressMapper;
	@Override
	public int insertUsualAddress(UsualAddress input) {
		
		
		UsualAddress example=new UsualAddress();
		example.setAddressId(input.getAddressId());
		example.setType(input.getType());
		if( this.listByExample(example).stream().count()>0) {
			throw new TraceBizException("城市已经存在");
		}
		
		CityDto city=this.findCityOrException(input.getAddressId());
		input.setAddressId(city.getId());
		input.setAddress(city.getName());
		input.setMergedAddress(city.getMergerName());
		input.setCreated(new Date());
		input.setModified(new Date());
		input.setTodayUsedCount(0);
		input.setPreDayUsedCount(0);
		input.setClearTime(new Date());
		this.insertSelective(input);
		return 0;
	}

	@Transactional
	@Override
	public int updateUsualAddress(UsualAddress input) {
		UsualAddress item=this.get(input.getId());
		if(item==null) {
			throw new TraceBizException("参数错误");
		}
		
		UsualAddress example=new UsualAddress();
		example.setAddressId(input.getAddressId());
		example.setType(input.getType());
		List<UsualAddress>list=this.listByExample(example);
		if(list.size()>0) {
			long count=list.stream().filter(add->{return !add.getId().equals(input.getId());}).count();
			if(count>0) {
				throw new TraceBizException("城市已经存在");
			}
		}
		
		CityDto city=this.findCityOrException(input.getAddressId());
		input.setAddressId(city.getId());
		input.setAddress(city.getName());
		input.setMergedAddress(city.getMergerName());
		input.setCreated(new Date());
		input.setModified(new Date());
		input.setTodayUsedCount(0);
		input.setPreDayUsedCount(0);
		input.setClearTime(new Date());
		this.delete(item.getId());
		this.insertSelective(input);
		
		return 0;
	}


	@Override
	public int deleteUsualAddress(Long id) {
		if(id!=null) {
			return this.delete(id);
		}
		return 0;
	}

	private CityDto findCityOrException(Long addressId) {
		return this.cityService.findCityById(addressId).orElseThrow(()->new TraceBizException("城市查询失败"));
	}

	@Override
	public List<UsualAddress> findUsualAddressByType(UsualAddressTypeEnum usualAddressType) {
		UsualAddress example=new UsualAddress();
		example.setType(usualAddressType.getType());
		return this.usualAddressMapper.findUsualAddressByType(example);
	}

	@Override
	public int increaseUsualAddressTodayCount(UsualAddressTypeEnum type,Long addressId) {
		
		return this.increaseUsualAddressTodayCount(type, null,addressId);
	}

	@Override
	public int increaseUsualAddressTodayCount(UsualAddressTypeEnum type,Long oldAddressId, Long newAddressId) {
		if(newAddressId==null||newAddressId.equals(oldAddressId)) {
			return 0;
		}
		UsualAddress item=this.findUsualAddressByTypeAndAddressId(newAddressId, type);
		if(item==null) {
			return 0;
		}
		UsualAddress domain=new UsualAddress();
		domain.setTodayUsedCount(item.getTodayUsedCount()+1);
		domain.setId(item.getId());
		this.updateSelective(domain);
		
		return 1;
	}
	private UsualAddress findUsualAddressByTypeAndAddressId(Long addressId,UsualAddressTypeEnum type) {
		UsualAddress example=new UsualAddress();
		example.setAddressId(addressId);
		example.setType(type.getType());
		return this.listByExample(example).stream().findFirst().orElse(null);
		
	}

}
