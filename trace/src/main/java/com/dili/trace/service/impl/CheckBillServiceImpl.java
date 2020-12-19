package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.POJOUtils;
import com.dili.trace.dao.CheckBillMapper;
import com.dili.trace.domain.CheckOrder;
import com.dili.trace.domain.CheckOrderData;
import com.dili.trace.domain.CheckOrderDispose;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.dto.CheckExcelDto;
import com.dili.trace.dto.CheckOrderDto;
import com.dili.trace.enums.CheckResultTypeEnum;
import com.dili.trace.enums.ImageCertBillTypeEnum;
import com.dili.trace.enums.ImageCertTypeEnum;
import com.dili.trace.service.CheckBillService;
import com.dili.trace.service.CheckOrderDataService;
import com.dili.trace.service.CheckOrderDisposeService;
import com.dili.trace.service.ImageCertService;
import com.dili.trace.util.MarketUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description:
 *
 * @author Ron.Peng
 * @date 2020/10/30
 */
@Service
public class CheckBillServiceImpl extends BaseServiceImpl<CheckOrder, Long> implements CheckBillService {
    @Autowired
    private ImageCertService imageCertService;

    @Autowired
    private CheckOrderDataService checkOrderDataService;

    @Autowired
    private CheckOrderDisposeService checkOrderDisposeService;

    public CheckBillMapper getActualDao() {
        return (CheckBillMapper) getDao();
    }
    @Override
    public EasyuiPageOutput selectForEasyuiPage(CheckOrderDto domain, boolean useProvider) throws Exception {
        if (domain.getRows() != null && domain.getRows() >= 1) {
            PageHelper.startPage(domain.getPage(), domain.getRows());
        }
        if (StringUtils.isNotBlank(domain.getSort())) {
            domain.setSort(POJOUtils.humpToLineFast(domain.getSort()));
        }
        List<CheckOrder> checkOrder = getActualDao().selectForPage(domain);
        long total = checkOrder instanceof Page ? ((Page) checkOrder).getTotal() : (long) checkOrder.size();
        List results = useProvider ? ValueProviderUtils.buildDataByProvider(domain, checkOrder) : checkOrder;
        return new EasyuiPageOutput(total, results);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertOtherTable(CheckOrderDto checkOrder,Long id) {
        ImageCert imageCert = new ImageCert();
//        imageCert.setUrl(checkOrder.getUrl());
        imageCert.setUid(checkOrder.getUrl());
        imageCert.setBillType(ImageCertBillTypeEnum.INSPECTION_TYPE.getCode());
        imageCert.setCertType(ImageCertTypeEnum.DETECT_REPORT.getCode());
        imageCert.setBillId(id);
        CheckOrderData checkOrderData = new CheckOrderData();
        checkOrderData.setProject(checkOrder.getProject());
        checkOrderData.setNormalValue(checkOrder.getNormalValue());
        checkOrderData.setResult(checkOrder.getCheckResult());
        checkOrderData.setValue(checkOrder.getValue());
        checkOrderData.setCheckId(id);
        imageCertService.insertSelective(imageCert);
        checkOrderDataService.insertSelective(checkOrderData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOtherTable(CheckOrderDto checkOrder) {
        CheckOrderData checkOrderData = new CheckOrderData();
        checkOrderData.setCheckId(checkOrder.getId());
        List<CheckOrderData> checkOrderDatas = checkOrderDataService.listByExample(checkOrderData);
        checkOrderData=checkOrderDatas.get(0);
        checkOrderData.setValue(checkOrder.getValue());
        checkOrderData.setNormalValue(checkOrderData.getNormalValue());
        checkOrderData.setProject(checkOrder.getProject());
        checkOrderDataService.updateSelective(checkOrderData);
        ImageCert imageCert = new ImageCert();
        imageCert.setBillId(checkOrder.getId());
        imageCert.setBillType(ImageCertBillTypeEnum.INSPECTION_TYPE.getCode());
        List<ImageCert> imageCerts = imageCertService.listByExample(imageCert);
        if(CollectionUtils.isNotEmpty(imageCerts) && imageCerts.size() == 1) {
            imageCert = imageCerts.get(0);
            imageCert.setUid(checkOrder.getUrl());
            imageCertService.updateSelective(imageCert);
        }
    }

    @Override
    public void noPassInsert(CheckOrderDto checkOrder) {
        if(CheckResultTypeEnum.NO_PASS.getCode()==Integer.parseInt(checkOrder.getCheckResult())){
            CheckOrderDispose checkOrderDispose=new CheckOrderDispose();
            checkOrderDispose.setMarketId(MarketUtil.returnMarket());
            checkOrderDispose.setCheckNo(checkOrder.getCheckNo());
            checkOrderDispose.setDisposeDate(checkOrder.getCheckTime());
            checkOrderDisposeService.insertSelective(checkOrderDispose);
        }
    }
}
