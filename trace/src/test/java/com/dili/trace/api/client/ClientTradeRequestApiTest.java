package com.dili.trace.api.client;


import com.dili.trace.util.JSON;
import com.dili.customer.sdk.domain.query.CustomerQueryInput;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.dto.CustomerExtendOutPutDto;
import com.dili.trace.rpc.dto.CustomerQueryDto;
import com.dili.trace.service.TradeRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("test")
public class ClientTradeRequestApiTest extends AutoWiredBaseTest {
    @Autowired
    private ClientTradeRequestApi tradeRequestApi;

    @Autowired
    private TradeRequestService tradeRequestService;

    @Test
    public void testListBuyHistory() {
        List<UserOutput> list = this.tradeRequestService.queryTradeSellerHistoryList(28L);
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void listSeller() {
        CustomerQueryDto input = new CustomerQueryDto();
        input.setKeyword("成都");
        input.setMarketId(8L);
        BaseOutput out = this.tradeRequestApi.listSeller(input);
        System.out.println(out);

    }
}
