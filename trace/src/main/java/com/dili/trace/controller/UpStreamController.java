package com.dili.trace.controller;

import com.dili.customer.sdk.domain.dto.CustomerExtendDto;

import com.dili.customer.sdk.rpc.CustomerRpc;
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
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.service.MarketService;
import com.dili.trace.service.RUserUpStreamService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.util.MarketUtil;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.UserQuery;
import com.dili.uap.sdk.rpc.UserRpc;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import one.util.streamex.StreamEx;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
@Api("/upStream")
@Controller
@RequestMapping("/upStream")
public class UpStreamController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpStreamController.class);
    @Autowired
    private UserRpc userRpc;
    @Autowired
    private UpStreamService upStreamService;
    @Autowired
    private RUserUpStreamService rUserUpStreamService;
    @Autowired
    private CustomerRpc customerRpc;

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
     * 跳转到查看详情页面
     * @param modelMap
     * @param id
     * @return
     */
    @RequestMapping(value = "/view.html", method = RequestMethod.GET)
    public String view(ModelMap modelMap, Long id, Long userId) {
        if (null != id) {
            modelMap.put("upstream", upStreamService.get(id));
        }
        if (userId != null) {
            User userItem = this.userRpc.get(userId).getData();
            modelMap.put("userItem", userItem);
        } else {
            modelMap.put("userItem", null);
        }
        return "upStream/view";
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
    EasyuiPageOutput listPage(@RequestBody UpStreamDto upStreamDto) throws Exception {
        // 业户名称查询
        if (StringUtils.isNotBlank(upStreamDto.getLikeUserName())) {
            upStreamDto.setMetadata(IDTO.AND_CONDITION_EXPR, " id in(select upstream_id from r_user_upstream where user_name like '%"+upStreamDto.getLikeUserName()+"%')");
        }

        // 设置市场查询条件
        upStreamDto.setMarketId(MarketUtil.returnMarket());
        BasePage<UpStream> streamBasePage = upStreamService.listPageByExample(upStreamDto);
        Long totalItem = streamBasePage.getTotalItem();
        List<UpStream> upStreams = streamBasePage.getDatas();
        List<UpStreamDto> upStreamDtos = this. buildUpUserMap(upStreams);
        List results = ValueProviderUtils.buildDataByProvider(upStreamDto, upStreamDtos);
        return new EasyuiPageOutput(totalItem, results);

    }

    /**
     * 构建上游企业用户map
     *
     * @param upStreams
     * @return
     */
    private   List<UpStreamDto> buildUpUserMap(List<UpStream> upStreams) {
        if(upStreams==null||upStreams.isEmpty()){
            return Lists.newArrayList();
        }
        //查询上游企业关联用户列表
        RUserUpstreamDto rUserUpstreamDto = new RUserUpstreamDto();
        rUserUpstreamDto.setUpstreamIds(StreamEx.of(upStreams).nonNull().map(o -> o.getId()).distinct().toList());
        List<RUserUpstream> upstreamsRefs = rUserUpStreamService.listByExample(rUserUpstreamDto);
        //用户id，name组成用户map
        Map<Long, List<String>> userMap = StreamEx.of(upstreamsRefs)
                .nonNull().mapToEntry(RUserUpstream::getUpstreamId, RUserUpstream::getUserName)
                .filterKeys(Objects::nonNull).grouping();
        List<UpStreamDto> upStreamDtos = new ArrayList<>();

        upStreams.forEach(o -> {
            UpStreamDto usd = new UpStreamDto();
            BeanUtils.copyProperties(o, usd);
            List<String> nameList = userMap.getOrDefault(o.getId(),Lists.newArrayList());
            usd.setUserNames(nameList.isEmpty()? "" : StringUtils.strip(String.join(",",nameList), "[]"));
            upStreamDtos.add(usd);
        });

        return upStreamDtos;
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
            List<CustomerExtendDto> customerExtendDtoList = new ArrayList<>();
            userIds.forEach(u ->{
                CustomerExtendDto customerExtendDto = customerRpc.get(Long.valueOf(u), MarketUtil.returnMarket()).getData();
                customerExtendDtoList.add(customerExtendDto);
            });
            return BaseOutput.success().setData(customerExtendDtoList);
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
     * @param query
     * @return
     */
    @RequestMapping(value = "/listByKeyWord.action", method = {RequestMethod.POST})
    @ResponseBody
    public BaseOutput queryUpStream(@RequestBody UpStreamDto query) {
        try {
            if(query==null||query.getUserId()==null){
                return BaseOutput.successData(Lists.newArrayList());
            }
            List<UpStream> list = this.upStreamService.queryUpStreamByKeyword(query.getUserId(), query.getKeyword());
            return BaseOutput.success().setData(list);

        } catch (Exception e) {
            LOGGER.error("查询失败", e);
            return BaseOutput.failure();
        }

    }

}