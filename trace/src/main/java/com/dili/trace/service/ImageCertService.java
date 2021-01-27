package com.dili.trace.service;

import java.util.List;
import java.util.function.Function;

import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.ImageCertBillTypeEnum;
import com.dili.trace.enums.ImageCertTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.ImageCert;
import com.google.common.collect.Lists;

import one.util.streamex.StreamEx;
import tk.mybatis.mapper.entity.Example;

/**
 * 图片存储
 */
@Service
public class ImageCertService extends BaseServiceImpl<ImageCert, Long> {
    /**
     * 图片转换
     *
     * @param urls
     * @param certTypeEnum
     * @param certBillTypeEnum
     * @return
     */
    public List<ImageCert> stringToImageCertList(String urls, ImageCertTypeEnum certTypeEnum, ImageCertBillTypeEnum certBillTypeEnum) {

        return StreamEx.ofNullable(urls).map(String::trim).filter(item -> item.length() > 0)
                .flatArray(item -> {
                    return item.split(",");
                }).filter(StringUtils::isNotBlank).map(uid -> {
                    ImageCert cert = new ImageCert();
//                    cert.setUrl(url);
                    cert.setUid(uid);
                    cert.setCertType(certTypeEnum.getCode());
                    cert.setBillType(certBillTypeEnum.getCode());
                    return cert;
                }).toList();
    }
    /**
     * 保存图片
     *
     * @param imageCertList
     * @param billId
     * @return
     */
    public List<ImageCert> insertImageCert(List<ImageCert> imageCertList, Long billId) {
        if (billId != null) {
            // 先删除全部原有图片
            ImageCert deleteConditon = new ImageCert();
            deleteConditon.setBillId(billId);
            deleteConditon.setBillType(BillTypeEnum.REGISTER_BILL.getCode());
            this.deleteByExample(deleteConditon);

            // 增加新的图片
            return StreamEx.ofNullable(imageCertList).flatCollection(Function.identity()).nonNull()
                    .filter(cert->cert.getCertType()!=null)
                    .filter(cert->StringUtils.isNotBlank(cert.getUid())).map(cert -> {
                cert.setBillId(billId);
                cert.setId(null);
                cert.setBillType(BillTypeEnum.REGISTER_BILL.getCode());
                this.insertSelective(cert);
                return cert;
            }).toList();
        }
        return Lists.newArrayList();
    }

    /**
     * 查找 图片
     *
     * @param billId
     * @return
     */
    public List<ImageCert> findImageCertListByBillId(Long billId) {
        if (billId == null) {
            return Lists.newArrayList();
        }
        ImageCert queryCondition = new ImageCert();
        queryCondition.setBillId(billId);
        queryCondition.setBillType(ImageCertBillTypeEnum.BILL_TYPE.getCode());
        return this.listByExample(queryCondition);
    }

    /**
     * 查找 图片
     *
     * @param billIdList
     * @return
     */
    public List<ImageCert> findImageCertListByBillIdList(List<Long> billIdList) {
        if (billIdList == null || billIdList.isEmpty()) {
            return Lists.newArrayList();
        }
        Example e = new Example(ImageCert.class);
        e.and().andIn("billId", billIdList).andEqualTo("certType",ImageCertBillTypeEnum.BILL_TYPE.getCode());
        return this.getDao().selectByExample(e);
    }

    /**
     * 保存图片
     *
     * @param imageCertList
     * @param billId
     * @param billType
     * @return
     */
    public List<ImageCert> insertImageCert(List<ImageCert> imageCertList, Long billId, Integer billType) {
        if (billId != null) {
            // 先删除全部原有图片
            ImageCert deleteConditon = new ImageCert();
            deleteConditon.setBillId(billId);
            deleteConditon.setBillType(billType);
            this.deleteByExample(deleteConditon);

            // 增加新的图片
            return StreamEx.of(imageCertList).nonNull().map(cert -> {
                cert.setBillId(billId);
                cert.setId(null);
                cert.setBillType(billType);
                this.insertSelective(cert);
                return cert;
            }).toList();
        }
        return Lists.newArrayList();
    }

    /**
     * 查找 图片
     *
     * @param billId
     * @param billType
     * @return
     */
    public List<ImageCert> findImageCertListByBillId(Long billId, ImageCertBillTypeEnum billType) {
        return this.findImageCertListByBillId(billId, billType.getCode());
    }

    /**
     * 查找 图片
     *
     * @param billId
     * @param billType
     * @return
     */
    public List<ImageCert> findImageCertListByBillId(Long billId, Integer billType) {
        if (billId == null) {
            return Lists.newArrayList();
        }
        ImageCert queryCondition = new ImageCert();
        queryCondition.setBillId(billId);
        queryCondition.setBillType(billType);
        return this.listByExample(queryCondition);
    }
}
