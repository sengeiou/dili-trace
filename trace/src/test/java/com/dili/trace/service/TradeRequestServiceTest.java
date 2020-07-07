package com.dili.trace.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.input.BatchStockInput;
import com.dili.trace.api.input.TradeDetailInputDto;
import com.dili.trace.api.input.TradeRequestInputDto;
import com.dili.trace.domain.BatchStock;
import com.dili.trace.domain.Brand;
import com.dili.trace.domain.Category;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.domain.User;
import com.dili.trace.enums.CheckinStatusEnum;
import com.dili.trace.enums.CheckoutStatusEnum;
import com.dili.trace.enums.PreserveTypeEnum;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.enums.TradeTypeEnum;
import com.google.common.collect.Lists;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

public class TradeRequestServiceTest extends AutoWiredBaseTest {
    @Autowired
    TradeRequestService tradeRequestService;
    @Autowired
    BatchStockService batchStockService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BrandService brandService;
    @Autowired
    TradeDetailService tradeDetailService;

    private TradeDetail createTradeDetail(BatchStock batchStock) {

        TradeDetail tradeDetail = new TradeDetail();
        tradeDetail.setStockWeight(BigDecimal.valueOf(120));
        tradeDetail.setTotalWeight(BigDecimal.valueOf(120));
        tradeDetail.setTradeType(TradeTypeEnum.NONE.getCode());
        tradeDetail.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
        tradeDetail.setBuyerId(batchStock.getUserId());
        tradeDetail.setBuyerName(batchStock.getUserName());
        tradeDetail.setProductName(batchStock.getProductName());
        tradeDetail.setBatchStockId(batchStock.getId());
        tradeDetail.setBillId(1L);
        tradeDetail.setCheckinStatus(CheckinStatusEnum.ALLOWED.getCode());
        tradeDetail.setCheckoutStatus(CheckoutStatusEnum.NONE.getCode());
        this.tradeDetailService.insertSelective(tradeDetail);
        return tradeDetail;
    }

    private BatchStock createBatchStock() {
        List<User> userList = super.findUsers();
        assertNotNull(userList);
        assertTrue(userList.size() >= 2);
        User seller = userList.get(0);
        User buyer = userList.get(1);

        Brand brand = new Brand();
        brand.setUserId(seller.getId());
        brand.setBrandName("demo");
        brandService.insertSelective(brand);

        Category category = super.findCategory();
        assertNotNull(category);
        BatchStock batchStock = new BatchStock();
        batchStock.setUserId(seller.getId());
        batchStock.setUserName(seller.getName());
        batchStock.setProductId(category.getId());
        batchStock.setProductName(category.getName());
        batchStock.setStockWeight(BigDecimal.valueOf(120));
        batchStock.setTotalWeight(BigDecimal.valueOf(120));
        batchStock.setPreserveType(PreserveTypeEnum.FRESH.getCode());
        batchStock.setBrandId(brand.getId());
        this.batchStockService.insertSelective(batchStock);

        assertNotNull(batchStock);
        return batchStock;

    }

    private TradeRequest createBuyTradeRequest(BatchStock batchStock) {
        List<User> userList = super.findUsers();
        assertNotNull(userList);
        assertTrue(userList.size() >= 2);
        User seller = userList.get(0);
        User buyer = userList.get(1);

        BatchStockInput input = new BatchStockInput();
        input.setBatchStockId(batchStock.getId());
        input.setTradeWeight(BigDecimal.valueOf(77));

        TradeRequest request = this.tradeRequestService.createTradeRequest(null, null, buyer.getId(), input);
        assertNotNull(request);
        return request;

    }

    @Test
    public void createAndCancelTradeRequest() {
        TradeRequest request = this.createBuyTradeRequest(this.createBatchStock());
        this.tradeRequestService.hanleRequest(request, Lists.newArrayList());
    }

    @Test
    public void createAndFinishTradeRequest() {
        BatchStock batchStock = this.createBatchStock();
        TradeRequest request = this.createBuyTradeRequest(batchStock);
        TradeDetail tradeDetail = this.createTradeDetail(batchStock);

        TradeDetailInputDto input = new TradeDetailInputDto();
        input.setTradeDetailId(tradeDetail.getId());
        input.setTradeWeight(request.getTradeWeight());
        this.tradeRequestService.hanleRequest(request, Lists.newArrayList(input));
    }

