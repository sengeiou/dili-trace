package com.dili.trace.service;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.EventMessageApi;
import com.dili.trace.domain.EventMessage;
import com.dili.trace.dto.MessageInputDto;
import com.dili.trace.enums.MessageStateEnum;
import com.dili.trace.enums.MessageTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.awt.*;
import java.text.MessageFormat;

@Rollback(false)
public class EventMessageServiceTest extends AutoWiredBaseTest {
    @Autowired
    EventMessageService eventMessageService;
    @Autowired
    EventMessageApi eventMessageApi;

    @Autowired
    MessageService messageService;



    @Test
    public void addMessageTest()
    {
        MessageInputDto inputDto = new MessageInputDto();
        inputDto.setMessageType(MessageTypeEnum.USERREGISTER.getCode());
        inputDto.setCreatorId(1L);
        inputDto.setReceiverIdArray(new Long[]{1277L});
        inputDto.setSourceBusinessType(MessageStateEnum.BUSINESS_TYPE_USER.getCode());
        inputDto.setSourceBusinessId(1L);
        inputDto.setEventMessageTitleParam(new Object[]{"xx"});
        inputDto.setEventMessageContentParam(new Object[]{"xxxccc"});
        messageService.addMessage(inputDto);
    }

    @Test
    public void addTest(){
        System.out.println("-------------->开始");
        for (int i = 1 ; i < 2; i++){
            EventMessage eventMessage = new EventMessage();
            eventMessage.setTitle("测试消息：报备单已审核，编号是"+i);
            eventMessage.setOverview("测试消息概览：报备单已审核，编号是"+i);
            eventMessage.setCreator("系统管理员-甲");
            eventMessage.setSourceBusinessId(Long.valueOf(i));
            eventMessage.setSourceBusinessType(MessageStateEnum.BUSINESS_TYPE_BILL.getCode());
            eventMessage.setReceiverType(EventMessage.RECEIVER_TYPE);
            eventMessage.setReceiverId(18l);
            eventMessage.setReceiver("庞先生");

            eventMessageService.addMessage(eventMessage);
        }
        System.out.println("---------------->结束");
    }

    @Test
    public void readIt(){
        eventMessageApi.signRead(null);
    }

    @Test
    public void messageList(){
        EventMessage eventMessage = new EventMessage();
        eventMessage.setRows(5);
        eventMessage.setReceiverId(18l);
        BaseOutput<BasePage<EventMessage>> out = eventMessageApi.pageMessage(eventMessage);
        System.out.println(JSONObject.toJSONString(out));
    }
}
