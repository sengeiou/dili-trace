package com.dili.trace.service;

import java.util.List;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.UsualAddress;
import com.dili.trace.glossary.UsualAddressTypeEnum;

public interface UsualAddressService extends BaseService<UsualAddress, Long> {
	public int insertUsualAddress(UsualAddress input);

	public int updateUsualAddress(UsualAddress input);

	public int deleteUsualAddress(Long id);
	
	public List<UsualAddress>findUsualAddressByType(UsualAddressTypeEnum usualAddressType);
}
