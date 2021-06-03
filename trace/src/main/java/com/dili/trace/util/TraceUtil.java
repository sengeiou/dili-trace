package com.dili.trace.util;

import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;

public class TraceUtil {

    public static <T extends IDTO> T newDTO(Class<T> clz) {
        return (T) DTOUtils.newDTO(clz);
    }
}
