package com.dili.trace.service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.dao.UserQrHistoryMapper;
import com.dili.trace.domain.*;
import com.dili.trace.dto.query.UserQrHistoryQueryDto;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.QrHistoryEventTypeEnum;
import com.dili.trace.glossary.UserQrStatusEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import one.util.streamex.StreamEx;

@Service
public class UserQrHistoryService extends BaseServiceImpl<UserQrHistory, Long> implements CommandLineRunner {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    TradeRequestService tradeRequestService;
    @Autowired
    BillService billService;
    @Autowired
    UserQrHistoryMapper qrHistoryMapper;

    public void run(String... args) {

    }

    public UserQrHistory createUserQrHistoryForWithousBills(UserQrHistoryQueryDto historyQueryDto) {
    	  UserQrStatusEnum qrStatusEnum = UserQrStatusEnum.BLACK;
    	  historyQueryDto.setContent("最近七天无报备且无交易单" + ",变为" + qrStatusEnum.getDesc() + "码");
    	this.qrHistoryMapper.updateQrStatusByQrHistory(historyQueryDto);
        UserInfo userItem = this.userInfoService.findByUserId(userId).orElse(null);
        if (java.util.Objects.equals(userItem, null)) {
            return null;
        }
        UserQrStatusEnum qrStatusEnum = UserQrStatusEnum.BLACK;
        UserQrHistoryQueryDto qrhis=new UserQrHistoryQueryDto();
        qrhis.setQrStatus(qrStatusEnum.getCode());
        
        
       
        this.updateUserQrStatus(userItem.getId(), qrStatusEnum);
        return this.findLatestUserQrHistoryByUserId(userItem.getId()).filter(qrhis -> {
            return qrStatusEnum.equalsCode(qrhis.getQrStatus());
        }).orElseGet(() -> {
            String color = qrStatusEnum.getDesc();
            UserQrHistory userQrHistory = this.buildUserQrHistory(userItem, qrStatusEnum,QrHistoryEventTypeEnum.NO_DATA,null);
            userQrHistory.setContent("最近七天无报备且无交易单" + ",变为" + color + "码");
            this.insertSelective(userQrHistory);
            return userQrHistory;
        });

    }

    public UserQrHistory createUserQrHistoryForUserRegist(Long userId, UserQrStatusEnum qrStatusEnum) {
        UserInfo userItem = this.userInfoService.findByUserId(userId).orElse(null);
        if (java.util.Objects.equals(userItem, null)) {
            return null;
        }

        this.updateUserQrStatus(userItem.getId(), qrStatusEnum);

        return this.findLatestUserQrHistoryByUserId(userItem.getId()).filter(qrhis -> {
            return qrStatusEnum.equalsCode(qrhis.getQrStatus());
        }).orElseGet(() -> {
            String color = qrStatusEnum.getDesc();

            UserQrHistory userQrHistory = this.buildUserQrHistory(userItem, qrStatusEnum,QrHistoryEventTypeEnum.NEW_USER,null);
            userQrHistory.setContent("完成注册,默认为" + color + "码");
            this.insertSelective(userQrHistory);
            return userQrHistory;
        });
    }

    public UserQrHistory createUserQrHistoryForVerifyBill(RegisterBill billItem, Long userId) {
        if (billItem == null) {
            return null;
        }
        BillVerifyStatusEnum billVerifyStatusEnum = BillVerifyStatusEnum.fromCode(billItem.getVerifyStatus())
                .orElse(null);
        if (billVerifyStatusEnum == null) {
            return null;
        }
        UserQrStatusEnum userQrStatus = getUserQrStatusEnum(billVerifyStatusEnum);
        Integer qrStatus = userQrStatus.getCode();
        UserInfo userItem = this.userInfoService.findByUserId(userId).orElse(null);
        if (java.util.Objects.equals(userItem, null)) {
            return null;
        }

        this.updateUserQrStatus(userItem.getId(), userQrStatus);
        return this.findLatestUserQrHistoryByUserId(userItem.getId()).filter(qrhis -> {
            return userQrStatus.equalsCode(qrhis.getQrStatus());
        }).orElseGet(() -> {
            String color = UserQrStatusEnum.fromCode(qrStatus).map(UserQrStatusEnum::getDesc)
                    .orElse(UserQrStatusEnum.BLACK.getDesc());
            UserQrHistory userQrHistory = this.buildUserQrHistory(userItem, userQrStatus,QrHistoryEventTypeEnum.REGISTER_BILL,billItem.getId());
            userQrHistory.setVerifyStatus(billVerifyStatusEnum.getCode());
            userQrHistory.setContent("最新操作报备单审核状态是" + billVerifyStatusEnum.getName() + ",变为" + color + "码");
            this.insertSelective(userQrHistory);
            return userQrHistory;
        });
    }

    private UserQrStatusEnum getUserQrStatusEnum(BillVerifyStatusEnum billVerifyStatusEnum) {
        UserQrStatusEnum userQrStatus = UserQrStatusEnum.BLACK;
        switch (billVerifyStatusEnum) {
            case PASSED:
                userQrStatus = UserQrStatusEnum.GREEN;
                break;
            case NO_PASSED:
                userQrStatus = UserQrStatusEnum.RED;
                break;
            case RETURNED:
                userQrStatus = UserQrStatusEnum.YELLOW;
                break;
            case WAIT_AUDIT:
                userQrStatus = UserQrStatusEnum.YELLOW;
                break;
            default:
                throw new TraceBizException("错误");
        }
        return userQrStatus;
    }

