package com.dili.trace.rpc.service;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.rpc.api.DfsRpc;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件上传接口
 */
@Service
public class DfsRpcService {
    @Value("${diliDfs.accessToken}")
    String accessToken;
    @Autowired(required = false)
    DfsRpc dfsRpc;
    /**
     * 上传图片
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public BaseOutput<String> uploadImage(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return BaseOutput.failure("图片文件为空");
        }
        if (!checkExtend(multipartFile.getOriginalFilename())) {
            return BaseOutput.failure("图片格式错误");
        }
        return this.fileUpload(multipartFile).map(uri -> {
            return BaseOutput.success().setData(uri);
        }).orElse(BaseOutput.failure());
    }
    /**
     * 上传图片
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public BaseOutput<String> uploadFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return BaseOutput.failure("文件为空");
        }

        return this.fileUpload(multipartFile).map(uri -> {
            return BaseOutput.success().setData(uri);
        }).orElse(BaseOutput.failure());
    }
    /**
     * 检查图片格式
     * @param fileName
     * @return
     */
    private  boolean checkExtend(String fileName) {
        String regex = ".*\\.(jpg|png|jpeg|bmp)$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileName);
        return matcher.matches();
    }
    /**
     * 上传文件
     * @param file
     * @return
     * @throws IOException
     */
    public Optional<String> fileUpload(File file) throws IOException {
        MultipartFile multipartFile = this.convertFileToMultipartFile(file);
        return this.uploadAndCheckOutput(multipartFile);
    }
    /**
     * 上传文件
     * @param multipartFile
     * @return
     * @throws IOException
     */
    private Optional<String> fileUpload(MultipartFile multipartFile) throws IOException {
        return this.uploadAndCheckOutput(multipartFile);
    }

    /**
     * 参数转换
     * @param file
     * @return
     * @throws IOException
     */
    private MultipartFile convertFileToMultipartFile(File file) throws IOException {
        FileItem fileItem = new DiskFileItemFactory().createItem("file",
                Files.probeContentType(file.toPath()), false, file.getName());

        try (InputStream in = new FileInputStream(file); OutputStream out = fileItem.getOutputStream()) {
            in.transferTo(out);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file: " + e, e);
        }

        CommonsMultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        return multipartFile;

    }

    /**
     * 调用上传接口并对结果进行转换
     * @param multipartFile
     * @return
     */
    private Optional<String> uploadAndCheckOutput(MultipartFile multipartFile) {
        BaseOutput<String> out = this.dfsRpc.fileUpload(multipartFile, accessToken);
        if (out != null && out.isSuccess()) {
            return Optional.ofNullable(out.getData()).map(StringUtils::trimToNull);
        }
        return Optional.empty();

    }
}
