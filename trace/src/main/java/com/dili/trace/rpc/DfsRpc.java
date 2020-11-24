package com.dili.trace.rpc;

import com.dili.ss.domain.BaseOutput;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 分布式文件系统rpc
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/8/17 18:37
 */
@FeignClient(name = "dili-dfs", contextId = "diliDfs", url = "${diliDfs.url:}", configuration = DfsRpc.FeignMultipartSupportConfig.class)
public interface DfsRpc {

    /**
     * 图片上传
     * @param file 文件类型
     * @param accessToken token
     * @return 上传结果 map
     */
    @PostMapping(value = "/file/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    BaseOutput<String> fileUpload(@RequestPart(name = "file") MultipartFile file,@RequestParam(value = "accessToken") String accessToken);

    /**
     * 通过base64方式进行图片文件上传
     * @param base64 图片文件字符串
     * @param accessToken token
     * @return
     */
    @PostMapping(value = "/file/uploadImgByBase64")
    BaseOutput<String> uploadImgByBase64(@RequestParam("base64") String base64, @RequestParam("accessToken") String accessToken);

    /**
     * Feign 传递文件所需要的配置类
     */
    class FeignMultipartSupportConfig{
        @Bean
        public Encoder multipartFormEncoder() {
            return new SpringFormEncoder();
        }
    }
}
