package com.dili;

import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.enums.ImageCertTypeEnum;
import org.apache.commons.jxpath.JXPathContext;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;

public class JXpathtest {
    @Test
    public void test() {
        RegisterBill rb = new RegisterBill();
        rb.setImageCertList(new ArrayList<>());

        ImageCert imageCert = new ImageCert();
        imageCert.setCertType(ImageCertTypeEnum.DETECT_REPORT.getCode());
        rb.getImageCertList().add(imageCert);


        ImageCert imageCert2 = new ImageCert();
        imageCert2.setCertType(ImageCertTypeEnum.ORIGIN_CERTIFIY.getCode());
        rb.getImageCertList().add(imageCert2);

        JXPathContext context = JXPathContext.newContext(rb);
        context.setLenient(true);
        Iterator ite = context.iterate("imageCertList/certType");
        while (ite.hasNext()) {
            System.out.println(ite.next());
        }

    }
}
