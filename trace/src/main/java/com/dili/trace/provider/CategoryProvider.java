package com.dili.trace.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import com.dili.trace.dto.CategoryListInput;

/**
 * 品类提供者
 * 
 * @author asiamaster
 */
@Component
public class CategoryProvider implements ValueProvider {


	@Override
	public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
		CategoryListInput categoryInput = new CategoryListInput();
		categoryInput.setKeyword(val.toString());

		List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();

		return buffer;
	}

	@Override
	public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
		return null;
	}
}
