package com.dili.trace.api;

import java.util.List;
import java.util.Map.Entry;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.enums.ImageCertTypeEnum;
import com.dili.trace.enums.SpecTypeEnum;

import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;

@RestController
@RequestMapping(value = "/api/enums")
public class EnumsApi {
    /**
     * 证明类型
     */
    @ApiOperation(value ="城市接口查询【接口已通】", notes = "城市接口查询")
    @RequestMapping(value = "/listImageCertType.api",method = RequestMethod.POST)
    public BaseOutput<List<Entry<Integer, String>>> listImageCertType(){
        try{
        	List<Entry<Integer, String>>list=StreamEx.of(ImageCertTypeEnum.values()).mapToEntry(ImageCertTypeEnum::getCode, ImageCertTypeEnum::getName).toList();
        	return BaseOutput.success().setData(list);
        }catch (Exception e){
            return BaseOutput.failure(e.getMessage());
        }
    }
    /**
     * 规格
     */
    @ApiOperation(value ="城市接口查询【接口已通】", notes = "城市接口查询")
    @RequestMapping(value = "/listSpecType.api",method = RequestMethod.POST)
    public BaseOutput<List<Entry<Integer, String>>> listSpecType(){
        try{
        	List<Entry<Integer, String>>list=StreamEx.of(SpecTypeEnum.values()).mapToEntry(SpecTypeEnum::getCode, SpecTypeEnum::getName).toList();
        	return BaseOutput.success().setData(list);
        }catch (Exception e){
            return BaseOutput.failure(e.getMessage());
        }
    }

}
