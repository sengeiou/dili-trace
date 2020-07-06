package com.dili.trace.service;

import com.dili.trace.AutoWiredBaseTest;
import com.google.common.collect.Lists;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ImageCertServiceTest extends AutoWiredBaseTest{
    @Autowired
    ImageCertService imageCertService;
    @Test
    public void findImageCertListByBillIdList(){
        this.imageCertService.findImageCertListByBillIdList(Lists.newArrayList(1L,2L));
    }
}