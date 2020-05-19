package com.dili.trace.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.trace.domain.RUserUpstream;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RUserUpstreamDto;
import com.dili.trace.dto.UpStreamDto;
import com.dili.trace.dto.UserListDto;
import com.dili.trace.service.RUserUpStreamService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;
import com.diligrp.manage.sdk.domain.UserTicket;
import com.diligrp.manage.sdk.session.SessionContext;

import org.apache.commons.collections.CollectionUtils;
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

import io.swagger.annotations.Api;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
@Api("/upStream")
@Controller
@RequestMapping("/upStream")
public class UpStreamController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UpStreamController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private UpStreamService upStreamService;
	@Autowired
	private RUserUpStreamService rUserUpStreamService;

	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		return "upStream/index";
	}

	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(RUserUpstreamDto rUserUpstreamDto) throws Exception {
		if(StringUtils.isNotBlank(rUserUpstreamDto.getLikeUserName())){
			UserListDto userListDto = DTOUtils.newInstance(UserListDto.class);
			userListDto.setLikeName(rUserUpstreamDto.getLikeUserName());
			List<Long> userIds = userService.listByExample(userListDto).stream().map(o->o.getId()).collect(Collectors.toList());
			rUserUpstreamDto.setUserIds(userIds);
			if(CollectionUtils.isEmpty(userIds)){
				return new EasyuiPageOutput(0, Collections.emptyList()).toString();
			}
		}

		if(StringUtils.isNotBlank(rUserUpstreamDto.getLikeUpstreamName())){
			UpStreamDto upStreamDto = new UpStreamDto();
			upStreamDto.setLikeName(rUserUpstreamDto.getLikeUpstreamName());
			List<Long> upstreamIds = upStreamService.listByExample(upStreamDto).stream().map(o->o.getId()).collect(Collectors.toList());
			rUserUpstreamDto.setUpstreamIds(upstreamIds);
			if(CollectionUtils.isEmpty(upstreamIds)){
				return new EasyuiPageOutput(0, Collections.emptyList()).toString();
			}
		}

		return rUserUpStreamService.listEasyuiPageByExample(rUserUpstreamDto,true).toString();
	}

    /**
     * 根据上游用户ID查询其绑定的所有业户
     * @param upstreamId
     * @return
     */
    @RequestMapping(value = "/listUserByUpstreamId.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody
    BaseOutput<List<User>> listUserByUpstreamId(Long upstreamId) {
        RUserUpstream rUserUpstream = new RUserUpstream();
        rUserUpstream.setUpstreamId(upstreamId);
        List<Long> userIds = rUserUpStreamService.list(rUserUpstream).stream().map(o->o.getUserId()).collect(Collectors.toList());
        if(!userIds.isEmpty()){
            UserListDto userListDto = DTOUtils.newInstance(UserListDto.class);
            userListDto.setIds(userIds);
            return BaseOutput.success().setData(userService.listByExample(userListDto));
        }
        return BaseOutput.success().setData(new ArrayList<>());
    }

    @RequestMapping(value="/save.action", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput saveLeaseOrder(@RequestBody UpStreamDto upStreamDto){
        try{
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if(null == userTicket){
				return BaseOutput.failure("未登录或登录过期");
			}
			OperatorUser operatorUser=new OperatorUser(userTicket.getId(), userTicket.getRealName());
            return null == upStreamDto.getId() ? upStreamService.addUpstream(upStreamDto,operatorUser) : upStreamService.updateUpstream(upStreamDto,operatorUser);
        }catch (BusinessException e){
            LOGGER.info("上游用户信息及业务绑定保存异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOGGER.error("上游用户信息及业务绑定保存异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

}