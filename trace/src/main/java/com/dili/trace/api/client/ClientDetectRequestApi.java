package com.dili.trace.api.client;

import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.AppException;
import com.dili.trace.domain.DetectRequest;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.DetectRequestDto;
import com.dili.trace.service.BillService;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.DetectRequestService;
import com.dili.trace.service.UserRpcService;
import com.dili.uap.sdk.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 检测单接口
 */
@RestController()
@RequestMapping("/api/client/clientDetectRequest")
//@AppAccess(role = Role.Client)
public class ClientDetectRequestApi {
    private static final Logger logger = LoggerFactory.getLogger(ClientDetectRequestApi.class);

    @Autowired
    LoginSessionContext loginSessionContext;
    @Autowired
    DetectRequestService detectRequestService;
    @Autowired
    BillService billService;
    @Autowired
    DetectRecordService detectRecordService;
    @Autowired
    UserRpcService userRpcService;
    @Autowired
    private LoginSessionContext sessionContext;


    /**
     * 创建检测单
     *
     * @param input
     * @return
     */
    @RequestMapping("/createDetectRequest.action")
    public BaseOutput<Long> createDetectRequest(@RequestBody DetectRequest input) {
        if (input == null || input.getBillId() == null) {
            return BaseOutput.failure("参数错误");
        }
        try {
            DetectRequest item = this.detectRequestService.createDetectRequestForBill(input.getBillId(), Optional.empty());
            return BaseOutput.successData(item.getId());
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }

    }

    /**
     * 创建场外委托检测单
     *
     * @param input
     * @return
     */
    @RequestMapping("/createOffSiteDetectRequest.action")
    public BaseOutput<Long> createOffSiteDetectRequest(@RequestBody DetectRequestDto input) {
        if (input == null) {
            return BaseOutput.failure("参数错误");
        }
        try {
            Long userId = loginSessionContext.getSessionData().getUserId();
            String userName = loginSessionContext.getSessionData().getUserName();
            Long marketId = loginSessionContext.getSessionData().getMarketId();
            
            if (null == userId) {
                return BaseOutput.failure("未登录或登录过期");
            }
            input.setCreatorId(userId);
            input.setCreatorName(userName);
            input.setMarketId(marketId);
            DetectRequest item = this.detectRequestService.createOffSiteDetectRequest(input, Optional.empty());
            return BaseOutput.successData(item.getId());
        } catch (AppException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }

    }

    /**
     * 创建检测单前报备单详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getBillDetailById.action", method = RequestMethod.GET)
    public BaseOutput<RegisterBill> getBillDetailById(Long id) {
        if (id == null) {
            return BaseOutput.failure("参数错误");
        }
        try {
            return BaseOutput.successData(billService.get(id));
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }
    }

    /**
     * 检测单详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getDetectRequestDetail.action", method = RequestMethod.GET)
    public BaseOutput<DetectRequestDto> getDetectRequestDetail(Long id) {
        try {
            DetectRequestDto detectRequest = new DetectRequestDto();
            detectRequest.setId(id);
            DetectRequestDto detail = detectRequestService.getDetectRequestDetail(detectRequest);
            //设置最新检测记录
            if (null != detail && StringUtils.isNotBlank(detail.getBillCode())) {
                detail.setDetectRecordList(detectRecordService.findTop2AndLatest(detail.getBillCode()));
                ;
            }
            return BaseOutput.successData(detail);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }
    }

    /**
     * 查询市场检测人员
     *
     * @param likeUserName
     * @return
     */
    @RequestMapping(value = "/getDetectUsers.action", method = RequestMethod.GET)
    public BaseOutput<List<User>> getDetectUsers(String likeUserName) {
        try {
            Long marketId = this.sessionContext.getSessionData().getMarketId();
            List<User> users = userRpcService.findDetectDepartmentUsers(likeUserName, marketId).orElse(new ArrayList<>());
            return BaseOutput.successData(users);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }
    }

}
