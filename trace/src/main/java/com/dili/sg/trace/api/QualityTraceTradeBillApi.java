package com.dili.sg.trace.api;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.dili.sg.trace.api.input.QualityTraceTradeBillInputDto;
import com.dili.sg.trace.api.output.QualityTraceTradeBillOutput;
import com.dili.sg.trace.service.BillService;
import com.dili.sg.trace.service.QrCodeService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.sg.QualityTraceTradeBill;
import com.dili.trace.service.QualityTraceTradeBillService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import one.util.streamex.StreamEx;

/**
 * 查询订单相关的检测信息
 * @author wangguofeng
 * @version 1.0
 */
@RestController
@RequestMapping("/api/qualityTraceTradeBillApi")
public class QualityTraceTradeBillApi {
    private static final Logger logger = LoggerFactory.getLogger(QualityTraceTradeBillApi.class);
    @Autowired
    QualityTraceTradeBillService qualityTraceTradeBillService;
    @Autowired
    BillService billService;
    @Autowired
    QrCodeService qrCodeService;

    /**
     * 查询检测结果
     * @param inputTradeBillIdList
     * @return
     */
    @RequestMapping("/queryByOrderIdList.api")
    public BaseOutput<List<QualityTraceTradeBillOutput>> queryByOrderIdList(@RequestBody List<Long> inputTradeBillIdList) {
        List<Long> tradeBillIdList = StreamEx.of(CollectionUtils.emptyIfNull(inputTradeBillIdList)).nonNull().toList();
        if (tradeBillIdList.isEmpty()) {
            return BaseOutput.success().setData(new ArrayList<>());
        }
        try {
            List<QualityTraceTradeBillOutput> list = this.qualityTraceTradeBillService.findQualityTraceTradeBillByTradeBillIdList(tradeBillIdList);

            return BaseOutput.success().setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure(e.getMessage());
        }


    }

    /**
     * 获得二维码图片
     * @param input
     * @return
     */
    @RequestMapping("/queryQrBase64Img.api")
    public BaseOutput<String> queryQrBase64Img(@RequestBody QualityTraceTradeBillInputDto input) {
        if(input==null||input.getBillId()==null){
            return BaseOutput.failure("参数错误");
        }

        QualityTraceTradeBill query= DTOUtils.newDTO(QualityTraceTradeBill.class);
        query.setBillId(input.getBillId());
        Long id=StreamEx.ofNullable(this.qualityTraceTradeBillService.listByExample(query)).nonNull()
                .flatCollection(Function.identity()).nonNull()
                .findFirst().map(QualityTraceTradeBill::getRegisterBillCode)
                .map(code->{
                    return this.billService.findByCode(code);
                }).map(RegisterBill::getId).orElse(null);
        if(id==null){
            return BaseOutput.failure("没有查到匹配的登记单");
        }
        try {
            String base64Img=this.qrCodeService.getBase64QrCode("http://www.baidu.com",200,200);
            return BaseOutput.success().setData(base64Img);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure(e.getMessage());
        }

    }

}
