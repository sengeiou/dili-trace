package com.dili.trace.api;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.domain.EventMessage;
import com.dili.trace.enums.MessageStateEnum;
import com.dili.trace.enums.MessageReceiverEnum;
import com.dili.trace.service.EventMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息api
 */
@Api(value = "/api/message", description = "有关于消息相关的接口")
@RestController
@RequestMapping(value = "/api/message")
public class EventMessageApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventMessageApi.class);

    @Autowired
    private LoginSessionContext sessionContext;
    @Autowired
    EventMessageService eventMessageService;

    @ApiOperation(value = "已读/未读", notes = "已读/未读")
    @RequestMapping(value = "/read.api", method = RequestMethod.POST)
    public BaseOutput signRead(@RequestBody EventMessage eventMessage) {
        if (eventMessage == null || null == eventMessage.getId()) {
            return BaseOutput.failure("用户id为空");
        }
        try {
            eventMessageService.readMessage(eventMessage, MessageStateEnum.READ);
            //更新同一事件为已读
            eventMessage = eventMessageService.get(eventMessage.getId());
            if (null != eventMessage.getSourceBusinessType() && null != eventMessage.getSourceBusinessId()) {
                EventMessage queObj = new EventMessage();
                queObj.setSourceBusinessType(eventMessage.getSourceBusinessType());
                queObj.setSourceBusinessId(eventMessage.getSourceBusinessId());
                EventMessage upObj = new EventMessage();
                upObj.setReadFlag(MessageStateEnum.READ.getCode());
                eventMessageService.updateExactByExample(upObj, queObj);
            }
            return BaseOutput.success();
        } catch (TraceBusinessException e) {
            LOGGER.error(e.getMessage(), e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("register", e);
            return BaseOutput.failure(e.getMessage());
        }
    }


    @Deprecated
    @ApiOperation(value = "查询消息列表", notes = "消息列表")
    @RequestMapping(value = "/pageMssage.api", method = RequestMethod.POST)
    @InterceptConfiguration
    public BaseOutput<BasePage<EventMessage>> pageMessage(@RequestBody EventMessage eventMessage) {
        if (eventMessage == null) {
            eventMessage = new EventMessage();
        }
        try {
            Long id = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
            eventMessage.setReceiverId(id);
            eventMessage.setSort("create_time");
            eventMessage.setOrder("desc");
            //默认查询普通用户消息
            if (null == eventMessage.getReceiverType()) {
                eventMessage.setReceiverType(MessageReceiverEnum.MESSAGE_RECEIVER_TYPE_NORMAL.getCode());
            }
            BasePage<EventMessage> out = eventMessageService.listPageByExample(eventMessage);
            return BaseOutput.success().setData(out);
        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("quit", e);
            return BaseOutput.failure();
        }
    }

    @ApiOperation(value = "查询消息列表（新）", notes = "消息列表")
    @RequestMapping(value = "/listPage.api", method = RequestMethod.POST)
    @InterceptConfiguration
    public BaseOutput<BasePage<EventMessage>> listPage(@RequestBody EventMessage eventMessage) {
        if (eventMessage == null) {
            return BaseOutput.failure("param is null");
        }
        try {
            eventMessage.setSort("create_time");
            eventMessage.setOrder("desc");
            //默认查询普通用户消息
            if (null == eventMessage.getReceiverType()) {
                eventMessage.setReceiverType(MessageReceiverEnum.MESSAGE_RECEIVER_TYPE_NORMAL.getCode());
            }
            BasePage<EventMessage> out = eventMessageService.listPageByExample(eventMessage);
            eventMessage.setReadFlag(MessageStateEnum.UNREAD.getCode());
            List<EventMessage> unReadList = eventMessageService.listByExample(eventMessage);
            int pendReadCount = 0;
            if (!unReadList.isEmpty()) {
                pendReadCount = unReadList.size();
            }
            Map<String, Object> map = new HashMap<>();
            map.put("list", out);
            map.put("pendReadCount", pendReadCount);
            return BaseOutput.success().setData(map);
        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("quit", e);
            return BaseOutput.failure();
        }
    }

    @ApiOperation(value = "已读全部", notes = "已读全部")
    @RequestMapping(value = "/doReadAll.api", method = RequestMethod.POST)
    public BaseOutput doReadAll(@RequestBody EventMessage eventMessage) {
        if (eventMessage == null || null == eventMessage.getReceiverId()) {
            LOGGER.info("已读全部");
            return BaseOutput.failure("已读全部用户id未获取");
        }
        try {
            EventMessage upSource = new EventMessage();
            upSource.setReadFlag(MessageStateEnum.READ.getCode());
            eventMessageService.updateExactByExample(upSource, eventMessage);

            //更新同一事件为已读
            List<EventMessage> messageList = eventMessageService.listByExample(eventMessage);
            if (!messageList.isEmpty()) {
                messageList.stream().forEach(s -> {
                    if (null != s.getSourceBusinessType() && null != s.getSourceBusinessId()) {
                        EventMessage queObj = new EventMessage();
                        queObj.setSourceBusinessType(s.getSourceBusinessType());
                        queObj.setSourceBusinessId(s.getSourceBusinessId());
                        EventMessage upObj = new EventMessage();
                        upObj.setReadFlag(MessageStateEnum.READ.getCode());
                        eventMessageService.updateExactByExample(upObj, queObj);
                    }
                });
            }

            return BaseOutput.success().setData(new HashMap<>().put("isRead", 1));
        } catch (TraceBusinessException e) {
            LOGGER.error(e.getMessage(), e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("register", e);
            return BaseOutput.failure(e.getMessage());
        }
    }
}
