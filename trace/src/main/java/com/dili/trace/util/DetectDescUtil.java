package com.dili.trace.util;

import com.dili.trace.enums.DetectResultEnum;
import com.dili.trace.enums.DetectTypeEnum;

public class DetectDescUtil {
    public static String buildDetectDesc(Integer detectType,Integer detectResult){
        if(detectType==null||detectResult==null){
            return "-";
        }
        if(DetectTypeEnum.NEW.equalsToCode(detectType)){
            return "-";
        }
        if(DetectResultEnum.NONE.equalsToCode(detectResult)){
            return "检测中";
        }
        return DetectTypeEnum.toName(detectType)+ DetectResultEnum.name(detectResult);

    }
}
