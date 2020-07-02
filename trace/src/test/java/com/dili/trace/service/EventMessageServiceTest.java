package com.dili.trace.service;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.EventMessageApi;
import com.dili.trace.domain.EventMessage;
import com.dili.trace.enums.MessageStateEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

@Rollback(false)
public class EventMessageServiceTest extends AutoWiredBaseTest {
    @Autowired
    EventMessageService eventMessageService;
    @Autowired
    EventMessageApi eventMessageApi;

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