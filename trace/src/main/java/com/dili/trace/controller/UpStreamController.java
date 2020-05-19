package com.dili.trace.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.RUserUpstream;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.dto.*;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.service.*;

import com.dili.trace.util.BeanMapUtil;
import com.github.pagehelper.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
		Date now = new Date();
		modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
		modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
		return "upStream/index";
	}

	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(UpStreamDto upStreamDto) throws Exception {
		//业户名称查询
		if(StringUtils.isNotBlank(upStreamDto.getLikeUserName())){
			UserListDto userListDto = DTOUtils.newInstance(UserListDto.class);
			userListDto.setLikeName(upStreamDto.getLikeUserName());
			List<Long> userIds = userService.listByExample(userListDto).stream().map(o->o.getId()).collect(Collectors.toList());
			if(CollectionUtils.isEmpty(userIds)){
				return new EasyuiPageOutput(0, Collections.emptyList()).toString();
			}

			RUserUpstreamDto rUserUpstream = new RUserUpstreamDto();
			rUserUpstream.setUserIds(userIds);
			Set<Long> upstreamIds = rUserUpStreamService.listByExample(rUserUpstream).stream().map(o->o.getUpstreamId()).collect(Collectors.toSet());
			upStreamDto.setIds(new ArrayList<>(upstreamIds));
			if(CollectionUtils.isEmpty(upStreamDto.getIds())){
				return new EasyuiPageOutput(0, Collections.emptyList()).toString();
			}
		}

		List<UpStream> upStreams = upStreamService.listByExample(upStreamDto);
		List<UpStreamDto> upStreamDtos = new ArrayList<>();
		if(!upStreams.isEmpty()){
			List<Map<String,String>> upstreamUsers = upStreamService.queryUsersByUpstreamIds(upStreams.stream().map(o->o.getId()).collect(Collectors.toList()));
			Map<String,String> userNamesMap = BeanMapUtil.listToMap(upstreamUsers,"upstreamIds","userNames");
			upStreams.forEach(o->{
				UpStreamDto usd = new UpStreamDto();
				BeanUtils.copyProperties(o,usd);
				usd.setUserNames(userNamesMap.get(o.getId()));
				upStreamDtos.add(usd);
			});
		}else{
			return new EasyuiPageOutput(0, Collections.emptyList()).toString();
		}
		List results = ValueProviderUtils.buildDataByProvider(upStreamDto, upStreamDtos);
		long total = results instanceof Page ? ( (Page) results).getTotal() : results.size();
		return  new EasyuiPageOutput(Integer.parseInt(String.valueOf(total)), results).toString();
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
    public @ResponseBody BaseOutput save(@RequestBody UpStreamDto upStreamDto){
        try{
            return null == upStreamDto.getId() ? upStreamService.addUpstream(upStreamDto) : upStreamService.updateUpstream(upStreamDto);
        }catch (BusinessException e){
            LOGGER.info("上游用户信息及业务绑定保存异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOGGER.error("上游用户信息及业务绑定保存异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

}