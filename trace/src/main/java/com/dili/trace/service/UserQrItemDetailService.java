package com.dili.trace.service;

import java.util.List;

import com.dili.common.exception.BusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserQrItem;
import com.dili.trace.domain.UserQrItemDetail;
import com.dili.trace.glossary.QrItemTypeEnum;
import com.dili.trace.glossary.UserTypeEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserQrItemDetailService extends BaseServiceImpl<UserQrItemDetail, Long> {
    @Autowired
    UserService userService;
    @Autowired
    UserQrItemService userQrItemService;

    /**
     * 通过登记单来更新二维码状态
     */
    public void updateQrItemDetail(RegisterBill registerBill) {
        UserQrItem queryCondition = new UserQrItem();
        queryCondition.setQrItemType(QrItemTypeEnum.BILL.getCode());

    }

    /**
     * 通过上游信息来更新二维码状态
     */
    public void updateQrItemDetail(UpStream upStream, Long userId) {

    }

    /**
     * 通过用户信息来更新二维码状态
     */
    public void updateQrItemDetail(User user) {
        Long userId = user.getId();
        User userItem = this.userService.get(userId);
        if (userItem == null) {
            throw new BusinessException("未能查询到用户信息");
        }
        if(UserTypeEnum.UPSTREAM.getCode().equals(userItem.getUserType())){

        }else{

        }
        UserQrItem qrItem = new UserQrItem();
        qrItem.setUserId(userId);
        qrItem.setQrItemType(QrItemTypeEnum.USER.getCode());
        List<UserQrItem> qrItemList = this.userQrItemService.listByExample(qrItem);
        if (qrItemList.isEmpty()) {
            this.userQrItemService.insertSelective(qrItem);
        } else {
            qrItem = qrItemList.stream().findFirst().orElse(null);
        }

    }

    private void createAllUserQrItem() {

    }

}