package com.dili.trace.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.common.entity.ExecutionConstants;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.util.DateUtils;
import com.dili.trace.dao.SmsMessageMapper;
import com.dili.trace.domain.EventMessage;
import com.dili.trace.domain.MessageConfig;
import com.dili.trace.domain.SmsMessage;
import com.dili.trace.domain.User;
import com.dili.trace.dto.MessageInputDto;
import com.dili.trace.enums.MessageStateEnum;
import com.dili.trace.rpc.MessageRpc;
import com.dili.trace.service.EventMessageService;
import com.dili.trace.service.MessageService;
import com.dili.trace.service.UserService;
import com.dili.trace.service.WxAppletsMessageNotityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@EnableRetry
public class MessageServiceImpl  extends BaseServiceImpl<MessageConfig,Long> implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private EventMessageService eventMessageService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageRpc messageRpc;

    @Autowired
    private SmsMessageMapper smsMessageMapper;

    @Autowired
    private WxAppletsMessageNotityService wxService;

    @Override
    public void addMessage(MessageInputDto messageInputDto) {
        logger.info("insert message, param:{}", JSON.toJSONString(messageInputDto));
        Integer messageType = messageInputDto.getMessageType();
        MessageConfig messageConfigParam = new MessageConfig();
        messageConfigParam.setOperation(messageType);
        MessageConfig messageConfig = getDao().selectOne(messageConfigParam);
        String messageFlag = messageConfig.getMessageFlag();
        String smsFlag = messageConfig.getSmsFlag();
        String wechatFlag = messageConfig.getWechatFlag();
        User creatorUser = userService.get(messageInputDto.getCreatorId());
        Long[] receiverIdArray = messageInputDto.getReceiverIdArray();
        if(messageFlag.equals("1"))
        {
            logger.info("send event message");
            for (Long recevierId:receiverIdArray) {
                User receiverUser = userService.get(recevierId);
                EventMessage eventMessage = new EventMessage();
                String title =  MessageFormat.format(messageConfig.getEventMessageTitle(),
                        messageInputDto.getEventMessageTitleParam());
                eventMessage.setTitle(title);
                eventMessage.setOverview(title);
                String content = MessageFormat.format(messageConfig.getEventMessageContent(),
                        messageInputDto.getEventMessageContentParam());
                eventMessage.setContent(content);
                eventMessage.setCreator(creatorUser.getName());
                eventMessage.setCreatorId(creatorUser.getId());
                //管理员类型的用户查询为空
                if(null!=receiverUser){
                    eventMessage.setReceiver(receiverUser.getName());
                }
                eventMessage.setReceiverId(recevierId);
                if(null==messageInputDto.getReceiverType()){
                    eventMessage.setReceiverType(MessageStateEnum.MESSAGE_RECEIVER_TYPE_NORMAL.getCode());
                }else{
                    eventMessage.setReceiverType(messageInputDto.getReceiverType());
                }

                eventMessage.setSourceBusinessId(messageInputDto.getSourceBusinessId());
                eventMessage.setSourceBusinessType(messageInputDto.getSourceBusinessType());
                eventMessageService.addMessage(eventMessage);
            }

        }

        if(smsFlag.equals("1"))
        {
            logger.info("send sms");
            for (Long recevierId:receiverIdArray) {
                User receiverUser = userService.get(recevierId);
                JSONObject params = new JSONObject();
                params.put("marketCode", ExecutionConstants.MARKET_CODE);
                params.put("systemCode", ExecutionConstants.SYSTEM_CODE);
                params.put("sceneCode", messageConfig.getSmsSceneCode());
                params.put("cellphone", receiverUser.getPhone());
                // 根据不同messageType，传不同的参数（在各个消息节点中传递过来）
                params.put("parameters", messageInputDto.getSmsContentParam());
                logger.info("send sms RPC:"+params.toJSONString());
                BaseOutput msgOutput = messageRpc.sendVerificationCodeMsg(params);

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

        if(wechatFlag.equals("1"))
        {
            logger.info("send wechat message");
           // wxService.sendSubscribeMessageNotity("ohS1P5TvrPl_9opdpTvawbudNEwE",messageType.toString(),null);
        }
    }
}
