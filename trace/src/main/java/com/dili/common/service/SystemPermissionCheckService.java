package com.dili.common.service;

import com.dili.uap.sdk.redis.UserUrlRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName SystemPermissionCheckService
 * @Description: 系统权限校验业务类
 * @Author Tab.Xie
 * @Date 2020/10/16
 * @Version V1.0
 **/
@Service
public class SystemPermissionCheckService {
    @Autowired
    private UserUrlRedis userUrlRedis;


    /**
     * TODO
     *
     * @param： userId:待校验用户id
     * url: 待检验url地址
     * @return：boolean
     * @author：Tab.Xie
     * @date：2020/10/16 13:43
     */
    public boolean checkUrl(Long userId, String url) {
        return userUrlRedis.checkUserMenuUrlRight(userId, url);
    }
}