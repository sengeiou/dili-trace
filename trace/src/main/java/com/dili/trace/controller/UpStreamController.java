package com.dili.trace.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.RUserUpstream;
import com.dili.trace.domain.UpStream;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RUserUpstreamDto;
import com.dili.trace.dto.UpStreamDto;
import com.dili.trace.dto.UserListDto;
import com.dili.trace.service.RUserUpStreamService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.util.MarketUtil;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.UserQuery;
import com.dili.uap.sdk.rpc.UserRpc;
import com.dili.uap.sdk.session.SessionContext;
import io.swagger.annotations.Api;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
@Api("/upStream")
@Controller
@RequestMapping("/upStream")
public class UpStreamController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpStreamController.class);
    @Resource
    private UserRpc userRpc;
    @Autowired
    private UpStreamService upStreamService;
    @Autowired
    private RUserUpStreamService rUserUpStreamService;

    /**
     * 跳转到index页面
     *
     * @param modelMap
     * @return
     */

    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        Date now = new Date();
        modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
        modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
        return "upStream/index";
    }

    /**
     * @param modelMap
     * @param id
     * @return
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(ModelMap modelMap, Long id, Long userId) {
        if (null != id) {
            modelMap.put("upstream", upStreamService.get(id));
        }
        if (userId != null) {
            User userItem = this.userRpc.get(userId).getData();
            modelMap.put("userItem", userItem);
        } else {
            modelMap.put("userItem", null);
        }
        return "upStream/edit";
    }

    /**
     * 查询数据
     *
     * @param upStreamDto
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(@RequestBody  UpStreamDto upStreamDto) throws Exception {
        // 业户名称查询
        if (StringUtils.isNotBlank(upStreamDto.getLikeUserName())) {
            UserListDto userListDto = DTOUtils.newInstance(UserListDto.class);
            userListDto.setLikeName(upStreamDto.getLikeUserName());
            UserQuery userQuery = DTOUtils.newDTO(UserQuery.class);
            userQuery.setUserName(upStreamDto.getLikeUserName());
            BaseOutput<List<User>> baseOutput = userRpc.listByExample(userQuery);
            List<Long> userIds = new ArrayList<>(16);
            if (null != baseOutput && null != baseOutput.getData()) {
                userIds = baseOutput.getData().stream().map(o -> o.getId()).collect(Collectors.toList());
            }
            if (CollectionUtils.isEmpty(userIds)) {
                return new EasyuiPageOutput(0L, Collections.emptyList()).toString();
            }
            RUserUpstreamDto rUserUpstream = new RUserUpstreamDto();
            rUserUpstream.setUserIds(userIds);
            Set<Long> upstreamIds = rUserUpStreamService.listByExample(rUserUpstream).stream()
                    .map(o -> o.getUpstreamId()).collect(Collectors.toSet());
            upStreamDto.setIds(new ArrayList<>(upstreamIds));
            if (CollectionUtils.isEmpty(upStreamDto.getIds())) {
                return new EasyuiPageOutput(0L, Collections.emptyList()).toString();
            }
        }

        // 设置市场查询条件
        upStreamDto.setMarketId(MarketUtil.returnMarket());
        BasePage<UpStream> streamBasePage = upStreamService.listPageByExample(upStreamDto);
        Long totalItem = streamBasePage.getTotalItem();
        List<UpStream> upStreams = streamBasePage.getDatas();
        List<UpStreamDto> upStreamDtos = new ArrayList<>();
        if (!upStreams.isEmpty()) {
            List<Map<String, Object>> upstreamUsers = upStreamService
                    .queryUsersByUpstreamIds(upStreams.stream().map(o -> o.getId()).collect(Collectors.toList()));

            Map<Object, Object> idNameListMap = upstreamUsers.stream()
                    .collect(Collectors.toMap(m -> m.get("upstreamIds"), m -> m.get("userNames")));
            upStreams.forEach(o -> {
                UpStreamDto usd = new UpStreamDto();
                BeanUtils.copyProperties(o, usd);
                Object nameList = idNameListMap.get(o.getId());
                usd.setUserNames(nameList == null ? "" : String.valueOf(nameList));
                upStreamDtos.add(usd);
            });
        } else {
            return new EasyuiPageOutput(0L, Collections.emptyList()).toString();
        }
        List results = ValueProviderUtils.buildDataByProvider(upStreamDto, upStreamDtos);
        return new EasyuiPageOutput(totalItem, results).toString();

    }

    /**
     * 根据上游用户ID查询其绑定的所有业户
     *
     * @param upstreamId
     * @return
     */
    @RequestMapping(value = "/listUserByUpstreamId.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput<List<User>> listUserByUpstreamId(Long upstreamId, Long userId) {
        if (userId != null) {
            BaseOutput<User> baseOutput = userRpc.get(userId);
            return BaseOutput.success().setData(Arrays.asList(baseOutput.getData()));
        }

        RUserUpstream rUserUpstream = new RUserUpstream();
        rUserUpstream.setUpstreamId(upstreamId);
        List<String> userIds = rUserUpStreamService.list(rUserUpstream).stream().map(o -> String.valueOf(o.getUserId()))
                .collect(Collectors.toList());
        if (!userIds.isEmpty()) {
            UserQuery userQuery = DTOUtils.newInstance(UserQuery.class);
            userQuery.setIds(userIds);
            return BaseOutput.success().setData(userRpc.listByExample(userQuery).getData());
        }
        return BaseOutput.success().setData(new ArrayList<>());
    }

    /**
     * 保存数据
     *
     * @param upStreamDto
     * @return
     */
    @RequestMapping(value = "/save.action", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput save(@RequestBody UpStreamDto upStreamDto) {
        try {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            if (null == userTicket) {
                return BaseOutput.failure("未登录或登录过期");
            }
            //设置市场id
            upStreamDto.setMarketId(MarketUtil.returnMarket());
            OperatorUser operatorUser = new OperatorUser(userTicket.getId(), userTicket.getRealName());
            return null == upStreamDto.getId() ? upStreamService.addUpstream(upStreamDto, operatorUser)
                    : upStreamService.updateUpstream(upStreamDto, operatorUser);
        } catch (BusinessException e) {
            LOGGER.info("上游用户信息及业务绑定保存异常！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("上游用户信息及业务绑定保存异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 根据关键字查询
     *
     * @param userId
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/listByKeyWord.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput queryUpStream(@RequestParam(required = true, name = "userId") Long userId, @RequestParam(required = true, name = "query") String keyword) {
        try {
            List<UpStream> list = this.upStreamService.queryUpStreamByKeyword(userId, keyword);
            return BaseOutput.success().setData(list);

        } catch (Exception e) {
            LOGGER.error("查询失败", e);
            return BaseOutput.failure();
        }

    }

}