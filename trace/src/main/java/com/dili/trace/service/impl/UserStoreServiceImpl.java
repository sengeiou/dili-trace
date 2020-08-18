package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.UserStore;
import com.dili.trace.service.UserStoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserStoreServiceImpl extends BaseServiceImpl<UserStore, Long> implements UserStoreService {
}
