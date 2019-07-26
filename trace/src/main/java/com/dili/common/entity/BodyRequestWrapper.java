package com.dili.common.entity;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;

public class BodyRequestWrapper extends HttpServletRequestWrapper {
    private static Logger LOGGER= LoggerFactory.getLogger(BodyRequestWrapper.class);

    private String jsonBody;

    private static Charset charset=Charset.forName("UTF-8");

    public BodyRequestWrapper(HttpServletRequest request) {
        super(request);
        jsonBody=getJsonBody(request);
    }

    private String getJsonBody(final HttpServletRequest request) {
        StringBuilder builder=new StringBuilder();
        try{
            InputStream is=request.getInputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(is,charset));
            String line;
            while((line=reader.readLine())!=null){
                builder.append(line);
            }
            is.close();
            reader.close();
        }catch (Exception e){
            LOGGER.error("getJsonBody",e);
        }
        return builder.toString();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if(StrUtil.isBlank(jsonBody)){
            jsonBody="";
        }
        final ByteArrayInputStream bis=new ByteArrayInputStream(jsonBody.getBytes(charset));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bis.read();
            }
        };
    }

    public String getJsonBody(){
        return this.jsonBody;
    }
}
