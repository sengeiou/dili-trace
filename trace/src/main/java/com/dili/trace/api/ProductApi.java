package com.dili.trace.api;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.IDTO;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.service.TradeDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品api
 *
 * @author asa.lee
 */
@Api(value = "/api/product")
@RestController
@RequestMapping(value = "/api/product")
public class ProductApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductApi.class);

    @Autowired
    TradeDetailService tradeDetailService;

    @ApiOperation(value ="查询上架商品列表", notes = "查询上架商品列表")
    @RequestMapping(value = "/shelvesProduct.api", method = RequestMethod.POST)
    @InterceptConfiguration
    public BaseOutput<BasePage<TradeDetail>> shelvesProduct(@RequestBody TradeDetail tradeDetail){
        if (tradeDetail == null ||  null==tradeDetail.getBuyerId()){
            return BaseOutput.failure("参数错误");
        }
        try{
            tradeDetail.setSort("product_name");
            tradeDetail.setOrder("desc");
            tradeDetail.setMetadata(IDTO.AND_CONDITION_EXPR," stock_weight > 0");
            return BaseOutput.success().setData(tradeDetailService.listPageByExample(tradeDetail));
        }catch (TraceBusinessException e){
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("quit",e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    @ApiOperation(value ="查询下架商品列表", notes = "查询下架商品列表")
    @RequestMapping(value = "/unavailableProduct.api", method = RequestMethod.POST)
    @InterceptConfiguration
    public BaseOutput<BasePage<TradeDetail>> unavailableProduct(@RequestBody TradeDetail tradeDetail){
        if (tradeDetail == null ||  null==tradeDetail.getBuyerId()){
            return BaseOutput.failure("参数错误");
        }
        try{

            tradeDetail.setSort("product_name");
            tradeDetail.setOrder("desc");
            tradeDetail.setMetadata(IDTO.AND_CONDITION_EXPR," pushaway_weight > 0");
            return BaseOutput.success().setData(tradeDetailService.listPageByExample(tradeDetail));
        }catch (TraceBusinessException e){
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("quit",e);
            return BaseOutput.failure(e.getMessage());
        }
    }
}
