package com.dili.trace.api;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.domain.EventMessage;
import com.dili.trace.enums.MessageStateEnum;
import com.dili.trace.service.EventMessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 消息api
 */
@Api(value = "/api/message", description = "有关于消息相关的接口")
@RestController
@RequestMapping(value = "/api/message")
public class EventMessageApi {
    private static final Logger LOGGER= LoggerFactory.getLogger(EventMessageApi.class);

    @Autowired
    private LoginSessionContext sessionContext;
    @Autowired
    EventMessageService eventMessageService;

    @ApiOperation(value ="已读/未读", notes = "已读/未读")
    @RequestMapping(value = "/read.api", method = RequestMethod.POST)
    public void signRead(@RequestBody EventMessage eventMessage){
        if (eventMessage == null || null == eventMessage.getId()){
            return;
        }
        try{
            eventMessageService.readMessage(eventMessage, MessageStateEnum.READ);
        }catch (TraceBusinessException e){
            LOGGER.error(e.getMessage(),e);
        }catch (Exception e){
            LOGGER.error("register",e);
        }
    }


    @ApiOperation(value ="查询消息列表", notes = "消息列表")
    @RequestMapping(value = "/pageMssage.api", method = RequestMethod.POST)
    @InterceptConfiguration
    public BaseOutput<BasePage<EventMessage>> pageMessage(@RequestBody EventMessage eventMessage){
        if (eventMessage == null){
            eventMessage = new EventMessage();
        }
        try{
            Long id = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
            eventMessage.setReceiverId(id);
            eventMessage.setSort("create_time");
            eventMessage.setOrder("desc");
            BasePage<EventMessage> out = eventMessageService.listPageByExample(eventMessage);
            return BaseOutput.success().setData(out);
        }catch (TraceBusinessException e){
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("quit",e);
            return BaseOutput.failure();
        }
    }

}
