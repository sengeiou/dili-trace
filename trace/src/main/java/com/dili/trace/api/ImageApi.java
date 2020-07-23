package com.dili.trace.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dili.common.service.ImageService;
import com.dili.ss.domain.BaseOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/imageApi")
public class ImageApi {
    private static final Logger LOGGER= LoggerFactory.getLogger(ImageApi.class);

    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public BaseOutput<String> upload(@RequestParam MultipartFile file, @RequestParam Integer type, @RequestParam(required = false) Boolean compress){
        try{
            if(file==null || file.isEmpty()){
                return BaseOutput.failure("图片文件为空");
            }
            if(!checkExtend(file.getOriginalFilename())){
                return BaseOutput.failure("图片格式错误");
            }
            String uri=imageService.save(file,type,compress);
            return BaseOutput.success().setData(uri);
        }catch (Exception e){
            LOGGER.error("upload",e);
            return BaseOutput.failure();
        }
    }

    /**
     * 验证文件是否符合标准
     * @param fileName
     * @return
     */
    public static boolean checkExtend(String fileName){
        String regex=".*\\.(jpg|png|jpeg|bmp)$";
        Pattern pattern=Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(fileName);
        return matcher.matches();
    }
}
