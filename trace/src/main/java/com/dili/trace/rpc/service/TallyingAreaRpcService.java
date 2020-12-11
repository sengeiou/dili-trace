package com.dili.trace.rpc.service;

import com.dili.customer.sdk.domain.TallyingArea;
import com.dili.customer.sdk.rpc.TallyingAreaRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Alvin.Li
 */
@Service
public class TallyingAreaRpcService {

    private static final Logger logger = LoggerFactory.getLogger(TallyingAreaRpcService.class);

    @Autowired
    TallyingAreaRpc tallyingAreaRpc;

    /**
     * 查询理货区
     *
     * @return
     */
    public TallyingArea findCustomerByIdOrEx(String tallyAreaNo, Long marketId) {

        TallyingArea tallyingAreaQuery = new TallyingArea();
        tallyingAreaQuery.setAssetsName(tallyAreaNo);
        this.tallyingAreaRpc.listByExample(tallyingAreaQuery);
        return null;
    }

}
