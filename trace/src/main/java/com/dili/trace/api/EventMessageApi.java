package com.dili.trace.api;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.domain.EventMessage;
import com.dili.trace.domain.TradeOrder;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.enums.MessageReceiverEnum;
import com.dili.trace.enums.MessageStateEnum;
import com.dili.trace.enums.MessageTypeEnum;
import com.dili.trace.service.EventMessageService;
import com.dili.trace.service.TradeOrderService;
import com.dili.trace.service.TradeRequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
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
    @Autowired
    TradeOrderService tradeOrderService;
    @Autowired
    TradeRequestService tradeRequestService;

    @ApiOperation(value = "已读/未读", notes = "已读/未读")
    @RequestMapping(value = "/read.api", method = RequestMethod.POST)
    public BaseOutput signRead(@RequestBody EventMessage eventMessage) {
        if (eventMessage == null || null == eventMessage.getId()) {
            return BaseOutput.failure("用户id为空");
        }
        try {
            eventMessageService.readMessage(eventMessage, MessageStateEnum.READ);
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
            eventMessage.setSort("read_flag,create_time");
            eventMessage.setOrder("asc,desc");
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
            setMessageOrderStatus(out);
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

    /**
     * 消息类型为订单，需要将订单状态返回
     * 用于前端跳转订单中，买家发起需要卖家确认，判断逻辑
     *
     * @param out
     */
    private void setMessageOrderStatus(BasePage<EventMessage> out) {
        List<EventMessage> messageList = out.getDatas();
        if (!messageList.isEmpty() && null != messageList) {
            StreamEx.of(messageList).nonNull().forEach(eventMessage -> {
                Integer sourceCode = eventMessage.getSourceBusinessType();
                //判断为订单交易类型
                if (null == eventMessage.getSourceBusinessType()) {
                    return;
                }
                if ((MessageTypeEnum.BUYERORDER.getCode().equals(sourceCode) || MessageTypeEnum.SALERORDER.getCode().equals(sourceCode))) {
                    Long sourceBusinessId = eventMessage.getSourceBusinessId();
                    if (null != sourceBusinessId) {
                        TradeRequest orderRequest = tradeRequestService.get(sourceBusinessId);
                        if (null != orderRequest) {
                            TradeOrder tradeOrder = this.tradeOrderService.get(orderRequest.getTradeOrderId());
                            if (null != tradeOrder) {
                                eventMessage.setSourceOrderStatus(tradeOrder.getOrderStatus());
                            }
                        }
                    }
                }
            });
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
