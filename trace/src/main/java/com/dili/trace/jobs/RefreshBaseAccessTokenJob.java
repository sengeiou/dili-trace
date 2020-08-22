package com.dili.trace.jobs;

import com.dili.trace.domain.WxApp;
import com.dili.trace.service.IWxAppService;
import com.dili.trace.util.WxServerUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 普通token的刷新
 *
 * @author travis.cheng
 */
@Component
public class RefreshBaseAccessTokenJob implements CommandLineRunner {
    private Logger log = LoggerFactory.getLogger(RefreshBaseAccessTokenJob.class);
    @Autowired
    private IWxAppService wxAppService;

    @Override
    public void run(String... args) throws Exception {
        log.info("===>>启动时刷新小程序accessToken<<===");
        this.refreshAccessToken();
    }

    @Scheduled(cron = "${app.wx.base-access-token.refresh.cron}")
    public void refreshAccessToken() {
        try {
            log.info("基础access_token刷新机制执行中。" + LocalDateTime.now());
            List<WxApp> list = wxAppService.list(null);
            for (WxApp wxApp : list) {
                String accessToken = wxApp.getAccessToken();
                if (StringUtils.isBlank(accessToken)) {
                    // 未初始化的appid不处理
                    continue;
                }
                // 有效时间最后5分钟内才进行刷新
                if ((System.currentTimeMillis() - wxApp.getAccessTokenUpdateTime().getTime())
                        / 1000 < wxApp.getAccessTokenExpiresIn() - 5 * 60) {
                    continue;
                }
                Map<String, Object> rs = WxServerUtil.getAccessToken(wxApp.getAppId(), wxApp.getAppSecret());
                WxApp updateWxApp = new WxApp();
                Date nowDate = new Date();
                Integer expiresTime = (Integer) rs.get("expires_in");
                accessToken = (String) rs.get("access_token");
                updateWxApp.setAppId(wxApp.getAppId());
                updateWxApp.setAppSecret(wxApp.getAppSecret());
                updateWxApp.setAccessToken(accessToken);
                updateWxApp.setAccessTokenExpiresIn(expiresTime);
                updateWxApp.setAccessTokenUpdateTime(nowDate);
                WxApp upid = new WxApp();
                upid.setAppId(wxApp.getAppId());
                wxAppService.updateByExample(updateWxApp, upid);

            }
        } catch (Exception e) {
            log.error("基础access_token刷新异常", e);
        }
    }

    public static Date add(Date d, int field, int amount) {
        if (d == null) {
            return null;
        }

        if (amount == 0) {
            return d;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(field, amount);

        return cal.getTime();
    }


}
