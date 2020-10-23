package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.dao.CountriesMapper;
import com.dili.trace.domain.Countries;
import com.dili.trace.service.CountriesService;
import org.springframework.stereotype.Service;

/**
 * 国外国家相关接口实现
 *
 * @author Lily
 */
@Service
public class CountriesServiceImpl extends BaseServiceImpl<Countries, Long> implements CountriesService {
    public CountriesMapper getActualDao() {
        return (CountriesMapper)getDao();
    }
}