    @Test
    public void createBuyRequest() {
        List<User> userList = super.findUsers();
        assertNotNull(userList);
        assertTrue(userList.size() >= 2);
        User seller = userList.get(0);
        User buyer = userList.get(1);

        Brand brand = new Brand();
        brand.setUserId(seller.getId());
        brand.setBrandName("demo");
        brandService.insertSelective(brand);

        Category category = super.findCategory();
        assertNotNull(category);
        BatchStock batchStock = new BatchStock();
        batchStock.setUserId(seller.getId());
        batchStock.setUserName(seller.getName());
        batchStock.setProductId(category.getId());
        batchStock.setProductName(category.getName());
        batchStock.setStockWeight(BigDecimal.valueOf(120));
        batchStock.setTotalWeight(BigDecimal.valueOf(120));
        batchStock.setPreserveType(PreserveTypeEnum.FRESH.getCode());
        batchStock.setBrandId(brand.getId());
        this.batchStockService.insertSelective(batchStock);

        TradeRequest request = new TradeRequest();
        request.setBuyerId(buyer.getId());
        request.setBatchStockId(batchStock.getId());
        request.setTradeWeight(BigDecimal.TEN);
        TradeRequest tradeRequestItem = ReflectionTestUtils.invokeMethod(this.tradeRequestService, "createBuyRequest",
                request);
        assertNotNull(tradeRequestItem);

        TradeDetail tradeDetail = new TradeDetail();
        tradeDetail.setBatchStockId(batchStock.getId());
        tradeDetail.setBuyerId(batchStock.getUserId());
        tradeDetail.setBuyerName(batchStock.getUserName());
        tradeDetail.setBillId(1L);
        tradeDetail.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
        tradeDetail.setTradeType(TradeTypeEnum.NONE.getCode());
        tradeDetail.setStockWeight(BigDecimal.valueOf(120));
        tradeDetail.setTotalWeight(BigDecimal.valueOf(120));
        tradeDetail.setCheckinStatus(CheckinStatusEnum.ALLOWED.getCode());
        tradeDetail.setCheckoutStatus(CheckoutStatusEnum.NONE.getCode());
        tradeDetail.setProductName(batchStock.getProductName());
        this.tradeDetailService.insertSelective(tradeDetail);
        assertNotNull(tradeDetail.getId());

    }

    @Test
    public void createSellRequest() {
        List<User> userList = super.findUsers();
        assertNotNull(userList);
        assertTrue(userList.size() >= 2);
        User ownedUser = userList.get(0);
        User buyer = userList.get(1);

        Brand brand = new Brand();
        brand.setUserId(ownedUser.getId());
        brand.setBrandName("demo");
        brandService.insertSelective(brand);

        Category category = super.findCategory();
        assertNotNull(category);
        BatchStock batchStock = new BatchStock();
        batchStock.setUserId(ownedUser.getId());
        batchStock.setUserName(ownedUser.getName());
        batchStock.setProductId(category.getId());
        batchStock.setProductName(category.getName());
        batchStock.setStockWeight(BigDecimal.valueOf(120));
        batchStock.setTotalWeight(BigDecimal.valueOf(120));
        batchStock.setPreserveType(PreserveTypeEnum.FRESH.getCode());
        batchStock.setBrandId(brand.getId());
        this.batchStockService.insertSelective(batchStock);

        TradeDetail tradeDetail = new TradeDetail();
        tradeDetail.setBatchStockId(batchStock.getId());
        tradeDetail.setBuyerId(ownedUser.getId());
        tradeDetail.setBuyerName(ownedUser.getName());
        tradeDetail.setBillId(1L);
        tradeDetail.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
        tradeDetail.setTradeType(TradeTypeEnum.NONE.getCode());
        tradeDetail.setStockWeight(BigDecimal.valueOf(120));
        tradeDetail.setTotalWeight(BigDecimal.valueOf(120));
        tradeDetail.setCheckinStatus(CheckinStatusEnum.ALLOWED.getCode());
        tradeDetail.setCheckoutStatus(CheckoutStatusEnum.NONE.getCode());
        tradeDetail.setProductName(batchStock.getProductName());
        this.tradeDetailService.insertSelective(tradeDetail);
        assertNotNull(tradeDetail.getId());

        TradeRequest request = new TradeRequest();
        request.setBuyerId(buyer.getId());
        request.setBuyerName(buyer.getName());
        request.setBatchStockId(batchStock.getId());
        request.setTradeWeight(BigDecimal.TEN);

        BatchStockInput input = new BatchStockInput();
        input.setBatchStockId(batchStock.getId());
        input.setTradeWeight(BigDecimal.valueOf(77));
        this.tradeRequestService.createSellRequest(ownedUser.getId(), buyer.getId(), Lists.newArrayList(input));

        // Long requestId =
        // this.tradeRequestService.createSellRequest(request,Lists.newArrayList());
        // assertNotNull(requestId);

    }

    @Test
    public void listPageTradeRequestByBuyerIdOrSellerId() {
        TradeRequestInputDto request = new TradeRequestInputDto();
        request.setSellerId(1L);
        request.setBuyerId(2L);
        request.setLikeProductName("abc");

        this.tradeRequestService.listPageTradeRequestByBuyerIdOrSellerId(request);

    }
}