/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dili.trace.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    /**
     * 获得git信息
     * @return
     */
    @RequestMapping(value = "/listGitInfo.api")
    public List<String> listGitInfo(){
    	List<String>list=new ArrayList<String>();
        try {
            File file=    ResourceUtils.getFile("classpath:git.properties");
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line=null;
            while((line=br.readLine())!=null) {
            	list.add(line);
            }
            br.close();
            return list;
        } catch (IOException ex) {
            return Arrays.asList(ex.getMessage());
        }
    }
    
}
