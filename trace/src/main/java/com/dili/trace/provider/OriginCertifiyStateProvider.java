/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dili.trace.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author wangguofeng
 */
    
@Component
public class OriginCertifiyStateProvider  implements ValueProvider {

	@Override
	public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
		return null;
	}

	@Override
	public String getDisplayText(Object val, Map metaMap, FieldMeta fieldMeta) {
		if(val==null||StringUtils.isBlank(String.valueOf(val))) {
			return "无";
		}
		return "有";
	}

}