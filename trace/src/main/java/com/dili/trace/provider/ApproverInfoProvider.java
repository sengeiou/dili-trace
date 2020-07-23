package com.dili.trace.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import com.dili.trace.service.ApproverInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 城市提供者
 * 
 * @author asiamaster
 */
@Component
public class ApproverInfoProvider implements ValueProvider {

	@Autowired
	ApproverInfoService approverInfoService;

	@Override
	public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {

		List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();

		return buffer;
	}

	@Override
	public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
		if(obj==null) {
			return "";
		}
		try {
			Long id=Long.valueOf(String.valueOf(obj));
			return this.approverInfoService.get(id).getUserName();
			
		}catch (Exception e) {
			return String.valueOf(obj);
		}
	}
}
