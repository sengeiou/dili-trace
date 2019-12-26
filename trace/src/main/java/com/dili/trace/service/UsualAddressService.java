package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.UsualAddress;

public interface UsualAddressService extends BaseService<UsualAddress, Long> {
	public int insertUsualAddress(UsualAddress input);

	public int updateUsualAddress(UsualAddress input);

	public int deleteUsualAddress(Long id);
}
