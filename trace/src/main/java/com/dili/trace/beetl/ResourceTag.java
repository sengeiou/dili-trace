package com.dili.trace.beetl;

import com.diligrp.manage.sdk.domain.UserTicket;
import com.diligrp.manage.sdk.session.SessionContext;
import org.apache.commons.lang3.StringUtils;
import org.beetl.core.Tag;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 用户资源权限检查标签
 * Created by asiamaster on 2017/7/21 0021.
 */
@Component("resource")
public class ResourceTag extends Tag {
	//标签自定义属性
	private static final String METHOD_FIELD = "method";
	private static final String URL_FIELD = "url";

	@Override
	public void render() {
		Map<String, Object> argsMap = (Map)this.args[1];
		String method = (String) argsMap.get(METHOD_FIELD);
		String url = (String) argsMap.get(URL_FIELD);
		if (StringUtils.isBlank(method) || StringUtils.isBlank(url)) {
			try {
				ctx.byteWriter.writeString("参数为空");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(userTicket == null) {
			try {
				ctx.byteWriter.writeString("用户未登录");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		if(SessionContext.hasAccess(method,url)){
			try {
				ctx.byteWriter.write(getBodyContent());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
