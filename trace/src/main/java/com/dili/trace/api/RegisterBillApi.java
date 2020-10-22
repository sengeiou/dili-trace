package com.dili.trace.api;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.trace.api.input.RegisterBillApiInputDto;
import com.dili.trace.api.input.RegisterBillQueryInputDto;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.glossary.YnEnum;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author
 */
@RestController
@RequestMapping(value = "/api/registerBillApi")
@Api(value = "/api/registerBillApi", description = "登记单相关接口")
@InterceptConfiguration
public class RegisterBillApi {
    private static final Logger logger = LoggerFactory.getLogger(RegisterBillApi.class);

    @Autowired
    private RegisterBillService registerBillService;
    @Autowired
    private LoginSessionContext sessionContext;
    @Autowired
    private UserService userService;


    @ApiOperation(value = "通过登记单ID获取登记单详细信息")
    @RequestMapping(value = "/viewTradeDetailBill.api", method = RequestMethod.POST)
    public BaseOutput<RegisterBillOutputDto> viewTradeDetailBill(@RequestBody RegisterBillApiInputDto inputDto) {
        if (inputDto == null || (inputDto.getBillId() == null && inputDto.getTradeDetailId() == null)) {
            return BaseOutput.failure("参数错误");
        }

        logger.info("获取登记单详细信息->billId:{},tradeDetailId:{}", inputDto.getBillId(), inputDto.getTradeDetailId());
        try {
            Long userId = this.sessionContext.getAccountId();
            if (userId == null) {
                return BaseOutput.failure("你还未登录");
            }
            RegisterBillOutputDto outputdto = this.registerBillService.viewTradeDetailBill(inputDto.getBillId(),
                    inputDto.getTradeDetailId());

            return BaseOutput.success().setData(outputdto);

        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("查询数据出错");
        }

    }

    @ApiOperation(value = "获取报备单列表")
    @RequestMapping(value = "/queryBillNo", method = RequestMethod.POST)
    @InterceptConfiguration(loginRequired = false,signRequired = true)
    public BaseOutput<RegisterBillOutputDto> queryBillNo(@RequestBody RegisterBillQueryInputDto inputDto,HttpServletRequest request) {
        boolean isValidate = inputDto == null || inputDto.getSupplierId() == null;
        if (isValidate) {
            return BaseOutput.failure("参数错误");
        }

        logger.info("获取报备单列表->billId:{},SupplierId:{}", inputDto.getBillId(),inputDto.getSupplierId());
        try {
            User user = DTOUtils.newDTO(User.class);
            user.setThirdPartyCode(inputDto.getSupplierId());
            user.setYn(YnEnum.YES.getCode());
            List<User> userList = userService.listByExample(user);
            Long userId =null;
            Integer row = 10;
            String billNo = inputDto.getBillId();
            if(CollectionUtils.isNotEmpty(userList)){
                userId=userList.get(0).getId();
            }
            RegisterBill query = new RegisterBill();
            query.setUserId(userId);
            if(StringUtils.isNotBlank(billNo)){
                query.setMetadata(IDTO.AND_CONDITION_EXPR," code like '%"+billNo+"%'");
            }else{
                query.setOrder("desc");
                query.setSort("created");
                query.setRows(row);
            }
            List<RegisterBill> billList = registerBillService.listByExample(query);
            List<String> billNOs = StreamEx.of(billList).nonNull().map(b->b.getCode()).collect(Collectors.toList());
            return BaseOutput.success().setData(billNOs);

        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("查询数据出错");
        }

    }


}
