package com.dili.trace.util;

import com.dili.common.exception.TraceBizException;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.dto.DetectRecordParam;
import com.dili.trace.enums.DetectResultEnum;
import com.dili.trace.enums.DetectTypeEnum;
import one.util.streamex.StreamEx;

import java.util.Objects;
import java.util.Optional;

public class DetectRecordUtil {


    public static Optional<DetectResultEnum> getDetectResultEnum(DetectRecordParam input){

        return StreamEx.ofNullable(input.getDetectState()).map(dt->{
            if(Objects.equals(1,dt)){
                return DetectResultEnum.PASSED;
            }
            if(Objects.equals(2,dt)){
                return DetectResultEnum.FAILED;
            }
            return null;
        }).nonNull().findFirst();


    }
    public static Optional<DetectTypeEnum> getDetectTypeEnum(DetectRecordParam input){
         return StreamEx.ofNullable(input.getDetectType()).map(dt->{
            if(Objects.equals(1,dt)){
                return DetectTypeEnum.INITIAL_CHECK;
            }
            if(Objects.equals(2,dt)){
                return DetectTypeEnum.RECHECK;
            }
            return null;
        }).nonNull().findFirst();
    }
}
