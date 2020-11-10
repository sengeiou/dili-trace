package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.POJOUtils;
import com.dili.trace.dao.CheckOrderDisposeMapper;
import com.dili.trace.domain.CheckOrderDispose;
import com.dili.trace.dto.CheckOrderDisposeDto;
import com.dili.trace.service.CheckOrderDisposeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author Ron.Peng
 * @date 2020/11/3
 */
@Service
public class CheckOrderDisposeServiceImpl extends BaseServiceImpl<CheckOrderDispose, Long> implements CheckOrderDisposeService {
    public CheckOrderDisposeMapper getActualDao() {
        return (CheckOrderDisposeMapper) getDao();
    }

    @Override
    public EasyuiPageOutput selectForEasyuiPage(CheckOrderDisposeDto domain, boolean useProvider) throws Exception {
        if (domain.getRows() != null && domain.getRows() >= 1) {
            PageHelper.startPage(domain.getPage(), domain.getRows());
        }
        if (StringUtils.isNotBlank(domain.getSort())) {
            domain.setSort(POJOUtils.humpToLineFast(domain.getSort()));
        }
        List<CheckOrderDispose> checkOrderDispose = getActualDao().selectForPage(domain);
        long total = checkOrderDispose instanceof Page ? ((Page) checkOrderDispose).getTotal() : (long) checkOrderDispose.size();
        List results = useProvider ? ValueProviderUtils.buildDataByProvider(domain, checkOrderDispose) : checkOrderDispose;
        return new EasyuiPageOutput((int) total, results);
    }
}
