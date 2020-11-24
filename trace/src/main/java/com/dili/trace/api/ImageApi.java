package com.dili.trace.api;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.service.DfsRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片上传接口
 */
@RestController
@RequestMapping(value = "/api/imageApi")
public class ImageApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageApi.class);

    @Autowired
    private DfsRpcService dfsRpcService;

    /**
     * 上传图片
     *
     * @param file
     * @param type
     * @param compress
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public BaseOutput<String> upload(@RequestParam MultipartFile file, @RequestParam Integer type, @RequestParam(required = false) Boolean compress) {
        try {
            return this.dfsRpcService.uploadImage(file);
        } catch (Exception e) {
            LOGGER.error("upload", e);
            return BaseOutput.failure();
        }
    }

}
