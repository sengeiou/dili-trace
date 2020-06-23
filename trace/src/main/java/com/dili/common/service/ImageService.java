package com.dili.common.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.common.config.DefaultConfiguration;
import com.dili.common.exception.TraceBusinessException;
import com.dili.common.util.UUIDUtil;
import com.dili.trace.glossary.ImageTypeEnum;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.Dictionary;

/**
 * 用于处理图片
 */
@Component
public class ImageService {
    private static final Logger logger= LoggerFactory.getLogger(ImageService.class);

    @Resource
    private DefaultConfiguration defaultConfiguration;
    
    
	@PostConstruct
	public void init() {
		logger.warn(">>>>>>>>>>>>>>请注意在应用所在服务器创建相应的图片保存目录 [{}] 及读写权限", this.defaultConfiguration.getImageDirectory());
	}

    /**
     * 保存图片文件的方法
     * @param type
     * @param file
     * @return
     */
    public String save(MultipartFile file,Integer type,Boolean compress) {
        try{
            //创建目录
            StringBuilder directory = createDirectory(type);
            //文件名获取
            String fileName= createFilename(file.getOriginalFilename());
            String uri=directory+fileName;

            if(compress!=null && Boolean.TRUE.equals(compress)){
                BufferedImage image= ImageIO.read(file.getInputStream());
                Thumbnails.of(image).scale(defaultConfiguration.getImageScale()).outputQuality(defaultConfiguration.getImageQuality()).toFile(defaultConfiguration.getImageDirectory()+uri);
            }else{
                file.transferTo(new File(defaultConfiguration.getImageDirectory()+uri));
            }
            return "/image/"+uri;
        }catch (Exception e){
            throw new TraceBusinessException("图片操作失败",e);
        }
    }

    /**
     * 创建目录 按类型、月份切割
     * @param type
     * @return
     */
    private StringBuilder createDirectory(Integer type) {
        StringBuilder directory = new StringBuilder();
        directory.append(ImageTypeEnum.getImageTypeEnum(type));
        directory.append("/");
        directory.append(DateUtil.format(new Date(),"yyyyMM"));
        directory.append("/");
        File temp=new File(defaultConfiguration.getImageDirectory()+directory);
        if(!temp.exists()){
            temp.mkdirs();
        }
        return directory;
    }

    private String createFilename(String originalFilename){
        String ex=originalFilename.substring(originalFilename.lastIndexOf("."));
        return UUIDUtil.get()+ex;
    }
}
