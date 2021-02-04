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

import java.util.List;

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

    /**
     * 更新过期用户的状态
     * @param historyQueryDto
     */
    public void updateUserQrForExpire(UserQrHistoryQueryDto historyQueryDto) {
        UserQrStatusEnum qrStatusEnum = UserQrStatusEnum.BLACK;
        historyQueryDto.setQrStatus(qrStatusEnum.getCode());
        //查询出符合条件的userid
        List<Long> userIdList = this.qrHistoryMapper.selectUserIdWithoutHistory(historyQueryDto);

        StreamEx.of(userIdList).forEach(userId -> {
            //锁定对应的userinfo对象
            this.userInfoService.selectByUserIdForUpdate(userId).ifPresent(userInfoItem->{
                historyQueryDto.setUserId(userId);
                //再次查询确认是否符合条件
                boolean isUpdateQr=this.qrHistoryMapper.selectUserIdWithoutHistory(historyQueryDto).contains(userId);
                if(!isUpdateQr){
                    return;
                }
                //插入qrhistory对象
                String content = "最近七天无报备且无交易单" + ",变为" + qrStatusEnum.getDesc() + "码";
                 this.buildUserQrHistory(userId, qrStatusEnum, QrHistoryEventTypeEnum.NO_DATA, null).ifPresent(userQrHistory-> {
                    userQrHistory.setContent(content);
                    this.insertSelective(userQrHistory);

                    //更新userinfo的qr信息
                    UserInfo userInfo = new UserInfo();
                    userInfo.setId(userInfoItem.getId());
                    userInfo.setQrHistoryId(userQrHistory.getId());
                    userInfo.setPreQrStatus(userInfoItem.getQrStatus());
                    userInfo.setQrStatus(qrStatusEnum.getCode());
                    userInfo.setQrContent(content);
                    this.userInfoService.updateSelective(userInfo);
                });
            });

        });

    }

    public void createUserQrHistoryForUserRegist(Long userId, Long marketId) {
        this.userInfoService.saveUserInfo(userId,marketId).ifPresent(userInfoItem->{

            if(userInfoItem.getQrHistoryId()!=null){
                return ;
            }
            UserQrStatusEnum qrStatusEnum = UserQrStatusEnum.BLACK;
            //插入qrhistory对象
            String content ="完成注册,默认为" + qrStatusEnum.getDesc() + "码";
            this.buildUserQrHistory(userId, qrStatusEnum, QrHistoryEventTypeEnum.NEW_USER, null).ifPresent(userQrHistory-> {

                userQrHistory.setContent(content);
                this.insertSelective(userQrHistory);

                //更新userinfo的qr信息
                UserInfo userInfo = new UserInfo();
                userInfo.setId(userInfoItem.getId());
                userInfo.setQrHistoryId(userQrHistory.getId());
                userInfo.setPreQrStatus(userInfoItem.getQrStatus());
                userInfo.setQrStatus(qrStatusEnum.getCode());
                userInfo.setQrContent(content);
                this.userInfoService.updateSelective(userInfo);
            });
        });
    }

    public void createUserQrHistoryForVerifyBill(RegisterBill billItem, Long userId) {
        if (billItem == null) {
            return ;
        }
        BillVerifyStatusEnum billVerifyStatusEnum = BillVerifyStatusEnum.fromCode(billItem.getVerifyStatus())
                .orElse(null);
        if (billVerifyStatusEnum == null) {
            return ;
        }
        UserQrStatusEnum userQrStatus = getUserQrStatusEnum(billVerifyStatusEnum);
        String content="最新操作报备单审核状态是" + billVerifyStatusEnum.getName() + ",变为" + userQrStatus.getDesc() + "码";

        this.userInfoService.saveUserInfo(userId,billItem.getMarketId()).ifPresent(userInfoItem->{

            if(userInfoItem.getQrHistoryId()!=null){
                return ;
            }
            UserQrStatusEnum qrStatusEnum = UserQrStatusEnum.BLACK;
            //插入qrhistory对象
             this.buildUserQrHistory(userId, qrStatusEnum, QrHistoryEventTypeEnum.REGISTER_BILL, billItem.getId()).ifPresent(userQrHistory->{

                 userQrHistory.setContent(content);
                 this.insertSelective(userQrHistory);

                 //更新userinfo的qr信息
                 UserInfo userInfo=new UserInfo();
                 userInfo.setId(userInfoItem.getId());
                 userInfo.setQrHistoryId(userQrHistory.getId());
                 userInfo.setPreQrStatus(userInfoItem.getQrStatus());
                 userInfo.setQrStatus(qrStatusEnum.getCode());
                 userInfo.setQrContent(content);
                 this.userInfoService.updateSelective(userInfo);
            });

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

    public void createUserQrHistoryForTradeRequest(Long tradeRequestId, Long userId) {

        UserQrStatusEnum qrStatusEnum = UserQrStatusEnum.GREEN;
        String content="订单交易完成, 变为" + qrStatusEnum.getDesc() + "码";

        this.userInfoService.saveUserInfo(userId,null).ifPresent(userInfoItem->{

            if(userInfoItem.getQrHistoryId()!=null){
                return ;
            }

            //插入qrhistory对象
            this.buildUserQrHistory(userId, qrStatusEnum, QrHistoryEventTypeEnum.TRADE_REQUEST, tradeRequestId).ifPresent(userQrHistory->{
                userQrHistory.setContent(content);
                this.insertSelective(userQrHistory);

                //更新userinfo的qr信息
                UserInfo userInfo=new UserInfo();
                userInfo.setId(userInfoItem.getId());
                userInfo.setQrHistoryId(userQrHistory.getId());
                userInfo.setPreQrStatus(userInfoItem.getQrStatus());
                userInfo.setQrStatus(qrStatusEnum.getCode());
                userInfo.setQrContent(content);
                this.userInfoService.updateSelective(userInfo);
            });

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

    private Optional<UserQrHistory> buildUserQrHistory(Long userId,
                                             UserQrStatusEnum qrStatusEnum,
                                             QrHistoryEventTypeEnum historyEventTypeEnum,
                                             Long qrHistoryEventId) {
        return StreamEx.of(this.userInfoService.findByUserId(userId)).map(userItem -> {
            return this.buildUserQrHistory(userItem, qrStatusEnum, historyEventTypeEnum, qrHistoryEventId);
        }).findFirst();

    }

    private UserQrHistory buildUserQrHistory(UserInfo userItem,
                                             UserQrStatusEnum qrStatusEnum,
                                             QrHistoryEventTypeEnum historyEventTypeEnum,
                                             Long qrHistoryEventId) {

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
     *
     * @param tradeRequest
     */
    public void rollBackByTradeRequest(TradeRequest tradeRequest) {

        if (tradeRequest == null || tradeRequest.getId() == null) {
            return;
        }
        TradeRequest item = this.tradeRequestService.get(tradeRequest.getId());
        Long userId = tradeRequest.getBuyerId();
        UserQrHistory rollbackQrHistory = new UserQrHistory();
        rollbackQrHistory.setUserId(item.getBuyerId());
        rollbackQrHistory.setQrHistoryEventId(item.getTradeRequestId());
        this.rollback(rollbackQrHistory, QrHistoryEventTypeEnum.TRADE_REQUEST);
    }

    /**
     * 回退报备单二维码颜色记录
     *
     * @param registerBill
     */
    public void rollbackByBill(RegisterBill registerBill) {

        if (registerBill == null || registerBill.getId() == null) {
            return;
        }
        RegisterBill item = this.billService.get(registerBill.getId());
        if (item == null) {
            return;
        }
        UserQrHistory rollbackQrHistory = new UserQrHistory();
        rollbackQrHistory.setUserId(item.getUserId());
        rollbackQrHistory.setQrHistoryEventId(item.getId());
        this.rollback(rollbackQrHistory, QrHistoryEventTypeEnum.REGISTER_BILL);

    }

    /**
     * 回退记录
     *
     * @param rollbackQrHistory
     * @param historyTypeEnum
     */
    private void rollback(UserQrHistory rollbackQrHistory, QrHistoryEventTypeEnum historyTypeEnum) {
        if (rollbackQrHistory == null || historyTypeEnum == null) {
            return;
        }
        if (rollbackQrHistory.getUserId() == null || rollbackQrHistory.getQrHistoryEventId() == null) {
            return;
        }

        Long userId = rollbackQrHistory.getUserId();

        UserQrHistory domain = new UserQrHistory();
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
