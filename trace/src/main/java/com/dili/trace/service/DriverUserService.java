package com.dili.trace.service;

import com.dili.common.entity.LoginSessionContext;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.domain.TallyingArea;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BasePage;
import com.dili.trace.dao.DriverUserMapper;
import com.dili.trace.domain.UserDriverRef;
import com.dili.trace.dto.query.UserDriverRefQueryDto;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.CheckinStatusEnum;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.util.BasePageUtil;
import one.util.streamex.StreamEx;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author asa.lee
 */
@Service
public class DriverUserService extends TraceBaseService<UserDriverRef, Long> {

    @Autowired
    DriverUserMapper driverUserMapper;
    @Autowired
    CustomerRpcService userService;
    @Autowired
    private LoginSessionContext sessionContext;

    /**
     * 新增/修改经营户的司机
     *
     * @param ref
     */
    public void updateDriverUserRef(UserDriverRef ref) {
        // 获取登录用户的市场
        Long marketId = sessionContext.getSessionData().getMarketId();
        UserDriverRef queRef = new UserDriverRef();
        queRef.setSellerId(ref.getSellerId());
        queRef.setDriverId(ref.getDriverId());
        List<UserDriverRef> oldUserDriverList = listByExample(queRef);
        Customer driverUser = userService.findCustByIdOrEx(ref.getDriverId());
        Customer seller = userService.findCustByIdOrEx(ref.getSellerId());
        Long driverId = ref.getDriverId();
        String driverName = "";
        if (null != driverUser) {
            driverName = driverUser.getName();
        }
        //若有记录更新
        if (CollectionUtils.isNotEmpty(oldUserDriverList)) {
            UserDriverRef driverRef = oldUserDriverList.get(0);
            Long sellerId = driverRef.getSellerId();
            UserDriverRef updateObj = new UserDriverRef();
            updateObj.setDriverId(driverId);
            updateObj.setDriverName(driverName);
            updateObj.setSellerId(sellerId);
            updateObj.setSellerName(seller.getName());
            updateObj.setShareTime(ref.getShareTime());
            updateObj.setTallyAreaNos(ref.getTallyAreaNos());
            driverUserMapper.updateUserDriverRefByIDParam(updateObj);
        } else {
            String sellerName = seller.getName();
            UserDriverRef addUserDriver = new UserDriverRef();
            addUserDriver.setDriverId(driverId);
            addUserDriver.setDriverName(driverName);
            addUserDriver.setSellerId(ref.getSellerId());
            addUserDriver.setSellerName(sellerName);
            addUserDriver.setShareTime(ref.getShareTime());
//            List<TallyingArea> tallyingAreaList = seller.getTallyingAreaList();
//            if (CollectionUtils.isEmpty(tallyingAreaList)) {
//                addUserDriver.setTallyAreaNos("");
//            } else {
//                String assetsName = StreamEx.of(tallyingAreaList).map(TallyingArea::getAssetsName).collect(Collectors.joining(","));
//                addUserDriver.setTallyAreaNos(assetsName);
//            }
            driverUserMapper.insert(addUserDriver);
        }
    }

    /**
     * 获取司机关联的经营户
     *
     * @param user
     * @return
     */
    public BasePage<UserDriverRef> getDriverUserList(UserDriverRefQueryDto user) {
        user.setCheckinStatus(CheckinStatusEnum.NONE.getCode());
        user.setBillType(BillTypeEnum.REGISTER_BILL.getCode());
        user.setIsDelete(YesOrNoEnum.NO.getCode());
        user.setVerifyStatus(BillVerifyStatusEnum.PASSED.getCode());
        return super.buildQuery(user).listPageByFun(u -> driverUserMapper.getDriverUserList(u));
    }

}
