package com.dili.trace.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.common.entity.ExecutionConstants;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.util.DateUtils;
import com.dili.trace.dao.SmsMessageMapper;
import com.dili.trace.domain.EventMessage;
import com.dili.trace.domain.MessageConfig;
import com.dili.trace.domain.SmsMessage;
import com.dili.trace.domain.UserInfo;
import com.dili.trace.dto.MessageInputDto;
import com.dili.trace.enums.MessageReceiverEnum;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.rpc.service.FirmRpcService;
import com.dili.trace.service.*;
import com.dili.uap.sdk.domain.Firm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;

@Service
@EnableRetry
public class MessageServiceImpl extends BaseServiceImpl<MessageConfig, Long> implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private EventMessageService eventMessageService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageRpcService messageRpcService;

    @Autowired
    private SmsMessageMapper smsMessageMapper;

    @Autowired
    private WxAppletsMessageNotityService wxService;

    @Autowired
    ExecutionConstants executionConstants;
    @Autowired
    FirmRpcService firmRpcService;
    @Autowired
    CustomerRpcService clientRpcService;

    @Override
    public void addMessage(MessageInputDto messageInputDto,Long marektId) {
        logger.info("insert message, param:{}", JSON.toJSONString(messageInputDto));
        Integer messageType = messageInputDto.getMessageType();
        MessageConfig messageConfigParam = new MessageConfig();
        messageConfigParam.setOperation(messageType);
        MessageConfig messageConfig = getDao().selectOne(messageConfigParam);
        if (messageConfig == null) {
            logger.info("message config is null");
            return;
        }
        String messageFlag = messageConfig.getMessageFlag();
        String smsFlag = messageConfig.getSmsFlag();
        String wechatFlag = messageConfig.getWechatFlag();
        Optional<CustomerExtendDto> creatorUser = this.clientRpcService.findCustomerById(messageInputDto.getCreatorId(), marektId);
        Long[] receiverIdArray = messageInputDto.getReceiverIdArray();
        if (messageFlag.equals("1")) {
            logger.info("send event message");
            for (Long recevierId : receiverIdArray) {
                Optional<CustomerExtendDto> receiverUser = this.clientRpcService.findCustomerById(recevierId, marektId);
                EventMessage eventMessage = new EventMessage();
                String title = MessageFormat.format(messageConfig.getEventMessageTitle(),
                        messageInputDto.getEventMessageTitleParam());
                eventMessage.setTitle(title);
                eventMessage.setOverview(title);
                String content = MessageFormat.format(messageConfig.getEventMessageContent(),
                        messageInputDto.getEventMessageContentParam());
                eventMessage.setContent(content);
                creatorUser.ifPresent(c -> {
                    eventMessage.setCreator(c.getName());
                    eventMessage.setCreatorId(c.getId());
                });
                //管理员类型的用户查询为空
                receiverUser.ifPresent(r -> {
                    eventMessage.setReceiver(r.getName());
                });
                eventMessage.setReceiverId(recevierId);
                if (null == messageInputDto.getReceiverType()) {
                    eventMessage.setReceiverType(MessageReceiverEnum.MESSAGE_RECEIVER_TYPE_NORMAL.getCode());
                } else {
                    eventMessage.setReceiverType(messageInputDto.getReceiverType());
                }

                eventMessage.setSourceBusinessId(messageInputDto.getSourceBusinessId());
                eventMessage.setSourceBusinessType(messageInputDto.getSourceBusinessType());
                eventMessageService.addMessage(eventMessage);
            }

        }

        String marketCode=this.firmRpcService.getFirmById(marektId).map(Firm::getCode).orElse(null);
        if(marketCode==null){
            logger.error("查找不到对应的市场code");
            return;
        }
        if (smsFlag.equals("1")) {
            logger.info("send sms");
            for (Long recevierId : receiverIdArray) {
                UserInfo receiverUser = userService.get(recevierId);
                JSONObject params = new JSONObject();
                params.put("marketCode", marketCode);
                params.put("systemCode", ExecutionConstants.SYSTEM_CODE);
                params.put("sceneCode", messageConfig.getSmsSceneCode());
                params.put("cellphone", receiverUser.getPhone());
                // 根据不同messageType，传不同的参数（在各个消息节点中传递过来）
                params.put("parameters", messageInputDto.getSmsContentParam());
                logger.info("send sms RPC:" + params.toJSONString());
                BaseOutput msgOutput = messageRpcService.sendVerificationCodeMsg(params);

                //插入日志
                SmsMessage smsMessage = new SmsMessage();
                smsMessage.setCreated(DateUtils.getCurrentDate());
                smsMessage.setReceivePhone(receiverUser.getPhone());
                smsMessage.setSourceBusinessId(messageInputDto.getSourceBusinessId());
                smsMessage.setSourceBusinessType(messageInputDto.getSourceBusinessType());
                smsMessage.setResultCode(msgOutput.getCode());
                smsMessage.setResultInfo(msgOutput.getMessage());
                smsMessage.setSendReason(messageType);
                smsMessageMapper.insert(smsMessage);
            }
        }

        if (wechatFlag.equals("1")) {
            logger.info("send wechat message");
            // wxService.sendSubscribeMessageNotity("ohS1P5TvrPl_9opdpTvawbudNEwE",messageType.toString(),null);
        }
    }
}