    public UserQrHistory createUserQrHistoryForOrder(Long tradeRequestId, Long userId) {
        UserInfo userItem = this.userInfoService.findByUserId(userId).orElse(null);
        if (java.util.Objects.equals(userItem, null)) {
            return null;
        }
        UserQrStatusEnum qrStatusEnum = UserQrStatusEnum.GREEN;
        this.updateUserQrStatus(userItem.getId(), UserQrStatusEnum.GREEN);

        return this.findLatestUserQrHistoryByUserId(userItem.getId()).filter(qrhis -> {
            return qrStatusEnum.equalsCode(qrhis.getQrStatus());
        }).orElseGet(() -> {
            String color = qrStatusEnum.getDesc();

            UserQrHistory userQrHistory = this.buildUserQrHistory(userItem, qrStatusEnum,QrHistoryEventTypeEnum.TRADE_REQUEST,tradeRequestId);
            userQrHistory.setContent("订单交易完成, 变为" + color + "码");
            this.insertSelective(userQrHistory);
            return userQrHistory;
        });
    }




    protected Date start(LocalDateTime now) {
        Date start = Date.from(
                now.minusDays(6).atZone(ZoneId.systemDefault()).toInstant());
        return start;
    }

    protected Date end(LocalDateTime now) {
        Date end = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        return end;
    }




    private void updateUserQrStatus(Long userId, UserQrStatusEnum qrStatus) {
        this.userInfoService.updateUserQrByUserId(userId, qrStatus);
    }

    private UserQrHistory buildUserQrHistory(UserInfo userItem
            ,UserQrStatusEnum qrStatusEnum
            ,QrHistoryEventTypeEnum historyEventTypeEnum
            ,Long qrHistoryEventId) {

        UserQrHistory userQrHistory = new UserQrHistory();
        userQrHistory.setUserId(userItem.getUserId());
        userQrHistory.setUserName(userItem.getName());
        userQrHistory.setQrStatus(qrStatusEnum.getCode());
        userQrHistory.setCreated(new Date());
        userQrHistory.setModified(new Date());
        userQrHistory.setIsValid(YesOrNoEnum.YES.getCode());
        userQrHistory.setQrHistoryEventType(historyEventTypeEnum.getCode());
        return userQrHistory;

    }

    private Optional<UserQrHistory> findLatestUserQrHistoryByUserId(Long userId) {
        UserQrHistory query = new UserQrHistory();
        query.setUserId(userId);
        query.setSort("id");
        query.setOrder("desc");
        query.setPage(1);
        query.setRows(1);
        query.setIsValid(YesOrNoEnum.YES.getCode());
        return StreamEx.of(this.listPageByExample(query).getDatas()).findFirst();
    }

    /**
     * 回退交易二维码记录
     * @param tradeRequest
     */
    public void rollBackByTradeRequest(TradeRequest tradeRequest) {

        if (tradeRequest == null  || tradeRequest.getId() == null) {
            return;
        }
        TradeRequest item=this.tradeRequestService.get(tradeRequest.getId());
        Long userId = tradeRequest.getBuyerId();
        UserQrHistory rollbackQrHistory = new UserQrHistory();
        rollbackQrHistory.setUserId(item.getBuyerId());
        rollbackQrHistory.setQrHistoryEventId(item.getTradeRequestId());
        this.rollback(rollbackQrHistory, QrHistoryEventTypeEnum.TRADE_REQUEST);
    }

    /**
     * 回退报备单二维码颜色记录
     * @param registerBill
     */
    public void rollbackByBill(RegisterBill registerBill) {


        if (registerBill == null  || registerBill.getId() == null) {
            return;
        }
        RegisterBill item=this.billService.get(registerBill.getId());
        if(item==null){
            return;
        }
        UserQrHistory rollbackQrHistory = new UserQrHistory();
        rollbackQrHistory.setUserId(item.getUserId());
        rollbackQrHistory.setQrHistoryEventId(item.getId());
        this.rollback(rollbackQrHistory, QrHistoryEventTypeEnum.REGISTER_BILL);

    }

    /**
     * 回退记录
     * @param rollbackQrHistory
     * @param historyTypeEnum
     */
    private void rollback(UserQrHistory rollbackQrHistory, QrHistoryEventTypeEnum historyTypeEnum) {
        if(rollbackQrHistory==null||historyTypeEnum==null){
            return;
        }
        if(rollbackQrHistory.getUserId()==null||rollbackQrHistory.getQrHistoryEventId()==null){
            return;
        }

        Long userId=rollbackQrHistory.getUserId();

        UserQrHistory domain = new UserQrHistory();
        // domain.setBillId(deletedBillId);
        domain.setIsValid(YesOrNoEnum.NO.getCode());

        UserQrHistory condition = new UserQrHistory();
        condition.setQrHistoryEventId(rollbackQrHistory.getQrHistoryEventId());
        condition.setUserId(userId);
        this.updateSelectiveByExample(domain, condition);

        UserQrHistory query = new UserQrHistory();
        query.setUserId(userId);
        query.setPage(1);
        query.setRows(1);
        query.setSort("id");
        query.setOrder("desc");
        query.setIsValid(YesOrNoEnum.YES.getCode());

        UserQrStatusEnum qrStatusEnum = StreamEx.of(this.listPageByExample(query).getDatas())
                .map(UserQrHistory::getQrStatus).map(UserQrStatusEnum::fromCode)
                .filter(Optional::isPresent).map(Optional::get).findFirst().orElse(UserQrStatusEnum.BLACK);

        this.updateUserQrStatus(userId, qrStatusEnum);

    }


}