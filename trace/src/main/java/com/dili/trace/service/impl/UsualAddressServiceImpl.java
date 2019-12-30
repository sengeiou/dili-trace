package com.dili.trace.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dili.common.service.BaseInfoRpcService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.trace.domain.City;
import com.dili.trace.domain.UsualAddress;
import com.dili.trace.glossary.UsualAddressTypeEnum;
import com.dili.trace.service.UsualAddressService;

@Service
public class UsualAddressServiceImpl extends BaseServiceImpl<UsualAddress, Long> implements UsualAddressService {
    @Resource
    BaseInfoRpcService baseInfoRpcService;
	@Override
	public int insertUsualAddress(UsualAddress input) {
		
		
		UsualAddress example=DTOUtils.newDTO(UsualAddress.class);
		example.setAddressId(input.getAddressId());
		example.setType(input.getType());
		if( this.listByExample(example).stream().count()>0) {
			throw new AppException("城市已经存在");
		}
		
		City city=this.findCityOrException(input.getAddressId());
		input.setAddressId(city.getId());
		input.setAddress(city.getName());
		input.setMergedAddress(city.getMergerName());
		input.setCreated(new Date());
		input.setModified(new Date());
		this.insertSelective(input);
		return 0;
	}

	@Override
	public int updateUsualAddress(UsualAddress input) {
		UsualAddress item=this.get(input.getId());
		if(item==null) {
			throw new AppException("参数错误");
		}
		
		UsualAddress example=DTOUtils.newDTO(UsualAddress.class);
		example.setAddressId(input.getAddressId());
		example.setType(input.getType());
		List<UsualAddress>list=this.listByExample(example);
		if(list.size()>0) {
			long count=list.stream().filter(add->{return !add.getAddressId().equals(input.getAddressId());}).count();
			if(count>0) {
				throw new AppException("城市已经存在");	
			}
		}
		
		City city=this.findCityOrException(input.getAddressId());
		input.setAddressId(city.getId());
		input.setAddress(city.getName());
		input.setMergedAddress(city.getMergerName());
		input.setCreated(new Date());
		input.setModified(new Date());
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

	private City findCityOrException(Long addressId) {
		return this.baseInfoRpcService.findCityById(addressId).orElseThrow(()->new AppException("城市查询失败"));
	}

	@Override
	public List<UsualAddress> findUsualAddressByType(UsualAddressTypeEnum usualAddressType) {
		UsualAddress example=DTOUtils.newDTO(UsualAddress.class);
		example.setType(usualAddressType.getType());
		return this.listByExample(example);
	}

}