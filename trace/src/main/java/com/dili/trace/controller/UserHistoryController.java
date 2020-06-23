package com.dili.trace.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.exception.TraceBusinessException;
import com.dili.common.service.BaseInfoRpcService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.UserHistory;
import com.dili.trace.domain.UsualAddress;
import com.dili.trace.dto.UserHistoryListDto;
import com.dili.trace.dto.UserHistoryStaticsDto;
import com.dili.trace.service.UserHistoryService;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UsualAddressService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
@Api("/userHistory")
@Controller
@RequestMapping("/userHistory")
public class UserHistoryController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserHistoryController.class);
	@Autowired
	UserHistoryService userHistoryService;

	@ApiOperation("跳转到User页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		Date now = new Date();
		
		modelMap.put("modifiedStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
		modelMap.put("modifiedEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
		
		modelMap.put("createdStart", "2019-01-01 00:00:00");
		modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
		return "userHistoryService/index";
	}

	@ApiOperation(value = "分页查询UserHistory", notes = "分页查询UserHistory，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "UserHistory", paramType = "form", value = "UserHistory的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(UserHistoryListDto userHistory) throws Exception {

		EasyuiPageOutput out = this.userHistoryService.listUserHistoryPageByExample(userHistory);
		return out.toString();
	}
	/**
	 * 查询统计信息
	 * @param userHistory
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value = "/queryStatics.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object queryStatics(UserHistoryListDto userHistory) throws Exception {
try {
		UserHistoryStaticsDto out = this.userHistoryService.queryStatics(userHistory);
		return out;
}catch (Exception e) {
	// TODO: handle exception
	e.printStackTrace();
}
return null;
	}

}