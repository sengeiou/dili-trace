package com.dili.trace.api;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.trace.api.input.RegisterBillApiInputDto;
import com.dili.trace.api.input.RegisterBillQueryInputDto;
import com.dili.trace.domain.Category;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.glossary.YnEnum;
import com.dili.trace.service.CategoryService;
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
    @Autowired
    private CategoryService categoryService;

    /**
     * 通过登记单ID获取登记单详细信息
     * @param inputDto
     * @return
     */
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

    /**
     *获取报备单列表
     * @param inputDto
     * @param request
     * @return
     */
    @ApiOperation(value = "获取报备单列表")
    @RequestMapping(value = "/queryBillNo", method = RequestMethod.POST)
    @InterceptConfiguration(loginRequired = false, signRequired = true, signValue = "cGFzczk4NzYyMDIw")
    public BaseOutput<RegisterBillOutputDto> queryBillNo(@RequestBody RegisterBillQueryInputDto inputDto, HttpServletRequest request) {
        boolean isValidate = inputDto == null || inputDto.getSupplierId() == null || inputDto.getCategoryCode() == null;
        if (isValidate) {
            String result = "参数错误";
            if (null == inputDto.getSupplierId()) {
                result = "参数：supplierId不能为空";
            }
            if (null == inputDto.getCategoryCode()) {
                result = "参数：categoryCode不能为空";
            }
            return BaseOutput.failure(result);
        }

        logger.info("获取报备单列表->billId:{},SupplierId:{}", inputDto.getBillId(), inputDto.getSupplierId());
        try {
            User user = DTOUtils.newDTO(User.class);
            user.setThirdPartyCode(inputDto.getSupplierId());
            user.setYn(YnEnum.YES.getCode());
            List<User> userList = userService.listByExample(user);
            if (CollectionUtils.isEmpty(userList)) {
                return BaseOutput.failure("supplierId没有匹配的经营户");
            }
            Long userId = null;
            Integer row = 10;
            Integer noDelete = 0;
            String billNo = inputDto.getBillId();
            String goodsCode = inputDto.getCategoryCode();
            Long productId = null;
            if (CollectionUtils.isNotEmpty(userList)) {
                userId = userList.get(0).getId();
            }
            if (StringUtils.isNotBlank(goodsCode)) {
                productId = getCategoryByGoodsCode(goodsCode);
            }
            RegisterBill query = new RegisterBill();
            query.setUserId(userId);
            query.setIsDeleted(noDelete);
            if (null != productId) {
                query.setProductId(productId);
            }
            if (StringUtils.isNotBlank(billNo)) {
                query.setMetadata(IDTO.AND_CONDITION_EXPR, " code like '%" + billNo + "%'");
            } else {
                query.setOrder("desc");
                query.setSort("created");
                query.setRows(row);
            }
            List<RegisterBill> billList = registerBillService.listByExample(query);
            List<RegisterBillQueryInputDto> billNos = StreamEx.of(billList).nonNull().map(b -> {
                RegisterBillQueryInputDto inp = new RegisterBillQueryInputDto();
                inp.setBillId(b.getCode());
                return inp;
            }).collect(Collectors.toList());

            return BaseOutput.success().setData(billNos);

        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("查询数据出错");
        }

    }

    /**
     * 返回商品码对应的第三级品种
     *
     * @param goodsCode
     * @return
     */
    private Long getCategoryByGoodsCode(String goodsCode) {
        Category queCate = new Category();
        queCate.setCode(goodsCode);
        List<Category> categories = categoryService.listByExample(queCate);
        if (CollectionUtils.isNotEmpty(categories)) {
            Category resCategory = categories.get(0);
            return recursionReturnGoodsId(resCategory);
        }
        return null;
    }

    private Long recursionReturnGoodsId(Category resCategory) {
        if (null == resCategory) {
            return null;
        }
        int resultLevel = 3;
        if (resCategory.getLevel().intValue() > resultLevel) {
            Long parentId = resCategory.getParentId();
            Category category = categoryService.get(parentId);
            return recursionReturnGoodsId(category);
        } else {
            return resCategory.getId();
        }
    }


}
