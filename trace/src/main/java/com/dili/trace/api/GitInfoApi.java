/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dili.trace.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查询当前发布版本的git信息
 * @author admin
 */
@RestController
@RequestMapping(value = "/api/imageApi")
public class GitInfoApi {
    @RequestMapping(value = "/listGitInfo.api")
    public List<String> listGitInfo(){
        try {
            File file=    ResourceUtils.getFile("classpath:*/git.properties");
            List<String> content = Files.readAllLines(Path.of(file.toURI()));
            return content;
        } catch (IOException ex) {
            return Arrays.asList(ex.getMessage());
        }
    }
    
}
