package com.dili.trace.service;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.rpc.DfsRpc;
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
    public Optional<String> fileUpload(MultipartFile multipartFile) throws IOException {
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
            return Optional.ofNullable(out.getData()).map(StringUtils.trimToNull());
        }
        return Optional.empty();

    }
}
